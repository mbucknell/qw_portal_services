package gov.usgs.cida.qw.codes.webservices;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import gov.usgs.cida.qw.BaseIT;
import gov.usgs.cida.qw.BaseRestController;

public abstract class BaseCodesRestControllerTest extends BaseIT {

	@Autowired
	TestRestTemplate restTemplate;

	public void runGetListAsJsonTest(String testEndpoint, String searchText, String compareFile, String searchJson) throws Exception {
		ResponseEntity<String> rtn = runMock(testEndpoint + "?mimeType=json", BaseRestController.MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE, null);
		assertThat(new JSONObject(rtn.getBody()),
			sameJSONObjectAs(new JSONObject(getCompareFile(compareFile))));

		rtn = runMock(testEndpoint, BaseRestController.MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON);
		assertThat(new JSONObject(rtn.getBody()),
			sameJSONObjectAs(new JSONObject(getCompareFile(compareFile))));

		rtn = runMock(testEndpoint + "?mimeType=json&text=" + searchText + "&pagenumber=2&pagesize=1", BaseRestController.MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE, null);
		assertThat(new JSONObject(rtn.getBody()),
			sameJSONObjectAs(new JSONObject(searchJson)));
	}

	public void runGetListAsXmlTest(String testEndpoint, String searchText, String compareFile, String searchXml) throws Exception {
		ResponseEntity<String> rtn = runMock(testEndpoint + "?mimeType=xml", BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, null);
		assertThat(rtn.getBody(), isIdenticalTo(getCompareFile(compareFile)).ignoreWhitespace().throwComparisonFailure());

		rtn = runMock(testEndpoint, BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, MediaType.APPLICATION_XML);
		assertThat(rtn.getBody(), isIdenticalTo(getCompareFile(compareFile)).ignoreWhitespace().throwComparisonFailure());

		rtn = runMock(testEndpoint + "?mimeType=xml&text=" + searchText + "&pagenumber=2&pagesize=1", BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, null);
		assertThat(rtn.getBody(), isIdenticalTo(searchXml).ignoreWhitespace().throwComparisonFailure());
	}

	public void runGetCodeAsJson(String testEndpoint, String codeValue, String codeJson) throws Exception {
		ResponseEntity<String> rtn = runMock(testEndpoint + "/validate?value=" + codeValue + "&mimeType=json", BaseRestController.MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE, null);
		assertThat(new JSONObject(rtn.getBody()),
			sameJSONObjectAs(new JSONObject(codeJson)));

		rtn = runMock(testEndpoint + "/validate?value=" + codeValue, BaseRestController.MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON);
		assertThat(new JSONObject(rtn.getBody()),
			sameJSONObjectAs(new JSONObject(codeJson)));
	}

	public void runGetCodeAsXml(String testEndpoint, String codeValue, String codeXml) throws Exception {
		ResponseEntity<String> rtn = runMock(testEndpoint + "/validate?value=" + codeValue + "&mimeType=xml", BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, null);
		assertThat(rtn.getBody(), isIdenticalTo(codeXml));

		rtn = runMock(testEndpoint + "/validate?value=" + codeValue, BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8_VALUE, MediaType.APPLICATION_XML);
		assertThat(rtn.getBody(), isIdenticalTo(codeXml));
	}

	public ResponseEntity<String> runMock(String url, String expectedMediaType, MediaType acceptMediaType) throws Exception {
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = null;

		if (null != acceptMediaType) {
			headers.setAccept(List.of(acceptMediaType));
			entity = new HttpEntity<String>(null ,headers);
		}
		ResponseEntity<String> rtn = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(rtn.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0), equalTo(expectedMediaType));
		return rtn;
	}
}
