package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.aarre.src.*;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor TorqueNADO Unit Tests", group = "Aarre")
@Disabled
public class MotorTorqueNADOUnitTests extends MotorUnitTests implements ConcreteMotorUnitTestsInterface {

	MotorTorqueNADO torqueNADOMotor = new MotorTorqueNADO(this, "left");

	Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Override
	public MotorTorqueNADO getMotor() {
		return torqueNADOMotor;
	}

	@Test
	public final void testGetTicksPerCycle01() {
		NonNegativeDouble ticksPerCycle = getMotor().getTicksPerCycle();
		assertEquals(120.0, ticksPerCycle.doubleValue());
	}

	@Test
	public final void testGetTicksPerMillisecond01() {
		NonNegativeDouble ticksPerMillisecond = getMotor().getTicksPerMillisecond();
		assertEquals(2.4, ticksPerMillisecond.doubleValue());
	}

	@Test
	public final void testGetTicksPerSecond01() {
		NonNegativeDouble ticksPerSecond = getMotor().getTicksPerSecond();
		assertEquals(2400.0, ticksPerSecond.doubleValue());
	}

	@Test
	public final void testGetTicksPerMinute01() {
		NonNegativeDouble ticksPerMinute = getMotor().getTicksPerMinute();
		assertEquals(144000.0, ticksPerMinute.doubleValue());
	}

	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown01() {

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		final int             tickNumberAtStartOfPeriod = 0;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(2000);

		final double actual = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                         numberOfTicksInPeriod,
		                                                         powerAtStart, powerAtEnd);

		assertEquals(800, actual);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric05() {

		final int             tickNumberAtStartOfPeriod = 60;
		final int             tickNumberCurrent         = 61;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(120);

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
		 *  180-67=113 not be going yet.
		 *
		 */


		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	/**
	 * Test calculating when to start a ramp down in a 10000-tick period.
	 */
	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown02() {

		/*
		 * A power difference of 1.0 requires 10 cycles of ramp.
		 */
		PowerVector powerAtStart = new PowerVector(1.0);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		int                   tickNumberAtStartOfPeriod = 0;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(10000);

		/*
		 * There are 120 ticks in a cycle, so the ramp should be 1200 ticks
		 */
		final double actual = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		/*
		 * Subtracting 1200 ticks from the total of 10000 should give us 8800 ticks
		 */
		assertEquals(8800, actual);
	}


	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when the revHDCoreHexMotor is not close
	 * enough to
	 * the target tick number.
	 */
	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown04() {

		final int             tickNumberAtStartOfPeriod = 60;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(10000);
		final PowerVector     powerAtStart              = new PowerVector(1.0);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(8860, result);
	}

	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown12() {

		final int             tickNumberAtStartOfPeriod = 60;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(-420, result);
	}

	/**
	 * Test calculating when to start a ramp down in a 10000-tick period.
	 */
	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown03() {

		/*
		 * A power difference of 1.0 requires 5 cycles of ramp.
		 * There are 120 ticks in a cycle.
		 * The ramp must be 600 ticks long
		 * 600 - 120 = -480
		 */
		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		final int             tickNumberAtStartOfPeriod = 0;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(120);


		final double actual = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                         numberOfTicksInPeriod,
		                                                         powerAtStart, powerAtEnd);

		assertEquals(-480, actual);
	}


	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown08() {

		final int             tickNumberAtStartOfPeriod = 0;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(120);

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		/*
		 * Power change requires 5 cycles
		 * Each cycle is 120 ticks
		 * 120 * 5 = 600
		 * We need to start ramp down at tick 120 - 600 ticks = -480 ticks
		 */
		final double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(-480, result);
	}



	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the
	 * target tick number (negative numbers)
	 */
	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown11() {


		final int             tickNumberAtStartOfPeriod = -60;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(1000);
		final PowerVector     powerAtStart              = new PowerVector(1.0);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need -1200 ticks
		 * Tick number at end of period = -1060
		 * Would need to start ramp at -1060 + 1200 tics = -260 ticks
		 */

		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(-260, result);
	}


	@Test
	@Override
	public final void whenThereAreEnoughTicks_thenSlowDownStartsOnTime() {

		final int             tickNumberAtStartOfPeriod = -60;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(10000);
		final PowerVector     powerAtStart              = new PowerVector(1.0);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need 1200 ticks
		 * Tick number at start of period = -60
		 * Tick number at end of period = 9940
		 * Would need to start ramp at 9940 - 1200 ticks = 8740 ticks
		 */

		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(8740, result);
	}


