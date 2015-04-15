package gov.usgs.cida.qw.summary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.usgs.cida.qw.BaseSpringTest;
import gov.usgs.cida.qw.IntegrationTest;
import gov.usgs.cida.qw.summary.SummaryController.RowCounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@Category(IntegrationTest.class)
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/summary.xml")
})
@DatabaseTearDown("classpath:/testData/clearAll.xml")
public class SummaryDaoTest extends BaseSpringTest {

    @Autowired
    private SummaryDao summaryDao;

    @Test
    public void retrieveCountsTestNAS() {
    	RowCounts rowCounts1 = new RowCounts();
    	rowCounts1.setValue(1);
    	rowCounts1.setValue(80);
    	rowCounts1.setValue(80);
    	rowCounts1.setValue(1);
    	RowCounts rowCounts2 = new RowCounts();
    	rowCounts2.setValue(2);
    	rowCounts2.setValue(270);
    	rowCounts2.setValue(270);
    	rowCounts2.setValue(1);
    	RowCounts rowCounts3 = new RowCounts();
    	rowCounts3.setValue(3);
    	rowCounts3.setValue(2720);
    	rowCounts3.setValue(2720);
    	rowCounts3.setValue(1);
    	RowCounts rowCounts4 = new RowCounts();
    	rowCounts4.setValue(4);
    	rowCounts4.setValue(4430);
    	rowCounts4.setValue(4430);
    	rowCounts4.setValue(1);
    	RowCounts rowCounts5 = new RowCounts();
    	rowCounts5.setValue(5);
    	rowCounts5.setValue(4520);
    	rowCounts5.setValue(4520);
    	rowCounts5.setValue(1);
    					
    	List<RowCounts> expectedBins = new ArrayList<>();
    	expectedBins.add(rowCounts1);
    	expectedBins.add(rowCounts2);
    	expectedBins.add(rowCounts3);
    	expectedBins.add(rowCounts4);
    	expectedBins.add(rowCounts5);
    	
		Map<String, Object> params = new HashMap<>();
		params.put("sources", new String[]{"N"});
		params.put("timeFrame","ALL_TIME");
		params.put("geometry","States");
		List<RowCounts> bins = summaryDao.retrieveCounts(params);
		assertBins(expectedBins, bins, 1, 1);
    }

    @Test
    public void retrieveCountsTestE12C() {
    	RowCounts rowCounts1 = new RowCounts();
    	rowCounts1.setValue(1);
    	rowCounts1.setValue(2);
    	rowCounts1.setValue(8);
    	rowCounts1.setValue(4);
    	RowCounts rowCounts2 = new RowCounts();
    	rowCounts2.setValue(2);
    	rowCounts2.setValue(10);
    	rowCounts2.setValue(37);
    	rowCounts2.setValue(4);
    	RowCounts rowCounts3 = new RowCounts();
    	rowCounts3.setValue(3);
    	rowCounts3.setValue(39);
    	rowCounts3.setValue(44);
    	rowCounts3.setValue(4);
    	RowCounts rowCounts4 = new RowCounts();
    	rowCounts4.setValue(4);
    	rowCounts4.setValue(45);
    	rowCounts4.setValue(50);
    	rowCounts4.setValue(3);
    	RowCounts rowCounts5 = new RowCounts();
    	rowCounts5.setValue(5);
    	rowCounts5.setValue(53);
    	rowCounts5.setValue(58);
    	rowCounts5.setValue(3);
    					
    	List<RowCounts> expectedBins = new ArrayList<>();
    	expectedBins.add(rowCounts1);
    	expectedBins.add(rowCounts2);
    	expectedBins.add(rowCounts3);
    	expectedBins.add(rowCounts4);
    	expectedBins.add(rowCounts5);
    	
		Map<String, Object> params = new HashMap<>();
		params.put("sources", new String[]{"E"});
		params.put("timeFrame","PAST_12_MONTHS");
		params.put("geometry","Counties");
		List<RowCounts> bins = summaryDao.retrieveCounts(params);
		assertBins(expectedBins, bins, 3, 4);
    }

    @Test
    public void retrieveCountsTestB60H() {
    	RowCounts rowCounts1 = new RowCounts();
    	rowCounts1.setValue(1);
    	rowCounts1.setValue(9);
    	rowCounts1.setValue(14);
    	rowCounts1.setValue(2);
    	RowCounts rowCounts2 = new RowCounts();
    	rowCounts2.setValue(2);
    	rowCounts2.setValue(25);
    	rowCounts2.setValue(31);
    	rowCounts2.setValue(2);
    	RowCounts rowCounts3 = new RowCounts();
    	rowCounts3.setValue(3);
    	rowCounts3.setValue(35);
    	rowCounts3.setValue(56);
    	rowCounts3.setValue(2);
    	RowCounts rowCounts4 = new RowCounts();
    	rowCounts4.setValue(4);
    	rowCounts4.setValue(728);
    	rowCounts4.setValue(728);
    	rowCounts4.setValue(1);
    	RowCounts rowCounts5 = new RowCounts();
    	rowCounts5.setValue(5);
    	rowCounts5.setValue(910);
    	rowCounts5.setValue(910);
    	rowCounts5.setValue(1);
    					
    	List<RowCounts> expectedBins = new ArrayList<>();
    	expectedBins.add(rowCounts1);
    	expectedBins.add(rowCounts2);
    	expectedBins.add(rowCounts3);
    	expectedBins.add(rowCounts4);
    	expectedBins.add(rowCounts5);
    	
		Map<String, Object> params = new HashMap<>();
		params.put("sources", new String[]{"N", "E"});
		params.put("timeFrame","PAST_60_MONTHS");
		params.put("geometry","Huc8");
		List<RowCounts> bins = summaryDao.retrieveCounts(params);
		assertBins(expectedBins, bins, 1, 2);
    }
    
    private void assertBins(List<RowCounts> expectedBins, List<RowCounts> bins, int minBinCnt, int maxBinCnt) {
		assertEquals(5, bins.size());
		int prevMax = 1;
		for (int i=0; i<bins.size(); i++) {
			assertEquals(i+1, bins.get(i).counts.get(0).intValue());
			assertTrue("bin "+ i +" min <= prev bin max", prevMax <= bins.get(i).counts.get(1).intValue());
			assertTrue("bin "+ i +" min <= bin max", bins.get(i).counts.get(1).intValue() <= bins.get(i).counts.get(2).intValue());
			assertTrue("bin "+ i +" count >= min count", minBinCnt <= bins.get(i).counts.get(3).intValue());
			assertTrue("bin "+ i +" count <= min count", maxBinCnt >= bins.get(i).counts.get(3).intValue());
			prevMax = bins.get(i).counts.get(2).intValue();
			for (int j=0; j<expectedBins.get(i).counts.size(); j++) {
				assertEquals(expectedBins.get(i).counts.get(j), bins.get(i).counts.get(j));
			}
		}
    }
}
