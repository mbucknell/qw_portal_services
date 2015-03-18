package gov.usgs.cida.qw;

import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

public abstract class BaseRestController {

    private static final Logger LOG = LoggerFactory.getLogger(BaseRestController.class);
    
    public static final String DEFAULT_ENCODING = "UTF-8";

	protected LastUpdateDao lastUpdateDao;

    @ExceptionHandler(Exception.class)
    public @ResponseBody String handleUncaughtException(Exception ex, WebRequest webRequest, HttpServletResponse response) {
        String msgText = "";
        //Note: we are giving the user a generic message.
        //Server logs can be used to troubleshoot problems.
        int hashValue = response.hashCode();
        msgText = "Internal Server Error. Reference Number: " + hashValue;
        LOG.error(msgText, ex);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        return msgText;
    }

    protected boolean isNotModified(WebRequest webRequest) {
        if (webRequest.checkNotModified(lastUpdateDao.getLastEtl().toDate(TimeZone.getTimeZone("UTC")).getTime())) {
        	return true;
        } else {
        	return false;
        }
	}

}
