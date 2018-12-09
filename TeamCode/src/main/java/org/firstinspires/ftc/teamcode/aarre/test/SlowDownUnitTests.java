package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlowDownUnitTests extends RampUnitTests {

	private SlowDown slowdown;

	@Override
	public Ramp getRamp() {
		return slowdown;
	}

	@Test
	@BeforeEach
	public void slowDownUnitTests() {
		slowdown = new SlowDown();
		assertNotNull(slowdown);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown01_Hex() {

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		final int                tickNumberAtStartOfPeriod = 0;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(2000);

		/*
		 * The revHDCoreHexMotor needs 10 cycles to ramp down 10 cycles in power
		 *
		 * Each cycle in power is 50 milliseconds long
		 *
		 * So the total ramp down will be 500 milliseconds
		 *
		 * The number of ticks in a millisecond is 0.2688
		 *
		 * Therefore the number of ticks to ramp down is 500*0.2688 = 134
		 *
		 * 2000 - 134.4 = 1865.6
		 */

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(1865.6, actual, 0.01);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown01_Torque() {

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		final int                tickNumberAtStartOfPeriod = 0;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(2000);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(800, actual);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown02_Hex() {

		/*
		 * A power difference of 1.0 requires 10 cycles of ramp.
		 */
		PowerVector powerAtStart = new PowerVector(1.0);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		final int                tickNumberAtStartOfPeriod = 0;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);

		/*
		 * There are 13.44 ticks in a cycle, so the ramp should be 134.4 ticks
		 */
		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		/*
		 * Subtracting 134.4 ticks from the total of 10000 should give us 8800 ticks
		 */
		assertEquals(9865.6, actual);
	}

	/**
	 * Test calculating when to start a ramp down in a 10000-tick period.
	 */
	@Test
	public final void testGetTickNumberToStartSlowDown02_Torque() {

		/*
		 * A power difference of 1.0 requires 10 cycles of ramp.
		 */
		PowerVector powerAtStart = new PowerVector(1.0);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		int                      tickNumberAtStartOfPeriod = 0;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);

		/*
		 * There are 120 ticks in a cycle, so the ramp should be 1200 ticks
		 */
		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		/*
		 * Subtracting 1200 ticks from the total of 10000 should give us 8800 ticks
		 */
		assertEquals(8800, actual);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown03_Hex() {

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		final int                tickNumberAtStartOfPeriod = 0;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		/*
		 * At 13.44 ticks per cycle, total ticks in ramp = 67.2
		 * 120 - 67.2 = 52.8
		 */
		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(52.8, actual, 0.01);
	}

	/**
	 * Test calculating when to start a ramp down in a 10000-tick period.
	 */
	@Test
	public final void testGetTickNumberToStartSlowDown03_Torque() {

		/*
		 * A power difference of 1.0 requires 5 cycles of ramp.
		 * There are 120 ticks in a cycle.
		 * The ramp must be 600 ticks long
		 * 600 - 120 = -480
		 */
		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		final int                tickNumberAtStartOfPeriod = 0;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(-480, actual);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown04_Hex() {

		final int                tickNumberAtStartOfPeriod = 60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * 10 power cycles
		 * 13.44 ticks per cycle
		 * = 134.4 ticks in ramp down
		 *
		 * 10060 - 134.4 = 9925.6
		 */
		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(9925.6, actual, 0.01);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when the revHDCoreHexMotor is not close enough to the
	 * target tick number.
	 */
	@Test
	public final void testGetTickNumberToStartSlowDown04_Torque() {

		final int                tickNumberAtStartOfPeriod = 60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(8860, actual);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown08_Hex() {

		final int                tickNumberAtStartOfPeriod = 0;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 * Power change requires 5 cycles
		 * Each cycle is 13.44 ticks
		 * 13.44 * 5 = 67.2
		 * We need to start ramp down at tick 120 - 67.2 ticks = 52.8 ticks
		 */
		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(52.8, actual);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown08_Torque() {

		final int                tickNumberAtStartOfPeriod = 0;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 * Power change requires 5 cycles
		 * Each cycle is 120 ticks
		 * 120 * 5 = 600
		 * We need to start ramp down at tick 120 - 600 ticks = -480 ticks
		 */
		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(-480, actual);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown11_Hex() {


		final int                tickNumberAtStartOfPeriod = -60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of slowing down
		 * At 13.44 ticks per cycle for Rev HD Core Hex Motor
		 * --> Need 134.4 ticks of slowing down
		 * Tick number at end of period = 1000-60 = 940
		 * Would need to start ramp at 940 - 134.4 tics = 805.6 ticks
		 */

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(805.6, actual);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number (negative numbers)
	 */
	@Test
	public final void testGetTickNumberToStartSlowDown11_Torque() {


		final int                tickNumberAtStartOfPeriod = -60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need -1200 ticks
		 * Tick number at end of period = -1060
		 * Would need to start ramp at -1060 + 1200 tics = -260 ticks
		 */

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(-260, actual);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown12_Hex() {

		final int                tickNumberAtStartOfPeriod = 60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 * Need 5 cycles to slow down from 0.5 to 0.0.
		 * With Rev HD Core Hex revHDCoreHexMotor, there are 13.44 ticks per cycle.
		 * Need 5 * 13.44 = 67.2 ticks to slow down.
		 * Tick number at end of period is 60 + 120 = 180.
		 * Tick to start is 180 - 67.2 = 112.8
		 */
		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(112.8, actual);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown12_Torque() {

		final int                tickNumberAtStartOfPeriod = 60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(-420, actual);
	}

	/**
	 * Test that slowing down should be in effect if the current tick number is close enough to the target tick number.
	 * This test depends only on comparing the current and target tick numbers, so it does not depend on any particular
	 * kind of revHDCoreHexMotor.
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning01_Hex() {

		final int                tickNumberAtStartOfPeriod = 100;
		final int                tickNumberCurrent         = 1000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);

		/*

	      The target tick number at the end of the period is 1100
		  The current tick number is 1000
		  The number of ticks that remain are 100
		  There are 10 cycles

		  With 120 ticks per cycle (TorqueNADO), slowing down would take 1200 ticks, meaning that it should have
		  started at tick 1100-1200 = -100 (if possible) and still be going

		  With 13.44 ticks per cycle (REV HD Core Hex), slowing down would take 134.4 ticks, meaning that it should
		  have started at tick 1100-134.4 = 965.6 and still be going

		 */

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertTrue(result);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number.
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning01_Torque() {

		final int                tickNumberAtStartOfPeriod = 100;
		final int                tickNumberCurrent         = 1000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);

		/*

	      The target tick number at the end of the period is 1100
		  The current tick number is 1000
		  The number of ticks that remain are 100
		  There are 10 cycles

		  With 120 ticks per cycle (TorqueNADO), slowing down would take 1200 ticks, meaning that it should have
		  started at tick 1100-1200 = -100 (if possible) and still be going

		  With 13.44 ticks per cycle (REV HD Core Hex), slowing down would take 134.4 ticks, meaning that it should
		  have started at tick 1100-134.4 = 965.6 and still be going

		 */

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertTrue(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning02_Hex() {

		final int                tickNumberAtStartOfPeriod = 60;
		final int                tickNumberCurrent         = 61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning02_Torque() {

		final int                tickNumberAtStartOfPeriod = 60;
		final int                tickNumberCurrent         = 61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning03_Hex() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 11000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning03_Torque() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 11000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning04_Hex() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 123;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(100);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning04_Torque() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 123;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(100);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when not enough ticks have passed to start the ramp.
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning05_Hex() {

		final int                tickNumberAtStartOfPeriod = 60;
		final int                tickNumberCurrent         = 61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 * Need 5 test cycles
		 * Ticks per test cycle is 13.44
		 * = 66.72 ticks
		 * tick at end of period = 180
		 * Tick to start ramp = 180 - 66.72 = 113.8
		 */
		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when not enough ticks have passed to start the ramp.
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning05_Torque() {

		final int                tickNumberAtStartOfPeriod = 60;
		final int                tickNumberCurrent         = 61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 *  The period goes from tick 60 to tick 180.
		 *  We are at tick 61.
		 *
		 *  We need 5 cycles for slowing down
		 *
		 *  The TorqueNADO needs 120 * 5 cycles = 600 ticks, so it should start at tick 180-600=-420and still be
		 *  slowing down. The Rev HD Core Hex needs 13.44 * 5 cycles = 67 ticks, so it should start at tick
		 *  180-67=113 and still be slowing down
		 *
		 */

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning07_Hex() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -59;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();
		assertFalse(result);

	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning07_Torque() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -59;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);

	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when enough ticks have passed to start the ramp but not
	 * enough have passed to finish the required movement (negative numbers)
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning08_Hex() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 * Power change requires 5 cycles
		 * Each cycle is 13.44 ticks
		 * We need 13.44 * 5 = 67.2 ticks for ramp
		 * End of the period is at -120 ticks
		 * We need to start ramp at -120 ticks + 67.2 ticks = -52.8 ticks
		 * The current position of -61 ticks is farther from the end of the cycle than the
		 * number of
		 * ticks needed for the ramp
		 */
		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean actual = slowdown.isSlowDownToEncoderTicksRunning();
		assertFalse(actual);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when enough ticks have passed to start the ramp but not
	 * enough have passed to finish the required movement (negative numbers)
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning08_Torque() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 * Power change requires 5 cycles
		 * Each cycle is 13.44 ticks
		 * We need 13.44 * 5 = 67.2 ticks for ramp
		 * End of the period is at -120 ticks
		 * We need to start ramp at -120 ticks + 67.2 ticks = -52.8 ticks
		 * The current position of -61 ticks is farther from the end of the cycle than the
		 * number of
		 * ticks needed for the ramp
		 */
		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean actual = slowdown.isSlowDownToEncoderTicksRunning();
		assertFalse(actual);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning09_Hex() {

		/*
		 * The current tick number exceed the total number of ticks we were supposed to
		 * move, so ramping down should stop.
		 *
		 * This is a made up example.
		 */

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -11000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning09_Torque() {

		/*
		 * The current tick number exceed the total number of ticks we were supposed to
		 * move, so ramping down should stop.
		 *
		 * This is a made up example.
		 */

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -11000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();
		assertFalse(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning10_Hex() {

		final int                tickNumberAtStartOfPeriod = -60;
		final int                tickNumberCurrent         = -61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunning10_Torque() {

		final int                tickNumberAtStartOfPeriod = -60;
		final int                tickNumberCurrent         = -61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();
		assertFalse(result);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number (negative numbers)
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning11_Hex() {


		final int                tickNumberAtStartOfPeriod = -60;
		final int                tickNumberCurrent         = -900;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 *
		 * At 120 ticks per cycle (on TorqueNADO)
		 * Need 1200 ticks
		 * Tick number at end of period = -1060
		 * Would need to start ramp at -1060 + 1200 tics = 140 ticks
		 * But we are at a negative tick and moving more negative, so we will never get to 140
		 *
		 * At
		 */

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);

	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number (negative numbers)
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning11_Torque() {


		final int                tickNumberAtStartOfPeriod = -60;
		final int                tickNumberCurrent         = -900;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 *
		 * At 120 ticks per cycle (on TorqueNADO)
		 * Need 1200 ticks
		 * Tick number at end of period = -1060
		 * Would need to start ramp at -1060 + 1200 tics = 140 ticks
		 * But we are at a negative tick and moving more negative, so we will never get to 140
		 *
		 * At
		 */

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();
		assertFalse(result);

	}

	@Test
	public void whenConstructorIsCalled_thenTheResultIsNotNull() {
		assertNotNull(slowdown);
	}

	/**
	 * The tick number to start a slow down should not depend on what the current tick number is.
	 */
	@Test
	public void whenCurrentTickChanges_thenTickToStartSlowDownDoesNot_givenMotorIsHex() {

		PowerVector powerAtStart = new PowerVector(1.0);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		Integer            tickNumberAtStartOfPeriod = new Integer(8);
		NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(60);

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		slowdown.setCurrentTickNumber(8);
		double result1 = slowdown.getTickNumberToStartSlowDown();

		slowdown.setCurrentTickNumber(15);
		double result2 = slowdown.getTickNumberToStartSlowDown();

		assertEquals(result1, result2);

	}

	/**
	 * The tick number to start a slow down should not depend on what the current tick number is.
	 */
	@Test
	public void whenCurrentTickChanges_thenTickToStartSlowDownDoesNot_givenMotorIsTorqueNADO() {

		PowerVector powerAtStart = new PowerVector(1.0);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		Integer            tickNumberAtStartOfPeriod = new Integer(8);
		NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(60);

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		slowdown.setCurrentTickNumber(8);
		double result1 = slowdown.getTickNumberToStartSlowDown();

		slowdown.setCurrentTickNumber(15);
		double result2 = slowdown.getTickNumberToStartSlowDown();

		assertEquals(result1, result2);

	}

	@Test
	public void whenObjectIsConstructed_thenTicksToSlowDownIsZero() {
		NonNegativeInteger expected = new NonNegativeInteger(0);
		NonNegativeInteger actual   = slowdown.getTicksToRotate();
		assertEquals(expected.doubleValue(), actual.doubleValue());
	}

	@Test
	public void whenSlowDownIsConstructed_thenTicksToSlowDownIsNotNull() {
		NonNegativeInteger actual = slowdown.getTicksToRotate();
		assertNotNull(actual);
	}

	@Test
	public final void whenSlowDownNotStarted_thenReturnFalse_Hex() {

		final int                tickNumberAtStartOfPeriod = 60;
		final int                tickNumberCurrent         = 114;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 *  The period runs from 60 - 180 ticks
		 *  We are currently at tick number 114
		 *  There are 5 cycles
		 *
		 *  For REV HD Core Hex, the slow down would be 13.44 * 5 = 67.2 ticks, and the slow down would start at tick
		 *  number 180 - 67.2 = 112.8. If the slow down started at tick 112.8, then it would still be going currently
		 *  at tick 114.
		 */
		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean actual = slowdown.isSlowDownToEncoderTicksRunning();

		assertTrue(actual);
	}

	@Test
	public final void whenSlowDownNotStarted_thenReturnFalse_Torque() {

		final int                tickNumberAtStartOfPeriod = 60;
		final int                tickNumberCurrent         = 114;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 *  The period runs from 60 - 174 ticks.
		 *  There are 5 cycles
		 *
		 *  For TorqueNADO, this would be 120 * 5 = 600 ticks, and the slow down would start at 174-600 ticks = -426
		 *  ticks and still be running --> true
		 *
		 *  For REV HD Core Hex, this would be 13.44 * 5 = 67.2 ticks, and the slow down would start at tick
		 *  number 174 - 67.2 = 106.8 and would not have started yet --> false
		 */
		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean actual = slowdown.isSlowDownToEncoderTicksRunning();

		assertTrue(actual);
	}

	@Test
	public final void whenSlowDownNotStarted_thenTickNumberToStartSlowDownIsCorrect_Hex() {

		final int                tickNumberAtStartOfPeriod = 60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 *  The period runs from 60 - 180 ticks. There are 5 cycles. For REV HD Core Hex, the slow down would be 13.44
		 *  x 5 = 67.2 ticks, and the slow down would start at tick
		 *  number 180 - 67.2 = 112.8.
		 */

		double expected = 112.8;

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(expected, actual);

	}

	@Test
	public final void whenSlowingDown_thenTicsPerCycleIsCorrect_Hex() {
		MotorRevHDCoreHex motorRevHDCoreHex = new MotorRevHDCoreHex();
		slowdown.setMotor(motorRevHDCoreHex);

		double ticksPerCycle = slowdown.getTicksPerCycle().doubleValue();
		assertEquals(13.44, ticksPerCycle, 0.001);
	}

	@Test
	public final void whenSlowingDown_thenTicsPerCycleIsCorrect_Torque() {

		MotorTorqueNADO motorTorqueNADO = new MotorTorqueNADO();
		SlowDown        slowdown        = new SlowDown();
		slowdown.setMotor(motorTorqueNADO);

		double ticksPerCycle = slowdown.getTicksPerCycle().doubleValue();
		assertEquals(13.44, ticksPerCycle, 0.001);
	}

	@Test
	public final void whenThereAreEnoughTicks_thenSlowDownStartsOnTime_Hex() {

		final int                tickNumberAtStartOfPeriod = -60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of slowing down. Thus, at 13.44 ticks per cycle, we need 134.4 ticks for slowdown. The tick
		 * number at end of period is -60 + 1000 = 940. Thus, we would need to start ramp at 940 - 134.4 tics = 805.6
		 * ticks
		 */

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(805.6, actual);
	}

	@Test
	public final void whenThereAreEnoughTicks_thenSlowDownStartsOnTime_Torque() {

		final int                tickNumberAtStartOfPeriod = -60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need 1200 ticks
		 * Tick number at start of period = -60
		 * Tick number at end of period = 9940
		 * Would need to start ramp at 9940 - 1200 ticks = 8740 ticks
		 */

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(8740, actual);
	}

	@Test
	public final void whenThereAreNotEnoughTicks_thenSlowDownStartsTooEarly_Hex() {

		final int                tickNumberAtStartOfPeriod = -60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(100);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of slowing down
		 * At 13.44 ticks per cycle
		 * Need 134.4 ticks
		 * Tick number at end of period = 40
		 * Would need to start ramp at 40 - 134.4 tics = -94.4 ticks
		 */

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(-94.4, actual);

	}

	@Test
	public final void whenThereAreNotEnoughTicks_thenSlowDownStartsTooEarly_Torque() {

		final int                tickNumberAtStartOfPeriod = -60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need 1200 ticks
		 * Tick number at start of period = -60
		 * Tick number at end of period = 940
		 * Would need to start ramp at 940 - 1200 ticks = -260 ticks
		 */

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(-260, actual);
	}

	@Test
	public final void whenTickNumberInRange_thenSlowDownIsRunning_Hex() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 59;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 *  Period runs from tick 0 to tick 120.
		 *  We need 5 cycles of slowdown. At 13.44 ticks per cycle, that is 67.2 ticks.
		 *  So the slowdown should start at tick number 120 - 67.2 = 52.8
		 *  In that case, the slowdown should be running currently at tick number 59
		 */
		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertTrue(result);
	}

	@Test
	public final void whenTickNumberInRange_thenSlowDownIsRunning_Torque() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 59;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 *  Period runs from 0 to 120.
		 *  Need 5 cycles.
		 *  At 120 ticks per cycle, that is 600 ticks.
		 *  Last tick is number 120
		 *  Ramp down would start at tick number 120-600 = -480 and still be going
		 */
		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean actual = slowdown.isSlowDownToEncoderTicksRunning();

		assertTrue(actual);
	}

	@Test
	public final void whenTickNumberOutsidePeriod_thenTrue_Hex() {

		// TODO: Rename this method

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 * The period runs from tick 0 to tick 120.
		 * The current tickNumber is -61.
		 * This should be impossible.
		 */

		MotorRevHDCoreHex motor = new MotorRevHDCoreHex();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setCurrentTickNumber(tickNumberCurrent);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean result = slowdown.isSlowDownToEncoderTicksRunning();

		assertFalse(result);
	}

	@Test
	public final void whenTickNumberOutsidePeriod_thenTrue_Torque() {

		// TODO: Rename this method
		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 * The period runs from tick 0 to tick 120.
		 * We need 5 cycles.
		 *
		 * With a TorqueNado, that is 5 * 134.4
		 *
		 * The current tickNumber is -61.
		 *
		 * If that really is the current tick number, then we should be started
		 */

		MotorTorqueNADO motor = new MotorTorqueNADO();

		slowdown.setMotor(motor);
		slowdown.setInitialTickNumber(tickNumberAtStartOfPeriod);
		slowdown.setTicksToRotate(numberOfTicksInPeriod);
		slowdown.setInitialPower(powerAtStart);
		slowdown.setTargetPower(powerAtEnd);

		boolean actual = slowdown.isSlowDownToEncoderTicksRunning();

		assertTrue(actual);

	}

}