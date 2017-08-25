package com.hartline.juggle.data;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigNumber {
	
	private BigDecimal value = new BigDecimal(0);
	
	public boolean isWholeNumber() {
		
		return value.remainder(BigDecimal.ONE).equals(BigDecimal.ZERO);
		
	}
	
	public void add(BigNumber toBeAdded) {
		
		value.add(toBeAdded.getValue());
		
	}
	
	public void sub(BigNumber toBeRemoved) {
		
		value.subtract(toBeRemoved.getValue());
		
	}
	
	public void mult(BigNumber toBeMultiplied) {
		
		value.multiply(toBeMultiplied.getValue());
		
	}
	
	public void div(BigNumber divisor) {
		
		value.divide(divisor.getValue());
		
	}
	
	private BigDecimal getValue() {
		return value;
	}

	@Override 
	public String toString() {
		
		if(isWholeNumber())
			return value.toBigInteger().toString();
		
		return value.toString();
		
	}
	
}
