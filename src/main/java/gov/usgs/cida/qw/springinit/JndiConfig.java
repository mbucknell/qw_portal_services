package gov.usgs.cida.qw.springinit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

public class JndiConfig {

	private final Context ctx;
	
	public JndiConfig() throws NamingException {
		ctx = new InitialContext();
	}

	@Bean
	public DataSource dataSource() throws Exception {
		return (DataSource) ctx.lookup("java:comp/env/jdbc/WQPQW");
	}

}
