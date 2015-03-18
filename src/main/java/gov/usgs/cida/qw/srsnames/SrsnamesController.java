package gov.usgs.cida.qw.srsnames;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.LastUpdateDao;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

//This off of the last system-wide etl rather than the public_srsnames.max_last_modified column
//since it might be possible for codes to go away without updating the former date...
@RestController
@RequestMapping(value={"publicsrsnames", "public_srsnames"}, method=RequestMethod.GET)
public class SrsnamesController extends BaseRestController {

    private static final Logger LOG = LoggerFactory.getLogger(SrsnamesController.class);
    public static final String MIME_TYPE_TEXT_CSV = "text/csv";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-disposition";
    
    private PCodeDao pCodeDao;

    @Autowired
	public SrsnamesController(@Qualifier("lastUpdateDao") final LastUpdateDao lastUpdateDao,
			@Qualifier("pCodeDao") final PCodeDao pCodeDao) {
    	this.lastUpdateDao = lastUpdateDao;
    	this.pCodeDao = pCodeDao;
    }

    @RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getPublicSrsnamesJson(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {
        LOG.debug("publicsrsnamesJson");
        if (isNotModified(webRequest)) {
            return null;
        } else {
	        Map<String, Object> rtn = new HashMap<>();
	        response.setCharacterEncoding(DEFAULT_ENCODING);
	        List<LinkedHashMap<String, Object>> data = pCodeDao.getRows();
	        String maxLastRevStr = "";
	        Date maxLastRevDate = pCodeDao.getLastModified();
	        if (null != maxLastRevDate) {
	            maxLastRevStr = new SimpleDateFormat("MMMMM yyyy").format(maxLastRevDate);
	        }
	
	        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	        rtn.put("maxLastRevDate", maxLastRevStr);
	        rtn.put("pcodes", data);
	        return rtn;
	    }
    }

    @RequestMapping(produces=MIME_TYPE_TEXT_CSV)
    public void getPublicSrsnamesCsv(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {
        LOG.debug("publicsrsnamesCsv");
        if (!isNotModified(webRequest)) {
	        response.setCharacterEncoding(DEFAULT_ENCODING);
	        List<LinkedHashMap<String, Object>> data = pCodeDao.getRows();
	        String dateString = "";
	        Date maxLastRevDate = pCodeDao.getLastModified();
	        if (null != maxLastRevDate) {
	            dateString = new SimpleDateFormat("MMMMM_yyyy").format(maxLastRevDate);
	        }
	
	        response.setHeader(HEADER_CONTENT_DISPOSITION, "attachment;filename=\"public_srsnames_" + dateString + ".zip\"");
	
	        try {
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
