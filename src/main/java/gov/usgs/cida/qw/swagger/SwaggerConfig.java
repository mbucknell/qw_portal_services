package gov.usgs.cida.qw.swagger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import springfox.documentation.service.Tag;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.AbstractPathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class SwaggerConfig {
	public static final String ASSEMBLAGE_TAG_NAME = "Assemblage";
	public static final String CHARACTERISTIC_NAME_TAG_NAME = "Characteristic Name";
	public static final String CHARACTERISTIC_TYPE_TAG_NAME = "Characteristic Type";
	public static final String COUNTRY_CODE_TAG_NAME = "Country Code";
	public static final String COUNTY_CODE_TAG_NAME = "County Code";
	public static final String ORGANIZATION_TAG_NAME = "Organization";
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

	@Autowired
	@Qualifier("displayHost")
	private String displayHost;

	@Autowired
	@Qualifier("displayPath")
	private String displayPath;

	@Value("file:${catalina.base}/conf/swaggerServices.yml")
	private Resource servicesConfigFile;

	@Bean
	public SwaggerServices swaggerServices() {
		SwaggerServices props = new SwaggerServices();
		Yaml yaml = new Yaml();  
		try( InputStream in = servicesConfigFile.getInputStream()) {
			props = yaml.loadAs(in, SwaggerServices.class );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

	@Bean
	public Docket qwPortalServicesApi() {
		List<Parameter> operationParameters = Arrays.asList(
			new ParameterBuilder()
				.name("mimeType")
				.description("Can be used in place of the 'Accept' header. See 'Response Content Type' for the valid subset of these values.")
				.modelRef(new ModelRef("string"))
				.parameterType("query")
				.required(false)
				.allowableValues(new AllowableListValues(Arrays.asList("xml", "json", "csv", "text"), "LIST"))
				.build()
		);

		return new Docket(DocumentationType.SWAGGER_2)
				.protocols(new HashSet<>(Arrays.asList("https")))
				.host(displayHost)
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
						new Tag(VERSION_TAG_NAME, "Display"))
		;
	}

	@Bean
	public PathProvider pathProvider() {
		PathProvider rtn = new ProxyPathProvider();
		return rtn;
	}

	public class ProxyPathProvider extends AbstractPathProvider {
		@Override
		protected String applicationPath() {
			return displayPath;
		}
	
		@Override
		protected String getDocumentationPath() {
			return displayPath;
		}
	}

	@Bean
	public UiConfiguration uiConfig() {
		//This is needed in swagger 2 for the "try it" button on head requests - should not be needed with swagger 3
		//It is needed in all WQP projects!!!
		return new UiConfiguration(null, "none", "alpha", "schema", new String[] { "get", "post", "head" }, false, true, null);
	}

}
