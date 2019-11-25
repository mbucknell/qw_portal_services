package gov.usgs.cida.qw.springinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import gov.usgs.cida.qw.BaseRestController;
import gov.usgs.cida.qw.CustomStringToArrayConverter;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

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
			.mediaType("csv", BaseRestController.MEDIA_TYPE_TEXT_CSV_UTF8)
			.mediaType("xml", MediaType.APPLICATION_XML)
			.mediaType("json", MediaType.APPLICATION_JSON)
			;
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		//This should make the url case insensitive
		AntPathMatcher matcher = new AntPathMatcher();
		matcher.setCaseSensitive(false);
		configurer.setPathMatcher(matcher);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("GET", "OPTIONS")
			.allowedHeaders("Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers");
	}

}