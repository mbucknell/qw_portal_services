package gov.usgs.cida.qw.codes.webservices;

import gov.usgs.cida.qw.DatabaseRequiredTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;

@Category(DatabaseRequiredTest.class)
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/stationCode.xml")
})
public class MonitoringLocationRestControllerTest extends BaseCodesRestControllerTest{
	
	public static String TEST_ENDPOINT = "/codes/monitoringlocation";
	public static String CODE_VALUE = "USGS-07083000";
	public static String CODE_JSON = "{\"value\":\"USGS-07083000\",\"desc\":\"HALFMOON CREEK NEAR MALTA, CO\",\"providers\":\"BIODATA NWIS\"}";
	public static String CODE_XML = XML_HEADER + "<Code value=\"USGS-07083000\" desc=\"HALFMOON CREEK NEAR MALTA, CO\" providers=\"BIODATA NWIS\"/>";
	public static String SEARCH_TEXT = "USGS-070837";
	public static String SEARCH_JSON = "{\"codes\":[{\"value\":\"USGS-07083710\",\"desc\":\"ARKANSAS RIVER BELOW EMPIRE GULCH NEAR MALTA, CO\",\"providers\":\"NWIS\"}],\"recordCount\":2}";
	public static String SEARCH_XML = XML_HEADER + "<Codes><Code value=\"USGS-07083710\" desc=\"ARKANSAS RIVER BELOW EMPIRE GULCH NEAR MALTA, CO\" providers=\"NWIS\"/><recordCount>2</recordCount></Codes>"; 
	public static String COMPARE_FILE_JSON = "stationCode.json";
	public static String COMPARE_FILE_XML = "stationCode.xml";

	@Test
	public void getListAsJsonTest() throws Exception {
		runGetListAsJsonTest(TEST_ENDPOINT, SEARCH_TEXT, COMPARE_FILE_JSON, SEARCH_JSON);
	}

	@Test
	public void getListAsXmlTest() throws Exception {
		runGetListAsXmlTest(TEST_ENDPOINT, SEARCH_TEXT, COMPARE_FILE_XML, SEARCH_XML);
	}

	@Test
	public void getCodeAsJsonTest() throws Exception {
		runGetCodeAsJson(TEST_ENDPOINT, CODE_VALUE, CODE_JSON);
	}

	@Test
	public void getCodeAsXmlTest() throws Exception {
		runGetCodeAsXml(TEST_ENDPOINT, CODE_VALUE, CODE_XML);
	}
}
