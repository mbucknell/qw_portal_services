package gov.usgs.cida.qw.webservice.codes;

import gov.usgs.cida.resourcefolder.OuterFace;
import gov.usgs.cida.resourcefolder.Request;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * Convenience concrete class for testing the abstract AggregatedCodesMvcService in Spock.
 * @author drsteini
 *
 */
public class TestAggregatedCodesMvcService extends AggregatedCodesMvcService {

	public void doCodeRequest(
			final HttpServletRequest inRequest,
			final OuterFace inOuterFace,
			final URI inResourceDefinitionURI,
			final Map<String, List<String>> inQueryParams,
			final HttpServletResponse inResponse) {
		super.doCodeRequest(inRequest, inOuterFace, inResourceDefinitionURI, inQueryParams, inResponse);
	}
	
	public Request buildRequest(final HttpServletRequest inRequest, final URI inResourceDefinitionURI, final Map<String, List<String>> inQueryParams) {
		//TODO - Does this really need testing??
		return super.buildRequest(inRequest, inResourceDefinitionURI, inQueryParams);
	}

}
