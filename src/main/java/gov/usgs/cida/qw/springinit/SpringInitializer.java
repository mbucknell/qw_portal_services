package gov.usgs.cida.qw.springinit;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import gov.usgs.cida.qw.WQPFilter;

public class SpringInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext) throws ServletException {		
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(SpringConfig.class, JndiConfig.class);

		FilterRegistration wqpFilter = servletContext.addFilter("wqpFilter", WQPFilter.class);
		wqpFilter.addMappingForUrlPatterns(null, true, "/*");

		Dynamic servlet = servletContext.addServlet("springDispatcher", new DispatcherServlet(ctx));
		servlet.addMapping("/");
		servlet.setAsyncSupported(true);
		servlet.setLoadOnStartup(1);
	}
}