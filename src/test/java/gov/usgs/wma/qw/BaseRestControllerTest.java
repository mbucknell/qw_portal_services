package gov.usgs.wma.qw;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import gov.usgs.wma.qw.BaseRestController;
import gov.usgs.wma.qw.LastUpdateDao;

@SpringBootTest
public class BaseRestControllerTest {

	@MockBean
	private LastUpdateDao lastUpdateDao;
	private TestController testController;
	private LocalDateTime localFromUTC; 

	private class TestController extends BaseRestController {
		public TestController(final LastUpdateDao lastUpdateDao) {
			this.lastUpdateDao = lastUpdateDao;
		}
	}

	@BeforeEach
	public void setup() {
		testController = new TestController(lastUpdateDao);
		localFromUTC = LocalDateTime.of(2014, 1, 1, 1, 1, 1);
	}

	@Test
	public void isNotModifiedTest() {
		//Not sure why, but we need to reset the requests/responses after each call...
		//Also, in real life these dates are UTC...
		when(lastUpdateDao.getLastEtl()).thenReturn(localFromUTC);

		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		MockHttpServletResponse servletResponse = new MockHttpServletResponse();
		NativeWebRequest webRequest = new ServletWebRequest(mockRequest, servletResponse);
		assertFalse(testController.isNotModified(webRequest), "Header Not set, so is modified.");

		mockRequest = new MockHttpServletRequest();
		servletResponse = new MockHttpServletResponse();
		webRequest = new ServletWebRequest(mockRequest, servletResponse);
		mockRequest.addHeader("If-Modified-Since", new Date());
		assertTrue(testController.isNotModified(webRequest), "Header set for after last modified, so is not modified.");

		mockRequest = new MockHttpServletRequest();
		servletResponse = new MockHttpServletResponse();
		webRequest = new ServletWebRequest(mockRequest, servletResponse);
		mockRequest.addHeader("If-Modified-Since", localFromUTC.toInstant(ZoneOffset.UTC).toEpochMilli());
		assertTrue(testController.isNotModified(webRequest), "Header set for same as last modified, so is not modified.");

		mockRequest = new MockHttpServletRequest();
		servletResponse = new MockHttpServletResponse();
		webRequest = new ServletWebRequest(mockRequest, servletResponse);
		mockRequest.addHeader("If-Modified-Since", localFromUTC.minusSeconds(1).toInstant(ZoneOffset.UTC).toEpochMilli());
		assertFalse(testController.isNotModified(webRequest), "Header set for before last modified, so is modified.");
	}

}
