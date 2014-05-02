package gov.usgs.cida.qw.webservice.codes;

import gov.usgs.cida.qw.QWConstants;
import gov.usgs.cida.qw.webservice.MvcService;
import gov.usgs.cida.resourcefolder.MessageBody;
import gov.usgs.cida.resourcefolder.OuterFace;
import gov.usgs.cida.resourcefolder.Request;
import gov.usgs.cida.resourcefolder.Response;
import gov.usgs.cida.resourcefolder.StatusCode;
import gov.usgs.cida.resourcefolder.Verb;
import gov.usgs.cida.resourcefolder.basicimpl.RequestImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("codes")
public abstract class AggregatedCodesMvcService extends MvcService {

    private static final Logger LOG = LoggerFactory.getLogger(AggregatedCodesMvcService.class);

    protected OuterFace outerFace;
    protected URI dataSource;

    protected void doCodeRequest(
            final HttpServletRequest inRequest,
            final OuterFace inOuterFace,
            final URI inResourceDefinitionURI,
            final Map<String, List<String>> inQueryParams,
            final HttpServletResponse inResponse) throws URISyntaxException, IOException {

        LOG.debug("requested URL: " + inRequest.getRequestURL() + "?" + inRequest.getQueryString());

        inResponse.setCharacterEncoding(QWConstants.DEFAULT_ENCODING);
        if (inRequest.getParameterMap().isEmpty()) {
            LOG.debug("No parameters");
            inResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            inResponse.addHeader(QWConstants.HEADER_CONTENT_TYPE, QWConstants.MIME_TYPE_TEXT);
        } else {
            LOG.debug("got parameters");
            inResponse.addHeader(QWConstants.HEADER_CONTENT_TYPE, QWConstants.MIME_TYPE_APPLICATION_XML);

            Response resp = inOuterFace.callResources(buildRequest(inRequest, inResourceDefinitionURI, inQueryParams));
            inResponse.setStatus(resp.getStatus().getCode());

            if (resp.getStatus().equals(StatusCode.OK_200)) {
                //TODO - Better way to put the Response to the HttpServletResponse?
                byte[] buffer = new byte[1024*16];
                int len;
                InputStream msg = resp.getMessageBody().deliverAsStream();
                while ((len = msg.read(buffer, 0, buffer.length)) != -1) {
                    LOG.debug("len:" + len);
                    inResponse.getOutputStream().write(buffer, 0, len);
                }
            }

            for (Entry<String, Set<String>> headerSet : resp.getHTTPHeaders().entrySet()) {
                for (String headerText : headerSet.getValue()) {
                    inResponse.addHeader(headerSet.getKey(), headerText);
                }
            }
        }
    }

    protected Request buildRequest(final HttpServletRequest inRequest, final URI inResourceDefinitionURI, final Map<String, List<String>> inQueryParams)
            throws URISyntaxException {
        Request rtn = null;
        URI destinationEndpointURI = new URI(inRequest.getRequestURL().toString());
        //TODO strategy for calculating jobCorrelationID.
        String jobCorrelationID = "efghijk";
        //TODO What is httpDeclaration for?
        String httpDeclaration = null;
        Map<String, Set<String>> headers = new HashMap<>();
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.putAll(inQueryParams);
        parameters.put("mimeType", Arrays.asList(new String[] {QWConstants.MIME_TYPE_FI}));
        parameters.put("zip", Arrays.asList(new String[] {"false"}));
        //This assumes we will not be sending input via the messageBody - should be OK for Code requests.
        MessageBody messageBody = null;

        rtn = new RequestImpl (
                jobCorrelationID,
                inResourceDefinitionURI,
                destinationEndpointURI,
                Verb.GET,
                httpDeclaration,
                parameters,
                headers,
                messageBody);
        return rtn;
    }

    public void setOuterFace(final OuterFace inOuterFace) {
        outerFace = inOuterFace;
    }

    public void setDataSource(final URI inDataSource) {
        dataSource = inDataSource;
    }

}
