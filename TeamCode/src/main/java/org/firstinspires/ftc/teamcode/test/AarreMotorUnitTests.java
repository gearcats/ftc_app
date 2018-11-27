package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.src.AarreMotor;
import org.firstinspires.ftc.teamcode.src.AarrePowerMagnitude;
import org.firstinspires.ftc.teamcode.src.AarrePowerVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor Unit Tests", group = "Aarre")
@Disabled
public abstract class AarreMotorUnitTests extends LinearOpMode {

	private AarreMotor motor;

	@BeforeEach
	public void AarreMotionUnitTests() {
		motor = new AarreMotor(this, "left");
	}

	@Test
	public final void testGetNumberOfCycles01() {

		int              ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(1.0);
		AarrePowerVector           proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = motor.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(10, numCycles);
	}

	@Test
	public final void testGetNumberOfCycles02() {

		int    ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(0.1);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = motor.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(1, numCycles);
	}

	@Test
	public final void testGetNumberOfCycles03() {

		int    ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(-0.1);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = motor.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(1, numCycles);
	}

	@Test
	public final void testGetNumberOfCycles04() {

		int    ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(-1.0);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = motor.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(10, numCycles);
	}


	@Test
	public final void testGetProportionPowerNew01() {
		AarrePowerVector powerVectorCurrent = new AarrePowerVector(1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(0.0);
		AarrePowerMagnitude powerIncrementMagnitude = new AarrePowerMagnitude(0.1);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(powerVectorCurrent,
		                                                              powerVectorRequested,
		                                                              powerIncrementMagnitude);
		assertEquals(0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetPowerVectorNew02() {
		AarrePowerVector powerVectorCurrent = new AarrePowerVector(-1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(0.0);
		AarrePowerMagnitude powerIncrementMagnitude = new AarrePowerMagnitude(0.1);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(powerVectorCurrent,
		                                                              powerVectorRequested,
		                                                              powerIncrementMagnitude);
		assertEquals(-0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetPowerVectorNew03() {
		AarrePowerVector powerVectorCurrent = new AarrePowerVector(0.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(1.0);
		AarrePowerMagnitude powerIncrementMagnitude = new AarrePowerMagnitude(0.1);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(powerVectorCurrent,
		                                                              powerVectorRequested,
		                                                              powerIncrementMagnitude);
		assertEquals(0.1, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetPowerVectorNew04() {
		AarrePowerVector powerVectorCurrent = new AarrePowerVector(0.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(-1.0);
		AarrePowerMagnitude powerIncrementMagnitude = new AarrePowerMagnitude(0.1);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(powerVectorCurrent,
		                                                              powerVectorRequested,
		                                                              powerIncrementMagnitude);
		assertEquals(-0.1, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetPowerVectorNew05() {
		AarrePowerVector powerVectorCurrent = new AarrePowerVector(1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(-1.0);
		AarrePowerMagnitude powerIncrementMagnitude = new AarrePowerMagnitude(0.1);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(powerVectorCurrent,
		                                                              powerVectorRequested,
		                                                              powerIncrementMagnitude);
		assertEquals(0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetPowerVectorNew06() {
		AarrePowerVector powerVectorCurrent = new AarrePowerVector(-1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(1.0);
		AarrePowerMagnitude powerIncrementMagnitude = new AarrePowerMagnitude(0.1);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(powerVectorCurrent,
		                                                              powerVectorRequested,
		                                                              powerIncrementMagnitude);
		assertEquals(-0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetPowerVectorNew07() {
		AarrePowerVector powerVectorCurrent = new AarrePowerVector(-1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(-1.0);
		AarrePowerMagnitude powerIncrementMagnitude = new AarrePowerMagnitude(0.1);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(powerVectorCurrent,
		                                                              powerVectorRequested,
		                                                              powerIncrementMagnitude);
		assertEquals(-1.0, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetPowerVectorNew08() {
		AarrePowerVector powerVectorCurrent = new AarrePowerVector(0.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(0.0);
		AarrePowerMagnitude powerIncrementMagnitude = new AarrePowerMagnitude(0.1);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(powerVectorCurrent,
		                                                              powerVectorRequested,
		                                                              powerIncrementMagnitude);
		assertEquals(0.0, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetPowerVectorNew09() {
		AarrePowerVector powerVectorCurrent = new AarrePowerVector(1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(1.0);
		AarrePowerMagnitude powerIncrementMagnitude = new AarrePowerMagnitude(0.1);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(powerVectorCurrent,
		                                                              powerVectorRequested,
		                                                              powerIncrementMagnitude);
		assertEquals(1.0, proportionPowerNew, "Wrong proportion power");
	}

	/**
	 * Test that the new proportion of power increases correctly from 0 up.
	 */
	@Test
	public final void testGetPowerVectorNew10() {

		/*
		 * The current power is ...
		 */
		AarrePowerVector proportionPowerCurrent = new AarrePowerVector(0.0);

		/*
		 * We are ramping to power of ...
		 */
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(1.0);

		/*
		 * The power can increment by ...
		 */
		AarrePowerMagnitude powerIncrementAbsolute = new AarrePowerMagnitude(0.1);

		/*
		 * So the correct power is...
		 */
		double correctValue = 0.1;

		AarrePowerVector newPowerVector = motor.getPowerVectorNew(proportionPowerCurrent,
		                                          proportionPowerRequested, powerIncrementAbsolute);

		assertEquals(newPowerVector.asDouble(), correctValue);

	}


	/**
	 * Test that the new proportion of power does not increase beyond 1.0
	 */
	@Test
	public final void testGetPowerVectorNew11() {

		/*
		 * The current power is ...
		 */
		AarrePowerVector proportionPowerCurrent = new AarrePowerVector(1.0);

		/*
		 * We are ramping to power of ...
		 */
		AarrePowerVector proportionPowerRequested = new AarrePowerVector( 1.0);

		/*
		 * The power can increment by ...
		 */
		AarrePowerMagnitude powerIncrementAbsolute = new AarrePowerMagnitude(0.1);

		/*
		 * So the correct power is...
		 */
		double correctValue = 1.0;

		AarrePowerVector newProportion = motor.getPowerVectorNew(proportionPowerCurrent,
		                                          proportionPowerRequested, powerIncrementAbsolute);


		assertEquals(newProportion.asDouble(), correctValue);

	}

	/**
	 * Test calculating when to start a ramp down
	 */
	public abstract void testGetTickNumberToStartRampDown01();

	public abstract void testGetTickNumberToStartRampDown02();

	public abstract void testGetTickNumberToStartRampDown03();

	public abstract void testGetTickNumberToStartRampDown04();

	public abstract void testGetTicksPerCycle01();

	public abstract void testGetTicksPerMinute01();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when the motor is close enough to the
	 * target tick number.
	 */
	@Test
	public abstract void testIsRampDownToEncoderTicksRunning01();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when the motor is not close
	 * enough to the target tick number.
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning02() {

		final int    tickNumberAtStartOfPeriod = 60;
		final int    tickNumberCurrent         = 61;
		final int    numberOfTicksInPeriod     = 10000;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                       tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertEquals(false, result);
	}

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when enough ticks have passed
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning03() {

		/*
		 * The current tick number exceed the total number of ticks we were supposed to
		 * move, so ramping down should stop.
		 *
		 * This is a made up example.
		 */

		final int tickNumberAtStartOfPeriod = 0;
		final int tickNumberCurrent         = 11000;
		final int numberOfTicksInPeriod     = 10000;

		final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when enough ticks have passed to
	 * finish the required movement.
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning04() {

		/*
		 * The current tick number exceeds the total number of ticks we were supposed to
		 * move, so ramping down should stop.
		 */
		final int    tickNumberAtStartOfPeriod = 0;
		final int    tickNumberCurrent         = 123;
		final int    numberOfTicksInPeriod     = 100;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(0.5);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                             tickNumberCurrent,
		                                                             numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Test
	public abstract void testIsRampDownToEncoderTicksRunning05();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when not enough ticks have passed to
	 * start the required movement.
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning06() {

		final int    tickNumberAtStartOfPeriod = 0;
		final int    tickNumberCurrent         = 59;
		final int    numberOfTicksInPeriod     = 120;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(0.5);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                             tickNumberCurrent,
		                                                             numberOfTicksInPeriod,
		                                                             powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when not enough ticks have passed to
	 * start the required movement (negative numbers).
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning07() {

		final int    tickNumberAtStartOfPeriod = 0;
		final int    tickNumberCurrent         = -59;
		final int    numberOfTicksInPeriod     = 120;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(0.5);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                             tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	public abstract void testIsRampDownToEncoderTicksRunning08();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when enough ticks have passed
	 * (negative numbers)
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning09() {

		/*
		 * The current tick number exceed the total number of ticks we were supposed to
		 * move, so ramping down should stop.
		 *
		 * This is a made up example.
		 */

		final int tickNumberAtStartOfPeriod = 0;
		final int tickNumberCurrent         = -11000;
		final int numberOfTicksInPeriod     = 10000;

		final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                             tickNumberCurrent,
		                                                             numberOfTicksInPeriod,
		                                                             powerAtStart, powerAtEnd);

		assertFalse(result);
	}


	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when the motor is not close
	 * enough to
	 * the target tick number (negative numbers)
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning10() {

		final int    tickNumberAtStartOfPeriod = -60;
		final int    tickNumberCurrent         = -61;
		final int    numberOfTicksInPeriod     = 10000;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	public abstract void testIsRampDownToEncoderTicksRunning11();

	@Test
	public final void testIsRampUpToEncoderTicksDone01() {

		/**
		 * ticksMoved is less than ticksMaximum, so no reason to stop. (We haven't moved far enough
		 * yet).
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 0;

		/**
		 * Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 0.0;

		/**
		 * Power delta is not within the tolerance, so no reason to stop. (We haven't ramped
		 * enough yet.)
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertFalse(result);
	}

	@Test
	public final void testIsRampUpToEncoderTicksDone02() {

		/**
		 * We have moved farther than we intended, so it is time to stop.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 1441;

		/**
		 * Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 0.0;

		/**
		 * Power delta is not within the tolerance, so no reason to stop. (We haven't ramped
		 * enough yet.)
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertTrue(result);
	}

	@Test
	public final void testIsRampUpToEncoderTicksDone03() {

		/**
		 * We have not moved enough yet, so continue.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 1439;

		/**
		 * Seconds running is more than timeout, so stop.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 6.0;

		/**
		 * Power delta is not within the tolerance, so no reason to stop. (We haven't ramped
		 * enough yet.)
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertTrue(result);
	}

	/**
	 * Test that having reached the maximum power (having changes to power be within tolerance)
	 * does
	 * not cause the loop to stop - it should continue until the number of ticks is reached.
	 */
	@Test
	public final void testIsRampUpToEncoderTicksDone04() {

		/*
		 * We have not moved enough yet, so continue.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 1439;

		/*
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		/*
		 * Power delta is within tolerance, so continue.
		 * (Continue moving, although power should not continue to increase.)
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.001);

		/*
		 * Current power is within reason, so no reason to stop.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertFalse(result);

	}

	/**
	 * Test that moving exactly the right amount causes the check to stop.
	 */
	@Test
	public final void testIsRampUpToEncoderTicksDone05() {

		/**
		 * We have moved exactly the right amount, so stop.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 1440;

		/**
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		/**
		 * Power delta is greater than tolerance, so continue.
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertTrue(result);
	}

	/**
	 * Test that reaching the target power by itself does not cause the check to stop.
	 */
	@Test
	public final void testIsRampUpToEncoderTicksDone06() {

		/**
		 * We have not moved the right amount, so continue.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 14;

		/**
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		/**
		 * Power delta is greater than tolerance, so continue.
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/**
		 * Current power is maxed out, but still no reason to stop, so continue.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(1.0);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertFalse(result);
	}


	/**
	 * Test that a negative power delta by itself does not cause the check to stop.
	 */
	@Test
	public final void testIsRampUpToEncoderTicksDone07() {

		/**
		 * We have not moved enough, so continue.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 144;

		/**
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		/**
		 * Power delta is negative, but absolute power delta is greater than tolerance, so
		 * continue.
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(-0.1);

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertFalse(result);
	}

	/**
	 * Test that a negative power by itself does not cause the check to stop.
	 */
	@Test
	public final void testIsRampUpToEncoderTicksDone08() {

		/**
		 * We have not moved enough, so continue.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 190;

		/**
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		/**
		 * Power delta is greater than tolerance, so continue.
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/**
		 * Current power is negative but within reason, so continue.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(-0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                  secondsRunning, ticksMoved, powerDelta,
		                                                  powerCurrent);

		assertFalse(result);
	}

	/**
	 * Test that a negative number of ticks does not cause the check to stop.
	 */
	@Test
	public final void testIsRampUpToEncoderTicksDone09() {

		/**
		 * We have not moved enough, so continue.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = -190;

		/**
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		/**
		 * Power delta is greater than tolerance, so continue.
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/**
		 * Current power is negative but within reason, so continue.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(-0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                  secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertFalse(result);
	}

	/**
	 * Test that a negative tick maximum does not cause the loop to stop
	 */
	@Test
	public final void testIsRampUpToEncoderTicksDone10() {

		/**
		 * We have not moved enough, so continue.
		 */
		int ticksMaximum = -5040;
		int ticksMoved   = 0;

		/**
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		/**
		 * Power delta is greater than tolerance, so continue.
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/**
		 * Current power is negative but within reason, so continue.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(-0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertFalse(result);
	}


	/**
	 * Test that a negative tick maximum does cause the loop to stop after it is exceeded
	 */
	@Test
	public final void testIsRampUpToEncoderTicksDone11() {

		/*
		 * We have moved enough (albeit in a negative direction), so stop
		 */
		int ticksMaximum = -5040;
		int ticksMoved   = -5041;

		/*
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		/*
		 * Power delta is greater than tolerance, so continue.
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		 * Current power is negative but within reason, so continue.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(-0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertTrue(result);
	}


	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {
	}
}
