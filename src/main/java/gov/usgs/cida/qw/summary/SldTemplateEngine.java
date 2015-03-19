package gov.usgs.cida.qw.summary;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;


public class SldTemplateEngine {
	
	private static String[] reds = {"#F3C391", "#F6A061", "#F07F45", "#E36122", "#D23F11"};
	private static String[] blues = {"#A3D9F0", "#61B5DE", "#308CC6", "#126BB0","#124989"};
	private static String[] greens = {"#C0E6C2", "#89C09D", "#55A87F", "#009C66", "#008556"};
	private static Map<String, String[]> colorMapBySource = new HashMap<String, String[]>();
	static {
		colorMapBySource.put(MapDataSource.All.getStringAbbreviation(), reds);
		colorMapBySource.put(MapDataSource.EPA.getStringAbbreviation(), blues);
		colorMapBySource.put(MapDataSource.USGS.getStringAbbreviation(), greens);
	}
	
	
	/**
	 * initialize the engine once.
	 */
	private final static VelocityEngine engine = new VelocityEngine();
	static 	{
		Properties properties = new Properties();
		properties.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
		properties.setProperty("classpath." + VelocityEngine.RESOURCE_LOADER + ".class",ClasspathResourceLoader.class.getName());
		properties.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
		properties.setProperty("runtime.log.logsystem.log4j.logger", "qwLogger");
		try {
			engine.init(properties);
		} catch (Exception e) {
			throw new RuntimeException("something else went terribly wrong with initiating the Velocity engine", e);
		}
	}
	
	/**
	 * create the sld based on the params and the bin values (using the given file as the template).
	 */
	public static String generateDynamicStyle(MapDataSource source, MapGeometry geom, String[] binValues, String fileName) {
		StringWriter xmlWriter = new StringWriter();
		try {
			Template template = engine.getTemplate(fileName);
			template.merge(buildVelocityContext(source, geom, binValues), xmlWriter);
		} catch (Exception e) {
			throw new RuntimeException("something went terribly wrong with the template: " + fileName, e);
		}
		return xmlWriter.toString();
	}

	/**
	 * Create a velocity context with the given parameters.
	 */
	private static VelocityContext buildVelocityContext(MapDataSource source, MapGeometry geom, String[] binValues) {
		String[] colors = colorMapBySource.get(source.getStringAbbreviation());
		List<Map<String, String>> bins = new ArrayList<Map<String,String>>();
		for (int bin = 0; bin < colors.length; bin++) {
			Map<String, String> attribs = new HashMap<String, String>();
			String min = binValues[(bin*2)];
			String max = binValues[(bin*2)+1];
			attribs.put("title", min + " - " +max);
			attribs.put("color", colors[bin]);
			attribs.put("betweenMin", min);
			attribs.put("betweenMax", max);
			bins.add(attribs);
		}
		VelocityContext velContext = new VelocityContext();
		velContext.put("layerName", "qw_portal_map:" + geom.toString().toLowerCase() +"_all");
		velContext.put("binList", bins);
		return velContext;
	}

	/**
	 * a simple enum to handle binding to parameters.
	 */
	public static enum MapGeometry {
		States('S'),
		Counties('C'),
		Huc8('H')
		;
		private final static Map<String, MapGeometry> charMap = new HashMap<String, MapGeometry>();
		private final char abbreviation;
		private MapGeometry(char inAbbrev) {
			abbreviation = inAbbrev; 
		}
		public String getStringAbbreviation(){
			return getStringAbbreviationFromChar(this.abbreviation);
		}
		private static String getStringAbbreviationFromChar(char abbrev) {
			return String.valueOf(abbrev).toUpperCase();
		}
		static {
			for (MapGeometry t : MapGeometry.values()) {
				MapGeometry dup = charMap.put(t.getStringAbbreviation(), t);
				if (dup != null){
					throw new RuntimeException("Pick a new char const for: " + t + ". " + t.getStringAbbreviation() + "is already used by: " + dup);
				}
			}
		}
		public static MapGeometry fromAbbreviation(String geometryString) {
			return geometryString == null || geometryString.length() < 1 
					? null
					: charMap.get(getStringAbbreviationFromChar(geometryString.charAt(0)));
		}
	}
	
	/**
	 * a simple enum to handle binding to parameters.
	 */
	public static enum MapDataSource {
		All('A'),
		EPA('E'),
		USGS('N');
		
		private final static Map<String, MapDataSource> charMap = new HashMap<String, MapDataSource>();
		private final char abbreviation;
		private MapDataSource(char inAbbrev) {
			abbreviation = inAbbrev;
		}
		public String getStringAbbreviation(){
			return getStringAbbreviationFromChar(this.abbreviation);
		}
		private static String getStringAbbreviationFromChar(char abbrev){
			return String.valueOf(abbrev).toUpperCase();
		} 
		static {
			for (MapDataSource t : MapDataSource.values()) {
				MapDataSource dup = charMap.put(t.getStringAbbreviation(), t);
				if (dup != null){
					throw new RuntimeException("Pick a new char const for: " + t + ". " + t.abbreviation + "is already used by: " + dup);
				}
			}
		}
		public static MapDataSource fromAbbreviation(String sourceString) {
			return sourceString == null || sourceString.length() < 1 
					? null
					: charMap.get(getStringAbbreviationFromChar(sourceString.charAt(0)));
		}
	}
}
