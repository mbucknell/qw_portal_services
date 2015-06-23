package gov.usgs.cida.qw.springinit;

import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

@Configuration
@PropertySource(value = "classpath:database_init.properties")
public class TestSpringConfig extends SpringConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	   
	@Bean
	public OracleDataSource dataSource() throws SQLException {
		OracleDataSource ds = new OracleDataSource();
		ds.setURL(env.getProperty("jdbc.wqpCore.url"));
		ds.setUser(env.getProperty("jdbc.wqpCore.username"));
		ds.setPassword(env.getProperty("jdbc.wqpCore.password"));
		//Because I cannot get DBUnit to load via an Oracle synonym, we must make sure the synonym points to our
		//permanent test table.
		ds.getConnection().createStatement().execute("create or replace synonym public_srsnames for public_srsnames_test");
		return ds;
	}
    
	@Bean
	public DatabaseConfigBean dbUnitDatabaseConfig() {
		DatabaseConfigBean dbUnitDbConfig = new DatabaseConfigBean();
		dbUnitDbConfig.setDatatypeFactory(new OracleDataTypeFactory());
		//DBUnit does not properly handle synonyms, so we cannot load via them...
		//dbUnitDbConfig.setTableType(new String[] {"TABLE", "SYNONYM"});
//		dbUnitDbConfig.setBatchedStatements(true);
//		dbUnitDbConfig.setBatchSize(20);
		dbUnitDbConfig.setSkipOracleRecyclebinTables(true);
		dbUnitDbConfig.setQualifiedTableNames(false);
		return dbUnitDbConfig;
	}

	@Bean
	public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() throws SQLException {
		DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection = new DatabaseDataSourceConnectionFactoryBean();
		dbUnitDatabaseConnection.setDatabaseConfig(dbUnitDatabaseConfig());
		dbUnitDatabaseConnection.setDataSource(dataSource());
		dbUnitDatabaseConnection.setSchema("WQP_CORE");
		return dbUnitDatabaseConnection;
	}

}
