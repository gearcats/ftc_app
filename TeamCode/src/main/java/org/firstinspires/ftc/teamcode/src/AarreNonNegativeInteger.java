package org.firstinspires.ftc.teamcode.src;

import java.util.logging.Logger;

/**
 * Wrapper class for Integer ensures the value is positive.
 * <p>
 * Not all operations are supported.
 */
public class AarreNonNegativeInteger {

	private Integer value;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	public AarreNonNegativeInteger(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Value must be non-negative");
		}
		this.value = new Integer(value);
	}

	public double doubleValue() {
		return value.doubleValue();
	}

	public int intValue() {
		return value.intValue();
	}

	public String toString() {
		return String.format("%f", this.value);
	}
}
