package com.hartline.juggle.io.errorImpl;

import com.hartline.juggle.io.Error;

public class LoadingError extends Error {
	
	public LoadingError(int position) {

		pointOfError = position;
		errorType = ErrorType.LOADING;
		severityOfError = null;
		reason = null;
		
	}
	
	public LoadingError(int position, String reasonText) {
		
		pointOfError = position;
		errorType = ErrorType.LOADING;
		severityOfError = null;
		reason = reasonText;
		
	}
	
	public LoadingError(int position, String reasonText, Severity severity) {
		
		pointOfError = position;
		errorType = ErrorType.LOADING;
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
