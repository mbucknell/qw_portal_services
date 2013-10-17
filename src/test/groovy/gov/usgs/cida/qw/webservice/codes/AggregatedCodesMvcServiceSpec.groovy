package gov.usgs.cida.qw
import gov.usgs.cida.qw.webservice.codes.TestAggregatedCodesMvcService

class AggregatedCodesMvcServiceSpec extends QwServicesSpec {
	
	def testMvcService

	def setup() {
		testMvcService = new TestAggregatedCodesMvcService()
	}

	def "No parameters is invalid"() {
		when: "Server is called"
		testMvcService.doCodeRequest(mockHttpServletRequest, mockOuterFace, uri, new HashMap<String, List<String>>(), mockHttpServletResponse)
		
		then: "We get a 400"
		mockHttpServletResponse.getStatus() == 400
		
		and: "We have correct encoding"
		mockHttpServletResponse.getCharacterEncoding() == "UTF-8"
	}

	def "Error in OuterFace"() {
		given: "A bad response"
		mockHttpServletRequest.setParameter("mimeType","fi")
		mockOuterFace.callResources(_) >> { args ->
			return mockBadResponse
		}
		
		when: "Server is called"
		testMvcService.doCodeRequest(mockHttpServletRequest, mockOuterFace, uri, new HashMap<String, List<String>>(), mockHttpServletResponse)
		
		then: "We get an error"
		mockHttpServletResponse.getStatus() == 400
		
		and: "We get correct encoding and headers"
		mockHttpServletResponse.getCharacterEncoding() == "UTF-8"
		mockHttpServletResponse.getHeader("Content-Type") == "application/xml;charset=UTF-8"
		mockHttpServletResponse.getHeader("test") == "wow, it failed!"
		mockHttpServletResponse.getHeaders("more").toString() == "[so did, this]"
	}
	
	def "Happy Path"() {
		given: "a good response"
		mockHttpServletRequest.setParameter("mimeType","fi")
		mockOuterFace.callResources(_) >> { args ->
			return mockOKResponse
		}
		
		when: "Server is called"
		testMvcService.doCodeRequest(mockHttpServletRequest, mockOuterFace, uri, new HashMap<String, List<String>>(), mockHttpServletResponse)
		
		then: "We get a good status"
		mockHttpServletResponse.getStatus() == 200
		
		and: "We get correct encoding, headers, and output"
		mockHttpServletResponse.getCharacterEncoding() == "UTF-8"
		mockHttpServletResponse.getHeader("Content-Type") == "application/xml;charset=UTF-8"
		mockHttpServletResponse.getHeader("test") == "wow, it works!"
		mockHttpServletResponse.getHeaders("more").toString() == "[how about, this]"
		mockHttpServletResponse.getContentAsString() == "<Codes/>"
	}
	
}