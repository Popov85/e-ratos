package ua.edu.ratos.service.lti;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.edu.ratos.service.lti.request.*;
import jakarta.annotation.PostConstruct;

@Slf4j
@Service
public class XMLRequestBodyBuilder {

	  private XmlMapper xmlMapper;

	    @PostConstruct
	    public void init() {
	        xmlMapper = new XmlMapper();
	        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
	    }
	
	/**
     * Create a Java-object ready for XML serialization according to LTI v1.1.1 specification
     * @param sourcedId a value needed to posting score to LMS as per LTI specification
     * @param messageIdentifier Some value for identifying messages (in our case the number of milliseconds since 1970)
     * @param textScore score between 0-1 gained by a user after learning session completion
     * @return fully populated object ready to be XML-serialized according to LTI specification
     * @see <a href="https://www.imsglobal.org/specs/ltiv1p1p1/implementation-guide#toc-3">LTI v 1.1.1</a>
     */
    public String build(String sourcedId, String messageIdentifier, String textScore) {
            	
    	IMSXPOXEnvelopeRequest envelopeRequest =
		new IMSXPOXEnvelopeRequest()
        .setIMSXPOXHeader(new IMSXPOXHeader()
                .setImsxPOXRequestHeaderInfo(new IMSXPOXRequestHeaderInfo()
                        .setImsxMessageIdentifier(messageIdentifier)))
        .setIMSXPOXBody(new IMSXPOXBody()
                .setReplaceResultRequest(new ReplaceResultRequest()
                        .setResultRecord(new ResultRecord()
                                .setSourcedGUID(new SourcedGUID()
                                        .setSourcedId(sourcedId))
                                .setResult(new Result()
                                        .setResultScore(new ResultScore()
                                                .setTextString(textScore))))));
		try {
			String body = xmlMapper.writeValueAsString(envelopeRequest);
			log.debug("XML body = {}", body);
			return body;
		} catch (Exception e) {
			log.error("Failed to convert request object to XML");
			throw new RuntimeException("Failed to convert request object to XML", e);
		}
    }
}
