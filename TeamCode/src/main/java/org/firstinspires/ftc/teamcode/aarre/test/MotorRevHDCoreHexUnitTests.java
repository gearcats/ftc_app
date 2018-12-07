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
@Autonomous(name = "Aarre Motor Rev HD Core Hex Unit Tests", group = "Aarre")
@Disabled
public class MotorRevHDCoreHexUnitTests extends MotorUnitTests implements ConcreteMotorUnitTestsInterface {

	Logger            javaLog           = Logger.getLogger(this.getClass().getName());
	/**
	 * Test AarreRobot
	 * <p>
	 * We can't break this up into different methods because the tests depend on overriding the FTC runOpMode() method.
	 * Properties inherited from LinearOpMode include: - hardwareMap - telemetry
	 */

	MotorRevHDCoreHex motorRevHDCoreHex = new MotorRevHDCoreHex(this, "arm");

	@Override
	public MotorRevHDCoreHex getMotor() {
		return motorRevHDCoreHex;
	}

	@Override
	@Test
	public final void testGetNumberOfCycles01() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(1.0);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);
		NonNegativeInteger numCycles = slowdown.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(10, numCycles.intValue());
	}

	@Override
	@Test
	public final void testGetNumberOfCycles02() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(0.1);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);
		NonNegativeInteger numCycles = slowdown.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(1, numCycles.intValue());
	}

	@Override
	@Test
	public final void testGetNumberOfCycles03() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(-0.1);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);
		NonNegativeInteger numCycles = slowdown.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(1, numCycles.intValue());
	}

	@Override
	@Test
	public final void testGetNumberOfCycles04() {

		NonNegativeInteger ticksToMove              = new NonNegativeInteger(1440);
		PowerVector        currentPower             = new PowerVector(-1.0);
		PowerVector        proportionPowerRequested = new PowerVector(0.0);

		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);
		NonNegativeInteger numCycles = slowdown.getNumberOfCycles(ticksToMove, currentPower, proportionPowerRequested);

		assertEquals(10, numCycles.intValue());
	}

	@Test
	public final void testGetTickNumberToStartSlowDown01() {

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

		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);

		final double actual = slowdown.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertEquals(1865.6, actual, 0.01);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown02() {

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
		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		/*
		 * Subtracting 134.4 ticks from the total of 10000 should give us 8800 ticks
		 */
		assertEquals(9865.6, actual);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown03() {

		PowerVector powerAtStart = new PowerVector(0.5);
		PowerVector powerAtEnd   = new PowerVector(0.0);

		final int                tickNumberAtStartOfPeriod = 0;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(120);

		/*
		 * At 13.44 ticks per cycle, total ticks in ramp = 67.2
		 * 120 - 67.2 = 52.8
		 */
		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);

		final double actual = slowdown.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertEquals(52.8, actual, 0.01);
	}

	@Test
	public final void testGetTickNumberToStartSlowDown04() {

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
		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(9925.6, actual, 0.01);
	}

	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown08() {

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
		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);

		final double actual = slowdown.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertEquals(52.8, actual);
	}

	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown11() {


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

		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);

		final double actual = slowdown.getTickNumberToStartSlowDown();

		assertEquals(805.6, actual);
	}

	@Test
	@Override
	public final void testGetTickNumberToStartSlowDown12() {

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
		SlowDown slowdown = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);

		final double actual = slowdown.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertEquals(112.8, actual);
	}


	@Test
	public final void testGetTicksPerMillisecond01() {
		double ticksPerMillisecond = motorRevHDCoreHex.getTicksPerMillisecond().doubleValue();
		assertEquals(0.2688, ticksPerMillisecond, 0.00001);
	}

	@Test
	public final void testGetTicksPerMinute01() {
		NonNegativeDouble ticksPerMinute = motorRevHDCoreHex.getTicksPerMinute();
		assertEquals(16128.0, ticksPerMinute.doubleValue());
	}

	@Test
	public final void testGetTicksPerSecond01() {
		double ticksPerSecond = motorRevHDCoreHex.getTicksPerSecond().doubleValue();
		assertEquals(268.8, ticksPerSecond);
	}

	@Test
	public final void whenSlowDownNotStarted_thenReturnFalse() {

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
		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	@Test
	public final void whenSlowDownNotStarted_thenTickNumberToStartSlowDownIsCorrect() {

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
		double actual = motorRevHDCoreHex.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertEquals(expected, actual);

	}

	@Test
	@Override
	public final void whenThereAreEnoughTicks_thenSlowDownStartsOnTime() {

		final int                tickNumberAtStartOfPeriod = -60;
		final NonNegativeInteger numberOfTicksInPeriod     = new NonNegativeInteger(1000);
		final PowerVector        powerAtStart              = new PowerVector(1.0);
		final PowerVector        powerAtEnd                = new PowerVector(0.0);

		/*
		 * Need 10 cycles of slowing down. Thus, at 13.44 ticks per cycle, we need 134.4 ticks for slowdown. The tick
		 * number at end of period is -60 + 1000 = 940. Thus, we would need to start ramp at 940 - 134.4 tics = 805.6
		 * ticks
		 */

		double result = motorRevHDCoreHex.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertEquals(805.6, result);
	}

	@Test
	@Override
	public final void whenThereAreNotEnoughTicks_thenSlowDownStartsTooEarly() {

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

		double result = motorRevHDCoreHex.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod,
				numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertEquals(-94.4, result);

	}

	@Override
	@Test
	public final void whenTickNumberInRange_thenSlowDownIsRunning() {

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
		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertTrue(result);
	}

	@Override
	@Test
	public final void whenTickNumberOutsidePeriod_thenExceptionThrown() {

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

		final boolean result = motorRevHDCoreHex.isSlowDownToEncoderTicksRunning(tickNumberAtStartOfPeriod,
				tickNumberCurrent, numberOfTicksInPeriod, powerAtStart, powerAtEnd);

		assertFalse(result);
	}


}
