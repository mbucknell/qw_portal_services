package gov.usgs.wma.qw.srsnames;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.usgs.wma.qw.BaseIT;
import gov.usgs.wma.qw.springinit.DBTestConfig;
import gov.usgs.wma.qw.srsnames.PCodeDao;

@SpringBootTest(webEnvironment=WebEnvironment.NONE,
		classes={DBTestConfig.class, PCodeDao.class})
@DatabaseSetup("classpath:/testData/srsnames.xml")
public class PCodeDaoIT extends BaseIT {

	@Autowired
	private PCodeDao pCodeDao;

	@Test
	public void getLastModified() {
		LocalDate lastModified = pCodeDao.getLastModified();
		assertNotNull(lastModified, "got something");
		assertEquals("2015-01-05", lastModified.toString());
	}

	@Test
	public void getRows() {
		List<LinkedHashMap<String, Object>> rows = pCodeDao.getRows();
		assertNotNull(rows, "got something");
		assertEquals(10, rows.size(), "got the correct number rows");
		assertEquals(11, rows.get(0).keySet().size(), "got the correct number of columns");

		Object[] cols = rows.get(0).keySet().toArray();
		assertEquals("parm_cd", cols[0], "got correct column in position 1");
		assertEquals("description", cols[1], "got correct column in position 2");
		assertEquals("characteristicname", cols[2], "got correct column in position 3");
		assertEquals("measureunitcode", cols[3], "got correct column in position 4");
		assertEquals("resultsamplefraction", cols[4], "got correct column in position 5");
		assertEquals("resulttemperaturebasis", cols[5], "got correct column in position 6");
		assertEquals("resultstatisticalbasis", cols[6], "got correct column in position 7");
		assertEquals("resulttimebasis", cols[7], "got correct column in position 8");
		assertEquals("resultweightbasis", cols[8], "got correct column in position 9");
		assertEquals("resultparticlesizebasis", cols[9], "got correct column in position 10");
		assertEquals("last_rev_dt", cols[10], "got correct column in position 11");

		assertEquals("00060", rows.get(0).get("parm_cd"));
		assertEquals("Discharge, cubic feet per second", rows.get(0).get("description"));
		assertEquals("Stream flow, mean. daily", rows.get(0).get("characteristicname"));
		assertEquals("ft3/s", rows.get(0).get("measureunitcode"));
		assertEquals("", rows.get(0).get("resultsamplefraction"));
		assertEquals("", rows.get(0).get("resulttemperaturebasis"));
		assertEquals("Mean", rows.get(0).get("resultstatisticalbasis"));
		assertEquals("1 Day", rows.get(0).get("resulttimebasis"));
		assertEquals("", rows.get(0).get("resultweightbasis"));
		assertEquals("", rows.get(0).get("resultparticlesizebasis"));
		assertEquals("2013-07-01", rows.get(0).get("last_rev_dt"));

		assertEquals("00065", rows.get(1).get("parm_cd"));
		assertEquals("Gage height, feet", rows.get(1).get("description"));
		assertEquals("Height, gage", rows.get(1).get("characteristicname"));
		assertEquals("ft", rows.get(1).get("measureunitcode"));
		assertEquals("", rows.get(1).get("resultsamplefraction"));
		assertEquals("", rows.get(1).get("resulttemperaturebasis"));
		assertEquals("", rows.get(1).get("resultstatisticalbasis"));
		assertEquals("", rows.get(1).get("resulttimebasis"));
		assertEquals("", rows.get(1).get("resultweightbasis"));
		assertEquals("", rows.get(1).get("resultparticlesizebasis"));
		assertEquals("2008-02-21", rows.get(1).get("last_rev_dt"));

		assertEquals("00315", rows.get(2).get("parm_cd"));
		assertEquals("Biochemical oxygen demand, water, unfiltered, 7 days at 20 degrees Celsius, milligrams per liter", rows.get(2).get("description"));
		assertEquals("Biochemical oxygen demand, non-standard conditions", rows.get(2).get("characteristicname"));
		assertEquals("mg/l", rows.get(2).get("measureunitcode"));
		assertEquals("Total", rows.get(2).get("resultsamplefraction"));
		assertEquals("20 deg C", rows.get(2).get("resulttemperaturebasis"));
		assertEquals("", rows.get(2).get("resultstatisticalbasis"));
		assertEquals("7 Day", rows.get(2).get("resulttimebasis"));
		assertEquals("", rows.get(2).get("resultweightbasis"));
		assertEquals("", rows.get(2).get("resultparticlesizebasis"));
		assertEquals("2008-02-21", rows.get(2).get("last_rev_dt"));

		assertEquals("00746", rows.get(3).get("parm_cd"));
		assertEquals("Sulfide, water, filtered, milligrams per liter", rows.get(3).get("description"));
		assertEquals("Sulfide", rows.get(3).get("characteristicname"));
		assertEquals("mg/l", rows.get(3).get("measureunitcode"));
		assertEquals("Dissolved", rows.get(3).get("resultsamplefraction"));
		assertEquals("", rows.get(3).get("resulttemperaturebasis"));
		assertEquals("", rows.get(3).get("resultstatisticalbasis"));
		assertEquals("", rows.get(3).get("resulttimebasis"));
		assertEquals("", rows.get(3).get("resultweightbasis"));
		assertEquals("", rows.get(3).get("resultparticlesizebasis"));
		assertEquals("2008-02-21", rows.get(3).get("last_rev_dt"));

		assertEquals("01090", rows.get(4).get("parm_cd"));
		assertEquals("Zinc, water, filtered, micrograms per liter", rows.get(4).get("description"));
		assertEquals("Zinc", rows.get(4).get("characteristicname"));
		assertEquals("ug/l", rows.get(4).get("measureunitcode"));
		assertEquals("Dissolved", rows.get(4).get("resultsamplefraction"));
		assertEquals("", rows.get(4).get("resulttemperaturebasis"));
		assertEquals("", rows.get(4).get("resultstatisticalbasis"));
		assertEquals("", rows.get(4).get("resulttimebasis"));
		assertEquals("", rows.get(4).get("resultweightbasis"));
		assertEquals("", rows.get(4).get("resultparticlesizebasis"));
		assertEquals("2008-09-23", rows.get(4).get("last_rev_dt"));

		assertEquals("01218", rows.get(5).get("parm_cd"));
		assertEquals("Terbium, water, unfiltered, micrograms per liter", rows.get(5).get("description"));
		assertEquals("Terbium", rows.get(5).get("characteristicname"));
		assertEquals("ug/l", rows.get(5).get("measureunitcode"));
		assertEquals("Total", rows.get(5).get("resultsamplefraction"));
		assertEquals("", rows.get(5).get("resulttemperaturebasis"));
		assertEquals("", rows.get(5).get("resultstatisticalbasis"));
		assertEquals("", rows.get(5).get("resulttimebasis"));
		assertEquals("", rows.get(5).get("resultweightbasis"));
		assertEquals("", rows.get(5).get("resultparticlesizebasis"));
		assertEquals("2008-09-23", rows.get(5).get("last_rev_dt"));

		assertEquals("01425", rows.get(6).get("parm_cd"));
		assertEquals("Total nitrogen, soil, total digestion, milligrams per liter", rows.get(6).get("description"));
		assertEquals("Nitrogen, mixed forms (NH3), (NH4), organic, (NO2) and (NO3)", rows.get(6).get("characteristicname")); 
		assertEquals("mg/l", rows.get(6).get("measureunitcode"));
		assertEquals("Total", rows.get(6).get("resultsamplefraction"));
		assertEquals("", rows.get(6).get("resulttemperaturebasis"));
		assertEquals("", rows.get(6).get("resultstatisticalbasis"));
		assertEquals("", rows.get(6).get("resulttimebasis"));
		assertEquals("", rows.get(6).get("resultweightbasis"));
		assertEquals("", rows.get(6).get("resultparticlesizebasis"));
		assertEquals("2009-06-23", rows.get(6).get("last_rev_dt"));

		assertEquals("19504", rows.get(7).get("parm_cd"));
		assertEquals("Polonium-210 counting error, water, filtered, picocuries per liter", rows.get(7).get("description"));
		assertEquals("Polonium-210", rows.get(7).get("characteristicname"));
		assertEquals("pCi/L", rows.get(7).get("measureunitcode"));
		assertEquals("Dissolved", rows.get(7).get("resultsamplefraction"));
		assertEquals("", rows.get(7).get("resulttemperaturebasis"));
		assertEquals("Counting Error", rows.get(7).get("resultstatisticalbasis"));
		assertEquals("", rows.get(7).get("resulttimebasis"));
		assertEquals("", rows.get(7).get("resultweightbasis"));
		assertEquals("", rows.get(7).get("resultparticlesizebasis"));
		assertEquals("2008-09-23", rows.get(7).get("last_rev_dt"));

		assertEquals("30171", rows.get(8).get("parm_cd"));
		assertEquals("Naphthalene, soil, recoverable, dry weight, milligrams per kilogram", rows.get(8).get("description"));
		assertEquals("Naphthalene", rows.get(8).get("characteristicname"));
		assertEquals("mg/kg", rows.get(8).get("measureunitcode"));
		assertEquals("Recoverable", rows.get(8).get("resultsamplefraction"));
		assertEquals("", rows.get(8).get("resulttemperaturebasis"));
		assertEquals("", rows.get(8).get("resultstatisticalbasis"));
		assertEquals("", rows.get(8).get("resulttimebasis"));
		assertEquals("Dry", rows.get(8).get("resultweightbasis"));
		assertEquals("", rows.get(8).get("resultparticlesizebasis"));
		assertEquals("2008-09-23", rows.get(8).get("last_rev_dt"));

		assertEquals("82279", rows.get(9).get("parm_cd"));
		assertEquals("Lead, street debris smaller than 0.5 millimeters, dry weight, milligrams per kilogram", rows.get(9).get("description"));
		assertEquals("Lead", rows.get(9).get("characteristicname"));
		assertEquals("mg/kg", rows.get(9).get("measureunitcode"));
		assertEquals("", rows.get(9).get("resultsamplefraction"));
		assertEquals("", rows.get(9).get("resulttemperaturebasis"));
		assertEquals("", rows.get(9).get("resultstatisticalbasis"));
		assertEquals("", rows.get(9).get("resulttimebasis"));
		assertEquals("Dry", rows.get(9).get("resultweightbasis"));
		assertEquals("< 0.5 mm", rows.get(9).get("resultparticlesizebasis"));
		assertEquals("2010-09-24", rows.get(9).get("last_rev_dt"));
	}


}
