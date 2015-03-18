package gov.usgs.cida.qw.codes;


public enum CodeType {

    ORGANIZATION ("codeOrganization"),
    SITETYPE ("codeSiteType"),
    SAMPLETYPE ("codeSampleType"),
    SAMPLEMEDIA ("codeSampleMedia"),
    COUNTYCODE ("codeCountyCode"),
    STATECODE ("codeStateCode"),
    COUNTRYCODE ("codeCountryCode"),
    CHARACTERISTICTYPE ("codeCharacteristicType"),
    CHARACTERISTICNAME ("codeCharacteristicName"),
    DATASOURCE ("codeDataSource");
    
    public static final String CODES_MAPPER_NAMESPACE = "codes";

    private final String singleSelectID;
    private final String listSelectID;
    private final String countSelectID;

    private CodeType(String selectID) {
        this.singleSelectID = CODES_MAPPER_NAMESPACE + "." + selectID;
        this.listSelectID = CODES_MAPPER_NAMESPACE + "." + selectID + "List";
        this.countSelectID = CODES_MAPPER_NAMESPACE + "." + selectID + "Count";
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