package com.hartline.juggle;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import com.hartline.juggle.data.Operand;
import com.hartline.juggle.io.Error.Severity;
import com.hartline.juggle.io.errorImpl.CalculatingError;

public class Interpreter {
	
	private Map<String, Double> variableMap;
	
	//Stores GOTO jump labels
	private Map<String, Integer> labelMap;
	
	private Scanner scanner;

	private int lineCount = 0;
	private int instructionCount = 0;
	
	public Interpreter() {
		variableMap = new HashMap<String, Double>();
		labelMap = new HashMap<String, Integer>();
		scanner = new Scanner(System.in);
	}
	
	public void process(Stack<String> postfixStack) {
		
		Stack<Operand> operandStack = new Stack<Operand>();
		
		List<String> inputTokens = new ArrayList<String>(postfixStack);
		
		for(String token : inputTokens) {
			
			if(Parser.isNumber(token)) {
				operandStack.push(new Operand(Double.parseDouble(token)));
			}
			if(Parser.isVariable(token)) {
				if(!variableMap.containsKey(token))
					variableMap.put(token, 0.0);
				operandStack.push(new Operand(token));
			}
			
			if(Parser.isLabel(token)) {
				//Replace "label" with nothing to make the hash generic
				labelMap.put(token.replaceFirst("label", ""), getLineCount());
			}
			if(Parser.isGoto(token)) {
				//Replace "goto" with nothing to match the generic hash
				token = token.replaceFirst("goto", "");
				try {
					forceLineCount(labelMap.get(token));
				}catch(NullPointerException e) {
					Main.errorHandler.send(new CalculatingError(getLineCount(), getInstructionCount(), "Could not find label " + token, Severity.FATAL));
				}
				return;
			}
			
			if(Parser.isOperator(token)) {
				
				try {
					switch(token) {
					
					case "+":{
						double num1 = getValueOfOperandPop(operandStack, variableMap);
						double num2 = getValueOfOperandPop(operandStack, variableMap);
						
						operandStack.push(new Operand(num1+num2));
						break;
					}
					case "-":{
						double num2 = getValueOfOperandPop(operandStack, variableMap);
						double num1 = getValueOfOperandPop(operandStack, variableMap);
						
						operandStack.push(new Operand(num1-num2));
						break;
					}
					case "*":{
						double num1 = getValueOfOperandPop(operandStack, variableMap);
						double num2 = getValueOfOperandPop(operandStack, variableMap);
						
						operandStack.push(new Operand(num1*num2));
						break;
					}
					case "/":{
						double num2 = getValueOfOperandPop(operandStack, variableMap);
						double num1 = getValueOfOperandPop(operandStack, variableMap);
						
						operandStack.push(new Operand(num1/num2));
						break;
					}
					case "{o}":{
						System.out.print(getValueOfNextOperandPeek(operandStack, variableMap));
						break;
					}
					case "\"":{
						System.out.print((char)getValueOfNextOperandPeek(operandStack, variableMap));
						break;
					}
					case "{p}":{
						System.out.println();
						break;
					}
					case "=":{
						String variableName = operandStack.pop().VARIABLE_NAME;
						double value = operandStack.peek().VALUE;
						variableMap.put(variableName, value);
						break;
						
					}
					case "{i}":{
						try {
							operandStack.push(new Operand(scanner.nextDouble()));
						}catch(java.util.InputMismatchException e) {
							/*This is here to allow a single statement of {i}
							  to halt a program until next input*/
							scanner = new Scanner(System.in);
						}
						break;
					}
					case "^":{
						double num2 = getValueOfOperandPop(operandStack, variableMap);
						double num1 = getValueOfOperandPop(operandStack, variableMap);
						
						operandStack.push(new Operand(Math.pow(num1,num2)));
					}
					default:
						break;
						
					}
				}catch(EmptyStackException e) {
					Main.errorHandler.send(new CalculatingError(getLineCount(), getInstructionCount(), "Not enough operands.", Severity.FATAL));
				}
				
			}
			
			incrementInstructionCount();
			
		}
		resetInstructionCount();
		incrementLineCount();
		
	}

	private double getValueOfNextOperandPeek(Stack<Operand> operandStack, Map<String, Double> variableMap) {
		if(operandStack.peek().VARIABLE_NAME != null)
			if(variableMap.containsKey(operandStack.peek().VARIABLE_NAME))
				return variableMap.get(operandStack.peek().VARIABLE_NAME);
			else
				Main.errorHandler.send(new CalculatingError(getLineCount(), getInstructionCount(), "Attempt to call nil", Severity.FATAL));
		return operandStack.peek().VALUE;
	}
	
	private double getValueOfOperandPop(Stack<Operand> operandStack, Map<String, Double> variableMap) {
		if(operandStack.peek().VARIABLE_NAME != null)
			if(variableMap.containsKey(operandStack.peek().VARIABLE_NAME))
				return variableMap.get(operandStack.pop().VARIABLE_NAME);
			else
				Main.errorHandler.send(new CalculatingError(getLineCount(), getInstructionCount(), "Attempt to call nil", Severity.FATAL));
		return operandStack.pop().VALUE;
	}
	
	public int getLineCount() {
		return lineCount;
	}
	
	public void incrementLineCount() {
		lineCount++;
	}
	
	private void forceLineCount(int newLineNumber) {
		lineCount = newLineNumber;
	}
	
	private int getInstructionCount() {
		return instructionCount;
	}

	private void resetInstructionCount() {
		instructionCount = 0;
	}

	private void incrementInstructionCount() {
		instructionCount++;
	}
}
