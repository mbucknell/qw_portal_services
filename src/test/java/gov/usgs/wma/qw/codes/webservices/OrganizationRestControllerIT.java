package gov.usgs.wma.qw.codes.webservices;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@EnableWebMvc
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/organization.xml")
public class OrganizationRestControllerIT extends BaseCodesRestControllerTest {

	public static String TEST_ENDPOINT = "/organization";
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
