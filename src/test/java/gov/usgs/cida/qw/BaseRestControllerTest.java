package gov.usgs.cida.qw;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

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
	private LocalDateTime localFromUTC; 

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		testController = new TestController(lastUpdateDao);
		localFromUTC = LocalDateTime.of(2014, 1, 1, 1, 1, 1);
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
		mockRequest.addHeader("If-Modified-Since", localFromUTC.toInstant(ZoneOffset.UTC).toEpochMilli());
		assertTrue("Header set for same as last modified, so is not modified.", testController.isNotModified(webRequest));

		mockRequest = new MockHttpServletRequest();
		servletResponse = new MockHttpServletResponse();
		webRequest = new ServletWebRequest(mockRequest, servletResponse);
		mockRequest.addHeader("If-Modified-Since", localFromUTC.minusSeconds(1).toInstant(ZoneOffset.UTC).toEpochMilli());
		assertFalse("Header set for before last modified, so is modified.", testController.isNotModified(webRequest));
	}

}
