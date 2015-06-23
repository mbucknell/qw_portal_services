package gov.usgs.cida.qw;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@Category(IntegrationTest.class)
@DatabaseSetup("classpath:/testData/lastUpdateDao.xml")
public class LastUpdateDaoTest extends BaseSpringTest {
	
    @Autowired
    private LastUpdateDao lastUpdateDao;

    @Test
    public void getLastEtlTest() {
    	LocalDateTime x = lastUpdateDao.getLastEtl();
    	assertEquals("2015-03-27T13:38:05.000", x.toString());
    }

}
