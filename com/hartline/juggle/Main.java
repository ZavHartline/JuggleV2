package com.hartline.juggle;

import com.hartline.juggle.io.Error.Severity;
import com.hartline.juggle.io.ErrorHandler;
import com.hartline.juggle.io.FileHandler;
import com.hartline.juggle.io.errorImpl.LoadingError;

public class Main {
	
	public static ErrorHandler errorHandler = null;
	public static FileHandler mainFileHandler = null;
	public static Interpreter interpreter = null;
	private static boolean debugMode = false;;
	
	static {
		
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
	
}
