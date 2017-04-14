package gov.usgs.cida.qw;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.web.context.request.WebRequest;

public abstract class BaseRestController {

	public static final String MIME_TYPE_TEXT_CSV = "text/csv";
	public static final String HEADER_CONTENT_DISPOSITION = "Content-disposition";

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
