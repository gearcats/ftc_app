package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.src.AarreMotor;
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
public class AarreMotorUnitTests extends LinearOpMode {

	/**
	 * Test AarreRobot
	 * <p>
	 * We can't break this up into different methods because the tests depend on overriding the FTC
	 * runOpMode() method. Properties inherited from LinearOpMode include: - hardwareMap -
	 * telemetry
	 */

	AarreMotor motor;

	@BeforeEach
	public final void testConstructor() {
		motor = new AarreMotor(this, "left");
	}

	@Test
	public final void testGetProportionNew01() {
		double proportionPowerNew = motor.getProportionPowerNew(1.0, 0.0, 0.1);
		assertEquals(0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew02() {
		double proportionPowerNew = motor.getProportionPowerNew(-1.0, 0.0, 0.1);
		assertEquals(-0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew03() {
		double proportionPowerNew = motor.getProportionPowerNew(0.0, 1.0, 0.1);
		assertEquals(0.1, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew04() {
		double proportionPowerNew = motor.getProportionPowerNew(0.0, -1.0, 0.1);
		assertEquals(-0.1, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew05() {
		double proportionPowerNew = motor.getProportionPowerNew(1.0, -1.0, 0.1);
		assertEquals(0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew06() {
		double proportionPowerNew = motor.getProportionPowerNew(-1.0, 1.0, 0.1);
		assertEquals(-0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew07() {
		double proportionPowerNew = motor.getProportionPowerNew(-1.0, -1.0, 0.1);
		assertEquals(-1.0, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew08() {
		double proportionPowerNew = motor.getProportionPowerNew(0.0, 0.0, 0.1);
		assertEquals(0.0, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew09() {
		double proportionPowerNew = motor.getProportionPowerNew(1.0, 1.0, 0.1);
		assertEquals(1.0, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testIsRampToEncoderTicksDone01() {

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
		double powerDelta = 0.1;

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		double powerCurrent = 0.6;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta,
		                                                powerCurrent);

		assertFalse(result);
	}

	@Test
	public final void testIsRampToEncoderTicksDone02() {

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
		double powerDelta = 0.1;

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		double powerCurrent = 0.6;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertTrue(result);
	}

	@Test
	public final void testIsRampToEncoderTicksDone03() {

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
		double powerDelta = 0.1;

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		double powerCurrent = 0.6;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertTrue(result);
	}

	@Test
	public final void testIsRampToEncoderTicksDone04() {

		/**
		 * We have not moved enough yet, so continue.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 1439;

		/**
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		/**
		 * Power delta is within tolerance, so stop.
		 */
		double powerDelta = 0.001;

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		double powerCurrent = 0.6;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta,
		                                                powerCurrent);

		assertTrue(result);
	}

	/**
	 * Test that moving exactly the right amount causes the check to stop.
	 */
	@Test
	public final void testIsRampToEncoderTicksDone05() {

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
		double powerDelta = 0.1;

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		double powerCurrent = 0.6;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta,
		                                                powerCurrent);

		assertTrue(result);
	}

	/**
	 * Test that reaching the target power by itself does not cause the check to stop.
	 */
	@Test
	public final void testIsRampToEncoderTicksDone06() {

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
		double powerDelta = 0.1;

		/**
		 * Current power is maxed out, but still no reason to stop, so continue.
		 */
		double powerCurrent = 1.0;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta,
		                                                powerCurrent);

		assertFalse(result);
	}


	/**
	 * Test that a negative power delta by itself does not cause the check to stop.
	 */
	@Test
	public final void testIsRampToEncoderTicksDone07() {

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
		double powerDelta = -0.1;

		/**
		 * Current power is within reason, so no reason to stop.
		 */
		double powerCurrent = 0.6;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta,
		                                                powerCurrent);

		assertFalse(result);
	}

	/**
	 * Test that a negative power by itself does not cause the check to stop.
	 */
	@Test
	public final void testIsRampToEncoderTicksDone08() {

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
		double powerDelta = 0.1;

		/**
		 * Current power is negative but within reason, so continue.
		 */
		double powerCurrent = -0.6;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta,
		                                                powerCurrent);

		assertFalse(result);
	}

	/**
	 * Test that a negative number of ticks does not cause the check to stop.
	 */
	@Test
	public final void testIsRampToEncoderTicksDone09() {

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
		double powerDelta = 0.1;

		/**
		 * Current power is negative but within reason, so continue.
		 */
		double powerCurrent = -0.6;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta,
		                                                powerCurrent);

		assertFalse(result);
	}

	/**
	 * Test that a negative tick maximum does not cause the loop to stop
	 */
	@Test
	public final void testIsRampToEncoderTicksDone10() {

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
		double powerDelta = 0.1;

		/**
		 * Current power is negative but within reason, so continue.
		 */
		double powerCurrent = -0.6;

		boolean result = motor.isRampToEncoderTicksDone(ticksMaximum, secondsTimeout,
		                                                secondsRunning, ticksMoved, powerDelta,
		                                                powerCurrent);

		assertFalse(result);
	}



	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {

	}

}
