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
@RequestMapping(value={"codes/organizations", "codes/organization"}, produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class OrganizationRestController extends CodesRestController {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationRestController.class);

    @Autowired
	public OrganizationRestController(@Qualifier("lastUpdateDao") final LastUpdateDao lastUpdateDao,
			@Qualifier("codeDao") final CodeDao codeDao) {
    	this.lastUpdateDao = lastUpdateDao;
    	this.codeDao = codeDao;
    }

    @RequestMapping(method=RequestMethod.GET)
    public CodeList getOrganizations(final @RequestParam(value="text", required=false) String text,
    		final @RequestParam(value="pagenumber", required=false) String pageNumber,
    		final @RequestParam(value="pagesize", required=false) String pageSize,
    		WebRequest webRequest) {
        LOG.debug("organizations");
        return getList(CodeType.ORGANIZATION, text, pageNumber, pageSize, null, webRequest);
    }

    @RequestMapping(value="{organization}", method=RequestMethod.GET)
    public Code getOrganization(final @PathVariable(value="organization") String organization, WebRequest webRequest, HttpServletResponse response) {
        LOG.debug("organization");
        return getCode(CodeType.ORGANIZATION, organization, webRequest, response);
    }
    
}
