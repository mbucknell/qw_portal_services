package gov.usgs.cida.qw.swagger;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import springfox.documentation.PathProvider;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("swagger")
public class SwaggerConfig {
	public static final String ASSEMBLAGE_TAG_NAME = "Assemblage";
	public static final String CHARACTERISTIC_NAME_TAG_NAME = "Characteristic Name";
	public static final String CHARACTERISTIC_TYPE_TAG_NAME = "Characteristic Type";
	public static final String COUNTRY_CODE_TAG_NAME = "Country Code";
	public static final String COUNTY_CODE_TAG_NAME = "County Code";
	public static final String ORGANIZATION_TAG_NAME = "Organization";
	public static final String MONITORING_LOCATION_TAG_NAME = "Monitoring Location";
	public static final String PROJECT_TAG_NAME = "Project";
	public static final String PROVIDERS_TAG_NAME = "Providers";
	public static final String SAMPLE_MEDIA_TAG_NAME = "Sample Media";
	public static final String SITE_TYPE_TAG_NAME = "Site Type";
	public static final String SRSNAMES_TAG_NAME = "NWIS Public SRS Names";
	public static final String STATE_CODE_TAG_NAME = "State Code";
	public static final String SUBJECT_TAXONOMIC_NAME_TAG_NAME = "Taxonomic Name";
	public static final String SUMMARY_TAG_NAME = "National Results Coverage Map SLD";
	public static final String VERSION_TAG_NAME = "Application Version";
	private static final String TAG_DESCRIPTION = "Lookup and Validate";

	@Value("${swagger.display.host}")
	private String swaggerDisplayHost;
	@Value("${swagger.display.path}")
	private String swaggerDisplayPath;

	@Autowired
	private Environment environment;

	@Bean
	public Docket qwPortalServicesApi() {
		List<Parameter> operationParameters = Arrays.asList(
			new ParameterBuilder()
				.name("mimeType")
				.description("Can be used in place of the 'Accept' header. See 'Response Content Type' for the valid subset of these values.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.allowableValues(new AllowableListValues(Arrays.asList("xml", "json", "csv"), "LIST"))
				.build()
		);

		Docket docket = new Docket(DocumentationType.SWAGGER_2)
			.protocols(new HashSet<>(Arrays.asList("http")))
			.host(swaggerDisplayHost)
			.pathProvider(pathProvider())
			.useDefaultResponseMessages(false)
			.globalOperationParameters(operationParameters)
			.tags(new Tag(ASSEMBLAGE_TAG_NAME, TAG_DESCRIPTION),
					new Tag(CHARACTERISTIC_NAME_TAG_NAME, TAG_DESCRIPTION),
					new Tag(CHARACTERISTIC_TYPE_TAG_NAME, TAG_DESCRIPTION),
					new Tag(COUNTRY_CODE_TAG_NAME, TAG_DESCRIPTION),
					new Tag(COUNTY_CODE_TAG_NAME, TAG_DESCRIPTION),
					new Tag(ORGANIZATION_TAG_NAME, TAG_DESCRIPTION),
					new Tag(PROJECT_TAG_NAME, TAG_DESCRIPTION),
					new Tag(PROVIDERS_TAG_NAME, TAG_DESCRIPTION),
					new Tag(SAMPLE_MEDIA_TAG_NAME, TAG_DESCRIPTION),
					new Tag(SITE_TYPE_TAG_NAME, TAG_DESCRIPTION),
					new Tag(SRSNAMES_TAG_NAME, "File Download"),
					new Tag(STATE_CODE_TAG_NAME, TAG_DESCRIPTION),
					new Tag(SUBJECT_TAXONOMIC_NAME_TAG_NAME, TAG_DESCRIPTION),
					new Tag(SUMMARY_TAG_NAME, "Download"),
					new Tag(VERSION_TAG_NAME, "Display")
				)
			.select().paths(PathSelectors.any())
			.apis(RequestHandlerSelectors.basePackage("gov.usgs.cida.qw"))
			.build()
		;

		if (ArrayUtils.contains(environment.getActiveProfiles(), "internal")) {
			//Add in the Authorize button for WQP Internal
			docket.securitySchemes(Collections.singletonList(apiKey()));
		}

		return docket;
	}

	@Bean
	public PathProvider pathProvider() {
		PathProvider rtn = new ProxyPathProvider();
		return rtn;
	}

	public class ProxyPathProvider extends AbstractPathProvider {
		@Override
		protected String applicationPath() {
			return swaggerDisplayPath;
		}
	
		@Override
		protected String getDocumentationPath() {
			return swaggerDisplayPath;
		}
		
	}

	private ApiKey apiKey() {
		return new ApiKey("mykey", "Authorization", "header");
	}

	@Bean
	public Object uiConfig() {
		//This is needed in swagger 2 for the "try it" button on head requests - should not be needed with swagger 3
		//It is needed in all WQP projects!!!
		return UiConfigurationBuilder
				.builder()
				.deepLinking(true)
				.displayOperationId(false)
				.defaultModelsExpandDepth(1)
				.defaultModelExpandDepth(1)
				.defaultModelRendering(ModelRendering.EXAMPLE)
				.displayRequestDuration(false)
				.docExpansion(DocExpansion.NONE)
				.filter(false)
				.maxDisplayedTags(null)
				.operationsSorter(OperationsSorter.ALPHA)
				.showExtensions(false)
				.tagsSorter(TagsSorter.ALPHA)
				.supportedSubmitMethods(new String[] { "get", "post", "head" })
				.validatorUrl(null)
				.build();
	}

}
