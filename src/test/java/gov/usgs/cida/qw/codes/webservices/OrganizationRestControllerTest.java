package gov.usgs.cida.qw.codes.webservices;

import gov.usgs.cida.qw.IntegrationTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;

@Category(IntegrationTest.class)
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/organization.xml")
})
public class OrganizationRestControllerTest extends BaseCodesRestControllerTest {

	public static String TEST_ENDPOINT = "/codes/organizations";
	public static String TEST_LEGACY_ENDPOINT = "/codes/organization";
	public static String CODE_VALUE = "FWC_WQMP";
	public static String CODE_JSON = "{\"value\":\"FWC_WQMP\",\"desc\":\"Florida Keys NMS - Water Quality Monitoring Program\",\"providers\":\"NWIS\"}";
	public static String CODE_XML = XML_HEADER +"<Code value=\"FWC_WQMP\" desc=\"Florida Keys NMS - Water Quality Monitoring Program\" providers=\"NWIS\"/>";
	public static String SEARCH_TEXT = "in";
	public static String SEARCH_JSON = "{\"codes\":[{\"value\":\"USGS-WA\",\"desc\":\"USGS Washington Water Science Center\",\"providers\":\"STEWARDS\"}],\"recordCount\":5}";
	public static String SEARCH_XML = XML_HEADER + "<Codes><Code value=\"USGS-WA\" desc=\"USGS Washington Water Science Center\" providers=\"STEWARDS\"/><recordCount>5</recordCount></Codes>"; 
	public static String COMPARE_FILE_JSON = "organization.json";
	public static String COMPARE_FILE_XML = "organization.xml";
	
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
