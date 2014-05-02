package gov.usgs.cida.qw;

import static org.junit.Assert.fail;
import gov.usgs.cida.qw.utility.XmlStreamUtils;
import gov.usgs.cida.resourcefolder.Folder;
import gov.usgs.cida.resourcefolder.MessageBody;
import gov.usgs.cida.resourcefolder.Request;
import gov.usgs.cida.resourcefolder.ResourcePoint;
import gov.usgs.cida.resourcefolder.Response;
import gov.usgs.cida.resourcefolder.StartLine;
import gov.usgs.cida.resourcefolder.StatusCode;
import gov.usgs.cida.resourcefolder.Verb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.w3c.dom.Document;

import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/testContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class BaseSpringTest {

    private static XMLInputFactory FACTORY = XMLInputFactory.newInstance();
    public static final String PROVIDER1 = "Test1";
    public static final String PROVIDER2 = "Test2";

    @BeforeClass
    public static void setupJndi() throws Exception {
        SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        try {
            Context context = new InitialContext();
            context.bind("java:comp/env/WQP/providers/" + PROVIDER1, "http://localhost:8080/testqwa/");
            context.bind("java:comp/env/WQP/providers/" + PROVIDER2, "http://localhost:8080/testqwb/");
        } catch (NamingException e) {
            e.printStackTrace();
            fail();
        }
    }

    public static InputStream createFiStream(final String xmlString) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StAXDocumentSerializer staxDocumentSerializer = new StAXDocumentSerializer();
            staxDocumentSerializer.setCharacterEncodingScheme(QWConstants.DEFAULT_ENCODING);
            staxDocumentSerializer.setOutputStream(outputStream);
            staxDocumentSerializer.writeStartDocument();

            XMLStreamReader streamReader = FACTORY.createXMLStreamReader(new StringReader(xmlString));
            XmlStreamUtils.copy(streamReader, staxDocumentSerializer);
            streamReader.close();

            staxDocumentSerializer.writeEndDocument();
            staxDocumentSerializer.flush();
            staxDocumentSerializer.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return null;
        }
    }

    public class TestRequest implements Request {

        private URI resourceDefinitionURI;
        private String jobCorrelationID;

        public void setJobCorrelationID(final String inJobCorrelationID) {
            jobCorrelationID = inJobCorrelationID;
        }

        @Override
        public String getJobCorrelationID() {
            return jobCorrelationID;
        }

        public void setResourceDefinitionURI(final String uriString) {
            try {
                resourceDefinitionURI = new URI(uriString);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Override
        public URI getResourceDefinitionURI() {
            return resourceDefinitionURI;
        }

        @Override
        public StartLine getHTTPStartLine() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isRequest() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isResponse() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Map<String, List<String>> getRequestParameters() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Map<String, Set<String>> getHTTPHeaders() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public MessageBody getMessageBody() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public URI getDestinationEndpointURI() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Verb getVerb() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Request createDelegate(URI newDestinationEndpointURI) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    public class TestResponse implements Response {
        private URI responsibleEndpoint;
        private MessageBody messageBody;
        private StatusCode status;
        private List<Response> containedResponses;
        private Map<String, Set<String>> httpHeaders;

        @Override
        public String getJobCorrelationID() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public URI getResourceDefinitionURI() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public StartLine getHTTPStartLine() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isRequest() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isResponse() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Map<String, List<String>> getRequestParameters() {
            // TODO Auto-generated method stub
            return null;
        }

        public void setHTTPHeaders(final Map<String, Set<String>> inHttpHeaders) {
            httpHeaders = inHttpHeaders;
        }

        @Override
        public Map<String, Set<String>> getHTTPHeaders() {
            return httpHeaders;
        }

        public void setMessageBody(final MessageBody inMessageBody) {
            messageBody = inMessageBody;
        }

        @Override
        public MessageBody getMessageBody() {
            return messageBody;
        }

        public void setResponsibleEndpoint(final String inResponsibleEndpoint) {
            try {
                responsibleEndpoint = new URI(inResponsibleEndpoint);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                fail();
            }
        }

        @Override
        public URI getResponsibleEndpoint() {
            return responsibleEndpoint;
        }

        public void setStatusCode(final StatusCode inStatus) {
            status = inStatus;
        }

        @Override
        public StatusCode getStatus() {
            return status;
        }

        public void setContainedResponses(final List<Response> inContainedResponses) {
            containedResponses = inContainedResponses;
        }

        @Override
        public List<Response> getContainedResponses() {
            return containedResponses;
        }


    }

    public class TestFolder implements Folder {

        @Override
        public URI getFolderURI() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Set<ResourcePoint> getResourcePoints() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Set<Folder> getContainedFolders() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Set<URI> getOfferedResources() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Document reportContents() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Document getJobHistories(String jobCorrelationID) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Response distribute(Request request) {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
