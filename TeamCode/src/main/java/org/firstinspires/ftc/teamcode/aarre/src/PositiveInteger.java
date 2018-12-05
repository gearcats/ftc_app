package org.firstinspires.ftc.teamcode.aarre.src;

import java.util.logging.Logger;

/**
 * Wrapper class for Integer ensures the value is positive.
 * <p>
 * Not all operations are supported.
 */
public class PositiveInteger {

	private final Logger  log = Logger.getLogger(this.getClass().getCanonicalName());
	private       Integer value;

	public PositiveInteger(int value) {
		if (value < 1) {
			throw new IllegalArgumentException(String.format("Value must be positive: %d", value));
		}
		this.value = new Integer(value);
	}

	public double doubleValue() {
		return value.doubleValue();
	}

	public int intValue() {
		return value.intValue();
	}

	@Override
	public String toString() {
		return String.format(this.value.toString());
	}

}
