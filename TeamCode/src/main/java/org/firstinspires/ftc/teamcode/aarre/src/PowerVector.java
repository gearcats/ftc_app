package org.firstinspires.ftc.teamcode.aarre.src;

import java.util.logging.Logger;

public class PowerVector {

	private double proportion;

	public static final int FORWARD = 1;
	public static final int REVERSE = -1;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	public PowerVector() {

	}

	public PowerVector(int proportion) {
		setProportion((double) proportion);
	}

	public PowerVector(double proportion) {
		setProportion(proportion);
	}

	public PowerVector(PowerMagnitude powerMagnitude, int direction) {
		double proportion = powerMagnitude.doubleValue() * direction;
		setProportion(proportion);
	}

	public PowerVector(PowerVector powerVector) {
		double proportion = powerVector.doubleValue();
		setProportion(proportion);
	}

	public PowerVector add(PowerVector other) {
		double sum = this.proportion + other.doubleValue();
		return new PowerVector(sum);
	}

	private void checkProportion(double proportion) {

		if (proportion < -1.0) {
			throw new IllegalArgumentException("Proportion expected to be greater than -1.");
		}

		if (proportion > 1.0) {
			throw new IllegalArgumentException("Proportion expected to be less than 1.");
		}

	}

	public PowerVector divideBy(PowerVector divisor) {
		double quotient = this.proportion / divisor.doubleValue();
		return new PowerVector(quotient);
	}

	public PowerVector divideBy(double divisor) {
		double quotient = this.proportion / divisor;
		return new PowerVector(quotient);
	}

	int getDirection() {
		return (int)Math.signum(this.proportion);
	}

	public boolean isGreaterThan(PowerVector comparator) {
		return this.doubleValue() > comparator.doubleValue();
	}

	public double doubleValue() {
		return proportion;
	}

	PowerMagnitude getMagnitude() {

		return new PowerMagnitude(Math.abs(this.proportion));

	}

	PowerVector multiplyBy(PowerVector multiplicand) {
		double product = this.proportion * multiplicand.doubleValue();
		return new PowerVector(product);
	}

	/**
	 * Reverse the direction of this power vector
	 */
	public void reverseDirection() {
		this.setProportion(-1 * this.doubleValue());
	}

	private void setProportion(double proportion) {
		checkProportion(proportion);
		this.proportion = proportion;
	}

	public PowerVector subtract(PowerVector other) {
		double difference = this.proportion - other.doubleValue();
		return new PowerVector(difference);
	}

	@Override
	public String toString() {
		return String.format("%f", this.proportion);
	}
}
