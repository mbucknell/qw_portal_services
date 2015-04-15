package gov.usgs.cida.qw;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class OnePlusParameterFilterTest {

	private MockHttpServletRequest mockRequest = new MockHttpServletRequest();
	private MockHttpServletResponse servletResponse = new MockHttpServletResponse();
	private MockFilterChain filterChain = new MockFilterChain();

    @Test
	public void testSomethingAboutDoFilter() throws IOException, ServletException {
		WQPFilter filter = new WQPFilter();

		//Bad request because no parameters sent
		filter.doFilter(mockRequest, servletResponse, filterChain);
	    assertEquals(HttpStatus.BAD_REQUEST.value(), servletResponse.getStatus());
	    assertEquals(WQPFilter.DEFAULT_ENCODING, servletResponse.getCharacterEncoding());
	    
	    //Good request because a parameter was sent
	    Map<String, String[]> parms = new HashMap<>();
    	parms.put("something", new String[]{"US"});
	    mockRequest.addParameters(parms);
	    filter.doFilter(mockRequest, servletResponse, filterChain);
	    assertEquals(WQPFilter.DEFAULT_ENCODING, servletResponse.getCharacterEncoding());
    }
	
}
