package gov.usgs.cida.qw.webservice.codes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import gov.usgs.cida.qw.BaseSpringTest;
import gov.usgs.cida.qw.QWConstants;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
public class CodesMvcServiceTest extends BaseSpringTest {
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getCharacteristicNames() throws Exception {
        mockMvc.perform(get("/codes/characteristicname?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/characteristicname").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/characteristicnames?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/characteristicnames").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));
    }
	
    @Test
    public void getCharacteristicTypes() throws Exception {
        mockMvc.perform(get("/codes/characteristictype?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/characteristictype").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/characteristictypes?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/characteristictypes").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));
    }
	
    @Test
    public void getCounties() throws Exception {
        mockMvc.perform(get("/codes/countycode?statecode=US:55").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/countycode?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().string("Required String[] parameter 'statecode' is not present"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/counties?statecode=US:55").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/counties?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().string("Required String[] parameter 'statecode' is not present"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));
    }
	
    @Test
    public void getCountries() throws Exception {
        mockMvc.perform(get("/codes/countrycode?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/countrycode").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/countries?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/countries").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));
    }
	
    @Test
    public void getOrganizations() throws Exception {
        mockMvc.perform(get("/codes/organization?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/organization").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/organizations?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/organizations").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));
    }
	
    @Test
    public void getProviders() throws Exception {
        this.mockMvc.perform(get("/codes/providers?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
          .andExpect(status().isOk())
          .andExpect(content().contentType("application/xml"))
          .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/providers").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));
    }
	
    @Test
    public void getSampleMedia() throws Exception {
        mockMvc.perform(get("/codes/samplemedia?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/samplemedia").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));
    }
	
    @Test
    public void getSiteTypes() throws Exception {
        mockMvc.perform(get("/codes/sitetype?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/sitetype").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/sitetypes?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/sitetypes").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/text"))
        .andExpect(content().string(""))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));
    }
	
    @Test
    public void getStates() throws Exception {
        mockMvc.perform(get("/codes/statecode?countrycode=US").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/statecode?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().string("Required String[] parameter 'countrycode' is not present"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/states?countrycode=US").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));

        mockMvc.perform(get("/codes/states?mimetype=xml").accept(MediaType.parseMediaType("application/xml")))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/xml"))
        .andExpect(content().string("Required String[] parameter 'countrycode' is not present"))
        .andExpect(content().encoding(QWConstants.DEFAULT_ENCODING));
    }

}
