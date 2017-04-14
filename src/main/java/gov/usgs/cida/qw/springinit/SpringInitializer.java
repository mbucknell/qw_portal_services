package gov.usgs.cida.qw.springinit;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import gov.usgs.cida.qw.WQPFilter;

public class SpringInitializer implements WebApplicationInitializer {

	/**
	 *  gets invoked automatically when application context loads
	 */
	public void onStartup(ServletContext servletContext) throws ServletException {		
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(SpringConfig.class, JndiConfig.class, SwaggerConfig.class);

		FilterRegistration urlRewriteFilter = servletContext.addFilter("UrlRewriteFilter", UrlRewriteFilter.class);
		urlRewriteFilter.setInitParameter("logLevel", "TRACE");
		urlRewriteFilter.setInitParameter("confReloadCheckInterval", "60");
		urlRewriteFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST), false, "/*");

		FilterRegistration wqpFilter = servletContext.addFilter("wqpFilter", WQPFilter.class);
		wqpFilter.addMappingForUrlPatterns(null, true, "/*");

		Dynamic servlet = servletContext.addServlet("springDispatcher", new DispatcherServlet(ctx));
		servlet.addMapping("/");
		servlet.setAsyncSupported(true);
		servlet.setLoadOnStartup(1);
	}
}