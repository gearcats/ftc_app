package org.firstinspires.ftc.teamcode.src;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Wrapper class for Integer ensures the value is positive.
 * <p>
 * Not all operations are supported.
 */
public class AarrePositiveInteger {

	private Integer value;

	private final XLogger log = XLoggerFactory.getXLogger(this.getClass().getCanonicalName());

	public AarrePositiveInteger(int value) {
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
