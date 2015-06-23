package gov.usgs.cida.qw;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController extends BaseRestController {

	@RequestMapping(value="version", method=RequestMethod.GET)
	@ResponseBody
	public String getVersion() {
		return ApplicationVersion.getVersion();
	}

}