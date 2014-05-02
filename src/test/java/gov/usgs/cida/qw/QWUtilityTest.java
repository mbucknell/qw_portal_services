package gov.usgs.cida.qw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QWUtilityTest extends BaseSpringTest {

    @Test
    public void providersTest() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><providers><provider>" + PROVIDER1
                + "</provider><provider>" + PROVIDER2 + "</provider></providers>", QWUtility.PROVIDERS_XML);

        assertEquals(2, QWUtility.PROVIDERS_MAP.size());
        assertTrue(QWUtility.PROVIDERS_MAP.containsKey(PROVIDER1));
        assertTrue(QWUtility.PROVIDERS_MAP.containsValue("http://localhost:8080/testqwa/"));
        assertEquals("http://localhost:8080/testqwa/", QWUtility.PROVIDERS_MAP.get(PROVIDER1));

        assertTrue(QWUtility.PROVIDERS_MAP.containsKey(PROVIDER2));
        assertTrue(QWUtility.PROVIDERS_MAP.containsValue("http://localhost:8080/testqwb/"));
        assertEquals("http://localhost:8080/testqwb/", QWUtility.PROVIDERS_MAP.get(PROVIDER2));
    }

    @Test
    public void determineProviderTest() {
        assertEquals("unknown", QWUtility.determineProvider(null));

        TestResponse testResponse = new TestResponse();
        assertEquals("unknown", QWUtility.determineProvider(testResponse));

        testResponse.setResponsibleEndpoint("");
        assertEquals("unknown", QWUtility.determineProvider(testResponse));

        testResponse.setResponsibleEndpoint("http://localhost:8080/testqw/");
        assertEquals("unknown", QWUtility.determineProvider(testResponse));

        testResponse.setResponsibleEndpoint("http://localhost:8080/testqwa/");
        assertEquals(PROVIDER1, QWUtility.determineProvider(testResponse));

        testResponse.setResponsibleEndpoint("http://localhost:8080/testqwb/");
        assertEquals(PROVIDER2, QWUtility.determineProvider(testResponse));

    }
}
