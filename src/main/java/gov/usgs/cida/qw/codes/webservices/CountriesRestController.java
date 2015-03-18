package gov.usgs.cida.qw.codes.webservices;

import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.codes.Code;
import gov.usgs.cida.qw.codes.CodeList;
import gov.usgs.cida.qw.codes.CodeType;
import gov.usgs.cida.qw.codes.dao.CodeDao;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value={"codes/countries", "codes/countrycode"}, produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class CountriesRestController extends CodesRestController {

	private static final Logger LOG = LoggerFactory.getLogger(CountriesRestController.class);

    @Autowired
    public CountriesRestController(@Qualifier("lastUpdateDao") final LastUpdateDao lastUpdateDao,
			@Qualifier("codeDao") final CodeDao codeDao) {
    	this.lastUpdateDao = lastUpdateDao;
		super.codeDao = codeDao;
	}

    @RequestMapping(method=RequestMethod.GET)
    public CodeList getCountries(final @RequestParam(value="text", required=false) String text,
    		final @RequestParam(value="pagenumber", required=false) String pageNumber,
    		final @RequestParam(value="pagesize", required=false) String pageSize,
    		WebRequest webRequest) {
        LOG.debug("countries");
        return getList(CodeType.COUNTRYCODE, text, pageNumber, pageSize, null, webRequest);
    }

    @RequestMapping(value="{countrycode}", method=RequestMethod.GET)
    public Code getCountry(final @PathVariable(value="countrycode") String countrycode, WebRequest webRequest, HttpServletResponse response) {
        LOG.debug("country");
        return getCode(CodeType.COUNTRYCODE, countrycode, webRequest, response);
    }

}
