package gov.usgs.cida.qw;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;


public class BaseRestControllerTest {

	@Mock
	private LastUpdateDao lastUpdateDao;
	
	private class TestController extends BaseRestController {
		public TestController(final LastUpdateDao lastUpdateDao) {
	    	this.lastUpdateDao = lastUpdateDao;
	    }
	}
	
	private TestController testController;
	private MockHttpServletRequest mockRequest;
	private NativeWebRequest webRequest; 
	private MockHttpServletResponse servletResponse;
	private Date dateFromUTC;
	private LocalDateTime localFromUTC; 
	
    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    	testController = new TestController(lastUpdateDao);
    	try {
    		dateFromUTC = (Date) new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss zzz").parseObject("2014.01.01 01:01:01 UTC");
    		localFromUTC = new LocalDateTime(dateFromUTC);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

	@Test
    public void isNotModifiedTest() {
		//Not sure why, but we need to reset the requests/responses after each call...
		//Also, in real life these dates are UTC...
        when(lastUpdateDao.getLastEtl()).thenReturn(localFromUTC);

    	mockRequest = new MockHttpServletRequest();
    	servletResponse = new MockHttpServletResponse();
    	webRequest = new ServletWebRequest(mockRequest, servletResponse);
        assertFalse("Header Not set, so is modified.", testController.isNotModified(webRequest));
        
    	mockRequest = new MockHttpServletRequest();
    	servletResponse = new MockHttpServletResponse();
    	webRequest = new ServletWebRequest(mockRequest, servletResponse);
        mockRequest.addHeader("If-Modified-Since", new Date());
        assertTrue("Header set for after last modified, so is not modified.", testController.isNotModified(webRequest));

    	mockRequest = new MockHttpServletRequest();
    	servletResponse = new MockHttpServletResponse();
    	webRequest = new ServletWebRequest(mockRequest, servletResponse);
    	mockRequest.addHeader("If-Modified-Since", localFromUTC.toDate(TimeZone.getTimeZone("UTC")).getTime());
        assertTrue("Header set for same as last modified, so is not modified.", testController.isNotModified(webRequest));
        

    	mockRequest = new MockHttpServletRequest();
    	servletResponse = new MockHttpServletResponse();
    	webRequest = new ServletWebRequest(mockRequest, servletResponse);
    	mockRequest.addHeader("If-Modified-Since", localFromUTC.minusSeconds(1).toDate(TimeZone.getTimeZone("UTC")).getTime());
        assertFalse("Header set for before last modified, so is modified.", testController.isNotModified(webRequest));
	}

}
