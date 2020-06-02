package gov.usgs.wma.qw.codes;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "Codes")
public class CodeList {

	private Collection<Code> codes;

	private int recordCount;

	@XmlElement (name = "Code")
	public Collection<Code> getCodes() {
		return codes;
	}

	public void setCodes(final Collection<Code> inCodes) {
		codes = inCodes;
	}

	@XmlElement (name = "recordCount")
	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(final int inRecordCount) {
		recordCount = inRecordCount;
	}

}
