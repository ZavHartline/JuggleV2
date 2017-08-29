package com.hartline.juggle.io.errorImpl;
import com.hartline.juggle.io.Error;
import com.hartline.juggle.io.Error.ErrorType;;

public class CalculatingError extends Error{
	
	private final int instructionPosition;
	
	public CalculatingError(int lineNumber, int instructionNumber, String reasonText, Severity severity) {
		pointOfError = lineNumber;
		instructionPosition = instructionNumber;
		errorType = ErrorType.CALCULATION;
		severityOfError = severity;
		reason = reasonText;
	}
	
	@Override
	public String getErrorMessage() {
		
		return "[" + severityOfError + "] "+ getErrorType() + " - Line: " + pointOfError + " - Instruction: " + instructionPosition + " - Reason: " + reason;
		
	}

}
