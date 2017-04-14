package gov.usgs.cida.qw.springinit;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.sql.DataSource;

import org.apache.ibatis.type.TypeAliasRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import gov.usgs.cida.qw.codes.Code;

public class MybatisConfig {

	public static final String MYBATIS_MAPPERS = "mybatis/*.xml";
	public static final String LINKED_HASH_MAP_ALIAS = "LinkedHashMap";
	public static final String DATE_ALIAS = "Date";
	public static final String CODE_ALIAS = "Code";

	@Autowired
	DataSource dataSource;

	@Bean
	public org.apache.ibatis.session.Configuration mybatisConfig() {
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setCallSettersOnNulls(true);
		config.setCacheEnabled(false);
		config.setLazyLoadingEnabled(false);
		config.setAggressiveLazyLoading(false);

		registerAliases(config.getTypeAliasRegistry());

		return config;
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setConfiguration(mybatisConfig());
		sqlSessionFactory.setDataSource(dataSource);
		Resource[] mappers = new PathMatchingResourcePatternResolver().getResources(MYBATIS_MAPPERS);
		sqlSessionFactory.setMapperLocations(mappers);
		return sqlSessionFactory;
	}

	private void registerAliases(TypeAliasRegistry registry) {
		registry.registerAlias(LINKED_HASH_MAP_ALIAS, LinkedHashMap.class);
		registry.registerAlias(DATE_ALIAS, Date.class);
		registry.registerAlias(CODE_ALIAS, Code.class);
	}

}
