package gov.usgs.cida.qw.webservice;

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

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	private IPCodeDao pCodeDao;

	@RequestMapping(value={"publicsrsnames", "public_srsnames"}, method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Map<String, Object> getPublicSrsnamesJson(HttpServletRequest request, HttpServletResponse response) {
		log.debug("publicsrsnamesJson");
		Map<String, Object> rtn = new HashMap<>();
		if (request.getParameterMap().isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} else {
			response.setCharacterEncoding("UTF-8");
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
		log.debug("publicsrsnamesCsv");
		if (request.getParameterMap().isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} else {
			response.setCharacterEncoding("UTF-8");
			List<LinkedHashMap<String, Object>> data = pCodeDao.getRows();
			String dateString = "";
			Date maxLastRevDate = pCodeDao.getLastModified();
			if (null != maxLastRevDate) {
				dateString = new SimpleDateFormat("MMMMM_yyyy").format(maxLastRevDate);
			}

			response.setContentType("text/csv");
			response.setHeader("Content-disposition", "attachment;filename=\"public_srsnames_" + dateString + ".csv\"");

			try {
				PrintWriter out = new PrintWriter(response.getOutputStream());
				doCsv(out, data);
				out.flush();
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
			LinkedHashMap<String, Object> pCode = pCodeIterator.next();
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
