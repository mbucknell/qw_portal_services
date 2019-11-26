package gov.usgs.cida.qw.codes.webservices;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import gov.usgs.cida.qw.springinit.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Country Code", description=OpenApiConfig.LOOKUP_TAG_DESCRIPTION)
@RestController
@RequestMapping(value="countrycode", produces={BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, BaseRestController.MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE})
public class CountriesRestController extends CodesRestController {

	private static final Logger LOG = LoggerFactory.getLogger(CountriesRestController.class);

	@Autowired
	public CountriesRestController(final LastUpdateDao lastUpdateDao, final CodeDao codeDao) {
		this.lastUpdateDao = lastUpdateDao;
		super.codeDao = codeDao;
	}

	@Operation(description="Return a filtered and paged list of valid Country Codes.")
	@GetMapping()
	public CodeList getCountries(final @RequestParam(value="text", required=false) String text,
			final @RequestParam(value="pagenumber", required=false) String pageNumber,
			final @RequestParam(value="pagesize", required=false) String pageSize,
			WebRequest webRequest) {
		LOG.debug("countries");
		return getList(CodeType.COUNTRYCODE, text, pageNumber, pageSize, null, webRequest);
	}

	@Operation(description="Validate and return the requested Country Code.")
	@GetMapping("/validate")
	public Code getCountry(final @RequestParam(value="value") String value, WebRequest webRequest, HttpServletResponse response) {
		LOG.debug("country");
		return getCode(CodeType.COUNTRYCODE, value, webRequest, response);
	}

}
