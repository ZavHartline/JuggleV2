package com.hartline.juggle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.hartline.juggle.io.Error.Severity;
import com.hartline.juggle.io.FileHandler;
import com.hartline.juggle.io.errorImpl.ParsingError;

public class Parser {
	
	private static final int PRINT_WITHOUT_NEW_LINE_PRECEDENCE = 500;
	private static final int PRINT_WITH_NEW_LINE_PRECEDENCE = 500;
	private static final int PRINT_CHARACTER_PRECEDENCE = 500;
	private static final int PLUS_PRECEDENCE = 1000;
	private static final int MINUS_PRECEDENCE = 1000;
	private static final int STAR_PRECEDENCE = 5000;
	private static final int SLASH_PRECEDENCE = 5000;
	private static final int ASSIGN_PRECEDENCE = 400;
	private static final int INPUT_PRECEDENCE = 10000;
	private static final int POWER_PRECEDENCE = 9000;
	
	private int lineCount = 1;
	private boolean flagAnnouncements = false;
	
	public void shuntingYard(List<String> tokens) {
		
		Stack<String> outputStack = new Stack<String>();
		Stack<String> operatorStack = new Stack<String>();
		
		for(String token : tokens) {
			
			if(isNumber(token)) {
				outputStack.push(token);
				continue;
			}
			
			if(isOperator(token)) {
				if(operatorStack.isEmpty()) {
					operatorStack.push(token);
					continue;
				}
				
				while(	!operatorStack.isEmpty() 
						&& (getPrecedence(operatorStack.peek()) >= getPrecedence(token)
						|| getPrecedence(operatorStack.peek()) > getPrecedence(token) && isRightToLeftAssociative(token))){
					outputStack.push(operatorStack.pop());
				}
				
				operatorStack.push(token);
				
				continue;
			}
			
			if(token.equals("(")) {
				operatorStack.push(token);
				continue;
			}
			
			if(token.equals(")")) {
				
				while(!operatorStack.isEmpty() && !operatorStack.peek().equals("(")){
					outputStack.push(operatorStack.pop());
				}
				
				if(operatorStack.isEmpty())
					Main.errorHandler.send(new ParsingError(lineCount, "Mismatched parentheses", Severity.FATAL));
				
				operatorStack.pop();
				continue;
			}
			
			if(isVariable(token)) {
				outputStack.push(token);
				continue;
			}
			
			if(isFlagset(token)) {
				processFlagset(token);
			}
			
			if(isStringOutput(token)) {
				System.out.print(token.substring(1));
			}
			
		}
		while(!operatorStack.isEmpty()) {
			if(operatorStack.peek().matches("\\(|\\)"))
				Main.errorHandler.send(new ParsingError(lineCount, "Mismatched parentheses", Severity.FATAL));
			outputStack.push(operatorStack.pop());
		}
		
		if(Main.getDebugMode())
			System.err.println("POSTFIX: " + outputStack);
		
		Main.interpreter.process(outputStack);
		
	}

	private boolean isRightToLeftAssociative(String token) {
		switch(token) {
		case "^":
			return true;
		default:
			return false;
		}
	}

	private boolean isStringOutput(String token) {
		return token.matches("\\$[\\S\\s]*");
	}

	private void processFlagset(String token) {
		//Truncate boilerplate of flagset
		String flagToken = token.substring(2,token.length()-1);
		
		//Flag syntax: ".*variableName=value,.*"
		String[] flagset = flagToken.split(",");
		
		for(String s : flagset) {
			
			s = s.trim();
			//Split token by =, regardless of spacing surrounding
			
			String flagName = s.split("\\s*=\\s*")[0];
			
			//Make sure there's a parameter to each flag!
			String value;
			try {
				value = s.split("\\s*=\\s*")[1];
			}catch(ArrayIndexOutOfBoundsException e) {
				Main.errorHandler.send(new ParsingError(lineCount, "Flag {"+flagName+"} has no parameter, skipping flag", Severity.WARN));
				continue;
			}
			
			switch(flagName) {
			
			case "verbose":{
				if(Boolean.parseBoolean(value))
					Main.errorHandler.enableVerboseLogging();
				else
					Main.errorHandler.disableVerboseLogging();
				break;
			}
			case "debug":{
				if(Boolean.parseBoolean(value))
					Main.enableDebugMode();
				else
					Main.disableDebugMode();
				break;
			}
			case "announce":{
				if(Boolean.parseBoolean(value))
					enableFlagAnnouncements();
				else
					disableFlagAnnouncements();
				break;
			}
			default:
				Main.errorHandler.send(new ParsingError(lineCount, "Cannot identify flag {" + flagName + "}", Severity.WARN));
				continue;
			}
			
			if(getFlagAnnouncementsMode())
				System.err.println("Flag {"+ flagName + "} has been set to " + value);
			
		}
	}

	private boolean getFlagAnnouncementsMode() {
		return flagAnnouncements;
	}

	private void disableFlagAnnouncements() {
		flagAnnouncements  = false;
	}

	private void enableFlagAnnouncements() {
		flagAnnouncements = true;
	}

	private boolean isFlagset(String token) {
		return token.matches("\\{/.*?\\}");
	}

	private int getPrecedence(String token) {
		switch(token) {
		
		case "+":
			return PLUS_PRECEDENCE;
		case "-":
			return MINUS_PRECEDENCE;
		case "*":
			return STAR_PRECEDENCE;
		case "/":
			return SLASH_PRECEDENCE;
		case "{o}":
			return PRINT_WITHOUT_NEW_LINE_PRECEDENCE;
		case "{p}":
			return PRINT_WITH_NEW_LINE_PRECEDENCE;
		case "\"":
			return PRINT_CHARACTER_PRECEDENCE;
		case "=":
			return ASSIGN_PRECEDENCE;
		case "{i}":
			return INPUT_PRECEDENCE;
		case "^":
			return POWER_PRECEDENCE;
		default:
			return -1;
		}
	}

	public static boolean isOperator(String token) {
		final int OPERATOR_LIST_START_INDEX = 2;
		final int OPERATOR_LIST_END_INDEX = 12;
		List<String> regex = FileHandler.regexList.subList(OPERATOR_LIST_START_INDEX, OPERATOR_LIST_END_INDEX);
		
		for(String s : regex) {
			if(token.matches(s))
				return true;
		}
		return false;
	}

	public static boolean isVariable(String token) {
		return token.matches("(:.*?:)");
	}

	public static boolean isNumber(String token) {
		return token.matches("(\\d+)?\\.\\d+|\\d+");
	}

	public void incrementLineCount() {
		lineCount++;
	}
	
}
