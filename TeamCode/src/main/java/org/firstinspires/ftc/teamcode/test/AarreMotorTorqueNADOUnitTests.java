package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.src.AarreMotor;
import org.firstinspires.ftc.teamcode.src.AarreMotorTorqueNADO;
import org.firstinspires.ftc.teamcode.src.AarrePositiveInteger;
import org.firstinspires.ftc.teamcode.src.AarrePowerVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor TorqueNADO Unit Tests", group = "Aarre")
@Disabled
public class AarreMotorTorqueNADOUnitTests extends AarreMotorUnitTests implements AarreConcreteMotorUnitTestsInterface {


	private AarreMotor motor;


	@BeforeEach
	public final void testConstructor() {
		motor = AarreMotorTorqueNADO.createAarreMotorTorqueNADO(this, "left");
	}

	@Test
	public final void testGetTicksPerCycle01() {
		double ticksPerCycle = motor.getTicksPerCycle();
		assertEquals(120.0, ticksPerCycle);
	}

	@Test
	public final void testGetTicksPerMillisecond01() {
		double ticksPerMillisecond = motor.getTicksPerMillisecond();
		assertEquals(2.4, ticksPerMillisecond);
	}

	@Test
	public final void testGetTicksPerSecond01() {
		double ticksPerSecond = motor.getTicksPerSecond();
		assertEquals(2400.0, ticksPerSecond);
	}

	@Test
	public final void testGetTicksPerMinute01() {
		double ticksPerMinute = motor.getTicksPerMinute();
		assertEquals(144000.0, ticksPerMinute);
	}

	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown01() {

		final AarrePowerVector powerAtStart = new AarrePowerVector( 1.0);
		final AarrePowerVector powerAtEnd = new AarrePowerVector( 0.0);

		final int                  tickNumberAtStartOfPeriod = 0;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(2000);

		final double actual = motor.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                         numberOfTicksInPeriod,
		                                                         powerAtStart, powerAtEnd);

