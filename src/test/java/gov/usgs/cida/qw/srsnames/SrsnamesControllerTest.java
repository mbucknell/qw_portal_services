package gov.usgs.cida.qw.srsnames;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.BaseSpringTest;
import gov.usgs.cida.qw.DatabaseRequiredTest;
import gov.usgs.cida.qw.LastUpdateDao;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;

@Category(DatabaseRequiredTest.class)
@DatabaseSetups({
	@DatabaseSetup("classpath:/testData/clearAll.xml"),
	@DatabaseSetup("classpath:/testData/srsnames.xml")
})
public class SrsnamesControllerTest extends BaseSpringTest {

	@Autowired
	private LastUpdateDao lastUpdateDao;
	@Autowired
	private PCodeDao pCodeDao;
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void getAsJsonTest() throws Exception {
		MvcResult rtn = mockMvc.perform(get("/publicsrsnames?mimetype=json").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(content().encoding(BaseRestController.DEFAULT_ENCODING))
			.andReturn();
		assertThat(new JSONObject(rtn.getResponse().getContentAsString()),
				sameJSONObjectAs(new JSONObject(getCompareFile("srsnames.json"))));
	}

	@Test
	public void doCsvTest() {
		OutputStream stream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(stream);
		SrsnamesController service = new SrsnamesController(lastUpdateDao, pCodeDao);
		LinkedHashMap<String, Object> item1 = new LinkedHashMap<String, Object>();
		item1.put("bb", "222");
		item1.put("aa", "111");
		LinkedHashMap<String, Object> item2 = new LinkedHashMap<String, Object>();
		item2.put("xx", "bbb");
		item2.put("zz", "ccc");
		List<LinkedHashMap<String, Object>> data = new ArrayList<LinkedHashMap<String, Object>>();
		data.add(item1);
		data.add(item2);
		service.doCsv(writer, data);
		writer.close();
		assertEquals("\"bb\",\"aa\"\n\"222\",\"111\"\n\"bbb\",\"ccc\"\n", stream.toString());
	}

	@Test
	public void getAsCsvTest() throws Exception {
		MvcResult rtn = mockMvc.perform(get("/publicsrsnames?mimetype=csv").accept(MediaType.parseMediaType(SrsnamesController.MEDIA_TYPE_TEXT_CSV_UTF8_VALUE)))
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
