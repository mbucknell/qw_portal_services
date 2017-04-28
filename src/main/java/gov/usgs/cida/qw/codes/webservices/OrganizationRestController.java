package gov.usgs.cida.qw.codes.webservices;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@Api(tags={SwaggerConfig.ORGANIZATION_TAG_NAME})
@RestController
@RequestMapping(value="codes/organization", produces={BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class OrganizationRestController extends CodesRestController {

	private static final Logger LOG = LoggerFactory.getLogger(OrganizationRestController.class);

	@Autowired
	public OrganizationRestController(final LastUpdateDao lastUpdateDao, final CodeDao codeDao) {
		this.lastUpdateDao = lastUpdateDao;
		this.codeDao = codeDao;
	}

	@ApiOperation(value="Return a filtered and paged list of valid Organization IDs.")
	@GetMapping()
	public CodeList getOrganizations(final @RequestParam(value="text", required=false) String text,
			final @RequestParam(value="pagenumber", required=false) String pageNumber,
			final @RequestParam(value="pagesize", required=false) String pageSize,
			WebRequest webRequest) {
		LOG.debug("organizations");
		return getList(CodeType.ORGANIZATION, text, pageNumber, pageSize, null, webRequest);
	}

	@ApiOperation(value="Return the requested Organization ID.")
	@GetMapping("/{value}")
	public Code getOrganization(final @PathVariable(value="value") String value, WebRequest webRequest, HttpServletResponse response) {
		LOG.debug("organization");
		return getCode(CodeType.ORGANIZATION, value, webRequest, response);
	}

}
