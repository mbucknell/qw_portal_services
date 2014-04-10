package gov.usgs.cida.qw.resourcefolder;

import gov.usgs.cida.qw.QWConstants;
import gov.usgs.cida.qw.QWUtility;
import gov.usgs.cida.qw.code.AggregatedCode;
import gov.usgs.cida.qw.code.Code;
import gov.usgs.cida.qw.code.CodeComparator;
import gov.usgs.cida.resourcefolder.Folder;
import gov.usgs.cida.resourcefolder.OuterFace;
import gov.usgs.cida.resourcefolder.Request;
import gov.usgs.cida.resourcefolder.Response;
import gov.usgs.cida.resourcefolder.StartLineResponse;
import gov.usgs.cida.resourcefolder.StatusCode;
import gov.usgs.cida.resourcefolder.basicimpl.ResponseImpl;
import gov.usgs.cida.resourcefolder.basicimpl.StreamMessageBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.StaxReader;

public class QwCodeOuterFace implements OuterFace {

	private static final Logger LOG = LoggerFactory.getLogger(QwCodeOuterFace.class);

	private final Folder folder;

	private static final XStream XSTREAM;
	static {
		XSTREAM = new XStream(new StaxDriver());
		XSTREAM.alias("Codes", List.class);
		XSTREAM.alias("Code", Code.class);
		XSTREAM.useAttributeFor(Code.class, "value");
		XSTREAM.useAttributeFor(Code.class, "desc");
	}

	private static XMLOutputFactory XML_OUTPUT_FACTORY = XMLOutputFactory.newInstance();

	private URI codeUri;

	public QwCodeOuterFace(final Folder inFolder) {
		if (inFolder == null) {
			throw new IllegalArgumentException ("Parameter 'inFolder' not permitted to be null.");
		}

		folder = inFolder;
	}

	@Override
	public Set<URI> getResourceDefinitions() {
		return new HashSet<URI>();
	}

	@Override
	public Response callResources(final Request request) {
		if (null == request) {
			throw new IllegalArgumentException("The Request is null.");
		}
		Response retval = null;

		Response distributedResponse = folder.distribute(request);

		if (StatusCode.OK_200 == distributedResponse.getStatus()) {
			try {
				retval = aggregateResponses(request, distributedResponse);
			} catch (XMLStreamException e) {
				LOG.info("Issues Aggregating response", e);
			}
		} else {
			LOG.info("Invalid StatusCode:" + distributedResponse.getStatus());
			retval = distributeError(request, distributedResponse);
		}


		return retval;
	}

	private Response distributeError(final Request request, Response distributedResponse) {
		return new ResponseImpl(
				request,
				codeUri,
				new StartLineResponse(distributedResponse.getStatus()),
				distributedResponse.getHTTPHeaders(),
				distributedResponse.getMessageBody(),
				null);
	}

	@Override
	public void callResources(HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}


	protected ResponseImpl aggregateResponses(final Request request, final Response response) throws XMLStreamException {
		if (null == request || null == request.getJobCorrelationID() || null == request.getResourceDefinitionURI()) {
			//TODO Houston we have a problem...
			throw new IllegalArgumentException("The Request is invalid.");
		}
		if (null == response) {
			//TODO Houston we have a problem...
			throw new IllegalArgumentException("The Response is null.");
		}
		Set<Response> collectedResponses = new HashSet<>();
		collectedResponses.add(response);
		Map<String, Set<String>> headers = new HashMap<>();
		Map<String, AggregatedCode> codeMap = new HashMap<>();

		//TODO - this should aggregate the status/headers from the responses... (or the folder should...)
		//TODO - what is the correct status/action when the if fails? Under what cases could null response.getContainedResponses() or
		//             null response.getHTTPHeaders() be valid (allow the provided response.getStatus() to be passed through)?
		StatusCode status = StatusCode.BAD_REQUEST_400;
		if (null != response.getStatus() && null != response.getContainedResponses() && null != response.getHTTPHeaders()) {
			status = response.getStatus();
			headers = response.getHTTPHeaders();

			for (Response endPointResp : response.getContainedResponses()) {
				String provider = QWUtility.determineProvider(endPointResp);

				if (StatusCode.OK_200 == endPointResp.getStatus()) {
					codeMap = processCodes(provider, endPointResp, codeMap);
				} else {
					//TODO - figure out headers for real... and what if a contained resp != 200...
					if (null != endPointResp.getHTTPHeaders() && endPointResp.getHTTPHeaders().containsKey(QWConstants.HEADER_WARNING)) {
						if (headers.containsKey(QWConstants.HEADER_WARNING)) {
							headers.get(QWConstants.HEADER_WARNING).addAll(endPointResp.getHTTPHeaders().get(QWConstants.HEADER_WARNING));
						} else {
							headers.put(QWConstants.HEADER_WARNING, endPointResp.getHTTPHeaders().get(QWConstants.HEADER_WARNING));
						}
					}
				}
			}

		}

		return new ResponseImpl(
				request,
				codeUri,
				new StartLineResponse (status),
				headers,
				new StreamMessageBody(convertCodesToXmlInputStream(codeMap)),
				collectedResponses);
	}

