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
	}

	@Test
	public void whenConstructorIsCalled_thenTheResultIsNotNull() {
		assertNotNull(slowdown);
	}

	/**
	 * The tick number to start a slow down should not depend on what the current tick number is.
	 */
	@Test
	public void whenCurrentTickChanges_thenTickToStartSlowDownDoesNot() {

		PowerVector powerVectorAtStartOfPeriod = new PowerVector(1.0);
		PowerVector powerVectorAtEndOfPeriod   = new PowerVector(0.0);

		Integer            tickNumberAtStartOfPeriod = new Integer(8);
		NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(60);

		slowdown.getMotor().setCurrentTickNumber(8);

		double result1 = slowdown.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerVectorAtStartOfPeriod, powerVectorAtEndOfPeriod);

		slowdown.getMotor().setCurrentTickNumber(15);

		double result2 = slowdown.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerVectorAtStartOfPeriod, powerVectorAtEndOfPeriod);

		assertEquals(result1, result2);

	}

	/**
	 * Test that slowing down should be in effect if the current tick number is close enough to the target tick number.
	 * This test depends only on comparing the current and target tick numbers, so it does not depend on any particular
	 * kind of revHDCoreHexMotor.
	 */
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunning01() {

		SlowDown          slowdown = new SlowDown();
		MotorRevHDCoreHex motor    = new MotorRevHDCoreHex();


		final int                tickNumberAtStartOfPeriod = 100;
		final int                tickNumberCurrent         = 1000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

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
	 * Test that isSlowDownToEncoderTicksRunning returns false when not enough ticks have passed to start the ramp.
	 */
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunning05() {

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
		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when enough ticks have passed to start the ramp but not
	 * enough have passed to finish the required movement (negative numbers)
	 */
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunning08() {

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
		boolean actual = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(actual);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number (negative numbers)
	 */
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunning11() {

		final int                tickNumberAtStartOfPeriod = -60;
		final int                tickNumberCurrent         = -900;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 13.44 ticks per cycle, we need 134.4 ticks to ramp
		 * Period goes from tick number -60 to tick number 940
		 * Therefore the current tick number (-900) is out of range.
		 *
		 */

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunningGeneric01() {

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

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	@Override
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunningGeneric02() {

		final int                tickNumberAtStartOfPeriod = 60;
		final int                tickNumberCurrent         = 61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunningGeneric03() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 11000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunningGeneric04() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 123;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(100);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunningGeneric05() {

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


		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunningGeneric07() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -59;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);

	}

	@Override
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunningGeneric09() {

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

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunningGeneric10() {

		final int                tickNumberAtStartOfPeriod = -60;
		final int                tickNumberCurrent         = -61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number (negative numbers)
	 */
	@Test
	public final void whenMotorIsHex_thenSlowDownToEncoderTicksRunningGeneric11() {


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

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);

	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number.
	 */
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunning01() {


		final int                tickNumberAtStartOfPeriod = 100;
		final int                tickNumberCurrent         = 1000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when not enough ticks have passed to start the ramp.
	 */
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunning05() {

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
		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when enough ticks have passed to start the ramp but not
	 * enough have passed to finish the required movement (negative numbers)
	 */
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunning08() {

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
		boolean actual = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(actual);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number (negative numbers)
	 */
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunning11() {


		final int                tickNumberAtStartOfPeriod = -60;
		final int                tickNumberCurrent         = -900;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 13.44 ticks per cycle, we need 134.4 ticks to ramp
		 * Period goes from tick number -60 to tick number 940
		 * Therefore the current tick number (-900) is out of range.
		 *
		 */

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunningGeneric01() {

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

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	@Override
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunningGeneric02() {

		final int                tickNumberAtStartOfPeriod = 60;
		final int                tickNumberCurrent         = 61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunningGeneric03() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 11000;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunningGeneric04() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = 123;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(100);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunningGeneric05() {

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


		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunningGeneric07() {

		final int                tickNumberAtStartOfPeriod = 0;
		final int                tickNumberCurrent         = -59;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);
		final PowerVector        powerAtStart              = new PowerVector(0.5);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);

	}

	@Override
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunningGeneric09() {

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

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);
	}

	@Override
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunningGeneric10() {

		final int                tickNumberAtStartOfPeriod = -60;
		final int                tickNumberCurrent         = -61;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(10000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number (negative numbers)
	 */
	@Test
	public final void whenMotorIsTorque_thenSlowDownToEncoderTicksRunningGeneric11() {


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

		boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		assertFalse(result);

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
	public final void whenSlowingDownAndMotorIsRev_thenTicsPerCycleIsCorrect() {
		MotorRevHDCoreHex motorRevHDCoreHex = new MotorRevHDCoreHex();
		SlowDown          slowdown          = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);

		double ticksPerCycle = slowdown.getTicksPerCycle().doubleValue();
		assertEquals(13.44, ticksPerCycle, 0.001);
	}

	@Test
	public final void whenSlowingDownAndMotorIsTorque_thenTicsPerCycleIsCorrect() {
		MotorTorqueNADO motorTorqueNADO = new MotorTorqueNADO();
		SlowDown        slowdown        = new SlowDown();
		slowdown.setMotor(motorTorqueNADO);

		double ticksPerCycle = slowdown.getTicksPerCycle().doubleValue();
		assertEquals(13.44, ticksPerCycle, 0.001);
	}

}