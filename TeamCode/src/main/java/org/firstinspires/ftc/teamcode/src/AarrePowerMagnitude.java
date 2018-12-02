package org.firstinspires.ftc.teamcode.src;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class AarrePowerMagnitude implements Comparable<AarrePowerMagnitude> {

	private double magnitude;

	private final XLogger log = XLoggerFactory.getXLogger("TEMP");

	public AarrePowerMagnitude(double magnitude) {
		// log.entry();
		setProportion(magnitude);
		// log.exit();
	}

	public AarrePowerMagnitude(AarrePowerVector powerVector) {
		double magnitude = powerVector.asDouble();
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
	public int compareTo(AarrePowerMagnitude comparator) {
		return Double.compare(this.magnitude, comparator.asDouble());
	}

	public double asDouble() {
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
	public double divideBy(AarrePowerMagnitude divisor) {
		return this.magnitude / divisor.asDouble();
	}

	public boolean isGreaterThan(AarrePowerMagnitude comparator) {
		return this.magnitude > comparator.asDouble();
	}

	public boolean isLessThan(AarrePowerMagnitude comparator) {
		return this.magnitude < comparator.asDouble();
	}

	private void setProportion(double magnitude) {
		checkMagnitude(magnitude);
		this.magnitude = magnitude;
	}

	public AarrePowerMagnitude subtract(AarrePowerMagnitude other) {
		double difference = this.magnitude - other.asDouble();
		return new AarrePowerMagnitude(difference);
	}

	@Override
	public String toString() {
		return String.format("%f", this.magnitude);
	}

}
