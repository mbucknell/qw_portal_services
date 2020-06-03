package gov.usgs.wma.qw.srsnames;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.qw.BaseIT;
import gov.usgs.wma.qw.BaseRestController;
import gov.usgs.wma.qw.LastUpdateDao;
import gov.usgs.wma.qw.srsnames.PCodeDao;
import gov.usgs.wma.qw.srsnames.SrsnamesController;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DatabaseSetup("classpath:/testData/srsnames.xml")
public class SrsnamesControllerIT extends BaseIT {

	@Autowired
	private LastUpdateDao lastUpdateDao;
	@Autowired
	private PCodeDao pCodeDao;

	@Test
	public void getAsJsonTest(@Autowired TestRestTemplate restTemplate) throws Exception {
		ResponseEntity<String> rtn = restTemplate.getForEntity("/public_srsnames?mimeType=json", String.class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(rtn.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0), equalTo(BaseRestController.MEDIA_TYPE_APPLICATION_JSON_UTF8_VALUE));

		assertThat(new JSONObject(rtn.getBody()),
				sameJSONObjectAs(new JSONObject(getCompareFile("srsnames.json"))));
	}

	@Test
	public void writeCsvDataTest() {
		OutputStream stream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(stream);
		SrsnamesController service = new SrsnamesController(lastUpdateDao, pCodeDao, null);
		LinkedHashMap<String, Object> item1 = new LinkedHashMap<String, Object>();
		item1.put("bb", "222");
		item1.put("aa", "111");
		LinkedHashMap<String, Object> item2 = new LinkedHashMap<String, Object>();
		item2.put("xx", "bbb");
		item2.put("zz", "ccc");
		List<LinkedHashMap<String, Object>> data = new ArrayList<LinkedHashMap<String, Object>>();
		data.add(item1);
		data.add(item2);
		service.writeCsvData(writer, data);
		writer.close();
		assertEquals("\"bb\",\"aa\"\n\"222\",\"111\"\n\"bbb\",\"ccc\"\n", stream.toString());
	}

	@Test
	public void getAsCsvTest(@Autowired TestRestTemplate restTemplate) throws Exception {
		ResponseEntity<byte[]> rtn = restTemplate.getForEntity("/public_srsnames?mimeType=csv", byte[].class);
		assertThat(rtn.getStatusCode(), equalTo(HttpStatus.OK));
		assertTrue(rtn.getHeaders().get(SrsnamesController.HEADER_CONTENT_DISPOSITION).get(0).contains("attachment;filename=\"public_srsnames_"));
		assertTrue(rtn.getHeaders().get(SrsnamesController.HEADER_CONTENT_DISPOSITION).get(0).contains(".zip\""));
		ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(rtn.getBody()));
		ZipEntry entry = zip.getNextEntry();
		assertTrue(entry.getName().contains("public_srsnames_"));
		assertTrue(entry.getName().contains(".csv"));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[1024];
		while ((len = zip.read(buffer)) > 0) {
			os.write(buffer, 0, len);
		}
		assertEquals(getCompareFile("srsnames.csv"), os.toString(BaseRestController.DEFAULT_ENCODING));
	}

}
