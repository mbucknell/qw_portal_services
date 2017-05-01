package gov.usgs.cida.qw.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary
public class GatewaySwaggerResourceProvider implements SwaggerResourcesProvider {

	SwaggerServices swaggerServices;

	@Autowired
	public GatewaySwaggerResourceProvider(final SwaggerServices swaggerServices) {
		this.swaggerServices = swaggerServices;
	}

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();

		swaggerServices.getServices().forEach(service -> {
			resources.add(buildSwaggerResource(service.getName(), service.getUrl(), service.getVersion()));
		});

		return resources;
	}

	private SwaggerResource buildSwaggerResource(String name, String location, String version) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion(version);
		return swaggerResource;	
	}

}
