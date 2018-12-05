package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeDouble;
import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeInteger;
import org.firstinspires.ftc.teamcode.aarre.src.PowerVector;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.logging.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor Unit Tests", group = "Aarre")
@Disabled
public abstract class MotorUnitTests extends LinearOpMode implements MotorUnitTestsInterface {

	private static Logger log;

	static {

		log = Logger.getLogger("MotorUnitTests");
		log.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		handler.setFormatter(new SimpleFormatter() {

			private static final String format = "%1$tF %1$tT [%2$s] %4$s::%5$s - %6$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				String formattedLogRecord = String.format(format, new Date(lr.getMillis()), lr.getLevel()
						.getLocalizedName(), lr.getLoggerName(), lr.getSourceClassName(), lr.getSourceMethodName(), lr
						.getMessage());
				return formattedLogRecord;
			}

		});
		log.addHandler(handler);
		log.setLevel(Level.ALL);
	}

	protected MotorUnitTests() {
	}

	@Test
	@Override
	public void runOpMode() {
	}

	@Override
	@Test
	public void testGetPowerVectorNew02() {
		PowerVector powerVectorCurrent   = new PowerVector(-1.0);
		PowerVector powerVectorRequested = new PowerVector(0.0);
		PowerVector proportionPowerNew   = getMotor().getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.9, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetPowerVectorNew03() {
		PowerVector powerVectorCurrent   = new PowerVector(0.0);
		PowerVector powerVectorRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew   = getMotor().getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.1, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetPowerVectorNew04() {
		PowerVector powerVectorCurrent   = new PowerVector(0.0);
		PowerVector powerVectorRequested = new PowerVector(-1.0);
		PowerVector proportionPowerNew   = getMotor().getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.1, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetPowerVectorNew05() {
		PowerVector powerVectorCurrent   = new PowerVector(1.0);
		PowerVector powerVectorRequested = new PowerVector(-1.0);
		PowerVector proportionPowerNew   = getMotor().getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.9, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetPowerVectorNew06() {
		PowerVector powerVectorCurrent   = new PowerVector(-1.0);
		PowerVector powerVectorRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew   = getMotor().getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.9, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetPowerVectorNew07() {
		PowerVector powerVectorCurrent   = new PowerVector(-1.0);
		PowerVector powerVectorRequested = new PowerVector(-1.0);
		PowerVector proportionPowerNew   = getMotor().getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-1.0, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetPowerVectorNew08() {
		PowerVector powerVectorCurrent   = new PowerVector(0.0);
		PowerVector powerVectorRequested = new PowerVector(0.0);
		PowerVector proportionPowerNew   = getMotor().getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.0, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetPowerVectorNew09() {
		PowerVector powerVectorCurrent   = new PowerVector(1.0);
		PowerVector powerVectorRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew   = getMotor().getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(1.0, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetPowerVectorNew10() {
		PowerVector proportionPowerCurrent   = new PowerVector(0.0);
		PowerVector proportionPowerRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew = getMotor().getPowerVectorNew(proportionPowerCurrent,
				proportionPowerRequested);
		assertEquals(0.1, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetPowerVectorNew11() {
		PowerVector proportionPowerCurrent   = new PowerVector(1.0);
		PowerVector proportionPowerRequested = new PowerVector(1.0);
		PowerVector proportionPowerNew = getMotor().getPowerVectorNew(proportionPowerCurrent,
				proportionPowerRequested);
		assertEquals(1.0, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testGetProportionPowerNew01() {
		PowerVector powerVectorCurrent   = new PowerVector(1.0);
		PowerVector powerVectorRequested = new PowerVector(0.0);
		PowerVector proportionPowerNew   = getMotor().getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.9, proportionPowerNew.doubleValue(), "Wrong proportion power");
	}

	@Override
	@Test
	public void testIsRampUpToEncoderTicksDone01() {

		/*
		  ticksMoved is less than ticksMaximum, so no reason to stop. (We haven't moved far enough
		  yet).
		 */
		NonNegativeInteger ticksMaximum = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(0);

		/*
		  Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(0.0);

		boolean result = true;

		result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

	@Override
	@Test
	public void testIsRampUpToEncoderTicksDone02() {

		/*
		  We have moved farther than we intended, so it is time to stop.
		 */
		NonNegativeInteger ticksMaximum = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(1441);

		/*
		  Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(0.0);

		boolean result = false;

		result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertTrue(result);
	}

	@Override
	@Test
	public void testIsRampUpToEncoderTicksDone03() {

		/*
		  We have not moved enough yet, so continue.
		 */
		NonNegativeInteger ticksMaximum = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(1439);

		/*
		  Seconds running is more than timeout, so stop.
		 */
		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(6.0);

		boolean result = false;
		result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertTrue(result);
	}

	@Override
	@Test
	public void testIsRampUpToEncoderTicksDone04() {

		/*
		 * We have not moved enough yet, so continue.
		 */
		NonNegativeInteger ticksMaximum = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(1439);

		/*
		 * Seconds running is less than timeout, so continue.
		 */
		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(4.0);

		boolean result = true;

		result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);

	}

	@Override
	@Test
	public void testIsRampUpToEncoderTicksDone05() {

		/*
		  We have moved exactly the right amount, so stop.
		 */
		NonNegativeInteger ticksMaximum = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(1440);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(4.0);

		boolean result = false;

		result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		assertTrue(result);
	}

	@Override
	@Test
	public void testIsRampUpToEncoderTicksDone06() {

		/*
		  We have not moved the right amount, so continue.
		 */
		NonNegativeInteger ticksMaximum = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(14);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(4.0);

		boolean result = true;

		log.finest("Calling method on 'revHDCoreHexMotor'");
		result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		log.finest("Return from method on 'revHDCoreHexMotor'");

		assertFalse(result);
	}

	@Override
	@Test
	public void testIsRampUpToEncoderTicksDone07() {

		/*
		  We have not moved enough, so continue.
		 */
		NonNegativeInteger ticksMaximum = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(144);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(4.0);

		boolean result = true;
		result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		assertFalse(result);
	}

	@Override
	@Test
	public void testIsRampUpToEncoderTicksDone08() {

		/*
		  We have not moved enough, so continue.
		 */
		NonNegativeInteger ticksMaximum = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(190);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(4.0);

		boolean result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning,
				ticksMoved);

		assertFalse(result);
	}

	/**
	 * The tick number to start a slow down should depend on the speed at the start of the slowdown but not on the
	 * speed
	 * in the middle of the slowdown.
	 */

	@Override
	@Test
	public void testSetDirection() {
		getMotor().setDirection(DcMotorSimple.Direction.REVERSE);
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

		getMotor().setCurrentTickNumber(8);

		double result1 = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerVectorAtStartOfPeriod, powerVectorAtEndOfPeriod);

		getMotor().setCurrentTickNumber(15);

		double result2 = getMotor().getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerVectorAtStartOfPeriod, powerVectorAtEndOfPeriod);

		assertEquals(result1, result2);

	}

	@Override
	@Test
	public void whenWeHaveMovedMoreThanEnough_thenSpeedUpStops() throws NoSuchMethodException {

		/*
		 * We have moved enough (albeit in a negative direction), so stop
		 */
		NonNegativeInteger ticksMaximum = new NonNegativeInteger(5040);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(5041);

		/*
		 * Seconds running is less than timeout, so continue.
		 */
		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(4.0);

		boolean result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning,
				ticksMoved);

		assertTrue(result);
	}

	@Override
	@Test
	public void whenWeHaveNotMovedEnough_thenSpeedUpContinues() {

		NonNegativeInteger ticksMaximum   = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved     = new NonNegativeInteger(190);
		NonNegativeDouble  secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble  secondsRunning = new NonNegativeDouble(4.0);

		boolean result = true;
		result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

	@Override
	@Test
	public void whenWeHaveNotMoved_thenSpeedUpContinues() {

		NonNegativeInteger ticksMaximum = new NonNegativeInteger(5040);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(0);

		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(4.0);

		boolean result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning,
				ticksMoved);

		assertFalse(result);
	}
}
