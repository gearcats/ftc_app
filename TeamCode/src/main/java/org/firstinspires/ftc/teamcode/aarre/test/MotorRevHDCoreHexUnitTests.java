package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.aarre.src.AarreMotor;
import org.firstinspires.ftc.teamcode.aarre.src.AarreMotorRevHDCoreHex;
import org.firstinspires.ftc.teamcode.aarre.src.AarrePositiveInteger;
import org.firstinspires.ftc.teamcode.aarre.src.AarrePowerVector;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor Rev HD Core Hex Unit Tests", group = "Aarre")
@Disabled
public class MotorRevHDCoreHexUnitTests extends MotorUnitTests implements
                                                                         AarreConcreteMotorUnitTestsInterface {

	/**
	 * Test AarreRobot
	 * <p>
	 * We can't break this up into different methods because the tests depend on overriding the FTC runOpMode() method.
	 * Properties inherited from LinearOpMode include: - hardwareMap - telemetry
	 */

	AarreMotorRevHDCoreHex revHDCoreHexMotor = AarreMotorRevHDCoreHex.createAarreMotorRevHDCoreHex(this, "arm");
	Logger                 javaLog           = Logger.getLogger(this.getClass().getName());

	@Override
	AarreMotor getMotor() {
		return revHDCoreHexMotor;
	}

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number.
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning01() {


		final int                  tickNumberAtStartOfPeriod = 100;
		final int                  tickNumberCurrent         = 1000;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(1000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	@Test
	public final void testGetTicksPerCycle01() {
		double ticksPerCycle = getMotor().getTicksPerCycle();
		assertEquals(13.44, ticksPerCycle, 0.001);
	}

	@Test
	public final void testGetTicksPerMillisecond01() {
		double ticksPerMillisecond = getMotor().getTicksPerMillisecond();
		assertEquals(0.2688, ticksPerMillisecond, 0.00001);
	}

	@Test
	public final void testGetTicksPerSecond01() {
		double ticksPerSecond = getMotor().getTicksPerSecond();
		assertEquals(268.8, ticksPerSecond);
	}

	@Test
	public final void testGetTicksPerMinute01() {
		double ticksPerMinute = getMotor().getTicksPerMinute();
		assertEquals(16128.0, ticksPerMinute);
	}


	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when not enough ticks have passed to start the ramp.
	 */
	@Test
	public final void testIsRampDownToEncoderTicksRunning05() {

		final int                  tickNumberAtStartOfPeriod = 60;
		final int                  tickNumberCurrent         = 61;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		/*
		 * Need 5 test cycles
		 * Ticks per test cycle is 13.44
		 * = 66.72 ticks
		 * tick at end of period = 180
		 * Tick to start ramp = 180 - 66.72 = 113.8
		 */
		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
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
		 *  180-67=113 and still be slowing down
		 *
		 */


		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown01() {

		final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
		final AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		final int                  tickNumberAtStartOfPeriod = 0;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(2000);

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

		final double actual = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(1865.6, actual, 0.01);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown02() {

		/*
		 * A power difference of 1.0 requires 10 cycles of ramp.
		 */
		AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		final int                  tickNumberAtStartOfPeriod = 0;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(10000);

		/*
		 * There are 13.44 ticks in a cycle, so the ramp should be 134.4 ticks
		 */
		final double actual = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		/*
		 * Subtracting 134.4 ticks from the total of 10000 should give us 8800 ticks
		 */
		assertEquals(9865.6, actual);
	}


	@Test
	public final void testGetTickNumberToStartSlowDown03() {

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		final int                  tickNumberAtStartOfPeriod = 0;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		/*
		 * At 13.44 ticks per cycle, total ticks in ramp = 67.2
		 * 120 - 67.2 = 52.8
		 */
		final double actual = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(52.8, actual, 0.01);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown04() {

		final int                  tickNumberAtStartOfPeriod = 60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(10000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * 10 power cycles
		 * 13.44 ticks per cycle
		 * = 134.4 ticks in ramp down
		 *
		 * 10060 - 134.4 = 9925.6
		 */
		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(9925.6, result, 0.01);
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
		 * Each cycle is 13.44 ticks
		 * 13.44 * 5 = 67.2
		 * We need to start ramp down at tick 120 - 67.2 ticks = 52.8 ticks
		 */
		final double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(52.8, result);
	}

	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown11() {


		final int                  tickNumberAtStartOfPeriod = -60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(1000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of slowing down
		 * At 13.44 ticks per cycle for Rev HD Core Hex Motor
		 * --> Need 134.4 ticks of slowing down
		 * Tick number at end of period = 1000-60 = 940
		 * Would need to start ramp at 940 - 134.4 tics = 805.6 ticks
		 */

		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(805.6, result);
	}

	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown12() {

		final int                  tickNumberAtStartOfPeriod = 60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		/*
		 * Need 5 cycles to slow down from 0.5 to 0.0.
		 * With Rev HD Core Hex revHDCoreHexMotor, there are 13.44 ticks per cycle.
		 * Need 5 * 13.44 = 67.2 ticks to slow down.
		 * Tick number at end of period is 60 + 120 = 180.
		 * Tick to start is 180 - 67.2 = 112.8
		 */
		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(112.8, result);
	}

	@Test
	@Override
	public final void whenThereAreEnoughTicks_thenSlowDownStartsOnTime() {

		final int                  tickNumberAtStartOfPeriod = -60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(1000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of slowing down. Thus, at 13.44 ticks per cycle, we need 134.4 ticks for slowdown. The tick
		 * number at end of period is -60 + 1000 = 940. Thus, we would need to start ramp at 940 - 134.4 tics = 805.6
		 * ticks
		 */

		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(805.6, result);
	}

	@Test
	@Override
	public final void whenThereAreNotEnoughTicks_thenSlowDownStartsTooEarly() {

		final int                  tickNumberAtStartOfPeriod = -60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(100);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of slowing down
		 * At 13.44 ticks per cycle
		 * Need 134.4 ticks
		 * Tick number at end of period = 40
		 * Would need to start ramp at 40 - 134.4 tics = -94.4 ticks
		 */

		double result = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(-94.4, result);

	}


	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number
	 * (negative numbers)
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning11() {


		final int                  tickNumberAtStartOfPeriod = -60;
		final int                  tickNumberCurrent         = -900;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(1000);
		final AarrePowerVector     powerAtStart              = new AarrePowerVector(1.0);
		final AarrePowerVector     powerAtEnd                = new AarrePowerVector(0.0);

		/*
		 * Need 10 cycles of ramp
		 * At 13.44 ticks per cycle, we need 134.4 ticks to ramp
		 * Period goes from tick number -60 to tick number 940
		 * Therefore the current tick number (-900) is out of range.
		 *
		 */

		assertThrows(IllegalArgumentException.class, () -> {
			getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		});
	}


	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when enough ticks have passed to start the ramp but not
	 * enough have passed to finish the required movement (negative numbers)
	 */
	@Test
	public final void testIsSlowDownToEncoderTicksRunning08() {

		final int                  tickNumberAtStartOfPeriod = 0;
		final int                  tickNumberCurrent         = -61;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

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
		assertThrows(IllegalArgumentException.class, () -> {
			getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		});
	}


	@Override
	@Test
	public final void testGetNumberOfCycles01() {

		int              ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = getMotor().getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(10, numCycles);
	}

	@Override
	@Test
	public final void testGetNumberOfCycles02() {

		int              ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(0.1);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = getMotor().getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(1, numCycles);
	}

	@Override
	@Test
	public final void testGetNumberOfCycles03() {

		int              ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(-0.1);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = getMotor().getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(1, numCycles);
	}

	@Override
	@Test
	public final void testGetNumberOfCycles04() {

		int              ticksToMove              = 1440;
		AarrePowerVector currentPower             = new AarrePowerVector(-1.0);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(0.0);

		int numCycles = getMotor().getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

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
		 *  The period runs from 60 - 180 ticks
		 *  We are currently at tick number 114
		 *  There are 5 cycles
		 *
		 *  For REV HD Core Hex, the slow down would be 13.44 * 5 = 67.2 ticks, and the slow down would start at tick
		 *  number 180 - 67.2 = 112.8. If the slow down started at tick 112.8, then it would still be going currently
		 *  at tick 114.
		 */
		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	@Test
	public final void whenSlowDownNotStarted_thenTickNumberToStartSlowDownIsCorrect() {

		final int                  tickNumberAtStartOfPeriod = 60;
		final AarrePositiveInteger numberOfTicksInPeriod     = new AarrePositiveInteger(120);

		AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
		AarrePowerVector powerAtEnd   = new AarrePowerVector(0.0);

		/*
		 *  The period runs from 60 - 180 ticks. There are 5 cycles. For REV HD Core Hex, the slow down would be 13.44
		 *  x 5 = 67.2 ticks, and the slow down would start at tick
		 *  number 180 - 67.2 = 112.8.
		 */

		double expected = 112.8;
		double actual = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		assertEquals(expected, actual);

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

		boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
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

		boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
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

		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
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

		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
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
		 *  Period runs from tick 0 to tick 120.
		 *  We need 5 cycles of slowdown. At 13.44 ticks per cycle, that is 67.2 ticks.
		 *  So the slowdown should start at tick number 120 - 67.2 = 52.8
		 *  In that case, the slowdown should be running currently at tick number 59
		 */
		final boolean result = getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
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

		assertThrows(IllegalArgumentException.class, () -> {
			getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
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
			getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
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
			getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
					powerAtStart, powerAtEnd);
		});
	}


	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number
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
			getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent, numberOfTicksInPeriod,
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
		 * The current tickNumber is -61.
		 * This should be impossible.
		 */

		assertThrows(IllegalArgumentException.class, () -> {
			getMotor().isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
					numberOfTicksInPeriod, powerAtStart, powerAtEnd);
		});
	}


}
