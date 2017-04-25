package gov.usgs.cida.qw.codes.webservices;

import java.util.HashMap;
import java.util.Map;

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

@RestController
@RequestMapping(value={"codes/states", "codes/statecode"}, produces={BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class StateRestController extends CodesRestController {

	private static final Logger LOG = LoggerFactory.getLogger(StateRestController.class);

	@Autowired
	public StateRestController(final LastUpdateDao lastUpdateDao, final CodeDao codeDao) {
		this.lastUpdateDao = lastUpdateDao;
		this.codeDao = codeDao;
	}

	@GetMapping(params="!value")
	public CodeList getStates(final @RequestParam(value="countrycode", required=false) String[] countrycodes,
			final @RequestParam(value="text", required=false) String text,
			final @RequestParam(value="pagenumber", required=false) String pageNumber,
			final @RequestParam(value="pagesize", required=false) String pageSize,
			WebRequest webRequest) {
		LOG.debug("states");
		Map<String, Object> addlParms = new HashMap<>();
		addlParms.put("countrycode", countrycodes);
		return getList(CodeType.STATECODE, text, pageNumber, pageSize, addlParms, webRequest);
	}

	@GetMapping("/{value}")
	public Code getState(final @PathVariable(value="value") String value, WebRequest webRequest, HttpServletResponse response) {
		LOG.debug("state");
		return getCode(CodeType.STATECODE, value, webRequest, response);
	}

}
