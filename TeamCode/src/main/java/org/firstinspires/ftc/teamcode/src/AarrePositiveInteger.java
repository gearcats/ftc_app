package org.firstinspires.ftc.teamcode.src;

/**
 * Wrapper class for Integer ensures the value is positive.
 * <p>
 * Not all operations are supported.
 */
public class AarrePositiveInteger {

	private Integer value;

	public AarrePositiveInteger(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("Value must be positive");
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
