package gov.usgs.cida.qw;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.cida.qw.springinit.DBTestConfig;

@SpringBootTest(webEnvironment=WebEnvironment.NONE,
		classes={DBTestConfig.class, LastUpdateDao.class})
@DatabaseSetup("classpath:/testData/lastUpdateDao.xml")
public class LastUpdateDaoIT extends BaseIT {

	@Autowired
	private LastUpdateDao lastUpdateDao;

	@Test
	public void getLastEtlTest() {
		LocalDateTime x = lastUpdateDao.getLastEtl();
		assertEquals("2015-03-27T13:38:05", x.toString());
	}

}