		assertEquals(800, actual);
	}

	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric05() {

		final int                  tickNumberAtStartOfPeriod = 60;
		final int                  tickNumberCurrent         = 61;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

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


		final boolean result = motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
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
		AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		int                        tickNumberAtStartOfPeriod = 0;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(10000);

		/*
		 * There are 120 ticks in a cycle, so the ramp should be 1200 ticks
		 */
		final double actual = motor.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		/*
		 * Subtracting 1200 ticks from the total of 10000 should give us 8800 ticks
		 */
		assertEquals(8800, actual);
	}


	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when the motor is not close
	 * enough to
	 * the target tick number.
	 */
	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown04() {

		final int                  tickNumberAtStartOfPeriod = 60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(10000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		double result = motor.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(8860, result);
	}

	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown12() {

		final int                  tickNumberAtStartOfPeriod = 60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		AarrePowerVector powerAtStart = new AarrePowerVector( 0.5);
		AarrePowerVector powerAtEnd = new AarrePowerVector( 0.0);

		double result = motor.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
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
		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		final int                  tickNumberAtStartOfPeriod = 0;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);


		final double actual = motor.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                         numberOfTicksInPeriod,
		                                                         powerAtStart, powerAtEnd);

		assertEquals(-480, actual);
	}


	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown08() {

		final int                  tickNumberAtStartOfPeriod = 0;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		/*
		 * Power change requires 5 cycles
		 * Each cycle is 120 ticks
		 * 120 * 5 = 600
		 * We need to start ramp down at tick 120 - 600 ticks = -480 ticks
		 */
		final double result = motor.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(-480, result);
	}



	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the motor is close enough to the
	 * target tick number (negative numbers)
	 */
	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown11() {


		final int                  tickNumberAtStartOfPeriod = -60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(1000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need -1200 ticks
		 * Tick number at end of period = -1060
		 * Would need to start ramp at -1060 + 1200 tics = -260 ticks
		 */

		double result = motor.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(-260, result);
	}


	@Test
	@Override
	public final void whenThereAreEnoughTicks_thenSlowDownStartsOnTime() {

		final int                  tickNumberAtStartOfPeriod = -60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(10000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need 1200 ticks
		 * Tick number at start of period = -60
		 * Tick number at end of period = 9940
		 * Would need to start ramp at 9940 - 1200 ticks = 8740 ticks
		 */

		double result = motor.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(8740, result);
	}


	@Test
	@Override
	public final void whenThereAreNotEnoughTicks_thenSlowDownStartsTooEarly() {

		final int                  tickNumberAtStartOfPeriod = -60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(1000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need 1200 ticks
		 * Tick number at start of period = -60
		 * Tick number at end of period = 940
		 * Would need to start ramp at 940 - 1200 ticks = -260 ticks
		 */

		double result = motor.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(-260, result);
	}


	@Override
	@Test
	public final void testGetNumberOfCycles01() {

		int              ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = motor.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(10, numCycles);
	}

	@Override
	@Test
	public final void testGetNumberOfCycles02() {

		int              ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(0.1);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = motor.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(1, numCycles);
	}

	@Override
	@Test
	public final void testGetNumberOfCycles03() {

		int              ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(-0.1);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = motor.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(1, numCycles);
	}

	@Override
	@Test
	public final void testGetNumberOfCycles04() {

		int              ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(-1.0);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = motor.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(10, numCycles);
	}


	@Test
	public final void whenSlowDownNotStarted_thenReturnFalse() {

		final int                  tickNumberAtStartOfPeriod = 60;
		final int                  tickNumberCurrent         = 114;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

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
		final boolean result = motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}


	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric01() {

		final int                  tickNumberAtStartOfPeriod = 100;
		final int                  tickNumberCurrent         = 1000;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(1000);

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

		final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		boolean result = motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric02() {

		final int                  tickNumberAtStartOfPeriod = 60;
		final int                  tickNumberCurrent         = 61;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(10000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		boolean result = motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric03() {

		final int                  tickNumberAtStartOfPeriod = 0;
		final int                  tickNumberCurrent         = 11000;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(10000);

		final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		final boolean result = motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric04() {

		final int                  tickNumberAtStartOfPeriod = 0;
		final int                  tickNumberCurrent         = 123;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(100);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(0.5);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		final boolean result = motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}


	@Override
	@Test
	public final void whenTickNumberInRange_thenSlowDownIsRunning() {

		final int                  tickNumberAtStartOfPeriod = 0;
		final int                  tickNumberCurrent         = 59;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(0.5);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 *  Period runs from 0 to 120.
		 *  Need 5 cycles.
		 *  At 120 ticks per cycle, that is 600 ticks.
		 *  Last tick is number 120
		 *  Ramp down would start at tick number 120-600 = -480 and still be going
		 */
		final boolean result = motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}


	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric07() {

		final int                  tickNumberAtStartOfPeriod = 0;
		final int                  tickNumberCurrent         = -59;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(0.5);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 *  The period runs from tick number 0 to tick number 120.
		 *  We are at tick number -59.
		 *  This should never happen.
		 */

		assertThrows(IllegalArgumentException.class, () -> {
			final boolean result = motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
					numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		});
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

		final int                  tickNumberAtStartOfPeriod = 0;
		final int                  tickNumberCurrent         = -11000;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(10000);

		final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		assertThrows(IllegalArgumentException.class, () -> {
			motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		});

	}


	@Override
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric10() {

		final int                  tickNumberAtStartOfPeriod = -60;
		final int                  tickNumberCurrent         = -61;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(10000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		assertThrows(IllegalArgumentException.class, () -> {
			motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		});

	}


	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the motor is close enough to the target tick number
	 * (negative numbers)
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunningGeneric11() {


		final int                  tickNumberAtStartOfPeriod = -60;
		final int                  tickNumberCurrent         = -900;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(1000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

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

		assertThrows(IllegalArgumentException.class, () -> {
			motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		});

	}


	@Override
	@Test
	public final void whenTickNumberOutsidePeriod_thenExceptionThrown() {

		final int                  tickNumberAtStartOfPeriod = 0;
		final int                  tickNumberCurrent         = -61;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		/*
		 * The period runs from tick 0 to tick 120.
		 * We need 5 cycles
		 *
		 * The current tickNumber is -61.
		 */

		assertThrows(IllegalArgumentException.class, () -> {
			motor.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		});

	}

}
