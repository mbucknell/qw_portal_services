package gov.usgs.cida.qw;

import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Generic extension to LinkedHashMap so we can intercept them in Mybatis and guarantee the map has the 
 * correct number of entries in the correct positions for the exports to work correctly.  (Mybatis currently
 * drops entries if the value is null.) 
 * @author drsteini
 *
 */
@XmlRootElement
public class Export extends LinkedHashMap<String, Object> {

	/** suid. */
	private static final long serialVersionUID = -4627386265399891290L;

}
