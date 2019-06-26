package gov.usgs.cida.qw.springinit;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;


@TestConfiguration
@Import(MybatisConfig.class)
public class DBTestConfig {
	private static final Logger LOG = LoggerFactory.getLogger(DBTestConfig.class);

	@Value("${wqpUrl}")
	private String datasourceUrl;

	@Value("${wqpOwnerUsername}")
	private String datasourceUsername;

	@Value("${wqpOwnerPassword}")
	private String datasourcePassword;

	@Bean
	public DataSource dataSource() throws SQLException {
		LOG.info("datasource URL:" + datasourceUrl);
		LOG.info("datasource Username:" + datasourceUsername);
		PGSimpleDataSource ds = new PGSimpleDataSource();
		ds.setURL(datasourceUrl);
		ds.setUser(datasourceUsername);
		ds.setPassword(datasourcePassword);
		//Because I cannot get DBUnit to load via an Oracle synonym, we must make sure the synonym points to our
		//permanent test table.
		//ds.getConnection().createStatement().execute("create or replace synonym public_srsnames for public_srsnames_test");
		return ds;
	};

	@Bean
	public DatabaseConfigBean dbUnitDatabaseConfig() {
		DatabaseConfigBean dbUnitDbConfig = new DatabaseConfigBean();
		dbUnitDbConfig.setDatatypeFactory(new PostgresqlDataTypeFactory());
		dbUnitDbConfig.setQualifiedTableNames(false);
		return dbUnitDbConfig;
	}

	@Bean
	public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() throws SQLException {
		DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection = new DatabaseDataSourceConnectionFactoryBean();
		dbUnitDatabaseConnection.setDatabaseConfig(dbUnitDatabaseConfig());
		dbUnitDatabaseConnection.setDataSource(dataSource());
		dbUnitDatabaseConnection.setSchema("wqp");
		return dbUnitDatabaseConnection;
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		return validator;
	}

}
