package gov.usgs.cida.qw.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("Summary")
public class SummaryMvcService extends MvcService {

	private final Logger log = LoggerFactory.getLogger(getClass());

}
