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

import gov.usgs.cida.qw.BaseSpringTest;
import gov.usgs.cida.qw.WQPFilter;

//Note that we have had database consistency issues in the past with this method of testing. Since WQP is read-only,
//we should not have a problem... Remove the @WebAppConfiguration, WebApplicationContext,
//and use MockMvcBuilders.standaloneSetup(controller).build() if you need to do database CUD...  
public abstract class BaseCodesRestControllerTest extends BaseSpringTest {

	@Autowired
	protected WQPFilter wqpFilter;
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(wqpFilter).build();
	}

	public void runGetListAsJsonTest(String testEndpoint, String searchText, String compareFile, String searchJson) throws Exception {
		//no query parms is bad
		mockMvc.perform(get(testEndpoint)).andExpect(status().isBadRequest());

		MvcResult rtn = runMock(testEndpoint + "?mimeType=json", MediaType.APPLICATION_JSON_UTF8_VALUE, null);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(getCompareFile(compareFile))));

		rtn = runMock(testEndpoint + "?x=y", MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(getCompareFile(compareFile))));

		rtn = runMock(testEndpoint + "?mimeType=json&text=" + searchText + "&pagenumber=2&pagesize=1", MediaType.APPLICATION_JSON_UTF8_VALUE, null);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(searchJson)));
	}

	public void runGetListAsXmlTest(String testEndpoint, String searchText, String compareFile, String searchXml) throws Exception {
		//xml is the default
		MvcResult rtn = runMock(testEndpoint + "?x=y", MediaType.APPLICATION_XML_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isSimilarTo(getCompareFile(compareFile)).ignoreWhitespace().throwComparisonFailure());

		rtn = runMock(testEndpoint + "?mimeType=xml", MediaType.APPLICATION_XML_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(getCompareFile(compareFile)).ignoreWhitespace().throwComparisonFailure());

		rtn = runMock(testEndpoint + "?x=y", MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_XML);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(getCompareFile(compareFile)).ignoreWhitespace().throwComparisonFailure());

		rtn = runMock(testEndpoint + "?mimeType=xml&text=" + searchText + "&pagenumber=2&pagesize=1", MediaType.APPLICATION_XML_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(searchXml).ignoreWhitespace().throwComparisonFailure());
	}

	public void runGetCodeAsJson(String testEndpoint, String codeValue, String codeJson) throws Exception {
		//no query parms is bad
		mockMvc.perform(get(testEndpoint)).andExpect(status().isBadRequest());

		MvcResult rtn = runMock(testEndpoint + "?value=" + codeValue + "&mimeType=json", MediaType.APPLICATION_JSON_UTF8_VALUE, null);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(codeJson)));

		rtn = runMock(testEndpoint + "?value=" + codeValue + "&x=y", MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON);
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
			sameJSONObjectAs(new JSONObject(codeJson)));
	}

	public void runGetCodeAsXml(String testEndpoint, String codeValue, String codeXml) throws Exception {
		//xml is the default
		MvcResult rtn = runMock(testEndpoint + "?value=" + codeValue + "&x=y", MediaType.APPLICATION_XML_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(codeXml));

		rtn = runMock(testEndpoint + "?value=" + codeValue + "&mimeType=xml", MediaType.APPLICATION_XML_VALUE, null);
		assertThat(rtn.getResponse().getContentAsString(), isIdenticalTo(codeXml));

		rtn = runMock(testEndpoint + "?value=" + codeValue + "&x=y", MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_XML);
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
			.andExpect(content().encoding(WQPFilter.DEFAULT_ENCODING))
			.andReturn();
	}
}
