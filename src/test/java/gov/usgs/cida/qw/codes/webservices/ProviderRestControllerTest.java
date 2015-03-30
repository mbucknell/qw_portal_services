package gov.usgs.cida.qw.codes.webservices;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;
import gov.usgs.cida.qw.BaseSpringTest;
import gov.usgs.cida.qw.IntegrationTest;
import gov.usgs.cida.qw.LastUpdateDao;
import gov.usgs.cida.qw.codes.dao.CodeDao;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

//Note that we have had database consistency issues in the past with this method of testing. Since WQP is read-only,
//   we should not have a problem... Remove the @WebAppConfiguration, WebApplicationContext,
//   and use MockMvcBuilders.standaloneSetup(controller).build() if you need to do database CUD...  
@Category(IntegrationTest.class)
@WebAppConfiguration
public class ProviderRestControllerTest extends BaseSpringTest {

	@Autowired
	private LastUpdateDao lastUpdateDao;
	@Autowired
	private CodeDao codeDao;
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    private CharacterEncodingFilter filter;
    
    public static final String ALL_AS_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Codes><Code value=\"NWIS\"/>"
    		+ "<Code value=\"STEWARDS\"/><Code value=\"STORET\"/><recordCount>3</recordCount></Codes>";
    public static final String ALL_AS_JSON = "{\"codes\":[{\"value\":\"NWIS\",\"desc\":null,\"providers\":null},"
			+ "{\"value\":\"STEWARDS\",\"desc\":null,\"providers\":null},{\"value\":\"STORET\",\"desc\":null,\"providers\":null}],"
			+ "\"recordCount\":3}";
    public static final String JUST_STORET_AS_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
    		+ "<Codes><Code value=\"STORET\"/><recordCount>2</recordCount></Codes>";
    public static final String NWIS_AS_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Code value=\"NWIS\"/>";
    public static final String STEWARDS_AS_JSON = "{\"value\":\"STEWARDS\",\"desc\":null,\"providers\":null}";
    
    @Before
    public void setup() {
    	filter = new CharacterEncodingFilter();
    	filter.setEncoding(DEFAULT_ENCODING);
    	filter.setForceEncoding(true);
    	mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(filter, "/*").build();
    }

	@Test
    public void getProvidersTest() throws Exception {
		//JSON
        MvcResult rtn = mockMvc.perform(get("/codes/providers?mimetype=json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().encoding(DEFAULT_ENCODING))
        .andReturn();

        assertThat(new JSONObject(rtn.getResponse().getContentAsString()), sameJSONObjectAs(new JSONObject(ALL_AS_JSON)));

        rtn = mockMvc.perform(get("/codes/providers").accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().encoding(DEFAULT_ENCODING))
        .andReturn();

        assertThat(new JSONObject(rtn.getResponse().getContentAsString()), sameJSONObjectAs(new JSONObject(ALL_AS_JSON)));
        
        
        //XML
        rtn = mockMvc.perform(get("/codes/providers?mimetype=xml"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
        .andExpect(content().encoding(DEFAULT_ENCODING))
        .andReturn();

        assertEquals(ALL_AS_XML, rtn.getResponse().getContentAsString());

        rtn = mockMvc.perform(get("/codes/providers").accept(MediaType.APPLICATION_XML_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
        .andExpect(content().encoding(DEFAULT_ENCODING))
        .andReturn();

        assertEquals(ALL_AS_XML, rtn.getResponse().getContentAsString());
        

        //Query params
        rtn = mockMvc.perform(get("/codes/providers?text=st&pagesize=1&pagenumber=2").accept(MediaType.APPLICATION_XML_VALUE))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
		.andExpect(content().encoding(DEFAULT_ENCODING))
		.andReturn();
		
		assertEquals(JUST_STORET_AS_XML, rtn.getResponse().getContentAsString());
	}

	@Test
    public void getProviderTest() throws Exception {
		//JSON
        MvcResult rtn = mockMvc.perform(get("/codes/providers/STEWARDS?mimetype=json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().encoding(DEFAULT_ENCODING))
        .andReturn();

        assertThat(new JSONObject(rtn.getResponse().getContentAsString()), sameJSONObjectAs(new JSONObject(STEWARDS_AS_JSON)));

        rtn = mockMvc.perform(get("/codes/providers/STEWARDS").accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().encoding(DEFAULT_ENCODING))
        .andReturn();

        assertThat(new JSONObject(rtn.getResponse().getContentAsString()), sameJSONObjectAs(new JSONObject(STEWARDS_AS_JSON)));
        
        
        //XML
        rtn = mockMvc.perform(get("/codes/providers/NWIS?mimetype=xml"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
        .andExpect(content().encoding(DEFAULT_ENCODING))
        .andReturn();

        assertEquals(NWIS_AS_XML, rtn.getResponse().getContentAsString());

        rtn = mockMvc.perform(get("/codes/providers/NWIS").accept(MediaType.APPLICATION_XML_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
        .andExpect(content().encoding(DEFAULT_ENCODING))
        .andReturn();

        assertEquals(NWIS_AS_XML, rtn.getResponse().getContentAsString());

	}
}
