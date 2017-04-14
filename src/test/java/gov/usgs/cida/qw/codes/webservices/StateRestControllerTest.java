package gov.usgs.cida.qw.codes.webservices;

import gov.usgs.cida.qw.DatabaseRequiredTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;

@Category(DatabaseRequiredTest.class)
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/stateCode.xml")
})
public class StateRestControllerTest extends BaseCodesRestControllerTest {

	public static String TEST_ENDPOINT = "/codes/states";
	public static String TEST_LEGACY_ENDPOINT = "/codes/statecode";
	public static String CODE_VALUE = "US:19";
	public static String CODE_JSON = "{\"value\":\"US:19\",\"desc\":\"US, IOWA\",\"providers\":\"STEWARDS\"}";
	public static String CODE_XML = XML_HEADER +"<Code value=\"US:19\" desc=\"US, IOWA\" providers=\"STEWARDS\"/>";
	public static String SEARCH_TEXT_WO = "a";
	public static String SEARCH_JSON_WO = "{\"codes\":[{\"value\":\"US:31\",\"desc\":\"NEBRASKA\",\"providers\":\"STEWARDS STORET\"}],\"recordCount\":6}";
	public static String SEARCH_XML_WO = XML_HEADER + "<Codes><Code value=\"US:31\" desc=\"NEBRASKA\" providers=\"STEWARDS STORET\"/><recordCount>6</recordCount></Codes>"; 
	public static String SEARCH_TEXT_WITH = "te&countrycode=US;CN;GT";
	public static String SEARCH_JSON_WITH = "{\"codes\":[{\"value\":\"US:48\",\"desc\":\"US, TEXAS\",\"providers\":\"STEWARDS\"}],\"recordCount\":2}";
	public static String SEARCH_XML_WITH = XML_HEADER + "<Codes><Code value=\"US:48\" desc=\"US, TEXAS\" providers=\"STEWARDS\"/><recordCount>2</recordCount></Codes>"; 
	public static String COMPARE_FILE_JSON = "stateCode.json";
	public static String COMPARE_FILE_XML = "stateCode.xml";

	@Test
	public void getListAsJsonTest() throws Exception {
		runGetListAsJsonTest(TEST_ENDPOINT, SEARCH_TEXT_WO, COMPARE_FILE_JSON, SEARCH_JSON_WO);
		runGetListAsJsonTest(TEST_LEGACY_ENDPOINT, SEARCH_TEXT_WO, COMPARE_FILE_JSON, SEARCH_JSON_WO);
		runGetListAsJsonTest(TEST_LEGACY_ENDPOINT, SEARCH_TEXT_WITH, COMPARE_FILE_JSON, SEARCH_JSON_WITH);
	}

	@Test
	public void getListAsXmlTest() throws Exception {
		runGetListAsXmlTest(TEST_ENDPOINT, SEARCH_TEXT_WO, COMPARE_FILE_XML, SEARCH_XML_WO);
		runGetListAsXmlTest(TEST_LEGACY_ENDPOINT, SEARCH_TEXT_WO, COMPARE_FILE_XML, SEARCH_XML_WO);
		runGetListAsXmlTest(TEST_LEGACY_ENDPOINT, SEARCH_TEXT_WITH, COMPARE_FILE_XML, SEARCH_XML_WITH);
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
