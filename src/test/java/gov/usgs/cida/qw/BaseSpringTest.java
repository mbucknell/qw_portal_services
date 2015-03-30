package gov.usgs.cida.qw;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
//import gov.usgs.cida.qw.utility.XmlStreamUtils;
//import gov.usgs.cida.resourcefolder.Folder;
//import gov.usgs.cida.resourcefolder.MessageBody;
//import gov.usgs.cida.resourcefolder.Request;
//import gov.usgs.cida.resourcefolder.ResourcePoint;
//import gov.usgs.cida.resourcefolder.Response;
//import gov.usgs.cida.resourcefolder.StartLine;
//import gov.usgs.cida.resourcefolder.StatusCode;
//import gov.usgs.cida.resourcefolder.Verb;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/testContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader = ColumnSensingFlatXMLDataSetLoader.class)
public abstract class BaseSpringTest {

	public static String DEFAULT_ENCODING = "UTF-8";

    public static String ia = "US:19";
    public static String nb = "CN:90";
    public static Object[] states = new Object[]{ia, nb};
    public static String[] organizations = new String[]{"ARS"};
    public static String[] countries1 = new String[]{"US"};
    public static String[] countries2 = new String[]{"US", "CN", "GT", "MX"};

	public String harmonizeXml(String xmlDoc) {
		return xmlDoc.replace("\"", "'").replace("\r", "").replace("\n", "").replace("\t", "").replaceAll("> *<", "><");
	}
}
