package gov.usgs.cida.qw.codes;

public enum CodeType {

	ASSEMBLAGE ("codeAssemblage"),
	CHARACTERISTICNAME ("codeCharacteristicName"),
	CHARACTERISTICTYPE ("codeCharacteristicType"),
	DATASOURCE ("codeDataSource"),
	COUNTRYCODE ("codeCountryCode"),
	COUNTYCODE ("codeCountyCode"),
	ORGANIZATION ("codeOrganization"),
	PROJECT ("codeProject"),
	SAMPLEMEDIA ("codeSampleMedia"),
	SAMPLETYPE ("codeSampleType"),
	SITETYPE ("codeSiteType"),
	STATECODE ("codeStateCode"),
	SUBJECTTAXONOMICNAME("codeSubjectTaxonomicName");

	public static final String CODES_MAPPER_NAMESPACE = "codes";

	private final String singleSelectID;
	private final String listSelectID;
	private final String countSelectID;

	private CodeType(String selectID) {
		this.singleSelectID = String.join(".", CODES_MAPPER_NAMESPACE, selectID);
		this.listSelectID = String.join(".", CODES_MAPPER_NAMESPACE, selectID + "List");
		this.countSelectID = String.join(".", CODES_MAPPER_NAMESPACE, selectID + "Count");
	}

	public String getSingleSelectID() {
		return singleSelectID;
	}

	public String getListSelectID() {
		return listSelectID;
	}

	public String getCountSelectID() {
		return countSelectID;
	}

}