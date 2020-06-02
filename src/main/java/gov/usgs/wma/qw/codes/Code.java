package gov.usgs.wma.qw.codes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;

@XmlRootElement (name = "Code")
@XmlType(propOrder = {"value", "desc", "providers"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Code implements Serializable {

	private static final long serialVersionUID = 7174466649683434137L;

	protected String value;
	protected String desc;
	protected String providers;

	@XmlAttribute (name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(final String inValue) {
		value = inValue;
	}

	@XmlAttribute (name = "desc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(final String inDesc) {
		desc = inDesc;
	}

	@XmlAttribute (name = "providers")
	public String getProviders() {
		return providers;
	}

	public void setProviders(final String inProviders) {
		providers = inProviders;
	}

	@Override
	public String toString() {
		return "(value=" + getValue() + ", desc=" + getDesc() + ")";
	}

}
