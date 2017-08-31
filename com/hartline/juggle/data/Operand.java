package com.hartline.juggle.data;

public class Operand {
	
	public final String VARIABLE_NAME;
	public final double VALUE;
	
	public Operand(double value) {
		VARIABLE_NAME = null;
		VALUE = value;
	}
	
	public Operand(String varName) {
		VARIABLE_NAME = varName;
		VALUE = 0.0;
	}

	public Operand(double value, String varName) {
		VARIABLE_NAME = varName;
		VALUE = value;
	}
	
	@Override
	public String toString() {
		return "{" + VARIABLE_NAME +","+ VALUE + "}";
	}
	
}
