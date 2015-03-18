package gov.usgs.cida.qw;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

public class OnePlusParameterFilter implements Filter {

    public static final String DEFAULT_ENCODING = "UTF-8";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Nothing to do here!!
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		response.setCharacterEncoding(DEFAULT_ENCODING);
        if (request.getParameterMap().isEmpty()) {
        	((HttpServletResponse) response).setStatus(HttpStatus.BAD_REQUEST.value());
        } else {
        	chain.doFilter(request, response);
        }
	}

	@Override
	public void destroy() {
		// Nothing to do here!!
	}

}
