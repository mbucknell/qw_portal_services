package gov.usgs.cida.qw;

import org.springframework.web.util.UrlPathHelper;

public class UrlPathHelperNonDecoding extends UrlPathHelper {

	public UrlPathHelperNonDecoding() {
		super.setUrlDecode(false);
	}

}
