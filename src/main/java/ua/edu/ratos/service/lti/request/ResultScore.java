package ua.edu.ratos.service.lti.request;

public class ResultScore {
	
	private final String language = "en";
	
	private String textString;

	public String getLanguage() {
		return language;
	}

	public String getTextString() {
		return textString;
	}

	public ResultScore setTextString(String textString) {
		this.textString = textString;
		return this;
	}
}
