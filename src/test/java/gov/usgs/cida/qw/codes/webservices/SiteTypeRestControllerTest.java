package gov.usgs.cida.qw.codes.webservices;

import gov.usgs.cida.qw.IntegrationTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@Category(IntegrationTest.class)
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/siteType.xml")
})
@DatabaseTearDown("classpath:/testData/clearAll.xml")
public class SiteTypeRestControllerTest extends BaseCodesRestControllerTest {

	public static String TEST_ENDPOINT = "/codes/sitetypes";
	public static String TEST_LEGACY_ENDPOINT = "/codes/sitetype";
	public static String CODE_VALUE = "Lake, Reservoir, Impoundment";
	public static String CODE_JSON = "{\"value\":\"Lake, Reservoir, Impoundment\",\"providers\":\"STEWARDS\"}";
	public static String CODE_XML = XML_HEADER +"<Code value=\"Lake, Reservoir, Impoundment\" providers=\"STEWARDS\"/>";
	public static String SEARCH_TEXT = "re";
	public static String SEARCH_JSON = "{\"codes\":[{\"value\":\"Aggregate surface-water-use\",\"providers\":\"NWIS STEWARDS STORET\"}],\"recordCount\":5}";
	public static String SEARCH_XML = XML_HEADER + "<Codes><Code value=\"Aggregate surface-water-use\" providers=\"NWIS STEWARDS STORET\"/><recordCount>5</recordCount></Codes>"; 
	public static String COMPARE_FILE_JSON = "siteType.json";
	public static String COMPARE_FILE_XML = "siteType.xml";
	
	@Test
	public void getListAsJsonTest() throws Exception {
		runGetListAsJsonTest(TEST_ENDPOINT, SEARCH_TEXT, COMPARE_FILE_JSON, SEARCH_JSON);
		runGetListAsJsonTest(TEST_LEGACY_ENDPOINT, SEARCH_TEXT, COMPARE_FILE_JSON, SEARCH_JSON);
    }

	@Test
	public void getListAsXmlTest() throws Exception {
		runGetListAsXmlTest(TEST_ENDPOINT, SEARCH_TEXT, COMPARE_FILE_XML, SEARCH_XML);
		runGetListAsXmlTest(TEST_LEGACY_ENDPOINT, SEARCH_TEXT, COMPARE_FILE_XML, SEARCH_XML);
	}

	@Test
	public void getCodeAsJsonTest() throws Exception {
		runGetCodeAsJson(TEST_ENDPOINT, CODE_VALUE, CODE_JSON);
		runGetCodeAsJson(TEST_LEGACY_ENDPOINT, CODE_VALUE, CODE_JSON);
	}

	@Test
	public void getCodeAsXmlTest() throws Exception {
		runGetCodeAsXml(TEST_ENDPOINT, CODE_VALUE, CODE_XML);
		runGetCodeAsXml(TEST_LEGACY_ENDPOINT, CODE_VALUE, CODE_XML);
	}

}
