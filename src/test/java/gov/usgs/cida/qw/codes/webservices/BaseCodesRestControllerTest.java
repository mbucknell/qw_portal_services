package gov.usgs.cida.qw.codes.webservices;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import org.json.JSONObject;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.BaseSpringTest;

//Note that we have had database consistency issues in the past with this method of testing. Since WQP is read-only,
//we should not have a problem... Remove the @WebAppConfiguration, WebApplicationContext,
//and use MockMvcBuilders.standaloneSetup(controller).build() if you need to do database CUD...  
public abstract class BaseCodesRestControllerTest extends BaseSpringTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	public void runGetListAsJsonTest(String testEndpoint, String searchText, String compareFile, String searchJson) throws Exception {
		MvcResult rtn = runMock(testEndpoint + "?mimeType=json", MediaType.APPLICATION_JSON_UTF8_VALUE, null);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(getCompareFile(compareFile))));

		rtn = runMock(testEndpoint, MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(getCompareFile(compareFile))));

		rtn = runMock(testEndpoint + "?mimeType=json&text=" + searchText + "&pagenumber=2&pagesize=1", MediaType.APPLICATION_JSON_UTF8_VALUE, null);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(searchJson)));
	}

	public void runGetListAsXmlTest(String testEndpoint, String searchText, String compareFile, String searchXml) throws Exception {
		//xml is the default
		MvcResult rtn = runMock(testEndpoint, BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isSimilarTo(getCompareFile(compareFile)).ignoreWhitespace().throwComparisonFailure());

		rtn = runMock(testEndpoint + "?mimeType=xml", BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(getCompareFile(compareFile)).ignoreWhitespace().throwComparisonFailure());

		rtn = runMock(testEndpoint, BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(getCompareFile(compareFile)).ignoreWhitespace().throwComparisonFailure());

		rtn = runMock(testEndpoint + "?mimeType=xml&text=" + searchText + "&pagenumber=2&pagesize=1", BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(searchXml).ignoreWhitespace().throwComparisonFailure());
	}

	public void runGetCodeAsJson(String testEndpoint, String codeValue, String codeJson) throws Exception {
		MvcResult rtn = runMock(testEndpoint + "/" + codeValue + "?mimeType=json", MediaType.APPLICATION_JSON_UTF8_VALUE, null);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(codeJson)));

		rtn = runMock(testEndpoint + "/" + codeValue, MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_UTF8);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(codeJson)));
	}

	public void runGetCodeAsXml(String testEndpoint, String codeValue, String codeXml) throws Exception {
		//xml is the default
		MvcResult rtn = runMock(testEndpoint + "/" + codeValue, BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(codeXml));

		rtn = runMock(testEndpoint + "/" + codeValue + "?mimeType=xml", BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(codeXml));

		rtn = runMock(testEndpoint + "/" + codeValue, BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(codeXml));
	}

	public MvcResult runMock(String url, String expectedMediaType, MediaType acceptMediaType) throws Exception {
		RequestBuilder rb = get(url);
		if (null != acceptMediaType) {
			rb = get(url).accept(acceptMediaType);
		}
		return mockMvc.perform(rb)
			.andExpect(status().isOk())
			.andExpect(content().contentType(expectedMediaType))
			.andExpect(content().encoding(BaseRestController.DEFAULT_ENCODING))
			.andReturn();
	}
}
