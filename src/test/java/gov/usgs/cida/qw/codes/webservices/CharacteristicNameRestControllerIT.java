package gov.usgs.cida.qw.codes.webservices;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/charName.xml")
public class CharacteristicNameRestControllerIT extends BaseCodesRestControllerTest {

	public static String TEST_ENDPOINT = "/characteristicname";
	public static String CODE_VALUE = "Aminomethylphosphonic Acid";
	public static String CODE_JSON = "{\"value\":\"Aminomethylphosphonic Acid\",\"providers\":\"STEWARDS\"}";
	public static String CODE_XML = XML_HEADER +"<Code value=\"Aminomethylphosphonic Acid\" providers=\"STEWARDS\"/>";
	public static String SEARCH_TEXT = "amm";
	public static String SEARCH_JSON = "{\"codes\":[{\"value\":\"Ammonium\",\"providers\":\"STEWARDS STORET\"}],\"recordCount\":3}";
	public static String SEARCH_XML = XML_HEADER + "<Codes><Code value=\"Ammonium\" providers=\"STEWARDS STORET\"/><recordCount>3</recordCount></Codes>"; 
	public static String COMPARE_FILE_JSON = "charName.json";
	public static String COMPARE_FILE_XML = "charName.xml";

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
