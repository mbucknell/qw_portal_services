package gov.usgs.cida.qw;

import gov.usgs.cida.qw.springinit.TestSpringConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.FileCopyUtils;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestSpringConfig.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ColumnSensingFlatXMLDataSetLoader.class, databaseConnection="dbUnitDatabaseConnection")
public abstract class BaseSpringTest {

    public static String ia = "US:19";
    public static String nb = "CN:90";
    public static Object[] states = new Object[]{ia, nb};
    public static String[] organizations = new String[]{"ARS"};
    public static String[] countries1 = new String[]{"US"};
    public static String[] countries2 = new String[]{"US", "CN", "GT", "MX"};

    public static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
    
	public List<String> acceptHeaders = new ArrayList<>(
			Arrays.asList("Total-Site-Count", "NWIS-Site-Count", "STEWARDS-Site-Count", "STORET-Site-Count",
					"Total-Result-Count", "NWIS-Result-Count", "STEWARDS-Result-Count", "STORET-Result-Count",
					"NWIS-Warning", "STEWARDS-Warning", "STORET-Warning"));

	public String harmonizeXml(String xmlDoc) {
		return xmlDoc.replace("\"", "'").replace("\r", "").replace("\n", "").replace("\t", "").replaceAll("> *<", "><");
	}
	
	public String getCompareFile(String file) throws IOException {
		return new String(FileCopyUtils.copyToByteArray(new ClassPathResource("testResult/" + file).getInputStream()));
	}

}
