package gov.usgs.wma.qw;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.http.MediaType;
import org.springframework.web.context.request.WebRequest;

public abstract class BaseRestController {

	public static final String MEDIA_TYPE_TEXT_CSV_UTF8_VALUE = "text/csv;charset=UTF-8";
	public static final String MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE = "application/xml;charset=UTF-8";
	public static final String MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
	public static final String HEADER_CONTENT_DISPOSITION = "Content-disposition";
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final MediaType MEDIA_TYPE_TEXT_CSV_UTF8 = new MediaType("text", "csv", Charset.forName(BaseRestController.DEFAULT_ENCODING));

	protected LastUpdateDao lastUpdateDao;

	protected boolean isNotModified(WebRequest webRequest) {
		LocalDateTime lastUpdatedUtc = lastUpdateDao.getLastEtl();
		if (null != lastUpdatedUtc) {
			return webRequest.checkNotModified(lastUpdatedUtc.toInstant(ZoneOffset.UTC).toEpochMilli());
		} else {
			return false;
		}
	}

}
