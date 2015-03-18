package gov.usgs.cida.qw.codes.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.usgs.cida.qw.codes.Code;
import gov.usgs.cida.qw.codes.CodeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CodeDaoTest extends gov.usgs.cida.qw.BaseSpringTest {

    @Autowired
    private CodeDao codeDao;

    @Test
    public void getCodesTest() {
        //Check them all without parameters - Note that these will be brittle if the ci database is allowed to change.
        List<Code> codes = codeDao.getCodes(CodeType.CHARACTERISTICNAME);
        assertNotNull(codes);
//        assertEquals(9, codes.size());
//        assertEquals("Simazine", codes.get(8).getValue());
//        assertNull(codes.get(8).getDesc());

        codes = codeDao.getCodes(CodeType.CHARACTERISTICTYPE);
        assertNotNull(codes);
//        assertEquals(9, codes.size());
//        assertEquals("Simazine", codes.get(8).getValue());
//        assertNull(codes.get(8).getDesc());

        codes = codeDao.getCodes(CodeType.COUNTRYCODE);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US", codes.get(0).getValue());
//        assertEquals("UNITED STATES OF AMERICA", codes.get(0).getDesc());

        codes = codeDao.getCodes(CodeType.COUNTYCODE);
        assertNotNull(codes);
//        assertEquals(4, codes.size());
//        assertEquals("US:19:079", codes.get(1).getValue());
//        assertEquals("US, IOWA, HAMILTON", codes.get(1).getDesc());

        codes = codeDao.getCodes(CodeType.DATASOURCE);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US", codes.get(0).getValue());
//        assertEquals("UNITED STATES OF AMERICA", codes.get(0).getDesc());

        codes = codeDao.getCodes(CodeType.ORGANIZATION);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("ARS", codes.get(0).getValue());
//        assertEquals("Agricultural Research Service", codes.get(0).getDesc());

        codes = codeDao.getCodes(CodeType.SAMPLEMEDIA);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("water", codes.get(0).getValue());
//        assertNull(codes.get(0).getDesc());

        codes = codeDao.getCodes(CodeType.SITETYPE);
        assertNotNull(codes);
//        assertEquals(2, codes.size());
//        assertEquals("Stream", codes.get(1).getValue());
//        assertNull(codes.get(1).getDesc());

        codes = codeDao.getCodes(CodeType.STATECODE);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US:19", codes.get(0).getValue());
//        assertEquals("IOWA", codes.get(0).getDesc());



        
        //Try a text search
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("text", "e");
        assertNotNull(codes);
//      assertEquals(9, codes.size());
//      assertEquals("Simazine", codes.get(8).getValue());
//      assertNull(codes.get(8).getDesc());

      codes = codeDao.getCodes(CodeType.CHARACTERISTICTYPE);
      assertNotNull(codes);
//      assertEquals(9, codes.size());
//      assertEquals("Simazine", codes.get(8).getValue());
//      assertNull(codes.get(8).getDesc());

      codes = codeDao.getCodes(CodeType.COUNTRYCODE);
      assertNotNull(codes);
//      assertEquals(1, codes.size());
//      assertEquals("US", codes.get(0).getValue());
//      assertEquals("UNITED STATES OF AMERICA", codes.get(0).getDesc());

      codes = codeDao.getCodes(CodeType.COUNTYCODE);
      assertNotNull(codes);
//      assertEquals(4, codes.size());
//      assertEquals("US:19:079", codes.get(1).getValue());
//      assertEquals("US, IOWA, HAMILTON", codes.get(1).getDesc());

      codes = codeDao.getCodes(CodeType.DATASOURCE);
      assertNotNull(codes);
//      assertEquals(1, codes.size());
//      assertEquals("US", codes.get(0).getValue());
//      assertEquals("UNITED STATES OF AMERICA", codes.get(0).getDesc());

      codes = codeDao.getCodes(CodeType.ORGANIZATION);
      assertNotNull(codes);
//      assertEquals(1, codes.size());
//      assertEquals("ARS", codes.get(0).getValue());
//      assertEquals("Agricultural Research Service", codes.get(0).getDesc());

      codes = codeDao.getCodes(CodeType.SAMPLEMEDIA);
      assertNotNull(codes);
//      assertEquals(1, codes.size());
//      assertEquals("water", codes.get(0).getValue());
//      assertNull(codes.get(0).getDesc());

      codes = codeDao.getCodes(CodeType.SITETYPE);
      assertNotNull(codes);
//      assertEquals(2, codes.size());
//      assertEquals("Stream", codes.get(1).getValue());
//      assertNull(codes.get(1).getDesc());

      codes = codeDao.getCodes(CodeType.STATECODE);
      assertNotNull(codes);
//      assertEquals(1, codes.size());
//      assertEquals("US:19", codes.get(0).getValue());
//      assertEquals("IOWA", codes.get(0).getDesc());
      

        //This one takes state code(s)
        parms.put("statecode", states);
        codes = codeDao.getCodes(CodeType.COUNTYCODE, parms);
        assertNotNull(codes);
//        assertEquals(4, codes.size());
//        assertEquals("US:19:015", codes.get(0).getValue());
//        assertEquals("US, IOWA, BOONE", codes.get(0).getDesc());

        //This one will accept country code(s)
        parms.clear();
        parms.put("countrycode", countries1);
        codes = codeDao.getCodes(CodeType.STATECODE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US:19", codes.get(0).getValue());
//        assertEquals("IOWA", codes.get(0).getDesc());

        parms.put("countrycode", countries2);
        codes = codeDao.getCodes(CodeType.STATECODE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US:19", codes.get(0).getValue());
//        //more than one country code gives us the country as part of the output.
//        assertEquals("US, IOWA", codes.get(0).getDesc());

    }

    @Test
    public void getCountsTest() {
        Map<String, Object> parms = new HashMap<String, Object>();
        //Check them all without parameters - Note that these will be brittle if the ci database is allowed to change.
        int codes = codeDao.getRecordCount(CodeType.CHARACTERISTICNAME, parms);
        assertNotNull(codes);
//        assertEquals(9, codes.size());
//        assertEquals("Simazine", codes.get(8).getValue());
//        assertNull(codes.get(8).getDesc());

        codes = codeDao.getRecordCount(CodeType.CHARACTERISTICTYPE, parms);
        assertNotNull(codes);
//        assertEquals(9, codes.size());
//        assertEquals("Simazine", codes.get(8).getValue());
//        assertNull(codes.get(8).getDesc());

        codes = codeDao.getRecordCount(CodeType.COUNTRYCODE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US", codes.get(0).getValue());
//        assertEquals("UNITED STATES OF AMERICA", codes.get(0).getDesc());

        codes = codeDao.getRecordCount(CodeType.COUNTYCODE, parms);
        assertNotNull(codes);
//        assertEquals(4, codes.size());
//        assertEquals("US:19:079", codes.get(1).getValue());
//        assertEquals("US, IOWA, HAMILTON", codes.get(1).getDesc());

        codes = codeDao.getRecordCount(CodeType.DATASOURCE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US", codes.get(0).getValue());
//        assertEquals("UNITED STATES OF AMERICA", codes.get(0).getDesc());

        codes = codeDao.getRecordCount(CodeType.ORGANIZATION, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("ARS", codes.get(0).getValue());
//        assertEquals("Agricultural Research Service", codes.get(0).getDesc());

        codes = codeDao.getRecordCount(CodeType.SAMPLEMEDIA, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("water", codes.get(0).getValue());
//        assertNull(codes.get(0).getDesc());

        codes = codeDao.getRecordCount(CodeType.SITETYPE, parms);
        assertNotNull(codes);
//        assertEquals(2, codes.size());
//        assertEquals("Stream", codes.get(1).getValue());
//        assertNull(codes.get(1).getDesc());

        codes = codeDao.getRecordCount(CodeType.STATECODE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US:19", codes.get(0).getValue());
//        assertEquals("IOWA", codes.get(0).getDesc());


        //Try a text search
        parms.put("text", "e");
        codes = codeDao.getRecordCount(CodeType.CHARACTERISTICNAME, parms);
        assertNotNull(codes);
//        assertEquals(9, codes.size());
//        assertEquals("Simazine", codes.get(8).getValue());
//        assertNull(codes.get(8).getDesc());

        codes = codeDao.getRecordCount(CodeType.CHARACTERISTICTYPE, parms);
        assertNotNull(codes);
//        assertEquals(9, codes.size());
//        assertEquals("Simazine", codes.get(8).getValue());
//        assertNull(codes.get(8).getDesc());

        codes = codeDao.getRecordCount(CodeType.COUNTRYCODE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US", codes.get(0).getValue());
//        assertEquals("UNITED STATES OF AMERICA", codes.get(0).getDesc());

        codes = codeDao.getRecordCount(CodeType.COUNTYCODE, parms);
        assertNotNull(codes);
//        assertEquals(4, codes.size());
//        assertEquals("US:19:079", codes.get(1).getValue());
//        assertEquals("US, IOWA, HAMILTON", codes.get(1).getDesc());

        codes = codeDao.getRecordCount(CodeType.DATASOURCE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US", codes.get(0).getValue());
//        assertEquals("UNITED STATES OF AMERICA", codes.get(0).getDesc());

        codes = codeDao.getRecordCount(CodeType.ORGANIZATION, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("ARS", codes.get(0).getValue());
//        assertEquals("Agricultural Research Service", codes.get(0).getDesc());

        codes = codeDao.getRecordCount(CodeType.SAMPLEMEDIA, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("water", codes.get(0).getValue());
//        assertNull(codes.get(0).getDesc());

        codes = codeDao.getRecordCount(CodeType.SITETYPE, parms);
        assertNotNull(codes);
//        assertEquals(2, codes.size());
//        assertEquals("Stream", codes.get(1).getValue());
//        assertNull(codes.get(1).getDesc());

        codes = codeDao.getRecordCount(CodeType.STATECODE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US:19", codes.get(0).getValue());
//        assertEquals("IOWA", codes.get(0).getDesc());


        //This one takes state code(s)
        parms.put("statecode", states);
        codes = codeDao.getRecordCount(CodeType.COUNTYCODE, parms);
        assertNotNull(codes);
//        assertEquals(4, codes.size());
//        assertEquals("US:19:015", codes.get(0).getValue());
//        assertEquals("US, IOWA, BOONE", codes.get(0).getDesc());

        //This one will accept country code(s)
        parms.clear();
        parms.put("countrycode", countries1);
        codes = codeDao.getRecordCount(CodeType.STATECODE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US:19", codes.get(0).getValue());
//        assertEquals("IOWA", codes.get(0).getDesc());

        parms.put("countrycode", countries2);
        codes = codeDao.getRecordCount(CodeType.STATECODE, parms);
        assertNotNull(codes);
//        assertEquals(1, codes.size());
//        assertEquals("US:19", codes.get(0).getValue());
//        //more than one country code gives us the country as part of the output.
//        assertEquals("US, IOWA", codes.get(0).getDesc());

    }
    
    @Test
    public void pagingTest() {
    	//no fetchsize = all
    	Map<String, Object> parms = new HashMap<String, Object>();
//        parms.put("offset", states);
//        parms.put("fetchSize", states);
        List<Code> codes = codeDao.getCodes(CodeType.COUNTRYCODE, parms);
        assertNotNull(codes);
        assertEquals(8, codes.size());
        
        parms.put("offset", 10);
//      parms.put("fetchSize", states);
        codes = codeDao.getCodes(CodeType.COUNTRYCODE, parms);
        assertNotNull(codes);
        assertEquals(8, codes.size());
        
        //null or zero offset starts at beginning
        parms.clear();
        parms.put("fetchSize", 3);
        codes = codeDao.getCodes(CodeType.COUNTRYCODE, parms);
        assertNotNull(codes);
        assertEquals(3, codes.size());
        assertEquals("CN", codes.get(0).getValue());

        parms.put("offset", 0);
        codes = codeDao.getCodes(CodeType.COUNTRYCODE, parms);
        assertNotNull(codes);
        assertEquals(3, codes.size());
        assertEquals("CN", codes.get(0).getValue());
        
        //Try getting next three
        parms.put("offset", 3);
        codes = codeDao.getCodes(CodeType.COUNTRYCODE, parms);
        assertNotNull(codes);
        assertEquals(3, codes.size());
        assertEquals("IZ", codes.get(0).getValue());
        
        //Try getting last bunch
        parms.put("offset", 6);
        codes = codeDao.getCodes(CodeType.COUNTRYCODE, parms);
        assertNotNull(codes);
        assertEquals(2, codes.size());
        assertEquals("RM", codes.get(0).getValue());
    }

}
