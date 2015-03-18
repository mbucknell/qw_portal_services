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

    //TODO
//The <Environment name="wqp.geoserver.coverage.sld.endpoint" type="java.lang.String" value="http://cida-eros-wqpdev.er.usgs.gov:8080/nwisqw/Summary"/>
//eventually goes here.
	
//	return Config.SLD_ENDPOINT +
//			'?dataSource=' + CoverageMapConfig.SLD_DATASOURCE[source] +
//			'&geometry=' + CoverageMapConfig.SLD_GEOMETRY[display_by] +
//			'&timeFrame=' + CoverageMapConfig.SLD_TIMEFRAME[date_filter];

    @RequestMapping(method=RequestMethod.GET)
    public String getSummarySld(final @RequestParam(value="dataSource") String[] dataSource,
    		final @RequestParam(value="geometry") String[] geometry,
    		final @RequestParam(value="timeFrame") String[] timeFrame,
    		HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {
        LOG.debug("publicsrsnamesJson");
        if (isNotModified(webRequest)) {
            return null;
        } else {
    		MapDataSource source = MapDataSource.fromAbbreviation(dataSource[0]);
    		MapGeometry geom = MapGeometry.fromAbbreviation(geometry[0]);
    		String tf = "ALL_TIME";
    		if (timeFrame != null && timeFrame.length > 0 && timeFrame[0].length() > 0) {
    			char timeFrameChar = timeFrame[0].charAt(0);
    			switch (timeFrameChar) {
    			case '5':
    				tf = "PAST_60_MONTHS";
    				break;
    			case '1':
    				tf = "PAST_12_MONTHS";
    				break;
    			default:
    				break;
    			}
    		} 
    		
    		
    		
    		
    		
    		
    		return SldTemplateEngine.generateDynamicStyle(source, geom, retrieveBinValues(source, geom, tf), "binSLDTemplate.vm");
        }
    }

	
//	private XMLStreamReader lookupDiscreteSampleCount(IRPCRequest request) {
//		//replace with
//		Map<String, Object> queryParams = preProcessAndValidateParams(request);
//		String[] sourceString = (String[]) queryParams.get("dataSource");
//		MapDataSource source = MapDataSource.fromAbbreviation(sourceString[0]);
//		
//		String[] geometryString = (String[]) queryParams.get("geometry");
//		MapGeometry geometry = MapGeometry.fromAbbreviation(geometryString[0]);
//		
//		String[] timeFrameString = (String[]) queryParams.get("timeFrame");
//		String timeFrame = "ALL_TIME";
//		if (timeFrameString != null && timeFrameString.length > 0 && timeFrameString[0].length() > 0) {
//			char timeFrameChar = timeFrameString[0].charAt(0);
//			switch (timeFrameChar) {
//			case '5':
//				timeFrame = "PAST_60_MONTHS";
//				break;
//			case '1':
//				timeFrame = "PAST_12_MONTHS";
//				break;
//			default:
//				break;
//			}
//		}
//		
//		String sldXML = getDynamicStyle(source, geometry, timeFrame);
//		XMLStreamReader streamReader = null;
//		try {
//			StringReader stringReader = new StringReader(sldXML);
//	        streamReader = XMLInputFactory.newInstance().createXMLStreamReader(stringReader);
//
//		} catch (Exception e) {
////			throw new WebServiceFrameworkException(e);
//		}
//
//		return streamReader;
//	}
	
//	public String getDynamicStyle(MapDataSource source, MapGeometry geom, String timeFrame) {
//		String key = "" + source.getStringAbbreviation() + geom.getStringAbbreviation();
//		String sld;// = SLD_CACHE.get(key);
//			sld = SldTemplateEngine.generateDynamicStyle(source, geom, retrieveBinValues(source, geom, timeFrame), "binSLDTemplate.vm");
////			if (sld != null) {
////				SLD_CACHE.put(key, sld);
////			}
//		return sld;
//	}
	
	private String[] retrieveBinValues(MapDataSource source, MapGeometry geom, String timeFrame) {
		String[] binValues = new String[0];
		if (summaryDao != null) {
			List<RowCounts> bins = summaryDao.retrieveCounts("discreteSampleCountBin", deriveDbParams(source, geom, timeFrame));
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

	private Map<String, Object> deriveDbParams(MapDataSource source, MapGeometry geom, String timeFrame){
		Map<String, Object> dbParams = new HashMap<String, Object>();
		switch (source) {
		case All:
			List<String> allSourcesList = new ArrayList<String>();;
			for (int i = 0; i < MapDataSource.values().length; i++) {
				MapDataSource tempSource = MapDataSource.values()[i];
				if (!MapDataSource.All.equals(tempSource)) {
					allSourcesList.add(""+tempSource.getStringAbbreviation());
				}
			}
			dbParams.put("sources", allSourcesList.toArray());
			break;
		default:
			dbParams.put("sources", new String[]{""+source.getStringAbbreviation()});
			break;
		}
		dbParams.put("timeFrame", timeFrame);
		dbParams.put("geometry", geom.toString());
		return dbParams;
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
