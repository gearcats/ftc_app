package org.firstinspires.ftc.teamcode.src;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class AarrePowerVector {

	private double proportion;

	public static final int FORWARD = 1;
	public static final int REVERSE = -1;

	private final XLogger log = XLoggerFactory.getXLogger("TEMP");

	public AarrePowerVector() {

	}

	public AarrePowerVector(int proportion) {
		setProportion((double) proportion);
	}

	public AarrePowerVector(double proportion) {
		setProportion(proportion);
	}

	public AarrePowerVector(AarrePowerMagnitude powerMagnitude, int direction) {
		double proportion = powerMagnitude.asDouble() * direction;
		setProportion(proportion);
	}

	public AarrePowerVector(AarrePowerVector powerVector) {
		double proportion = powerVector.asDouble();
		setProportion(proportion);
	}

	public AarrePowerVector add(AarrePowerVector other) {
		double sum = this.proportion + other.asDouble();
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
		double quotient = this.proportion / divisor.asDouble();
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
		return this.asDouble() > comparator.asDouble();
	}

	public double asDouble() {
		return proportion;
	}

	AarrePowerMagnitude getMagnitude() {

		return new AarrePowerMagnitude(Math.abs(this.proportion));

	}

	AarrePowerVector multiplyBy(AarrePowerVector multiplicand) {
		double product = this.proportion * multiplicand.asDouble();
		return new AarrePowerVector(product);
	}

	/**
	 * Reverse the direction of this power vector
	 */
	public void reverseDirection() {
		this.setProportion(-1*this.asDouble());
	}

	private void setProportion(double proportion) {
		checkProportion(proportion);
		this.proportion = proportion;
	}

	public AarrePowerVector subtract(AarrePowerVector other) {
		double difference = this.proportion - other.asDouble();
		return new AarrePowerVector(difference);
	}

	@Override
	public String toString() {
		return String.format("%f", this.proportion);
	}
}
