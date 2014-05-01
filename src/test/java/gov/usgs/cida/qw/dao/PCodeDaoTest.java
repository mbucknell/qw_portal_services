package gov.usgs.cida.qw.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.usgs.cida.qw.BaseSpringTest;
import gov.usgs.cida.qw.dao.intfc.IPCodeDao;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PCodeDaoTest extends BaseSpringTest {

    @Autowired
    private IPCodeDao pCodeDao;

    @Test
    public void getLastModified() {
        Date lastModified = pCodeDao.getLastModified();
        assertNotNull("got something", lastModified);
    }

    @Test
    public void getRows() {
        List<LinkedHashMap<String, Object>> rows = pCodeDao.getRows();
        assertNotNull("got something", rows);
        assertTrue("got a bunch of rows of data", rows.size() > 5);
        assertEquals("got the correct number of columns", 11, rows.get(0).keySet().size());

        Object[] cols = rows.get(0).keySet().toArray();
        assertEquals("got correct column in position 1", "parm_cd", cols[0]);
        assertEquals("got correct column in position 2", "description", cols[1]);
        assertEquals("got correct column in position 3", "characteristicname", cols[2]);
        assertEquals("got correct column in position 4", "measureunitcode", cols[3]);
        assertEquals("got correct column in position 5", "resultsamplefraction", cols[4]);
        assertEquals("got correct column in position 6", "resulttemperaturebasis", cols[5]);
        assertEquals("got correct column in position 7", "resultstatisticalbasis", cols[6]);
        assertEquals("got correct column in position 8", "resulttimebasis", cols[7]);
        assertEquals("got correct column in position 9", "resultweightbasis", cols[8]);
        assertEquals("got correct column in position 10", "resultparticlesizebasis", cols[9]);
        assertEquals("got correct column in position 11", "last_rev_dt", cols[10]);
    }

}
