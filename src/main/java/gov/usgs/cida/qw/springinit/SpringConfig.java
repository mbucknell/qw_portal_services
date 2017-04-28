package gov.usgs.cida.qw.springinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.CustomStringToArrayConverter;
import gov.usgs.cida.qw.UrlPathHelperNonDecoding;

@Import(MybatisConfig.class)
@ComponentScan(basePackages="gov.usgs.cida.qw")
@EnableWebMvc
public class SpringConfig extends WebMvcConfigurerAdapter {

	@Autowired
	CustomStringToArrayConverter customStringToArrayConverter;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(customStringToArrayConverter);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
}
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer
			.favorPathExtension(false)
			.favorParameter(true)
			.parameterName("mimeType")
			.defaultContentType(BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8)
			.mediaType("csv", BaseRestController.MEDIA_TYPE_TEXT_CSV_UTF8)
			.mediaType("xml", BaseRestController.MEDIA_TYPE_APPLICATION_XML_UTF8)
			.mediaType("json", MediaType.APPLICATION_JSON_UTF8)
			.mediaType("text", MediaType.TEXT_PLAIN)
			;
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		//This should make the url case insensitive
		AntPathMatcher matcher = new AntPathMatcher();
		matcher.setCaseSensitive(false);
		configurer.setPathMatcher(matcher);
	}

	@Bean
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		//This is needed to allow encoded slashes in the code url - for example Fish/Nekton is a valid value and must be passed in the url as Fish%2FNekton
		//Be sure to also set the tomcat property org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true
		RequestMappingHandlerMapping rtn = new RequestMappingHandlerMapping();
		rtn.setOrder(-1);
		rtn.setUrlPathHelper(new UrlPathHelperNonDecoding());
		return rtn;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html", "webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");

		registry.setOrder(-1);
	}

}