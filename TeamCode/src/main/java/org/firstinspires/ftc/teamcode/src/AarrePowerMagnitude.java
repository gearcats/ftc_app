package org.firstinspires.ftc.teamcode.src;

public class AarrePowerMagnitude implements Comparable<AarrePowerMagnitude> {

	private double magnitude;

	public AarrePowerMagnitude(double magnitude) {
		setProportion(magnitude);
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

	double asDouble() {
		return this.magnitude;
	}

	public AarrePowerMagnitude divideBy(AarrePowerMagnitude divisor) {
		double ratio = this.magnitude / divisor.asDouble();
		return new AarrePowerMagnitude(ratio);
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

}