	/**
	 * Take the map of Aggregated codes, order them according the business rules in CodeComparator, and marshal them as xml.
	 * We always create an xml document. It will only contain the root node (<Codes/>) if the codeMap is null or empty.
	 * @param codes - a map of AggregatedCode.
	 * @return - the xml representation of them as an InputStream.
	 */
	protected InputStream convertCodesToXmlInputStream(final Map<String, AggregatedCode> codeMap) throws XMLStreamException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		XMLStreamWriter streamWriter = XML_OUTPUT_FACTORY.createXMLStreamWriter(out);
		streamWriter.writeStartDocument();
		streamWriter.writeStartElement("Codes");
		if (codeMap != null && !codeMap.isEmpty()) {
			CodeComparator codeComparator = new CodeComparator();
			SortedSet<AggregatedCode> orderedCodes = new TreeSet<>(codeComparator);
			orderedCodes.addAll(codeMap.values());
			for (AggregatedCode code : orderedCodes) {
				streamWriter.writeStartElement("Code");
				streamWriter.writeAttribute("value", null == code.getValue() ? "" : code.getValue());
				streamWriter.writeAttribute("desc", null == code.getDesc() ? "" : code.getDesc());
				//Providers cannot be null...
				streamWriter.writeAttribute("providers", null == code.getProviders() ? "" : code.getProviders());
				streamWriter.writeEndElement();
			}
		}
		streamWriter.writeEndDocument();
		streamWriter.flush();
		streamWriter.close();

		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * Converts a FastInfoset input stream into a list of codes and merges that with the existing map - adding the source provider in the process.
	 * @param inProvider - the common name of the source system for this code list.
	 * @param inResponse - the Resource Folder Response from the provider's system
	 * @param inCodeMap - A map of codes already aggregated from other providers. Can be null.
	 * @return - the merged map of codes with the provider(s) identified.
	 */
	protected Map<String, AggregatedCode> processCodes(final String inProvider, final Response inResponse, final Map<String, AggregatedCode> inCodeMap) {
		Map<String, AggregatedCode> codeMap = new HashMap<>();
		if (null == inProvider || 0 == inProvider.length()) {
			//TODO Houston we have a problem...
			throw new IllegalArgumentException("The Provider is null.");
		}
		if (null != inCodeMap) {
			codeMap.putAll(inCodeMap);
		}
		if (null != inResponse && null != inResponse.getMessageBody()) {
			InputStream fiDocument = inResponse.getMessageBody().deliverAsStream();
			Object o = null;
			try {
				o = XSTREAM.unmarshal(new StaxReader(new QNameMap(), new StAXDocumentParser(fiDocument)));
			} catch (Exception e) {
				//TODO Houston we have a problem...
				e.printStackTrace();
				throw new IllegalArgumentException("Problems unmarshalling MessageBody stream: " + e.getMessage());
			}
			//o will be a list and c will be a code - otherwise we would get an exception from the unmarshal above.
			List<?> codes = (List<?>) o;
			for (Object c : codes) {
				Code code = (Code) c;
				String codeValue = code.getValue();
				if (codeMap.containsKey(codeValue)) {
					codeMap.get(codeValue).addProvider(inProvider);
				} else {
					codeMap.put(codeValue, new AggregatedCode(code, inProvider));
				}
			}
		} else {
			//TODO Houston we have a problem...
			throw new IllegalArgumentException("The Response or it's MessageBody are null.");
		}
		return codeMap;
	}

	public void setCodeUri(final URI inCodeUri) {
		codeUri = inCodeUri;
	}

	public URI getCodeUri() {
		return codeUri;
	}
}
