package gov.usgs.cida.qw;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CORSFilter implements Filter {

	public static String HEADER_CORS = "Access-Control-Allow-Origin";
	public static String HEADER_CORS_VALUE = "*";
	public static String HEADER_CORS_METHODS = "Access-Control-Allow-Methods";
	public static String HEADER_CORS_METHODS_VALUE = "GET, OPTIONS";
	public static String HEADER_CORS_MAX_AGE = "Access-Control-Max-Age";
	public static String HEADER_CORS_MAX_AGE_VALUE = "3600";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing needed here
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader(HEADER_CORS, HEADER_CORS_VALUE);
		resp.setHeader(HEADER_CORS_METHODS, HEADER_CORS_METHODS_VALUE);
		resp.setHeader(HEADER_CORS_MAX_AGE, HEADER_CORS_MAX_AGE_VALUE);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// nothing needed here
	}

}
