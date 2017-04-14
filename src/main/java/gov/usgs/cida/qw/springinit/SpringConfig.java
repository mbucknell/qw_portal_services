package gov.usgs.cida.qw.springinit;

import gov.usgs.cida.qw.CustomStringToArrayConverter;
import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.codes.dao.CodeDao;
import gov.usgs.cida.qw.srsnames.PCodeDao;
import gov.usgs.cida.qw.summary.SummaryDao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages="gov.usgs.cida.qw")
@EnableWebMvc
public class SpringConfig extends WebMvcConfigurerAdapter {

	@Autowired
	CustomStringToArrayConverter customStringToArrayConverter;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(customStringToArrayConverter);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer
			.favorPathExtension(false)
			.favorParameter(true)
			.parameterName("mimeType")
			.defaultContentType(MediaType.APPLICATION_XML)
			.mediaType("csv", new MediaType("text","csv"))
			.mediaType("xml", MediaType.APPLICATION_XML)
			.mediaType("json", MediaType.APPLICATION_JSON)
			;
	}

	@Bean
	public DataSource dataSource() throws Exception {
		Context ctx = new InitialContext();
		return (DataSource) ctx.lookup("java:comp/env/jdbc/WQPQW");
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		Resource mybatisConfig = new ClassPathResource("mybatis/mybatisConfig.xml");
		sqlSessionFactory.setConfigLocation(mybatisConfig);
		sqlSessionFactory.setDataSource(dataSource());
		return sqlSessionFactory;
	}

	@Bean
	public PCodeDao pCodeDao() throws Exception {
		PCodeDao dao = new PCodeDao();
		dao.setSqlSessionFactory(sqlSessionFactory().getObject());
		return dao;
	}

	@Bean
	public CodeDao codeDao() throws Exception {
		CodeDao dao = new CodeDao();
		dao.setSqlSessionFactory(sqlSessionFactory().getObject());
		return dao;
	}

	@Bean
	public LastUpdateDao lastUpdateDao() throws Exception {
		LastUpdateDao dao = new LastUpdateDao();
		dao.setSqlSessionFactory(sqlSessionFactory().getObject());
		return dao;
	}

	@Bean
	public SummaryDao summaryDao() throws Exception {
		SummaryDao dao = new SummaryDao();
		dao.setSqlSessionFactory(sqlSessionFactory().getObject());
		return dao;
	}

}