package gov.usgs.cida.qw;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.usgs.cida.qw.swagger.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags={SwaggerConfig.VERSION_TAG_NAME})
@RestController
public class VersionController extends BaseRestController {

	@ApiOperation(value="Return the web service version information.")
	@GetMapping(value="version", produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String getVersion() {
		return ApplicationVersion.getVersion();
	}

}