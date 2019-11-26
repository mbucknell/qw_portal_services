package gov.usgs.cida.qw.srsnames;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.swagger.SwaggerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags={SwaggerConfig.SRSNAMES_TAG_NAME})
@RestController
@RequestMapping(value="public_srsnames")
public class SrsnamesController extends BaseRestController {

	private static final Logger LOG = LoggerFactory.getLogger(SrsnamesController.class);

	private PCodeDao pCodeDao;
	private ContentNegotiationStrategy contentStrategy;

	@Autowired
	public SrsnamesController(final LastUpdateDao lastUpdateDao, final PCodeDao pCodeDao, ContentNegotiationStrategy contentStrategy) {
		this.lastUpdateDao = lastUpdateDao;
		this.pCodeDao = pCodeDao;
		this.contentStrategy = contentStrategy;
	}

	@ApiOperation(value="Return the list of NWIS Public SRS Names.")
	@GetMapping(produces={MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE, MEDIA_TYPE_TEXT_CSV_UTF8_VALUE})
	public Object getPublicSrsnamesJson(HttpServletRequest request, HttpServletResponse response, @ApiIgnore WebRequest webRequest) throws HttpMediaTypeNotAcceptableException {
		LOG.debug("publicsrsnamesJson");
		if (isNotModified(webRequest)) {
			return null;
		} else {
			//This is off of the last system-wide etl rather than the public_srsnames.max_last_modified column
			//since it might be possible for codes to go away without updating the former date...
			String maxLastRevDate = getMaxLastRevDate();
			List<LinkedHashMap<String, Object>> data = pCodeDao.getRows();

			List<MediaType> mediaTypes = contentStrategy.resolveMediaTypes(new ServletWebRequest(request));
			if (mediaTypes.contains(MEDIA_TYPE_TEXT_CSV_UTF8)) {
				doCsv(response, maxLastRevDate.replace(" ", "_"), data);
				return null;
			} else {
				return doJson(maxLastRevDate, data);
			}
		}
	}

	protected String getMaxLastRevDate() {
		String rtn = "";
		Date maxLastRevDate = pCodeDao.getLastModified();
		if (null != maxLastRevDate) {
			rtn = new SimpleDateFormat("MMMMM yyyy").format(maxLastRevDate);
		}
		return rtn;
	}

	protected Object doJson(String maxlastRevDate, List<LinkedHashMap<String, Object>> data) {
		Map<String, Object> rtn = new HashMap<>();
		rtn.put("maxLastRevDate", maxlastRevDate);
		rtn.put("pcodes", data);
		return rtn;
	}

	public void doCsv(HttpServletResponse response, String maxlastRevDate, List<LinkedHashMap<String, Object>> data) {
		response.setHeader(HEADER_CONTENT_DISPOSITION, "attachment;filename=\"public_srsnames_" + maxlastRevDate + ".zip\"");
		response.setCharacterEncoding(DEFAULT_ENCODING);
		try {
			ZipOutputStream gzipOut = new ZipOutputStream(response.getOutputStream());
			gzipOut.putNextEntry(new ZipEntry("public_srsnames_" + maxlastRevDate + ".csv"));
			PrintWriter out = new PrintWriter(gzipOut);
			writeCsvData(out, data);
			out.flush();
			gzipOut.closeEntry();
			gzipOut.close();
		} catch (IOException e) {
			throw new RuntimeException("publicsrsnames: Could not open output stream for csv download.", e);
		}
	}

	protected void writeCsvData(final PrintWriter out, final List<LinkedHashMap<String, Object>> data) {
		Iterator<String> colList = data.get(0).keySet().iterator();
		while (colList.hasNext()) {
			String colName = colList.next();
			out.print("\"" + colName.toLowerCase() + "\"");
			if (colList.hasNext()) {
				out.print(",");
			}
		}
		out.println();

		Iterator<LinkedHashMap<String, Object>> pCodeIterator = data.iterator();
		while (pCodeIterator.hasNext()) {
			Map<String, Object> pCode = pCodeIterator.next();
			Iterator<Object> cols = pCode.values().iterator();
			while (cols.hasNext()) {
				Object colVal = cols.next();
				out.print("\"" + colVal + "\"");
				if (cols.hasNext()) {
					out.print(",");
				}
			}
			out.println();
		}
	}

}
