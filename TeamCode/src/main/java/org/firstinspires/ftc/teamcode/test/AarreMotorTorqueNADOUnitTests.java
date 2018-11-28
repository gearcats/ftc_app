package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.src.AarreMotor;
import org.firstinspires.ftc.teamcode.src.AarreMotorTorqueNADO;
import org.firstinspires.ftc.teamcode.src.AarrePositiveInteger;
import org.firstinspires.ftc.teamcode.src.AarrePowerVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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



}
