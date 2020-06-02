package gov.usgs.wma.qw;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT,
properties={"codes.service.url=/"})
public class RedirectControllerTest {

	@Test
	public void getVersionTest(@Autowired TestRestTemplate restTemplate) throws Exception {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/version", String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertTrue(rtn.getBody().contains("\"artifact\":\"qw_portal_services\""));

	}
}
