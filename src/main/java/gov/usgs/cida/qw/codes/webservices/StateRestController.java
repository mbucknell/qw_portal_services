package gov.usgs.cida.qw.codes.webservices;

import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.codes.Code;
import gov.usgs.cida.qw.codes.CodeList;
import gov.usgs.cida.qw.codes.CodeType;
import gov.usgs.cida.qw.codes.dao.CodeDao;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value={"codes/states", "codes/statecode"}, produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class StateRestController extends CodesRestController {

    private static final Logger LOG = LoggerFactory.getLogger(StateRestController.class);

    @Autowired
	public StateRestController(@Qualifier("lastUpdateDao") final LastUpdateDao lastUpdateDao,
			@Qualifier("codeDao") final CodeDao codeDao) {
    	this.lastUpdateDao = lastUpdateDao;
    	this.codeDao = codeDao;
    }

    @RequestMapping(params="!value", method=RequestMethod.GET)
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

    @RequestMapping(params="value", method=RequestMethod.GET)
    public Code getState(final @RequestParam(value="value") String value, WebRequest webRequest, HttpServletResponse response) {
        LOG.debug("state");
        return getCode(CodeType.STATECODE, value, webRequest, response);
    }

}
