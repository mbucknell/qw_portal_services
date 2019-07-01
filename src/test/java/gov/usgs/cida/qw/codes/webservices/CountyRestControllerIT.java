package gov.usgs.cida.qw.codes.webservices;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;

import gov.usgs.cida.qw.CustomStringToArrayConverter;
import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.codes.dao.CodeDao;
import gov.usgs.cida.qw.springinit.DBTestConfig;
import gov.usgs.cida.qw.springinit.SpringConfig;

@EnableWebMvc
@AutoConfigureMockMvc(secure=false)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK,
	classes={DBTestConfig.class, SpringConfig.class, CustomStringToArrayConverter.class,
			CountyRestController.class, LastUpdateDao.class, CodeDao.class})
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/countyCode.xml")
})
public class CountyRestControllerIT extends BaseCodesRestControllerTest {

	public static String TEST_ENDPOINT = "/countycode";
	public static String CODE_VALUE = "US:19:015";
	public static String CODE_JSON = "{\"value\":\"US:19:015\",\"desc\":\"US, IOWA, BOONE\",\"providers\":\"NWIS STORET\"}";
	public static String CODE_XML = XML_HEADER +"<Code value=\"US:19:015\" desc=\"US, IOWA, BOONE\" providers=\"NWIS STORET\"/>";
	public static String SEARCH_TEXT_WO = "ne";
	public static String SEARCH_JSON_WO = "{\"codes\":[{\"value\":\"US:19:015\",\"desc\":\"US, IOWA, BOONE\",\"providers\":\"NWIS STORET\"}],\"recordCount\":3}";
	public static String SEARCH_XML_WO = XML_HEADER + "<Codes><Code value=\"US:19:015\" desc=\"US, IOWA, BOONE\" providers=\"NWIS STORET\"/><recordCount>3</recordCount></Codes>"; 
	public static String SEARCH_TEXT_WITH = "ne&statecode=CN:90;US:19";
	public static String SEARCH_JSON_WITH = "{\"codes\":[{\"value\":\"US:19:015\",\"desc\":\"US, IOWA, BOONE\",\"providers\":\"NWIS STORET\"}],\"recordCount\":2}";
	public static String SEARCH_XML_WITH = XML_HEADER + "<Codes><Code value=\"US:19:015\" desc=\"US, IOWA, BOONE\" providers=\"NWIS STORET\"/><recordCount>2</recordCount></Codes>"; 
	public static String COMPARE_FILE_JSON = "countyCode.json";
	public static String COMPARE_FILE_XML = "countyCode.xml";

	@Test
	public void getListAsJsonTest() throws Exception {
		runGetListAsJsonTest(TEST_ENDPOINT, SEARCH_TEXT_WO, COMPARE_FILE_JSON, SEARCH_JSON_WO);
		runGetListAsJsonTest(TEST_ENDPOINT, SEARCH_TEXT_WITH, COMPARE_FILE_JSON, SEARCH_JSON_WITH);
	}

	@Test
	public void getListAsXmlTest() throws Exception {
		runGetListAsXmlTest(TEST_ENDPOINT, SEARCH_TEXT_WO, COMPARE_FILE_XML, SEARCH_XML_WO);
		runGetListAsXmlTest(TEST_ENDPOINT, SEARCH_TEXT_WITH, COMPARE_FILE_XML, SEARCH_XML_WITH);
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
