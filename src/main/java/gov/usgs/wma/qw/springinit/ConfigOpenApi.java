package gov.usgs.wma.qw.springinit;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class ConfigOpenApi {

	public static final String LOOKUP_TAG_DESCRIPTION = "Lookup and Validate";

	@Value("${codes.service.url}")
	private String serverUrl;

	@Value("${springdoc.version}")
	private String appVersion;

	@Value("${codes.swagger.deployName}")
	private String deployName;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.addServersItem(new Server().url(serverUrl))
				.info(new Info()
						.title(deployName + " API")
						.description("Documentation for the " + deployName + " Lookup and Validation API")
						.version(appVersion)
						);
	}

	@Bean
	public OpenApiCustomiser sortTagsAlphabetically() {
		return openApi -> openApi.setTags(openApi.getTags()
				.stream()
				.sorted(Comparator.comparing(tag -> StringUtils.stripAccents(tag.getName())))
				.collect(Collectors.toList()));
	}
}
