package gov.usgs.cida.qw;

import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LastUpdateDaoTest extends BaseSpringTest {
	
    @Autowired
    private LastUpdateDao lastUpdateDao;

    @Test
    public void getLastEtlTest() {
    	LocalDateTime x = lastUpdateDao.getLastEtl();
    	assertNotNull(x);
    	//TODO make sure correct value after we have a ci database...
    }

}
