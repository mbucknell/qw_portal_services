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

@RestController
@RequestMapping(value={"codes/samplemedia"}, produces={BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class SampleMediaRestController extends CodesRestController {

	private static final Logger LOG = LoggerFactory.getLogger(SampleMediaRestController.class);

	@Autowired
	public SampleMediaRestController(final LastUpdateDao lastUpdateDao, final CodeDao codeDao) {
		this.lastUpdateDao = lastUpdateDao;
		this.codeDao = codeDao;
	}

	@GetMapping(params="!value")
	public CodeList getSampleMedia(final @RequestParam(value="text", required=false) String text,
			final @RequestParam(value="pagenumber", required=false) String pageNumber,
			final @RequestParam(value="pagesize", required=false) String pageSize,
			WebRequest webRequest) {
		LOG.debug("samplemedia");
		return getList(CodeType.SAMPLEMEDIA, text, pageNumber, pageSize, null, webRequest);
	}

	@GetMapping("/{value}")
	public Code getASampleMedia(final @PathVariable(value="value") String value, WebRequest webRequest, HttpServletResponse response) {
		LOG.debug("aSampleMedia");
		return getCode(CodeType.SAMPLEMEDIA, value, webRequest, response);
	}

}
