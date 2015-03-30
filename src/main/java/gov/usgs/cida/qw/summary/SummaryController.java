package gov.usgs.cida.qw.summary;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.srsnames.SrsnamesController;
import gov.usgs.cida.qw.summary.SldTemplateEngine.MapDataSource;
import gov.usgs.cida.qw.summary.SldTemplateEngine.MapGeometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;


@RestController
@RequestMapping("Summary")
public class SummaryController extends BaseRestController {

    private static final Logger LOG = LoggerFactory.getLogger(SrsnamesController.class);
    public static final String MIME_TYPE_TEXT_CSV = "text/csv";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-disposition";
    
    private SummaryDao summaryDao;

    @Autowired
	public SummaryController(@Qualifier("lastUpdateDao") final LastUpdateDao lastUpdateDao,
			@Qualifier("summaryDao") final SummaryDao summaryDao) {
    	this.lastUpdateDao = lastUpdateDao;
    	this.summaryDao = summaryDao;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String getSummarySld(final @RequestParam(value="dataSource") String dataSource,
    		final @RequestParam(value="geometry") String geometry,
    		final @RequestParam(value="timeFrame") String timeFrame,
    		HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {
        LOG.debug("summary");
        if (isNotModified(webRequest)) {
            return null;
        } else {
    		MapDataSource mapDataSource = MapDataSource.fromAbbreviation(dataSource);
    		MapGeometry mapGeometry = MapGeometry.fromAbbreviation(geometry);

        	Map<String, Object> dbparms = deriveDbParams(mapDataSource, mapGeometry, timeFrame);
        	String[] binValues = retrieveBinValues(dbparms);
    		return SldTemplateEngine.generateDynamicStyle(mapDataSource, mapGeometry, binValues, "binSLDTemplate.vm");
        }
    }

	protected String[] retrieveBinValues(Map<String, Object> parms) {
		String[] binValues = new String[0];
		if (null != summaryDao && null != parms && 3 == parms.size()) {
			List<RowCounts> bins = summaryDao.retrieveCounts(parms);
			if (bins != null) {
				binValues = new String[bins.size()*2];
				Integer previousMax = -1;
				int i=0;
				for (RowCounts binVals : bins) {
					//value one is the bin number, not currently needed
					//binVals.getCounts().get(1)
					if (previousMax > 0){
						//just to make sure there are no holes in the bin
						binValues[i*2] = String.valueOf(previousMax+1);
					}else{
						binValues[i*2] = String.valueOf(binVals.getCounts().get(1));
					}
					previousMax = binVals.getCounts().get(2);
					binValues[i*2+1] = String.valueOf(previousMax);
					 
					//no present need for the count of data points in the given bin
					//binVals.getCounts().get(3);
					i++;
				}
			}
		}
		return binValues;
	}

	protected Map<String, Object> deriveDbParams(MapDataSource mapDataSource, MapGeometry mapGeometry, String timeFrame) {
		Map<String, Object> parms = new HashMap<String, Object>();
		Object[] ds = getDataSources(mapDataSource);
		String geom = getGeometry(mapGeometry);
		String tf = getTimeFrame(timeFrame);
		if(0 < ds.length && null != geom && null != tf) {
			parms.put("sources", ds);
			parms.put("geometry", geom);
			parms.put("timeFrame", tf);
		}
		return parms;
	}
	
	protected Object[] getDataSources(MapDataSource mapDataSource) {
		List<String> rtn = new ArrayList<>();
		if (null != mapDataSource) {
			switch (mapDataSource) {
			case All:
				for (int i = 0; i < MapDataSource.values().length; i++) {
					MapDataSource tempSource = MapDataSource.values()[i];
					if (!MapDataSource.All.equals(tempSource)) {
						rtn.add(tempSource.getStringAbbreviation());
					}
				}
				break;
			default:
				rtn.add(mapDataSource.getStringAbbreviation());
				break;
			}
		}
		return rtn.toArray();
	}
	
	protected String getGeometry(MapGeometry mapGeometry) {
		if (null != mapGeometry) {
			return mapGeometry.toString();
		} else {
			return null;
		}
	}
	
	protected String getTimeFrame(String timeFrame) {
		String rtn = "ALL_TIME";
		if (timeFrame != null && timeFrame.length() > 0) {
			char timeFrameChar = timeFrame.charAt(0);
			switch (timeFrameChar) {
			case '5':
				rtn = "PAST_60_MONTHS";
				break;
			case '1':
				rtn = "PAST_12_MONTHS";
				break;
			default:
				break;
			}
		} 
		return rtn;
	}
	
	public static class RowCounts {
		List<Integer> counts = new ArrayList<Integer>();
		public void setValue(Integer value) {
			counts.add(value);
		}
		public List<Integer> getCounts() {
			return counts;
		}
		
	}
}
