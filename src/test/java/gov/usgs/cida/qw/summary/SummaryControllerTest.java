package gov.usgs.cida.qw.summary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;

import gov.usgs.cida.qw.BaseSpringTest;
import gov.usgs.cida.qw.DatabaseRequiredTest;
import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.summary.SldTemplateEngine.MapDataSource;
import gov.usgs.cida.qw.summary.SldTemplateEngine.MapGeometry;

@Category(DatabaseRequiredTest.class)
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/summary.xml")
})
public class SummaryControllerTest extends BaseSpringTest {

	@Autowired
	private LastUpdateDao lastUpdateDao;
	@Autowired
	private SummaryDao summaryDao;
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

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
	public void getSummarySldTest() throws Exception {
		MvcResult rtn = mockMvc.perform(get("/summary?dataSource=A&geometry=S&timeFrame=1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(new MediaType("application", "xml", Charset.forName("ISO-8859-1"))))
				.andExpect(content().encoding("ISO-8859-1"))
				.andReturn();
		assertThat(rtn.getResponse().getContentAsString(), isSimilarTo(getCompareFile("summary.sld")).ignoreWhitespace().throwComparisonFailure());
	}

}
