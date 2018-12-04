package org.firstinspires.ftc.teamcode.aarre.src;

import java.util.logging.Logger;

public class AarrePowerVector {

	private double proportion;

	public static final int FORWARD = 1;
	public static final int REVERSE = -1;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	public AarrePowerVector() {

	}

	public AarrePowerVector(int proportion) {
		setProportion((double) proportion);
	}

	public AarrePowerVector(double proportion) {
		setProportion(proportion);
	}

	public AarrePowerVector(AarrePowerMagnitude powerMagnitude, int direction) {
		double proportion = powerMagnitude.doubleValue() * direction;
		setProportion(proportion);
	}

	public AarrePowerVector(AarrePowerVector powerVector) {
		double proportion = powerVector.doubleValue();
		setProportion(proportion);
	}

	public AarrePowerVector add(AarrePowerVector other) {
		double sum = this.proportion + other.doubleValue();
		return new AarrePowerVector(sum);
	}

	private void checkProportion(double proportion) {

		if (proportion < -1.0) {
			throw new IllegalArgumentException("Proportion expected to be greater than -1.");
		}

		if (proportion > 1.0) {
			throw new IllegalArgumentException("Proportion expected to be less than 1.");
		}

	}

	public AarrePowerVector divideBy(AarrePowerVector divisor) {
		double quotient = this.proportion / divisor.doubleValue();
		return new AarrePowerVector(quotient);
	}

	public AarrePowerVector divideBy(double divisor) {
		double quotient = this.proportion / divisor;
		return new AarrePowerVector(quotient);
	}

	int getDirection() {
		return (int)Math.signum(this.proportion);
	}

	public boolean isGreaterThan(AarrePowerVector comparator) {
		return this.doubleValue() > comparator.doubleValue();
	}

	public double doubleValue() {
		return proportion;
	}

	AarrePowerMagnitude getMagnitude() {

		return new AarrePowerMagnitude(Math.abs(this.proportion));

	}

	AarrePowerVector multiplyBy(AarrePowerVector multiplicand) {
		double product = this.proportion * multiplicand.doubleValue();
		return new AarrePowerVector(product);
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

	public AarrePowerVector subtract(AarrePowerVector other) {
		double difference = this.proportion - other.doubleValue();
		return new AarrePowerVector(difference);
	}

	@Override
	public String toString() {
		return String.format("%f", this.proportion);
	}
}
