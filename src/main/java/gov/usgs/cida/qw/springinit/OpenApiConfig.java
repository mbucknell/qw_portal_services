package gov.usgs.cida.qw.springinit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	public static final String LOOKUP_TAG_DESCRIPTION = "Lookup and Validate";

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(new Info().title("Water Quality Portal Codes API").description(
						"Documentation for the Water Quality Portal Codes Lookup and Validation API"));

	}
}
