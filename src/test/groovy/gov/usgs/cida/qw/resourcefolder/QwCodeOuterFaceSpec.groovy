package gov.usgs.cida.qw

import gov.usgs.cida.qw.resourcefolder.QwCodeOuterFace
import gov.usgs.cida.resourcefolder.Folder
import gov.usgs.cida.resourcefolder.PackageConstants;
import gov.usgs.cida.resourcefolder.Request
import gov.usgs.cida.resourcefolder.StatusCode
import gov.usgs.cida.resourcefolder.Response
import gov.usgs.cida.resourcefolder.basicimpl.PackageUtils;
import gov.usgs.cida.resourcefolder.basicimpl.ResponseImpl
import gov.usgs.cida.resourcefolder.basicimpl.StringMessageBody
import gov.usgs.cida.resourcefolder.basicimpl.StreamMessageBody
import gov.usgs.cida.resourcefolder.PackageConstants;
import gov.usgs.cida.resourcefolder.basicimpl.PackageUtils;

class QwCodeOuterFaceSpec extends QwServicesSpec {
    
    def testQwCodeOuterFace
    def mockRequest
    def mockFolder
    def mockFolderResponse
    def mockContainedResponse

    def setup() {
        mockContainedResponse = Mock(ResponseImpl)
        mockContainedResponse.getStatus() >> {
            return StatusCode.OK_200
        }
        mockContainedResponse.getHTTPHeaders() >> {
            def headers = new HashMap<String, Set<String>>()
            def values1 = new HashSet<>()
            values1.add("wow, it works!")
            def values2 = new HashSet<>()
            values2.add("how about")
            values2.add("this")
            headers.put("test", values1)
            headers.put("more", values2)
            return headers
        }
        mockContainedResponse.getMessageBody() >> {
            return new StreamMessageBody(BaseSpringTest.createFiStream("<Codes><Code value=\"a\" desc=\"abc\"/></Codes>"))
        }

        mockFolderResponse = Mock(ResponseImpl)
        mockFolderResponse.getStatus() >> {
            return StatusCode.OK_200
        }
        mockFolderResponse.getHTTPHeaders() >> {
            def headers = new HashMap<String, Set<String>>()
            def values1 = new HashSet<>()
            values1.add("wow, it works!")
            def values2 = new HashSet<>()
            values2.add("how about")
            values2.add("this")
            headers.put("test", values1)
            headers.put("more", values2)
            return headers
        }
        mockFolderResponse.getMessageBody() >> {
            return new StringMessageBody("<Codes/>")
        }
        mockFolderResponse.getContainedResponses() >> {
            def resps = new ArrayList<Response>()
            resps.add(mockContainedResponse)
            return resps
        }
        
        mockFolder = Mock(Folder)
        mockRequest = Mock(Request)
        mockRequest.getResourceDefinitionURI() >> {
            return uri
        }
        mockRequest.getJobCorrelationID() >> {
            return "123"
        }
        
        testQwCodeOuterFace = new QwCodeOuterFace(mockFolder)
        testQwCodeOuterFace.codeUri = uri
    }

    def "Error in OuterFace"() {
        given: "A bad response"
        mockFolder.distribute(_) >> { args ->
            return mockBadResponse
        }
        
        when: "OuterFace is called"
        def resp = testQwCodeOuterFace.callResources(mockRequest)
        
        then: "We get an error Response"
        resp.getStatus() == StatusCode.BAD_REQUEST_400
        
        and: "We get correct response"
        resp.getHTTPStartLine().toString() == "HTTP/1.1 400 Bad Request"
        resp.getHTTPHeaders().size() == 3
        resp.getHTTPHeaders().toString() =="[more:[so did, this], test:[wow, it failed!], Content-length:[11]]"
        resp.getMessageBody().toString() == "<BadCodes/>"
        resp.getContainedResponses() == null
    }
    
    def "Happy Path"() {
        mockFolder.distribute(_) >> { args ->
            return mockFolderResponse
        }

        when: "OuterFace is called"
        def resp = testQwCodeOuterFace.callResources(mockRequest)
        
        then: "We get a good status"
        resp.getStatus() == StatusCode.OK_200
        
        and: "We get correct response"
        resp.getHTTPStartLine().toString() == "HTTP/1.1 200 OK"
        resp.getHTTPHeaders().size() == 2
        resp.getHTTPHeaders().toString() =="[more:[how about, this], test:[wow, it works!]]"
        PackageUtils.writeFromStream(resp.getMessageBody().deliverAsStream(), null, "").replace(PackageConstants.CRLF, "") == "<?xml version='1.0' encoding='UTF-8'?><Codes><Code value=\"a\" desc=\"abc\" providers=\"UNKNOWN\"/></Codes>"
        resp.getContainedResponses().toString() == "[" + mockFolderResponse.toString() + "]"
    }
    
}