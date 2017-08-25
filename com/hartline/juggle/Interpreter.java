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
	private Scanner scanner;
	
	public Interpreter() {
		variableMap = new HashMap<String, Double>();
		scanner = new Scanner(System.in);
	}
	
	public void process(Stack<String> postfixStack) {
		
		int instructionCount = 0;
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
			if(Parser.isOperator(token)) {
				
				try {
					switch(token) {
					
					case "+":{
						double num1 = getValueOfOperand(operandStack, variableMap);
						double num2 = getValueOfOperand(operandStack, variableMap);
						
						operandStack.push(new Operand(num1+num2));
						break;
					}
					case "-":{
						double num2 = getValueOfOperand(operandStack, variableMap);
						double num1 = getValueOfOperand(operandStack, variableMap);
						
						operandStack.push(new Operand(num1-num2));
						break;
					}
					case "*":{
						double num1 = getValueOfOperand(operandStack, variableMap);
						double num2 = getValueOfOperand(operandStack, variableMap);
						
						operandStack.push(new Operand(num1*num2));
						break;
					}
					case "/":{
						double num2 = getValueOfOperand(operandStack, variableMap);
						double num1 = getValueOfOperand(operandStack, variableMap);
						
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
						}
						break;
					}
					default:
						break;
						
					}
				}catch(EmptyStackException e) {
					Main.errorHandler.send(new CalculatingError(instructionCount, "Not enough operands.", Severity.FATAL));
				}
				
			}
			
			instructionCount++;
			
		}
		
	}

	private double getValueOfNextOperandPeek(Stack<Operand> operandStack, Map<String, Double> variableMap) {
		if(operandStack.peek().VARIABLE_NAME != null)
			if(variableMap.containsKey(operandStack.peek().VARIABLE_NAME))
				return variableMap.get(operandStack.peek().VARIABLE_NAME);
			else
				Main.errorHandler.send(new CalculatingError(-1, "Attempt to call nil", Severity.FATAL));
		return operandStack.peek().VALUE;
	}

	private double getValueOfOperand(Stack<Operand> operandStack, Map<String, Double> variableMap) {
		if(operandStack.peek().VARIABLE_NAME != null)
			if(variableMap.containsKey(operandStack.peek().VARIABLE_NAME))
				return variableMap.get(operandStack.pop().VARIABLE_NAME);
			else
				Main.errorHandler.send(new CalculatingError(-1, "Attempt to call nil", Severity.FATAL));
		return operandStack.pop().VALUE;
	}

}
