package com.hartline.juggle.io;

public abstract class Error {
	
	protected int pointOfError;
	protected ErrorType errorType;
	protected Severity severityOfError;
	protected String reason;
	
	public enum ErrorType{
		
		SYNTAX,
		PARSING,
		LEXING,
		LOADING, 
		CALCULATION,
		
	}
	
	public enum Severity{
		
		LOW,
		WARN,
		FATAL,
		
	}
	
	public abstract String getErrorMessage();
	
	public ErrorType getErrorType() {
		
		return errorType;
		
	}

	public Severity getSeverity() {
		return severityOfError;
	}
	
}
