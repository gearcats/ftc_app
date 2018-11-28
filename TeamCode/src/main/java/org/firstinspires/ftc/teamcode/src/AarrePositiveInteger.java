package org.firstinspires.ftc.teamcode.src;

public class AarrePositiveInteger {

	private int value;

	public AarrePositiveInteger(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("Value must be positive");
		}
		this.value = value;
	}
}
