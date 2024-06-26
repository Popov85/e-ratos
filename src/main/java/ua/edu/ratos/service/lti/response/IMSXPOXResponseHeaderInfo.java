package ua.edu.ratos.service.lti.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@SuppressWarnings("ALL")
public class IMSXPOXResponseHeaderInfo {
	
	@JacksonXmlProperty(localName = "imsx_version")
	private String imsxVersion;

	@JacksonXmlProperty(localName = "imsx_messageIdentifier")
	private String imsxMessageIdentifier;
	
	@JacksonXmlProperty(localName = "imsx_statusInfo")
	private IMSXStatusInfo imsxStatusInfo;


	public void setImsxVersion(String imsxVersion) {
		this.imsxVersion = imsxVersion;
	}
	
	public String getImsxVersion() {
		return imsxVersion;
	}

	public String getImsxMessageIdentifier() {
		return imsxMessageIdentifier;
	}

	public IMSXPOXResponseHeaderInfo setImsxMessageIdentifier(String imsxMessageIdentifier) {
		this.imsxMessageIdentifier = imsxMessageIdentifier;
		return this;
	}

	public IMSXStatusInfo getImsxStatusInfo() {
		return imsxStatusInfo;
	}

	public IMSXPOXResponseHeaderInfo setImsxStatusInfo(IMSXStatusInfo imsxStatusInfo) {
		this.imsxStatusInfo = imsxStatusInfo;
		return this;
	}
	
}
