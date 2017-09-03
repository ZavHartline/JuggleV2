package com.hartline.juggle;

import java.text.SimpleDateFormat;

import com.hartline.juggle.io.Error.Severity;
import com.hartline.juggle.io.ErrorHandler;
import com.hartline.juggle.io.FileHandler;
import com.hartline.juggle.io.errorImpl.LoadingError;

public class Main {
	
	public static ErrorHandler errorHandler = null;
	public static FileHandler mainFileHandler = null;
	public static Interpreter interpreter = null;
	private static boolean debugMode = false;
	private static boolean timerMode = false;
	public static final long TIME_OF_START;
	
	static {
		
		TIME_OF_START = System.nanoTime();
		errorHandler = new ErrorHandler();
		interpreter = new Interpreter();
		
	}
	
	public static void main(String[] args) {
		
		System.out.println();
		
		//If no file is input, don't execute program
		if(args.length < 1) {
			errorHandler.send(new LoadingError(-1, "No file path has been input!", Severity.FATAL));
		}
		
		mainFileHandler = new FileHandler(args[0]) {{
			loadFileContents();
			tokenizeTextData();
		}};
		
		if(getTimerMode()) {
			long currentTime = System.nanoTime();
			long duration = (currentTime - TIME_OF_START) / (long)1e6;
			long ms = (duration % 1000) / 100;
			long seconds = (duration / 1000) % 60;
			long minutes = (duration / (1000 * 60)) % 60;
			long hours = (duration / (1000 * 60 * 60)) % 24;
			System.out.printf("Execution time: %02d:%02d:%02d.%d\n", hours, minutes, seconds, ms);
		}
		
		System.out.println();
		
	}

	public static void enableDebugMode() {
		debugMode  = true;
	}
	public static void disableDebugMode() {
		debugMode = false;
	}
	public static boolean getDebugMode() {
		return debugMode;
	}
	
	public static void enableTimerMode() {
		timerMode  = true;
	}
	public static void disableTimerMode() {
		timerMode = false;
	}
	public static boolean getTimerMode() {
		return timerMode;
	}
}
