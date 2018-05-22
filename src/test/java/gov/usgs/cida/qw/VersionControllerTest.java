package gov.usgs.cida.qw;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import gov.usgs.cida.qw.springinit.SpringConfig;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure=false)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK,
	classes={SpringConfig.class, CustomStringToArrayConverter.class,
			VersionController.class})
public class VersionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getVersionTest() throws Exception {
		mockMvc.perform(get("/version"))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/actuator/info"));
	}

}
