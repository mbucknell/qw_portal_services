package gov.usgs.cida.qw.srsnames;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;

import gov.usgs.cida.qw.BaseIT;
import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.CustomStringToArrayConverter;
import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.springinit.DBTestConfig;
import gov.usgs.cida.qw.springinit.SpringConfig;

@EnableWebMvc
@AutoConfigureMockMvc(secure=false)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK,
	classes={DBTestConfig.class, SpringConfig.class, CustomStringToArrayConverter.class,
			SrsnamesController.class, LastUpdateDao.class, PCodeDao.class})
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/srsnames.xml")
})
public class SrsnamesControllerIT extends BaseIT {

	@Autowired
	private LastUpdateDao lastUpdateDao;
	@Autowired
	private PCodeDao pCodeDao;
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getAsJsonTest() throws Exception {
		MvcResult rtn = mockMvc.perform(get("/public_srsnames?mimetype=json").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(content().encoding(BaseRestController.DEFAULT_ENCODING))
			.andReturn();
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
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
	public void getAsCsvTest() throws Exception {
		MvcResult rtn = mockMvc.perform(get("/public_srsnames?mimetype=csv").accept(MediaType.parseMediaType(SrsnamesController.MEDIA_TYPE_TEXT_CSV_UTF8_VALUE)))
			.andExpect(status().isOk())
			.andExpect(content().encoding(BaseRestController.DEFAULT_ENCODING))
			.andReturn();
		assertTrue(rtn.getResponse().getHeader(SrsnamesController.HEADER_CONTENT_DISPOSITION).contains("attachment;filename=\"public_srsnames_"));
		assertTrue(rtn.getResponse().getHeader(SrsnamesController.HEADER_CONTENT_DISPOSITION).contains(".zip\""));
		ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(rtn.getResponse().getContentAsByteArray()));
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
