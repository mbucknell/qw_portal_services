package gov.usgs.cida.qw;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController extends BaseRestController {

	@GetMapping(value="version")
	@ResponseBody
	public String getVersion() {
		return ApplicationVersion.getVersion();
	}

}