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
		Context ctx = new InitialContext();
		return (DataSource) ctx.lookup("java:comp/env/jdbc/WQPQW");
	}

	@Bean
	public String displayHost() throws NamingException {
		return (String) ctx.lookup("java:comp/env/qw/displayHost");
	}

	@Bean
	public String displayPath() throws NamingException {
		return (String) ctx.lookup("java:comp/env/qw/displayPath");
	}
	@Bean
	public String rootUrl() throws NamingException {
		return "https://" + displayHost() + displayPath();
	}

}
