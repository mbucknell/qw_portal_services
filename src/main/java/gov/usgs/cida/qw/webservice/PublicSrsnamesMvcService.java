package gov.usgs.cida.qw.webservice;

import gov.usgs.cida.qw.QWConstants;
import gov.usgs.cida.qw.dao.intfc.IPCodeDao;

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

import javax.annotation.Resource;
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
public class PublicSrsnamesMvcService extends MvcService {

	private static final Logger LOG = LoggerFactory.getLogger(PublicSrsnamesMvcService.class);

	@Resource
	private IPCodeDao pCodeDao;

	@RequestMapping(value={"publicsrsnames", "public_srsnames"}, method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Map<String, Object> getPublicSrsnamesJson(HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("publicsrsnamesJson");
		Map<String, Object> rtn = new HashMap<>();
		if (request.getParameterMap().isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} else {
			response.setCharacterEncoding(QWConstants.DEFAULT_ENCODING);
			List<LinkedHashMap<String, Object>> data = pCodeDao.getRows();
			String maxLastRevStr = "";
			Date maxLastRevDate = pCodeDao.getLastModified();
			if (null != maxLastRevDate) {
				maxLastRevStr = new SimpleDateFormat("MMMMM yyyy").format(maxLastRevDate);
			}

			response.setContentType("application/json");
			rtn.put("maxLastRevDate", maxLastRevStr);
			rtn.put("pcodes", data);
		}
		return rtn;
	}

	@RequestMapping(value={"publicsrsnames", "public_srsnames"}, method=RequestMethod.GET, produces="text/csv")
	public void getPublicSrsnamesCsv(HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("publicsrsnamesCsv");
		if (request.getParameterMap().isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} else {
			response.setCharacterEncoding(QWConstants.DEFAULT_ENCODING);
			//            response.setHeader("Content-Encoding", "gzip");
			List<LinkedHashMap<String, Object>> data = pCodeDao.getRows();
			String dateString = "";
			Date maxLastRevDate = pCodeDao.getLastModified();
			if (null != maxLastRevDate) {
				dateString = new SimpleDateFormat("MMMMM_yyyy").format(maxLastRevDate);
			}

			//            response.setContentType("text/csv");
			//            response.setHeader("Content-disposition", "attachment;filename=\"public_srsnames_" + dateString + ".csv\"");
			response.setHeader("Content-disposition", "attachment;filename=\"public_srsnames_" + dateString + ".zip\"");

			try {
				//                GZIPOutputStream gzipOut = new GZIPOutputStream(response.getOutputStream(), true);
				ZipOutputStream gzipOut = new ZipOutputStream(response.getOutputStream());
				gzipOut.putNextEntry(new ZipEntry("public_srsnames_" + dateString + ".csv"));
				PrintWriter out = new PrintWriter(gzipOut);
				doCsv(out, data);
				out.flush();
				gzipOut.closeEntry();
				gzipOut.close();
			} catch (IOException e) {
				throw new RuntimeException("publicsrsnames: Could not open output stream for csv download.", e);
			}
		}
	}

	protected void doCsv(final PrintWriter out, final List<LinkedHashMap<String, Object>> data) {
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
