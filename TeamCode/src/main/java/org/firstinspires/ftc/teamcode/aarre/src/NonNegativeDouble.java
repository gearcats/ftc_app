package org.firstinspires.ftc.teamcode.aarre.src;

public class NonNegativeDouble {

	private Double value;

	public NonNegativeDouble() {
		value = Double.valueOf(0);
	}

	public NonNegativeDouble(double value) {
		this.value = Double.valueOf(value);
	}

	public double doubleValue() {
		return value.doubleValue();
	}

	public int intValue() {
		int result = (int) Math.round(value);
		return result;
	}

	public NonNegativeDouble multiplyBy(NonNegativeDouble multiplicand) {
		double            multiplicand1 = multiplicand.doubleValue();
		double            multiplicand2 = doubleValue();
		double            product       = multiplicand1 * multiplicand2;
		NonNegativeDouble result        = new NonNegativeDouble(product);
		return result;
	}

	public NonNegativeDouble multiplyBy(NonNegativeInteger multiplicand) {
		double            multiplicand1 = multiplicand.doubleValue();
		double            multiplicand2 = doubleValue();
		double            product       = multiplicand1 * multiplicand2;
		NonNegativeDouble result        = new NonNegativeDouble(product);
		return result;
	}

}
