package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeInteger;
import org.firstinspires.ftc.teamcode.aarre.src.Ramp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class RampUnitTests {

	/**
	 * Must be overridden by subclasses
	 *
	 * @return
	 */
	public Ramp getRamp() {
		return null;
	}

	@Test
	public void whenSetTicksToRotate_thenValueIsStored() {
		NonNegativeInteger expected = new NonNegativeInteger(1000);
		getRamp().setTicksToRotate(expected);
		NonNegativeInteger actual = getRamp().getTicksToRotate();
		assertEquals(expected, actual);
	}



}