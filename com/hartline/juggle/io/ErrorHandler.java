package com.hartline.juggle.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.hartline.juggle.io.Error;
import com.hartline.juggle.io.Error.Severity;

public class ErrorHandler {
	
	private boolean verboseLogging = false;
	private FileWriter logger;
	
	public ErrorHandler() {
		/*
		try {
			logger = new FileWriter(new File(System.nanoTime() + "_log.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	public boolean getVerboseLogging() {
		return verboseLogging;
	}
	
	public void enableVerboseLogging() {
		
		verboseLogging = true;
		
	}
	
	public void disableVerboseLogging() {
		
		verboseLogging = false;
		
	}
	
	public void send(Error error) {
		
		System.err.println(error.getErrorMessage());
		
		
		
		if(error.getSeverity().equals(Severity.FATAL)) {
			System.err.println("Terminating program.");
			System.exit(-1);
			/*
			try {
				logger.flush();
				logger.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
		
	}
	
}
