package gov.usgs.cida.qw

import gov.usgs.cida.resourcefolder.OuterFace
import gov.usgs.cida.resourcefolder.StatusCode
import gov.usgs.cida.resourcefolder.basicimpl.ResponseImpl
import gov.usgs.cida.resourcefolder.basicimpl.StringMessageBody

import javax.naming.Context
import javax.naming.InitialContext
import javax.naming.NamingException

import org.springframework.mock.jndi.SimpleNamingContextBuilder
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

import spock.lang.Specification

class QwServicesSpec extends Specification {
    
    def mockHttpServletRequest
    def mockHttpServletResponse
    def mockOuterFace
    def mockOKResponse
    def mockBadResponse
    def uri
    
    def setup() {
        
        mockHttpServletRequest = new MockHttpServletRequest()
        mockHttpServletResponse = new MockHttpServletResponse()

            SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        try {
            Context context = new InitialContext();
            context.bind("java:comp/env/WQP/providers/" + BaseSpringTest.PROVIDER1, "http://localhost:8080/testqwa/");
            context.bind("java:comp/env/WQP/providers/" + BaseSpringTest.PROVIDER2, "http://localhost:8080/testqwb/");
        } catch (NamingException e) {
            e.printStackTrace();
            fail();
        }
        
        mockOKResponse = Mock(ResponseImpl)
        mockOKResponse.getStatus() >> {
            return StatusCode.OK_200
        }
        mockOKResponse.getHTTPHeaders() >> {
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
        mockOKResponse.getMessageBody() >> {
            return new StringMessageBody("<Codes/>")
        }

        mockBadResponse = Mock(ResponseImpl)
        mockBadResponse.getStatus() >> {
            return StatusCode.BAD_REQUEST_400
        }
        mockBadResponse.getHTTPHeaders() >> {
            def headers = new HashMap<String, Set<String>>()
            def values1 = new HashSet<>()
            values1.add("wow, it failed!")
            def values2 = new HashSet<>()
            values2.add("so did")
            values2.add("this")
            headers.put("test", values1)
            headers.put("more", values2)
            return headers
        }
        mockBadResponse.getMessageBody() >> {
            return new StringMessageBody("<BadCodes/>")
        }

        mockOuterFace = Mock(OuterFace)
        
        uri = new URI("http://localhost:8080/testqw/")

    }

}
