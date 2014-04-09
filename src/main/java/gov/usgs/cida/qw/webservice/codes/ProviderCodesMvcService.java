package gov.usgs.cida.qw.webservice.codes;

import gov.usgs.cida.qw.QWConstants;
import gov.usgs.cida.qw.QWUtility;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("codes")
public class ProviderCodesMvcService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @RequestMapping(value="providers", method=RequestMethod.GET, produces="application/xml")
    @ResponseBody
    public String getProviders(HttpServletRequest request, HttpServletResponse response) throws NamingException {
        log.debug("providers");
        response.setCharacterEncoding(QWConstants.DEFAULT_ENCODING);
        if (request.getParameterMap().isEmpty()) {
            log.debug("No parameters");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "";
        } else {
            return QWUtility.PROVIDERS_XML;
        }
    }

}
