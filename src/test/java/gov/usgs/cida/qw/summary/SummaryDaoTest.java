package gov.usgs.cida.qw.summary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.usgs.cida.qw.summary.SummaryController.RowCounts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SummaryDaoTest extends gov.usgs.cida.qw.BaseSpringTest {

    @Autowired
    private SummaryDao summaryDao;

    @Test
    public void retrieveCountsTest() {
		Map<String, Object> params = new HashMap<>();
		params.put("sources", new String[]{"N"});
		params.put("timeFrame","ALL_TIME");
		params.put("geometry","States");
		List<RowCounts> bins = summaryDao.retrieveCounts(params);
		assertBins(bins, 10, 11);

		params.put("sources", new String[]{"E"});
		params.put("timeFrame","PAST_12_MONTHS");
		params.put("geometry","Counties");
		bins = summaryDao.retrieveCounts(params);
		assertBins(bins, 291, 292);

		params.put("sources", new String[]{"N", "E"});
		params.put("timeFrame","PAST_60_MONTHS");
		params.put("geometry","Huc8");
		bins = summaryDao.retrieveCounts(params);
		assertBins(bins, 405, 406);
    }
    
    private void assertBins(List<RowCounts> bins, int minBinCnt, int maxBinCnt) {
		assertEquals(5, bins.size());
		int prevMax = 1;
		for (int i=0 ; i<bins.size() ; i++) {
			assertEquals(i+1, bins.get(i).counts.get(0).intValue());
			assertTrue(prevMax <= bins.get(i).counts.get(1).intValue());
			assertTrue(bins.get(i).counts.get(1).intValue() < bins.get(i).counts.get(2).intValue());
			assertTrue(minBinCnt <= bins.get(i).counts.get(3).intValue());
			assertTrue(maxBinCnt >= bins.get(i).counts.get(3).intValue());
			prevMax = bins.get(i).counts.get(2).intValue();
		}
    }
}
