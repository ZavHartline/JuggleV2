package com.hartline.juggle.io.errorImpl;

import com.hartline.juggle.io.Error;
import com.hartline.juggle.io.Error.ErrorType;
import com.hartline.juggle.io.Error.Severity;

public class ParsingError extends Error{
	public ParsingError(int position) {

		pointOfError = position;
		errorType = ErrorType.PARSING;
		severityOfError = null;
		reason = null;
		
	}
	
	public ParsingError(int position, String reasonText) {
		
		pointOfError = position;
		errorType = ErrorType.PARSING;
		severityOfError = null;
		reason = reasonText;
		
	}
	
	public ParsingError(int position, String reasonText, Severity severity) {
		
		pointOfError = position;
		errorType = ErrorType.PARSING;
		severityOfError = severity;
		reason = reasonText;
		
	}
	
	public ParsingError(String reasonText, Severity severity) {
		pointOfError = -1;
		errorType = ErrorType.PARSING;
		severityOfError = severity;
		reason = reasonText;
	}

	@Override
	public String getErrorMessage() {
		
		if(severityOfError != null)
			return "[" + severityOfError + "] "+getErrorType() + " - Point of Error: " + pointOfError + " - Reason: " + reason;
		return getErrorType() + " - Point of Error: " + pointOfError + " - Reason: " + reason;
	}
}