	@Test
	@Override
	public final void whenThereAreNotEnoughTicks_thenSlowDownStartsTooEarly() {

		final int             tickNumberAtStartOfPeriod = -60;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(1000);
		final PowerVector     powerAtStart              = new PowerVector(1.0);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need 1200 ticks
		 * Tick number at start of period = -60
		 * Tick number at end of period = 940
		 * Would need to start ramp at 940 - 1200 ticks = -260 ticks
		 */

		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(-260, result);
	}


	@Override
	@Test
	public final void testGetNumberOfCycles01() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(1.0);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		NonNegativeInteger numCycles = getMotor().getNumberOfCycles(ticksToMove, currentPower,
				proportionPowerRequested);

		assertEquals(10, numCycles.intValue());
	}

	@Override
	@Test
	public final void testGetNumberOfCycles02() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(0.1);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		NonNegativeInteger numCycles = getMotor().getNumberOfCycles(ticksToMove, currentPower,
				proportionPowerRequested);

		assertEquals(1, numCycles.intValue());
	}

	@Override
	@Test
	public final void testGetNumberOfCycles03() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(-0.1);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		NonNegativeInteger numCycles = getMotor().getNumberOfCycles(ticksToMove, currentPower,
				proportionPowerRequested);

		assertEquals(1, numCycles.intValue());
	}

	@Override
	@Test
	public final void testGetNumberOfCycles04() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(-1.0);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		NonNegativeInteger numCycles = getMotor().getNumberOfCycles(ticksToMove, currentPower,
				proportionPowerRequested);

		assertEquals(10, numCycles.intValue());
	}


	@Test
	public final void whenSlowDownNotStarted_thenReturnFalse() {

		final int             tickNumberAtStartOfPeriod = 60;
		final int             tickNumberCurrent         = 114;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(120);

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
		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}


	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric01() {

		final int             tickNumberAtStartOfPeriod = 100;
		final int             tickNumberCurrent         = 1000;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(1000);

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

		boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric02() {

		final int             tickNumberAtStartOfPeriod = 60;
		final int             tickNumberCurrent         = 61;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(10000);
		final PowerVector     powerAtStart              = new PowerVector(1.0);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

		boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric03() {

		final int             tickNumberAtStartOfPeriod = 0;
		final int             tickNumberCurrent         = 11000;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(10000);

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric04() {

		final int             tickNumberAtStartOfPeriod = 0;
		final int             tickNumberCurrent         = 123;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(100);
		final PowerVector     powerAtStart              = new PowerVector(0.5);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}


	@Override
	@Test
	public final void whenTickNumberInRange_thenSlowDownIsRunning() {

		final int             tickNumberAtStartOfPeriod = 0;
		final int             tickNumberCurrent         = 59;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(120);
		final PowerVector     powerAtStart              = new PowerVector(0.5);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

		/*
		 *  Period runs from 0 to 120.
		 *  Need 5 cycles.
		 *  At 120 ticks per cycle, that is 600 ticks.
		 *  Last tick is number 120
		 *  Ramp down would start at tick number 120-600 = -480 and still be going
		 */
		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}


	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric07() {

		final int             tickNumberAtStartOfPeriod = 0;
		final int             tickNumberCurrent         = -59;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(120);
		final PowerVector     powerAtStart              = new PowerVector(0.5);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

		/*
		 *  The period runs from tick number 0 to tick number 120.
		 *  We are at tick number -59.
		 *
		 *  If that is really the case, then the slow down should be in progress.
		 *
		 */

		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}


	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric09() {

		/*
		 * The current tick number exceed the total number of ticks we were supposed to
		 * move, so ramping down should stop.
		 *
		 * This is a made up example.
		 */

		final int             tickNumberAtStartOfPeriod = 0;
		final int             tickNumberCurrent         = -11000;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(10000);

		final PowerVector powerAtStart = new PowerVector(1.0);
		final PowerVector powerAtEnd   = new PowerVector(0.0);

		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		assertFalse(result);

	}


	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric10() {

		final int             tickNumberAtStartOfPeriod = -60;
		final int             tickNumberCurrent         = -61;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(10000);
		final PowerVector     powerAtStart              = new PowerVector(1.0);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		assertFalse(result);

	}


	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number
	 * (negative numbers)
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric11() {


		final int             tickNumberAtStartOfPeriod = -60;
		final int             tickNumberCurrent         = -900;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(1000);
		final PowerVector     powerAtStart              = new PowerVector(1.0);
		final PowerVector     powerAtEnd                = new PowerVector(0.0);

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

		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		assertFalse(result);
	}


	@Override
	@Test
	public final void whenTickNumberOutsidePeriod_thenExceptionThrown() {

		// TODO: Rename this method
		final int             tickNumberAtStartOfPeriod = 0;
		final int             tickNumberCurrent         = -61;
		final PositiveInteger numberOfTicksInPeriod     = new PositiveInteger(120);

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

		boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);

		assertTrue(result);

	}

}
