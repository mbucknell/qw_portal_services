package gov.usgs.cida.qw.codes.webservices;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ProviderRestControllerIT extends BaseCodesRestControllerTest {

	public static String TEST_ENDPOINT = "/providers";
	public static String CODE_VALUE = "STEWARDS";
	public static String CODE_JSON = "{\"value\":\"STEWARDS\"}";
	public static String CODE_XML = XML_HEADER +"<Code value=\"STEWARDS\"/>";
	public static String SEARCH_TEXT = "st";
	public static String SEARCH_JSON = "{\"codes\":[{\"value\":\"STORET\"}],\"recordCount\":2}";
	public static String SEARCH_XML = XML_HEADER + "<Codes><Code value=\"STORET\"/><recordCount>2</recordCount></Codes>"; 
	public static String COMPARE_FILE_JSON = "provider.json";
	public static String COMPARE_FILE_XML = "provider.xml";

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
