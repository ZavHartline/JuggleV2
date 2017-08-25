package com.hartline.juggle.io.errorImpl;
import com.hartline.juggle.io.Error;
import com.hartline.juggle.io.Error.ErrorType;;

public class CalculatingError extends Error{

	public CalculatingError(int position, String reasonText, Severity severity) {
		pointOfError = position;
		errorType = ErrorType.CALCULATION;
		severityOfError = severity;
		reason = reasonText;
	}
	
	@Override
	public String getErrorMessage() {
		
		if(severityOfError != null)
			return "[" + severityOfError + "] "+ getErrorType() + " - Instruction: " + pointOfError + " - Reason: " + reason;
		return getErrorType() + " - Instruction: " + pointOfError + " - Reason: " + reason;
	}

}
