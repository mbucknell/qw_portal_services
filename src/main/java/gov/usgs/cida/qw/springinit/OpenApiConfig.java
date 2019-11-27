package gov.usgs.cida.qw.springinit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	public static final String LOOKUP_TAG_DESCRIPTION = "Lookup and Validate";

	@Value("${CODES_SERVICE_URL}")
	private String serverUrl;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.addServersItem(new Server().url(serverUrl))
				.components(new Components())
				.info(new Info().title("Water Quality Portal Codes API").description(
						"Documentation for the Water Quality Portal Codes Lookup and Validation API"));

	}
}
