package com.hartline.juggle.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hartline.juggle.Main;
import com.hartline.juggle.Parser;
import com.hartline.juggle.io.Error.Severity;
import com.hartline.juggle.io.errorImpl.LoadingError;

public class FileHandler {
	
	public static List<String> regexList = new ArrayList<String>() {{
		
		add("(:.*?:)");	// Group matches variables
		add("((\\d+)?\\.\\d+|\\d+)");	// Group matches number literals
		add("(\\+)");	// Group matches add
		add("(\\-)");	// Group matches sub
		add("(\\*)");	// Group matches mult
		add("(/)");		// Group matches div
		add("(%)");		// Group matches modulus
		add("(\")");	// Group matches printChar no \n
		add("(\\{o\\})");//Group matches printVal no \n
		add("(\\{p\\})");//Group matches printVal with \n
		add("(\\{i\\})");//Group matches pushInput
		add("(=)");		// Group matches assignment
		add("(\\^)");	// Group matches power symbol
		add("(if)");	// Group matches if conditional
		add("(<<)");	// Group matches left bit shift
		add("(>>>)");	// Group matches unsigned right bit shift
		add("(>>)");	// Group matches signed right bit shift
		add("(\\|)");	// Group matches bitwise OR
		add("(&)");		// Group matches bitwise AND
		add("(~)");		// Group matches bitwise NEGATE
		add("(<)");		// Group matches boolean LESS_THAN
		add("(>)");		// Group matches boolean GREATER_THAN
		add("(\\()");	// Group matches left parenthesis
		add("(\\))");	// Group matches right parenthesis
		add("(\\{/.*?\\})");// Group matches flags
		add("(\\$[\\S\\s]+)");	//Group matches standard string output
		add("(label\\{.*?\\})"); //Group matches GOTO labels
		add("(goto\\{.*?\\})");	//Group matches GOTO{label}
		
	}};
	
	private final Path PATH;
	private byte[] rawData = null;
	private String textData = null;
	
	public FileHandler(String filePath) {
		
		PATH = Paths.get(filePath);
		
	}
	
	public void loadFileContents() {
		
		try {
			rawData = Files.readAllBytes(PATH);
			
			textData = new String(rawData);
			
		} catch (IOException e) {
			
			Main.errorHandler.send(new LoadingError(-1, e.getMessage() + " does not exist or cannot be read.", Severity.FATAL));
			
		}
		
	}
	
	public void tokenizeTextData() {
		
		if(Main.getDebugMode())
			System.err.println("FILE_INPUT: " + textData);
		
		String regex = "";
		
		for(String s : regexList) {
			regex += s + "|";
		}
		
		//Truncate trailing "|"
		regex = regex.substring(0, regex.length()-1);
		
		Pattern pattern = Pattern.compile(regex);
		
		Parser parser = new Parser();
		
		String[] lines = textData.split(";");
		
		//Stores all of the tokenized text lines from the following loop
		List<List<String>> tokenizedLineLists = new ArrayList<List<String>>();
		
		for(int i = 0; i < lines.length; ++i) {
			
			String line = lines[i];
			
			Matcher matcher = pattern.matcher(line);
			List<String> tokenList = new ArrayList<String>();
			
			//if is comment
			if(line.trim().startsWith("#")) {
				tokenizedLineLists.add(tokenList);
				continue;
			}
			
			//Capture every matching regex group in regexList into the tokenList
			while(matcher.find())
				tokenList.add(matcher.group());
			
			if(Main.getDebugMode()) {
				System.err.println("");
				System.err.println("LINE_CONTENT: " + line.trim());
				System.err.println("LINE_NUMBER: " + Main.interpreter.getLineCount());
				System.err.println("TOKENLIST: " + tokenList);
			}
			
			//Automatically pre-scan for labels
			for(String token : tokenList) {
				if(Parser.isLabel(token)) {
					//Replace "label" with nothing to make the hash generic
					Main.interpreter.labelMap.put(token.replaceFirst("label", ""), i);
				}
			}
			
			tokenizedLineLists.add(tokenList);
			
		}
		
		//Process token lists
		for(int i = 0; i < tokenizedLineLists.size(); i = Main.interpreter.getLineCount()) {
			List<String> tokenList = tokenizedLineLists.get(i);
			parser.shuntingYard(tokenList);
		}
		
	}
	
}
