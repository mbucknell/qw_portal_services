package gov.usgs.cida.qw;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

public abstract class BaseRestController {

	private static final Logger LOG = LoggerFactory.getLogger(BaseRestController.class);

	public static final String MIME_TYPE_TEXT_CSV = "text/csv";
	public static final String HEADER_CONTENT_DISPOSITION = "Content-disposition";

	protected LastUpdateDao lastUpdateDao;

	@ExceptionHandler(Exception.class)
	public @ResponseBody String handleUncaughtException(Exception ex, WebRequest webRequest, HttpServletResponse response) {
		String msgText = "";
		if (ex instanceof MissingServletRequestParameterException) {
			//We just didn't get all the query params we require...
			msgText = ex.getLocalizedMessage();
			response.setCharacterEncoding(WQPFilter.DEFAULT_ENCODING);
			response.addHeader("Content-Type", MediaType.TEXT_PLAIN_VALUE);
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} else {
			//Note: we are giving the user a generic message.
			//Server logs can be used to troubleshoot problems.
			int hashValue = response.hashCode();
			msgText = "Internal Server Error. Reference Number: " + hashValue;
			LOG.error(msgText, ex);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return msgText;
	}

	protected boolean isNotModified(WebRequest webRequest) {
		LocalDateTime lastUpdatedUtc = lastUpdateDao.getLastEtl();
		if (null != lastUpdatedUtc) {
			return webRequest.checkNotModified(lastUpdatedUtc.toInstant(ZoneOffset.UTC).toEpochMilli());
		} else {
			return false;
		}
	}

}
