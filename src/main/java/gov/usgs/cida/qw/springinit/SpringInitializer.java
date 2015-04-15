package gov.usgs.cida.qw.springinit;

import java.util.EnumSet;

import gov.usgs.cida.qw.WQPFilter;
import gov.usgs.cida.qw.CORSFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

public class SpringInitializer implements WebApplicationInitializer {
	
	/**
	 *  gets invoked automatically when application context loads
	 */
	public void onStartup(ServletContext servletContext) throws ServletException {		
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(SpringConfig.class);
		
		FilterRegistration urlRewriteFilter = servletContext.addFilter("UrlRewriteFilter", UrlRewriteFilter.class);
		urlRewriteFilter.setInitParameter("logLevel", "ERROR");
		urlRewriteFilter.setInitParameter("confReloadCheckInterval", "60");
		urlRewriteFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST), false, "/*");

		FilterRegistration wqpFilter = servletContext.addFilter("wqpFilter", WQPFilter.class);
		wqpFilter.addMappingForUrlPatterns(null, true, "/*");

		FilterRegistration corsFilter = servletContext.addFilter("corsFilter", CORSFilter.class);
		corsFilter.addMappingForUrlPatterns(null, true, "/*");

		Dynamic servlet = servletContext.addServlet("springDispatcher", new DispatcherServlet(ctx));
		servlet.addMapping("/");
		servlet.setAsyncSupported(true);
		servlet.setLoadOnStartup(1);
	}
}