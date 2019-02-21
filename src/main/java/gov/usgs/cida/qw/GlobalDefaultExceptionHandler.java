package gov.usgs.cida.qw;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);
	public static final String ERROR = "Error Encountered";

	@ExceptionHandler(Exception.class)
	public void handleUncaughtException(Exception ex, WebRequest request, HttpServletResponse response) throws IOException {
		if (ex instanceof HttpMediaTypeNotAcceptableException) {
			response.sendError(HttpStatus.NOT_ACCEPTABLE.value());
		} else if (ex instanceof HttpMessageNotReadableException
				|| ex instanceof MissingServletRequestParameterException
				|| ex instanceof HttpMediaTypeNotSupportedException) {
			int x = ex.getLocalizedMessage().indexOf("\n");
			response.sendError(HttpStatus.BAD_REQUEST.value(), (x > 0 ? ex.getLocalizedMessage().substring(0, x) : ex.getLocalizedMessage()) );
		} else {
			int hashValue = response.hashCode();
			//Note: we are giving the user a generic message.  
			//Server logs can be used to troubleshoot problems.
			String msgText = "Something bad happened. Contact us with Reference Number: " + hashValue;
			LOG.error(msgText, ex);
			response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), msgText);
		}
	}

}
