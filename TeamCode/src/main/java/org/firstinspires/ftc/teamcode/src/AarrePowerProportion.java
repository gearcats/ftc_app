package org.firstinspires.ftc.teamcode.src;

public class AarrePowerProportion {

	private double proportion;

	public AarrePowerProportion() {

	}

	public AarrePowerProportion(int proportion) {

		setProportion((double) proportion);

	}

	public AarrePowerProportion(double proportion) {

		setProportion(proportion);

	}

	private void checkProportion(double proportion) {
		if (proportion < -1.0) {
			throw new IllegalArgumentException("Proportion expected to be greater than -1.");
		}

		if (proportion > 1.0) {
			throw new IllegalArgumentException("Proportion expected to be less than 1.");
		}
	}

	private void checkProportion(int proportion) {
		checkProportion((double) proportion);
	}

	public double getProportion() {
		return proportion;
	}

	public void setProportion(double proportion) {
		checkProportion(proportion);
		this.proportion = proportion;
	}
}
