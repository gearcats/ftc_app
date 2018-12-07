package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.*;
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
	public final void testGetNumberOfCycles01_Hex() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(1.0);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		Ramp ramp = getRamp();

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		ramp.setMotor(motor);
		ramp.setTicksToRotate(ticksToMove);
		ramp.setInitialPower(currentPower);
		ramp.setTargetPower(proportionPowerRequested);

		NonNegativeInteger actual = ramp.getNumberOfCycles();

		assertEquals(10, actual.intValue());
	}

	@Test
	public final void testGetNumberOfCycles01_Torque() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(1.0);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		Ramp ramp = getRamp();

		MotorTorqueNADO motor = new MotorTorqueNADO();

		ramp.setMotor(motor);
		ramp.setTicksToRotate(ticksToMove);
		ramp.setInitialPower(currentPower);
		ramp.setTargetPower(proportionPowerRequested);

		NonNegativeInteger actual = ramp.getNumberOfCycles();

		assertEquals(10, actual.intValue());
	}

	@Test
	public final void testGetNumberOfCycles02_Hex() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(0.1);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		Ramp ramp = getRamp();

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		ramp.setMotor(motor);
		ramp.setTicksToRotate(ticksToMove);
		ramp.setInitialPower(currentPower);
		ramp.setTargetPower(proportionPowerRequested);

		NonNegativeInteger actual = ramp.getNumberOfCycles();

		assertEquals(1, actual.intValue());
	}

	@Test
	public final void testGetNumberOfCycles02_Torque() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(0.1);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		Ramp ramp = getRamp();

		MotorTorqueNADO motor = new MotorTorqueNADO();

		ramp.setMotor(motor);
		ramp.setTicksToRotate(ticksToMove);
		ramp.setInitialPower(currentPower);
		ramp.setTargetPower(proportionPowerRequested);

		NonNegativeInteger actual = ramp.getNumberOfCycles();

		assertEquals(1, actual.intValue());
	}

	@Test
	public final void testGetNumberOfCycles03_Hex() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(-0.1);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		Ramp ramp = getRamp();

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		ramp.setMotor(motor);
		ramp.setTicksToRotate(ticksToMove);
		ramp.setInitialPower(currentPower);
		ramp.setTargetPower(proportionPowerRequested);

		NonNegativeInteger actual = ramp.getNumberOfCycles();

		assertEquals(1, actual.intValue());
	}

	@Test
	public final void testGetNumberOfCycles03_Torque() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(-0.1);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		Ramp ramp = getRamp();

		MotorTorqueNADO motor = new MotorTorqueNADO();

		ramp.setMotor(motor);
		ramp.setTicksToRotate(ticksToMove);
		ramp.setInitialPower(currentPower);
		ramp.setTargetPower(proportionPowerRequested);

		NonNegativeInteger actual = ramp.getNumberOfCycles();

		assertEquals(1, actual.intValue());
	}

	@Test
	public final void testGetNumberOfCycles04_Hex() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(-1.0);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		Ramp ramp = getRamp();

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		ramp.setMotor(motor);
		ramp.setTicksToRotate(ticksToMove);
		ramp.setInitialPower(currentPower);
		ramp.setTargetPower(proportionPowerRequested);

		NonNegativeInteger actual = ramp.getNumberOfCycles();

		assertEquals(10, actual.intValue());
	}

	@Test
	public final void testGetNumberOfCycles04_Torque() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(-1.0);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		Ramp ramp = getRamp();

		MotorTorqueNADO motor = new MotorTorqueNADO();

		ramp.setMotor(motor);
		ramp.setTicksToRotate(ticksToMove);
		ramp.setInitialPower(currentPower);
		ramp.setTargetPower(proportionPowerRequested);

		NonNegativeInteger actual = ramp.getNumberOfCycles();

		assertEquals(10, actual.intValue());
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

	@Test
	public final void testGetTicksPerCycle_Hex() {

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();
		getRamp().setMotor(motor);
		NonNegativeDouble ticksPerCycle = getRamp().getTicksPerCycle();
		assertEquals(120.0, ticksPerCycle.doubleValue());

	}

	@Test
	public final void testGetTicksPerCycle_Torque() {

		MotorTorqueNADO motor = new MotorTorqueNADO();
		getRamp().setMotor(motor);
		NonNegativeDouble ticksPerCycle = getRamp().getTicksPerCycle();
		assertEquals(13.44, ticksPerCycle.doubleValue());

	}

	@Test
	public void whenCurrentTickNumberIsSet_thenGetReturnsTheSameValue() {

		int expected = 100;
		getRamp().setCurrentTickNumber(expected);
		int actual = getRamp().getCurrentTickNumber();
		assertEquals(expected, actual);
	}

	@Test
	public void whenPowerAtStartOfPeriodIsSet_thenGetReturnsTheSameValue() {

		PowerVector expected = new PowerVector(0.0);
		getRamp().setInitialPower(expected);
		PowerVector actual = getRamp().getInitialPower();
		assertEquals(expected, actual);

	}

	@Test
	public void whenSetTicksToRotate_thenValueIsStored() {
		NonNegativeInteger expected = new NonNegativeInteger(1000);
		getRamp().setTicksToRotate(expected);
		NonNegativeInteger actual = getRamp().getTicksToRotate();
		assertEquals(expected, actual);
	}


}