package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeInteger;
import org.firstinspires.ftc.teamcode.aarre.src.PowerVector;
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

	@Test
	public void testGetPowerVectorNew02() {
		PowerVector powerVectorCurrent   = new PowerVector(-1.0);
		PowerVector powerVectorRequested = new PowerVector(0.0);
		PowerVector proportionPowerNew   = Ramp.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.9, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetPowerVectorNew03() {
		PowerVector powerVectorCurrent   = new PowerVector(0.0);
		PowerVector powerVectorRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew   = Ramp.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.1, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetPowerVectorNew04() {
		PowerVector powerVectorCurrent   = new PowerVector(0.0);
		PowerVector powerVectorRequested = new PowerVector(-1.0);
		PowerVector proportionPowerNew   = Ramp.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.1, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetPowerVectorNew05() {
		PowerVector powerVectorCurrent   = new PowerVector(1.0);
		PowerVector powerVectorRequested = new PowerVector(-1.0);
		PowerVector proportionPowerNew   = Ramp.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.9, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetPowerVectorNew06() {
		PowerVector powerVectorCurrent   = new PowerVector(-1.0);
		PowerVector powerVectorRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew   = Ramp.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.9, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetPowerVectorNew07() {
		PowerVector powerVectorCurrent   = new PowerVector(-1.0);
		PowerVector powerVectorRequested = new PowerVector(-1.0);
		PowerVector proportionPowerNew   = Ramp.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-1.0, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetPowerVectorNew08() {
		PowerVector powerVectorCurrent   = new PowerVector(0.0);
		PowerVector powerVectorRequested = new PowerVector(0.0);
		PowerVector proportionPowerNew   = Ramp.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.0, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetPowerVectorNew09() {
		PowerVector powerVectorCurrent   = new PowerVector(1.0);
		PowerVector powerVectorRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew   = Ramp.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(1.0, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetPowerVectorNew10() {
		PowerVector proportionPowerCurrent   = new PowerVector(0.0);
		PowerVector proportionPowerRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew = Ramp.getPowerVectorNew(proportionPowerCurrent, proportionPowerRequested);
		assertEquals(0.1, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetPowerVectorNew11() {
		PowerVector proportionPowerCurrent   = new PowerVector(1.0);
		PowerVector proportionPowerRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew = Ramp.getPowerVectorNew(proportionPowerCurrent, proportionPowerRequested);
		assertEquals(1.0, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Test
	public void testGetProportionPowerNew01() {
		PowerVector powerVectorCurrent   = new PowerVector(1.0);
		PowerVector powerVectorRequested = new PowerVector(0.0);
		PowerVector proportionPowerNew   = Ramp.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.9, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}







}