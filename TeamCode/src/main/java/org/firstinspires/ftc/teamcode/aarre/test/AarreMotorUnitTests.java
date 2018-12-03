package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.aarre.src.AarreMotor;
import org.firstinspires.ftc.teamcode.aarre.src.AarreNonNegativeInteger;
import org.firstinspires.ftc.teamcode.aarre.src.AarrePositiveInteger;
import org.firstinspires.ftc.teamcode.aarre.src.AarrePowerVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor Unit Tests", group = "Aarre")
@Disabled
public class AarreMotorUnitTests extends LinearOpMode implements AarreMotorUnitTestsInterface {

	private AarreMotor motor;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Override
	@BeforeEach
	public void AarreMotorUnitTests() {
		motor = new AarreMotor(this, "left");
	}

	@Override
	@Test
	public final void testGetProportionPowerNew01() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(0.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.9, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew02() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(-1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(0.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.9, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew03() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(0.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.1, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew04() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(0.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(-1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.1, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew05() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(-1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.9, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew06() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(-1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.9, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew07() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(-1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(-1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-1.0, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew08() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(0.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(0.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.0, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew09() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(1.0, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew10() {
		AarrePowerVector proportionPowerCurrent   = new AarrePowerVector(0.0);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(proportionPowerCurrent,
				proportionPowerRequested);
		assertEquals(0.1, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew11() {
		AarrePowerVector proportionPowerCurrent   = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerRequested = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerNew = motor.getPowerVectorNew(proportionPowerCurrent,
				proportionPowerRequested);
		assertEquals(1.0, proportionPowerNew.asDouble(), "Wrong proportion power");
	}


	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone01() {

		/*
		  ticksMoved is less than ticksMaximum, so no reason to stop. (We haven't moved far enough
		  yet).
		 */
		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(1440);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(0);

		/*
		  Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 0.0;

		boolean result = true;

		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertFalse(result);
	}

	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone02() {

		/*
		  We have moved farther than we intended, so it is time to stop.
		 */
		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(1440);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(1441);

		/*
		  Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 0.0;

		/*
		  Power delta is not within the tolerance, so no reason to stop. (We haven't ramped
		  enough yet.)
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		  Current power is within reason, so no reason to stop.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

		boolean result = false;
		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertTrue(result);
	}

	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone03() {

		/*
		  We have not moved enough yet, so continue.
		 */
		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(1440);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(1439);

		/*
		  Seconds running is more than timeout, so stop.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 6.0;

		boolean result = false;
		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertTrue(result);
	}

	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone04() {

		/*
		 * We have not moved enough yet, so continue.
		 */
		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(1440);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(1439);

		/*
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = true;

		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertFalse(result);

	}

	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone05() {

		/*
		  We have moved exactly the right amount, so stop.
		 */
		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(1440);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(1440);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = false;

		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertTrue(result);
	}

	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone06() {

		/*
		  We have not moved the right amount, so continue.
		 */
		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(1440);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(14);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = true;

		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertFalse(result);
	}


	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone07() {

		/*
		  We have not moved enough, so continue.
		 */
		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(1440);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(144);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = true;
		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}
		assertFalse(result);
	}

	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone08() {

		/*
		  We have not moved enough, so continue.
		 */
		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(1440);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(190);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = true;

		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertFalse(result);
	}

	@Override
	@Test
	public final void whenWeHaveNotMovedEnough_thenSpeedUpContinues() {

		AarrePositiveInteger    ticksMaximum   = new AarrePositiveInteger(1440);
		AarreNonNegativeInteger ticksMoved     = new AarreNonNegativeInteger(190);
		double                  secondsTimeout = 5.0;
		double                  secondsRunning = 4.0;

		boolean result = true;
		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertFalse(result);
	}

	@Override
	@Test
	public final void whenWeHaveNotMoved_thenSpeedUpContinues() {

		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(5040);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(0);

		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = false;
		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertFalse(result);
	}


	@Override
	@Test
	public final void whenWeHaveMovedMoreThanEnough_thenSpeedUpStops() {

		/*
		 * We have moved enough (albeit in a negative direction), so stop
		 */
		AarrePositiveInteger    ticksMaximum = new AarrePositiveInteger(5040);
		AarreNonNegativeInteger ticksMoved   = new AarreNonNegativeInteger(5041);

		/*
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = false;
		try {
			result = motor.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		} catch (NoSuchMethodException e) {
			javaLog.severe(e.toString());
		}

		assertTrue(result);
	}


	@Override
	@Test
	public final void whenGetTicksPerCycleCalledAbstract_thenThrowsException() {
		assertThrows(IllegalStateException.class, () -> {
			motor.getTicksPerCycle();
		});
	}

	@Override
	@Test
	public final void testSetDirection() {
		motor.setDirection(DcMotorSimple.Direction.REVERSE);
	}

	@Test
	@Override
	public final void runOpMode() {
	}
}
