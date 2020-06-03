package gov.usgs.wma.qw.summary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import gov.usgs.wma.qw.BaseRestController;
import gov.usgs.wma.qw.LastUpdateDao;
import gov.usgs.wma.qw.srsnames.SrsnamesController;
import gov.usgs.wma.qw.summary.SldTemplateEngine.MapDataSource;
import gov.usgs.wma.qw.summary.SldTemplateEngine.MapGeometry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="National Results Coverage Map SLD", description="Download")
@RestController
@RequestMapping(value="summary", produces=BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE)
public class SummaryController extends BaseRestController {

	private static final Logger LOG = LoggerFactory.getLogger(SrsnamesController.class);

	private SummaryDao summaryDao;

	@Autowired
	public SummaryController(final LastUpdateDao lastUpdateDao, final SummaryDao summaryDao) {
		this.lastUpdateDao = lastUpdateDao;
		this.summaryDao = summaryDao;
	}

	@Operation(description="Return the requested National Results Coverage Map SLD.")
	@GetMapping
	public String getSummarySld(
			@Parameter(
					description="A=All; E=EPA; N=NWIS",
					schema=@Schema(allowableValues={"A","E","N"})
					)
			final @RequestParam(value="dataSource") String dataSource,
			@Parameter(
					description="S=States; C=Counties; H=Huc8",
					schema=@Schema(allowableValues={"S","C","H"})
					)
			final @RequestParam(value="geometry") String geometry,
			@Parameter(
					description="A=All; 1=Last 12 Months; 5=Last 5 Years",
					schema=@Schema(allowableValues={"A","1","5"})
					)
			final @RequestParam(value="timeFrame") String timeFrame,
			HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) throws IOException {
		LOG.debug("summary");
		response.setCharacterEncoding("ISO-8859-1");
		if (isNotModified(webRequest)) {
			return null;
		} else {
			MapDataSource mapDataSource = MapDataSource.fromAbbreviation(dataSource);
			MapGeometry mapGeometry = MapGeometry.fromAbbreviation(geometry);

			Map<String, Object> dbparms = deriveDbParams(mapDataSource, mapGeometry, timeFrame);
			String[] binValues = retrieveBinValues(dbparms);
			if (SldTemplateEngine.COLOR_COUNT > binValues.length) {
				response.sendError(HttpStatus.NO_CONTENT.value());
				return null;
			} else {
				return SldTemplateEngine.generateDynamicStyle(mapDataSource, mapGeometry, binValues, "binSLDTemplate.vm");
			}
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
					if (previousMax > 0){
						//just to make sure there are no holes in the bin
						binValues[i*2] = String.valueOf(previousMax+1);
					}else{
						binValues[i*2] = String.valueOf(binVals.getCounts().get(1));
					}
					previousMax = binVals.getCounts().get(2);
					binValues[i*2+1] = String.valueOf(previousMax);
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
		private List<Integer> counts = new ArrayList<Integer>();
		public void setValue(Integer value) {
			counts.add(value);
		}
		public List<Integer> getCounts() {
			return counts;
		}
	}
}
