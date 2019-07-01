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
			CountriesRestController.class, LastUpdateDao.class, CodeDao.class})
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/countryCode.xml")
})
public class CountriesRestControllerIT extends BaseCodesRestControllerTest {

	public static String TEST_ENDPOINT = "/countrycode";
	public static String CODE_VALUE = "US";
	public static String CODE_JSON = "{\"value\":\"US\",\"desc\":\"UNITED STATES OF AMERICA\",\"providers\":\"NWIS STEWARDS STORET\"}";
	public static String CODE_XML = XML_HEADER +"<Code value=\"US\" desc=\"UNITED STATES OF AMERICA\" providers=\"NWIS STEWARDS STORET\"/>";
	public static String SEARCH_TEXT = "ca";
	public static String SEARCH_JSON = "{\"codes\":[{\"value\":\"CV\",\"desc\":\"CAPE VERDE\",\"providers\":\"NWIS\"}],\"recordCount\":3}";
	public static String SEARCH_XML = XML_HEADER + "<Codes><Code value=\"CV\" desc=\"CAPE VERDE\" providers=\"NWIS\"/><recordCount>3</recordCount></Codes>"; 
	public static String COMPARE_FILE_JSON = "countryCode.json";
	public static String COMPARE_FILE_XML = "countryCode.xml";

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
