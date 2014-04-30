package gov.usgs.cida.qw.resourcefolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.usgs.cida.qw.BaseSpringTest;
import gov.usgs.cida.qw.QWConstants;
import gov.usgs.cida.qw.code.AggregatedCode;
import gov.usgs.cida.qw.code.Code;
import gov.usgs.cida.resourcefolder.PackageConstants;
import gov.usgs.cida.resourcefolder.Response;
import gov.usgs.cida.resourcefolder.StatusCode;
import gov.usgs.cida.resourcefolder.basicimpl.PackageUtils;
import gov.usgs.cida.resourcefolder.basicimpl.ResponseImpl;
import gov.usgs.cida.resourcefolder.basicimpl.StreamMessageBody;
import gov.usgs.cida.resourcefolder.basicimpl.StringMessageBody;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class QwCodeOuterFaceTest extends BaseSpringTest {

    @Test
    public void constructorTest() {
        try {
            new QwCodeOuterFace(null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Parameter 'inFolder' not permitted to be null.", e.getMessage());
        }
    }

    @Test
    public void callResourcesTest() {
        QwCodeOuterFace qcof = new QwCodeOuterFace(new TestFolder());
        try {
            qcof.callResources(null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The Request is null.", e.getMessage());
        }

        try {
            qcof.callResources(null, null);
            fail("Should have gotten an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not implemented yet.", e.getMessage());
        }
    }

    @Test
    public void aggregateResponsesTest() {
        //private Response aggregateResponses(final Request request, final Response response)
        QwCodeOuterFace qcof = new QwCodeOuterFace(new TestFolder());
        try {
            qcof.setCodeUri(new URI("http://jimbob.bus/a"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail();
        }

        try {
            qcof.aggregateResponses(null, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (Exception e) {
            assertEquals("The Request is invalid.", e.getMessage());
        }

        TestRequest testRequest = new TestRequest();
        try {
            qcof.aggregateResponses(testRequest, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (Exception e) {
            assertEquals("The Request is invalid.", e.getMessage());
        }

        testRequest.setJobCorrelationID("abc");
        try {
            qcof.aggregateResponses(testRequest, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (Exception e) {
            assertEquals("The Request is invalid.", e.getMessage());
        }

        testRequest.setResourceDefinitionURI("http://jimbob.bus/a");
        try {
            qcof.aggregateResponses(testRequest, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (Exception e) {
            assertEquals("The Response is null.", e.getMessage());
        }

        TestResponse testResponse = new TestResponse();
        try {
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.BAD_REQUEST_400, resp.getStatus());
            assertEquals("HTTP/1.1 400 Bad Request", resp.getHTTPStartLine().toString());
            assertEquals(0, resp.getHTTPHeaders().size());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            testResponse.setStatusCode(StatusCode.REDIRECT_300);
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.BAD_REQUEST_400, resp.getStatus());
            assertEquals("HTTP/1.1 400 Bad Request", resp.getHTTPStartLine().toString());
            assertEquals(0, resp.getHTTPHeaders().size());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            testResponse.setContainedResponses(new ArrayList<Response>());
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.BAD_REQUEST_400, resp.getStatus());
            assertEquals("HTTP/1.1 400 Bad Request", resp.getHTTPStartLine().toString());
            assertEquals(0, resp.getHTTPHeaders().size());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            testResponse.setHTTPHeaders(new HashMap<String, Set<String>>());
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.REDIRECT_300, resp.getStatus());
            assertEquals("HTTP/1.1 300 Redirection", resp.getHTTPStartLine().toString());
            assertEquals(0, resp.getHTTPHeaders().size());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        TestResponse innerResponse = new TestResponse();
        try {
            innerResponse.setMessageBody(new StreamMessageBody(createFiStream("<Codes><Code value=\"a\" desc=\"adesc\"/></Codes>")));
            testResponse.setContainedResponses(Arrays.asList((Response) innerResponse));
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.REDIRECT_300, resp.getStatus());
            assertEquals("HTTP/1.1 300 Redirection", resp.getHTTPStartLine().toString());
            assertEquals(0, resp.getHTTPHeaders().size());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        Map<String, Set<String>> innerHttpHeaders = new HashMap<>();
        try {
            Set<String> values = new HashSet<>();
            values.add("dude");
            innerHttpHeaders.put("dave", values);
            innerResponse.setHTTPHeaders(innerHttpHeaders);
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.REDIRECT_300, resp.getStatus());
            assertEquals("HTTP/1.1 300 Redirection", resp.getHTTPStartLine().toString());
            assertEquals(0, resp.getHTTPHeaders().size());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            Set<String> values2 = new HashSet<>();
            values2.add("warning dude");
            innerHttpHeaders.put(QWConstants.HEADER_WARNING, values2);
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.REDIRECT_300, resp.getStatus());
            assertEquals("HTTP/1.1 300 Redirection", resp.getHTTPStartLine().toString());
            assertEquals(1, resp.getHTTPHeaders().size());
            assertEquals("{Warning=[warning dude]}", resp.getHTTPHeaders().toString());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            Map<String, Set<String>> outerHttpHeaders = new HashMap<>();
            Set<String> values3 = new HashSet<>();
            values3.add("outer warning dude");
            outerHttpHeaders.put(QWConstants.HEADER_WARNING, values3);
            testResponse.setHTTPHeaders(outerHttpHeaders);
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.REDIRECT_300, resp.getStatus());
            assertEquals("HTTP/1.1 300 Redirection", resp.getHTTPStartLine().toString());
            assertEquals(1, resp.getHTTPHeaders().size());
            assertEquals("{Warning=[outer warning dude, warning dude]}", resp.getHTTPHeaders().toString());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            innerResponse.setStatusCode(StatusCode.REDIRECT_300);
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.REDIRECT_300, resp.getStatus());
            assertEquals("HTTP/1.1 300 Redirection", resp.getHTTPStartLine().toString());
            assertEquals(1, resp.getHTTPHeaders().size());
            assertEquals("{Warning=[outer warning dude, warning dude]}", resp.getHTTPHeaders().toString());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            innerResponse.setStatusCode(StatusCode.OK_200);
            ResponseImpl resp = qcof.aggregateResponses(testRequest, testResponse);
            assertNotNull(resp);
            assertEquals("abc", resp.getJobCorrelationID());
            assertEquals(qcof.getCodeUri(), resp.getResponsibleEndpoint());
            assertEquals(qcof.getCodeUri(), resp.getResourceDefinitionURI());
            assertEquals(StatusCode.REDIRECT_300, resp.getStatus());
            assertEquals("HTTP/1.1 300 Redirection", resp.getHTTPStartLine().toString());
            assertEquals(1, resp.getHTTPHeaders().size());
            assertEquals("{Warning=[outer warning dude, warning dude]}", resp.getHTTPHeaders().toString());
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes><Code value=\"a\" desc=\"adesc\" providers=\"UNKNOWN\"/></Codes>",
                    PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, ""));
            assertEquals(1, resp.getContainedResponses().size());
            assertTrue(resp.getContainedResponses().contains(testResponse));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void processCodesTest() {
        Map<String, AggregatedCode> inCodes = new HashMap<>();
        Map<String, AggregatedCode> outCodes = new HashMap<>();
        TestResponse testResponse = new TestResponse();
        QwCodeOuterFace qcof = new QwCodeOuterFace(new TestFolder());

        try {
            qcof.processCodes(null, null, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The Provider is null.", e.getMessage());
        }

        try {
            qcof.processCodes("", null, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The Provider is null.", e.getMessage());
        }

        try {
            qcof.processCodes(PROVIDER1, null, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The Response or it's MessageBody are null.", e.getMessage());
        }

        try {
            qcof.processCodes(PROVIDER1, testResponse, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The Response or it's MessageBody are null.", e.getMessage());
        }

        testResponse.setMessageBody(new StringMessageBody("junk"));
        try {
            qcof.processCodes(PROVIDER1, testResponse, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Problems unmarshalling MessageBody stream:  : java.io.EOFException: Unexpeceted EOF", e.getMessage());
        }

        testResponse.setMessageBody(new StreamMessageBody(createFiStream("<junk/>")));
        try {
            qcof.processCodes(PROVIDER1, testResponse, null);
            fail("Should have gotten an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Problems unmarshalling MessageBody stream: junk : junk", e.getMessage());
        }

        testResponse.setMessageBody(new StreamMessageBody(createFiStream("<Codes/>")));
        outCodes = qcof.processCodes(PROVIDER1, testResponse, null);
        assertEquals(0, outCodes.size());

        testResponse.setMessageBody(new StreamMessageBody(createFiStream("<Codes><Code value=\"a\" desc=\"adesc\"/></Codes>")));
        outCodes = qcof.processCodes(PROVIDER1, testResponse, null);
        assertEquals(1, outCodes.size());
        assertTrue(outCodes.containsKey("a"));
        assertEquals("a", outCodes.get("a").getValue());
        assertEquals("adesc", outCodes.get("a").getDesc());
        assertEquals(PROVIDER1.toUpperCase(), outCodes.get("a").getProviders());

        inCodes.putAll(outCodes);
        testResponse.setMessageBody(new StreamMessageBody(createFiStream("<Codes><Code value=\"a\" desc=\"adesc\"/><Code value=\"b\" desc=\"bdesc\"/></Codes>")));
        outCodes = qcof.processCodes(PROVIDER2, testResponse, inCodes);
        assertEquals(2, outCodes.size());

        assertTrue(outCodes.containsKey("a"));
        assertEquals("a", outCodes.get("a").getValue());
        assertEquals("adesc", outCodes.get("a").getDesc());
        assertEquals(PROVIDER1.toUpperCase() + " " + PROVIDER2.toUpperCase(), outCodes.get("a").getProviders());

        assertTrue(outCodes.containsKey("b"));
        assertEquals("b", outCodes.get("b").getValue());
        assertEquals("bdesc", outCodes.get("b").getDesc());
        assertEquals(PROVIDER2.toUpperCase(), outCodes.get("b").getProviders());

        testResponse.setMessageBody(new StreamMessageBody(createFiStream("<Codes><Code/></Codes>")));
        outCodes = qcof.processCodes(PROVIDER1, testResponse, null);
        inCodes.clear();
        inCodes.putAll(outCodes);
        testResponse.setMessageBody(new StreamMessageBody(createFiStream("<Codes><Code/></Codes>")));
        outCodes = qcof.processCodes(PROVIDER2, testResponse, inCodes);
        assertEquals(1, outCodes.size());

        assertTrue(outCodes.containsKey(null));
        assertEquals(null, outCodes.get(null).getValue());
        assertEquals(null, outCodes.get(null).getDesc());
        assertEquals(PROVIDER1.toUpperCase() + " " + PROVIDER2.toUpperCase(), outCodes.get(null).getProviders());

        testResponse.setMessageBody(new StreamMessageBody(createFiStream("<Codes><Code value=\"\" desc=\"\"/></Codes>")));
        outCodes = qcof.processCodes(PROVIDER1, testResponse, null);
        inCodes.clear();
        inCodes.putAll(outCodes);
        testResponse.setMessageBody(new StreamMessageBody(createFiStream("<Codes><Code value=\"\" desc=\"\"/></Codes>")));
        outCodes = qcof.processCodes(PROVIDER2, testResponse, inCodes);
        assertEquals(1, outCodes.size());

        assertTrue(outCodes.containsKey(""));
        assertEquals("", outCodes.get("").getValue());
        assertEquals("", outCodes.get("").getDesc());
        assertEquals(PROVIDER1.toUpperCase() + " " + PROVIDER2.toUpperCase(), outCodes.get("").getProviders());
    }

    @Test
    public void testConvertCodesToXmlInputStream() {
        Map<String, AggregatedCode> codes = new HashMap<>();
        QwCodeOuterFace qcof = new QwCodeOuterFace(new TestFolder());


        try {
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(qcof.convertCodesToXmlInputStream(null), null, "").replace(PackageConstants.CRLF, ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes></Codes>",
                    PackageUtils.writeFromStream(qcof.convertCodesToXmlInputStream(codes), null, "").replace(PackageConstants.CRLF, ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            codes.put("wow", new AggregatedCode(new Code("wow", "wowDesc"), PROVIDER1));
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes><Code value=\"wow\" desc=\"wowDesc\" providers=\"" + PROVIDER1.toUpperCase() + "\"/></Codes>",
                    PackageUtils.writeFromStream(qcof.convertCodesToXmlInputStream(codes), null, "").replace(PackageConstants.CRLF, ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            codes.put("any", new AggregatedCode(new Code("any", "anyDesc"), PROVIDER1 + " " + PROVIDER2));
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes><Code value=\"any\" desc=\"anyDesc\" providers=\"TEST1 TEST2\"/>"
                    + "<Code value=\"wow\" desc=\"wowDesc\" providers=\"TEST1\"/></Codes>",
                    PackageUtils.writeFromStream(qcof.convertCodesToXmlInputStream(codes), null, "").replace(PackageConstants.CRLF, ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            codes.put("some", new AggregatedCode(new Code("some", "someDesc"), PROVIDER1 + " " + PROVIDER2));
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes><Code value=\"any\" desc=\"anyDesc\" providers=\"TEST1 TEST2\"/>"
                    + "<Code value=\"some\" desc=\"someDesc\" providers=\"TEST1 TEST2\"/><Code value=\"wow\" desc=\"wowDesc\" providers=\"TEST1\"/></Codes>",
                    PackageUtils.writeFromStream(qcof.convertCodesToXmlInputStream(codes), null, "").replace(PackageConstants.CRLF, ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            codes.clear();
            codes.put(null, new AggregatedCode(new Code(null, null), PROVIDER1));
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes><Code value=\"\" desc=\"\" providers=\"" + PROVIDER1.toUpperCase() + "\"/></Codes>",
                    PackageUtils.writeFromStream(qcof.convertCodesToXmlInputStream(codes), null, "").replace(PackageConstants.CRLF, ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        try {
            codes.clear();
            codes.put("", new AggregatedCode(new Code("", ""), PROVIDER1));
            assertEquals("<?xml version='1.0' encoding='UTF-8'?><Codes><Code value=\"\" desc=\"\" providers=\"" + PROVIDER1.toUpperCase() + "\"/></Codes>",
                    PackageUtils.writeFromStream(qcof.convertCodesToXmlInputStream(codes), null, "").replace(PackageConstants.CRLF, ""));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
