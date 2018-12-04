package org.firstinspires.ftc.teamcode.aarre.src;

import java.util.logging.Logger;

public class PowerMagnitude implements Comparable<PowerMagnitude> {

	private double magnitude;

	private final Logger log = Logger.getLogger(this.getClass().getName());

	public PowerMagnitude(double magnitude) {
		log.entering("AarrePowerMagnitude", "AarrePowerMagnitude");
		setProportion(magnitude);
		log.exiting("AarrePowerMagnitude", "AarrePowerMagnitude");
	}

	public PowerMagnitude(PowerVector powerVector) {
		double magnitude = powerVector.doubleValue();
		setProportion(magnitude);
	}

	private void checkMagnitude(double magnitude) {
		if (magnitude < 0.0) {
			throw new IllegalArgumentException("Magnitude expected to be >= 0.");
		}

		if (magnitude > 1.0) {
			throw new IllegalArgumentException("Magnitude expected to be <= 1.");
		}
	}

	@Override
	public int compareTo(PowerMagnitude comparator) {
		return Double.compare(this.magnitude, comparator.doubleValue());
	}

	public double doubleValue() {
		return this.magnitude;
	}

	/**
	 * Divide this power magnitude by another.
	 * <p>
	 * The return type is double because dividing one power magnitude by another does not result in a power magnitude.
	 * Consider dividing magnitude 1.0 by magnitude 0.5. In that case, the result of the division is a magnitude of
	 * 2.0,
	 * which is not a legal power magnitude.
	 *
	 * @param divisor
	 * 		The power magnitude by which to divide this one.
	 *
	 * @return The quotient between the two magnitudes.
	 */
	public double divideBy(PowerMagnitude divisor) {
		return this.magnitude / divisor.doubleValue();
	}

	public boolean isGreaterThan(PowerMagnitude comparator) {
		return this.magnitude > comparator.doubleValue();
	}

	public boolean isLessThan(PowerMagnitude comparator) {
		return this.magnitude < comparator.doubleValue();
	}

	private void setProportion(double magnitude) {
		checkMagnitude(magnitude);
		this.magnitude = magnitude;
	}

	public PowerMagnitude subtract(PowerMagnitude other) {
		double difference = this.magnitude - other.doubleValue();
		return new PowerMagnitude(difference);
	}

	@Override
	public String toString() {
		return String.format("%f", this.magnitude);
	}

}
