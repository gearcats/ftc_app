package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.src.AarreMotor;
import org.firstinspires.ftc.teamcode.src.AarreMotorTorqueNADO;
import org.firstinspires.ftc.teamcode.src.AarrePowerVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor TorqueNADO Unit Tests", group = "Aarre")
@Disabled
public class AarreMotorTorqueNADOUnitTests extends AarreMotorUnitTests {

	/**
	 * Test AarreRobot
	 * <p>
	 * We can't break this up into different methods because the tests depend on overriding the FTC
	 * runOpMode() method. Properties inherited from LinearOpMode include: - hardwareMap -
	 * telemetry
	 */

	AarreMotor motor = null;

	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when the motor is close enough to the
	 * target tick number.
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning01() {


		final int              tickNumberAtStartOfPeriod = 100;
		final int              tickNumberCurrent         = 1000;
		final int              numberOfTicksInPeriod     = 1000;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                       tickNumberCurrent,
		                                                       numberOfTicksInPeriod,
		                                                       powerAtStart, powerAtEnd);

		assertTrue(result);
	}

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

	/**
	 * Test calculating when to start a ramp down in a 10000-tick period.
	 */
	@Test
	public final void testGetTickNumberToStartRampDown02() {

		/*
		 * A power difference of 1.0 requires 10 cycles of ramp.
		 */
		AarrePowerVector powerAtStart = new AarrePowerVector( 1.0);
		AarrePowerVector powerAtEnd = new AarrePowerVector( 0.0);

		final int tickNumberAtStartOfPeriod = 0;
		final int numberOfTicksInPeriod     = 10000;

		/*
		 * There are 120 ticks in a cycle, so the ramp should be 1200 ticks
		 */
		final double actual = motor.getTickNumberToStartRampDown(tickNumberAtStartOfPeriod,
		                                                         numberOfTicksInPeriod,
		                                                         powerAtStart, powerAtEnd);

		/*
		 * Subtracting 1200 ticks from the total of 10000 should give us 8800 ticks
		 */
		assertEquals(8800, actual);
	}

	/**
	 * Test calculating when to start a ramp down in a 2000-tick period.
	 */
	@Test
	public final void testGetTickNumberToStartRampDown01() {

		final AarrePowerVector powerAtStart = new AarrePowerVector( 1.0);
		final AarrePowerVector powerAtEnd = new AarrePowerVector( 0.0);

		final int tickNumberAtStartOfPeriod = 0;
		final int numberOfTicksInPeriod     = 2000;

		final double actual = motor.getTickNumberToStartRampDown(tickNumberAtStartOfPeriod,
		                                                         numberOfTicksInPeriod,
		                                                         powerAtStart, powerAtEnd);

		assertEquals(800, actual);
	}

	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when enough ticks have passed to
	 * start
	 * the ramp but not enough have passed to finish the required movement.
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning05() {

		final int tickNumberAtStartOfPeriod = 60;
		final int tickNumberCurrent         = 61;
		final int numberOfTicksInPeriod     = 120;

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		/*
		 * 5 cycles needed
		 * 120 ticks per cycle
		 * 600 ticks needed
		 * 120-600 = -480
		 * Ramp down should have started a long time ago!
		 */

		final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                             tickNumberCurrent,
		                                                             numberOfTicksInPeriod,
		                                                             powerAtStart, powerAtEnd);

		assertTrue(result);
	}


	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when the motor is not close
	 * enough to
	 * the target tick number.
	 */
	@Test
	public final void testGetTickNumberToStartRampDown04() {

		final int    tickNumberAtStartOfPeriod = 60;
		final int    numberOfTicksInPeriod     = 10000;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		double result = motor.getTickNumberToStartRampDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(8860, result);
	}

	@Test
	public final void testGetTickNumberToStartRampDown12() {

		final int tickNumberAtStartOfPeriod = 60;
		final int tickNumberCurrent         = 114;
		final int numberOfTicksInPeriod     = 120;

		AarrePowerVector powerAtStart = new AarrePowerVector( 0.5);
		AarrePowerVector powerAtEnd = new AarrePowerVector( 0.0);

		double result = motor.getTickNumberToStartRampDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(-420, result);
	}

	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when enough ticks have passed to
	 * start
	 * the ramp but not enough have passed to finish the required movement.
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning12() {

		final int tickNumberAtStartOfPeriod = 60;
		final int tickNumberCurrent         = 114;
		final int numberOfTicksInPeriod     = 120;

		AarrePowerVector powerAtStart = new AarrePowerVector( 0.5);
		AarrePowerVector powerAtEnd = new AarrePowerVector( 0.0);

		/*
		 * Need 5 test cycles
		 * Ticks per power cycle is 120
		 * = need 600 ticks for the ramp
		 * But tick at end of period = 180
		 * Not enough time for ramp but it should have started at -480 ticks
		 */
		final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                             tickNumberCurrent,
		                                                             numberOfTicksInPeriod,
		                                                             powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	/**
	 * Test calculating when to start a ramp down in a 10000-tick period.
	 */
	@Test
	public final void testGetTickNumberToStartRampDown03() {

		/*
		 * A power difference of 1.0 requires 5 cycles of ramp.
		 * There are 120 ticks in a cycle.
		 * The ramp must be 600 ticks long
		 * 600 - 120 = -480
		 */
		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		final int tickNumberAtStartOfPeriod = 0;
		final int numberOfTicksInPeriod     = 120;


		final double actual = motor.getTickNumberToStartRampDown(tickNumberAtStartOfPeriod,
		                                                         numberOfTicksInPeriod,
		                                                         powerAtStart, powerAtEnd);

		assertEquals(-480, actual);
	}

	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when the motor is close enough to the
	 * target tick number (negative numbers)
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning11() {


		final int    tickNumberAtStartOfPeriod = -60;
		final int    tickNumberCurrent         = -900;
		final int    numberOfTicksInPeriod     = 1000;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need 1200 ticks
		 * Tick number at end of period = -1060
		 * Would need to start ramp at -1060 + 1200 tics = 140 ticks
		 * But we are at a negative tick and moving more negative, so we will never get to 140
		 */

		boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                       tickNumberCurrent,
		                                                       numberOfTicksInPeriod,
		                                                       powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when the motor is close enough to the
	 * target tick number (negative numbers)
	 */
	@Test
	public final void testGetTickNumberToStartRampDown11() {


		final int    tickNumberAtStartOfPeriod = -60;
		final int    numberOfTicksInPeriod     = 1000;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need -1200 ticks
		 * Tick number at end of period = -1060
		 * Would need to start ramp at -1060 + 1200 tics = -260 ticks
		 */

		double result = motor.getTickNumberToStartRampDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(-260, result);
	}

	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when the motor is close enough to the
	 * target tick number (negative numbers)
	 */
	@Test
	public final void testGetTickNumberToStartRampDown13() {


		final int    tickNumberAtStartOfPeriod = -60;
		final int    numberOfTicksInPeriod     = -1000;
		final AarrePowerVector powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 120 ticks per cycle
		 * Need -1200 ticks
		 * Tick number at end of period = -1060
		 * Would need to start ramp at -1060 - 1200 tics = -2260 ticks
		 */

		double result = motor.getTickNumberToStartRampDown(tickNumberAtStartOfPeriod,
		                                                   numberOfTicksInPeriod, powerAtStart,
		                                                   powerAtEnd);

		assertEquals(-2260, result);
	}


	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when enough ticks have passed to
	 * start
	 * the ramp but not enough have passed to finish the required movement (negative numbers)
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning08() {

		final int tickNumberAtStartOfPeriod = 0;
		final int tickNumberCurrent         = -61;
		final int numberOfTicksInPeriod     = 120;

		AarrePowerVector powerAtStart = new AarrePowerVector( 0.5);
		AarrePowerVector powerAtEnd = new AarrePowerVector( 0.0);

		final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
		                                                             tickNumberCurrent,
		                                                             numberOfTicksInPeriod,
		                                                             powerAtStart, powerAtEnd);

		assertTrue(result);
	}

}
