
package gov.usgs.wma.qw.codes.webservices;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import gov.usgs.wma.qw.BaseRestController;
import gov.usgs.wma.qw.LastUpdateDao;
import gov.usgs.wma.qw.codes.Code;
import gov.usgs.wma.qw.codes.CodeList;
import gov.usgs.wma.qw.codes.CodeType;
import gov.usgs.wma.qw.codes.dao.CodeDao;
import gov.usgs.wma.qw.springinit.ConfigOpenApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Monitoring Location", description=ConfigOpenApi.LOOKUP_TAG_DESCRIPTION)
@RestController
@RequestMapping(value="monitoringlocation", produces={BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, BaseRestController.MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE})
public class MonitoringLocationRestController extends CodesRestController {

	private static final Logger LOG = LoggerFactory.getLogger(MonitoringLocationRestController.class);

	@Autowired
	public MonitoringLocationRestController(final LastUpdateDao lastUpdateDao, final CodeDao codeDao) {
		this.lastUpdateDao = lastUpdateDao;
		this.codeDao = codeDao;
	}

	@Operation(description="Return a filtered and paged list of valid Monitoring Locations.")
	@GetMapping()
	public CodeList getMonitoringLocations(final @RequestParam(value="organizationid", required=false) String [] organizationid,
			final @RequestParam(value="provider", required=false) String [] provider, 
			final @RequestParam(value="text", required=false) String text,
			final @RequestParam(value="pagenumber", required=false, defaultValue="1") String pageNumber,
			final @RequestParam(value="pagesize", required=false, defaultValue="25") String pageSize,
			WebRequest webRequest) {
		LOG.debug("monitoringlocations");
		Map<String, Object> addlParms = new HashMap<>();
		addlParms.put("organizationid", organizationid);
		addlParms.put("provider", provider);
		return getList(CodeType.MONITORINGLOCATION, text, pageNumber, pageSize, addlParms, webRequest);
	}

	@Operation(description="Validate and return the requested Monitoring Location.")
	@GetMapping("/validate")
	public Code getMonitoringLocation(final @RequestParam(value="value") String value, WebRequest webRequest, HttpServletResponse response) {
		LOG.debug("monitoringlocation");
		return getCode(CodeType.MONITORINGLOCATION, value, webRequest, response);
	}

}
