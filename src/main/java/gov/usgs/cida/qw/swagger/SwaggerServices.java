package gov.usgs.cida.qw.swagger;

import java.util.List;

public class SwaggerServices {

	List<SwaggerService> services;

	public List<SwaggerService> getServices() {
		return services;
	}

	public void setServices(List<SwaggerService> services) {
		this.services = services;
	}

	public static class SwaggerService{
		private String name;
		private String url;
		private String version;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		@Override
		public String toString() {
			return "SwaggerService [name=" + name + ", url=" + url + ", version=" + version + "]";
		}
	}

}
