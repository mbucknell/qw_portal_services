package gov.usgs.wma.qw.summary;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.qw.BaseIT;
import gov.usgs.wma.qw.BaseRestController;
import gov.usgs.wma.qw.LastUpdateDao;
import gov.usgs.wma.qw.summary.SummaryController;
import gov.usgs.wma.qw.summary.SummaryDao;
import gov.usgs.wma.qw.summary.SldTemplateEngine.MapDataSource;
import gov.usgs.wma.qw.summary.SldTemplateEngine.MapGeometry;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/summary.xml")
public class SummaryControllerIT extends BaseIT {

	@Autowired
	private LastUpdateDao lastUpdateDao;
	@Autowired
	private SummaryDao summaryDao;

	@Test
	public void getDataSourcesTest() {
		SummaryController controller = new SummaryController(lastUpdateDao, summaryDao);
		assertEquals(0, controller.getDataSources(null).length);
		assertArrayEquals(new Object[]{"N"}, controller.getDataSources(MapDataSource.USGS));
		assertArrayEquals(new Object[]{"E"}, controller.getDataSources(MapDataSource.EPA));
		assertArrayEquals(new Object[]{"E","N"}, controller.getDataSources(MapDataSource.All));
	}

	@Test
	public void getGeometryTest() {
		SummaryController controller = new SummaryController(lastUpdateDao, summaryDao);
		assertNull(controller.getGeometry(null));
		assertEquals("States", controller.getGeometry(MapGeometry.States));
		assertEquals("Counties", controller.getGeometry(MapGeometry.Counties));
		assertEquals("Huc8", controller.getGeometry(MapGeometry.Huc8));
	}

	@Test
	public void getTimeFrameTest() {
		SummaryController controller = new SummaryController(lastUpdateDao, summaryDao);
		assertEquals("ALL_TIME", controller.getTimeFrame(null));
		assertEquals("ALL_TIME", controller.getTimeFrame(""));
		assertEquals("ALL_TIME", controller.getTimeFrame("QQ"));
		assertEquals("ALL_TIME", controller.getTimeFrame("A"));
		assertEquals("PAST_12_MONTHS", controller.getTimeFrame("1"));
		assertEquals("PAST_60_MONTHS", controller.getTimeFrame("5"));
	}

	@Test
	public void deriveDbParamsTest() {
		SummaryController controller = new SummaryController(lastUpdateDao, summaryDao);
		assertEquals(0, controller.deriveDbParams(null, null, null).size());
		assertEquals(0, controller.deriveDbParams(MapDataSource.All, null, null).size());
		assertEquals(3, controller.deriveDbParams(MapDataSource.All, MapGeometry.Huc8, null).size());
		assertEquals(0, controller.deriveDbParams(MapDataSource.All, null, "A").size());
		assertEquals(0, controller.deriveDbParams(null, MapGeometry.Huc8, null).size());
		assertEquals(0, controller.deriveDbParams(null, MapGeometry.Huc8, "A").size());
		assertEquals(0, controller.deriveDbParams(null, null, "A").size());

		Map<String, Object> parms = controller.deriveDbParams(MapDataSource.All, MapGeometry.Huc8, "A");
		assertEquals(3, parms.size());
		assertTrue(parms.containsKey("sources"));
		assertArrayEquals(new Object[]{"E","N"}, (Object[]) parms.get("sources"));
		assertTrue(parms.containsKey("geometry"));
		assertEquals("Huc8", parms.get("geometry"));
		assertTrue(parms.containsKey("timeFrame"));
		assertEquals("ALL_TIME", parms.get("timeFrame"));

		parms = controller.deriveDbParams(MapDataSource.EPA, MapGeometry.States, "1");
		assertEquals(3, parms.size());
		assertTrue(parms.containsKey("sources"));
		assertArrayEquals(new Object[]{"E"}, (Object[]) parms.get("sources"));
		assertTrue(parms.containsKey("geometry"));
		assertEquals("States", parms.get("geometry"));
		assertTrue(parms.containsKey("timeFrame"));
		assertEquals("PAST_12_MONTHS", parms.get("timeFrame"));

		parms = controller.deriveDbParams(MapDataSource.USGS, MapGeometry.Counties, "5");
		assertEquals(3, parms.size());
		assertTrue(parms.containsKey("sources"));
		assertArrayEquals(new Object[]{"N"}, (Object[]) parms.get("sources"));
		assertTrue(parms.containsKey("geometry"));
		assertEquals("Counties", parms.get("geometry"));
		assertTrue(parms.containsKey("timeFrame"));
		assertEquals("PAST_60_MONTHS", parms.get("timeFrame"));
	}

	@Test
	public void retrieveBinValuesTest() {
		Map<String, Object> parms = new HashMap<>();
		SummaryController controller = new SummaryController(null, null);
		assertArrayEquals(new String[0], controller.retrieveBinValues(null));

		assertArrayEquals(new String[0], controller.retrieveBinValues(parms));

		parms.put("geometry", "States");
		parms.put("timeFrame", "PAST_60_MONTHS");
		assertArrayEquals(new String[0], controller.retrieveBinValues(parms));

		controller = new SummaryController(lastUpdateDao, summaryDao);
		assertArrayEquals(new String[0], controller.retrieveBinValues(parms));

		parms.put("sources", new Object[]{"E","N"});
		String[] bins = controller.retrieveBinValues(parms);
		assertEquals(10, bins.length);
		assertArrayEquals(new String[]{"9", "9", "10", "56", "57", "495", "496", "520", "521", "728"}, bins);
	}

	@Test
	public void getSummarySldTest(@Autowired TestRestTemplate restTemplate) throws Exception {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/summary?dataSource=A&geometry=S&timeFrame=1", String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(rtn.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0), equalTo(BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE));
		assertThat(rtn.getBody(), isSimilarTo(getCompareFile("summary.sld")).ignoreWhitespace().throwComparisonFailure());
	}

}
