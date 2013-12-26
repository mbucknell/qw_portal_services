package gov.usgs.cida.qw.webservice.codes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CountryCodesMvcService extends AggregatedCodesMvcService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value={"countries", "countrycode"}, method=RequestMethod.GET, produces="application/xml")
	public void getCountries(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException, IOException, XMLStreamException, TransformerFactoryConfigurationError, TransformerException, JAXBException {
		log.debug("countries");
		Map<String, List<String>> queryParams = new HashMap<>();
		doCodeRequest(request, outerFace, dataSource, queryParams, response);
	}

}
