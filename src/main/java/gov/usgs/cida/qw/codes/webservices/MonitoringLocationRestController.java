
package gov.usgs.cida.qw.codes.webservices;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.codes.Code;
import gov.usgs.cida.qw.codes.CodeList;
import gov.usgs.cida.qw.codes.CodeType;
import gov.usgs.cida.qw.codes.dao.CodeDao;
import gov.usgs.cida.qw.swagger.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags={SwaggerConfig.MONITORING_LOCATION_TAG_NAME})
@RestController
@RequestMapping(value="codes/monitoringlocation", produces={BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class MonitoringLocationRestController extends CodesRestController {
	
	private static final Logger LOG = LoggerFactory.getLogger(MonitoringLocationRestController.class);
	
	@Autowired
	public MonitoringLocationRestController(final LastUpdateDao lastUpdateDao, final CodeDao codeDao) {
		this.lastUpdateDao = lastUpdateDao;
		this.codeDao = codeDao;
	}
	
	@ApiOperation(value="Return a filtered and paged list of valid Monitoring Locations.")
	@GetMapping()
	public CodeList getMonitoringLocations(final @RequestParam(value="text", required=false) String text,
			final @RequestParam(value="pagenumber", required=false) String pageNumber,
			final @RequestParam(value="pagesize", required=false) String pageSize,
			WebRequest webRequest) {
		LOG.debug("monitoringlocations");
		return getList(CodeType.MONITORINGLOCATION, text, pageNumber, pageSize, null, webRequest);
	}
	
	@ApiOperation(value="Validate and return the requested Monitoring Location.")
	@GetMapping("/validate")
	public Code getMonitoringLocation(final @RequestParam(value="value") String value, WebRequest webRequest, HttpServletResponse response) {
		LOG.debug("monitoringlocation");
		return getCode(CodeType.MONITORINGLOCATION, value, webRequest, response);
	}
	
}
