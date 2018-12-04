package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeInteger;
import org.firstinspires.ftc.teamcode.aarre.src.PositiveInteger;
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
	public void testIsRampUpToEncoderTicksDone01() {

		/*
		  ticksMoved is less than ticksMaximum, so no reason to stop. (We haven't moved far enough
		  yet).
		 */
		PositiveInteger    ticksMaximum = new PositiveInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(0);

		/*
		  Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 0.0;

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
		PositiveInteger    ticksMaximum = new PositiveInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(1441);

		/*
		  Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 0.0;

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
		PositiveInteger    ticksMaximum = new PositiveInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(1439);

		/*
		  Seconds running is more than timeout, so stop.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 6.0;

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
		PositiveInteger    ticksMaximum = new PositiveInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(1439);

		/*
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

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
		PositiveInteger    ticksMaximum = new PositiveInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(1440);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

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
		PositiveInteger    ticksMaximum = new PositiveInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(14);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

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
		PositiveInteger    ticksMaximum = new PositiveInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(144);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

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
		PositiveInteger    ticksMaximum = new PositiveInteger(1440);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(190);

		/*
		  Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

	@Override
	@Test
	public void whenWeHaveNotMovedEnough_thenSpeedUpContinues() {

		PositiveInteger    ticksMaximum   = new PositiveInteger(1440);
		NonNegativeInteger ticksMoved     = new NonNegativeInteger(190);
		double             secondsTimeout = 5.0;
		double             secondsRunning = 4.0;

		boolean result = true;
		result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

	@Override
	@Test
	public void whenWeHaveNotMoved_thenSpeedUpContinues() {

		PositiveInteger    ticksMaximum = new PositiveInteger(5040);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(0);

		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

	@Override
	@Test
	public void whenWeHaveMovedMoreThanEnough_thenSpeedUpStops() throws NoSuchMethodException {

		/*
		 * We have moved enough (albeit in a negative direction), so stop
		 */
		PositiveInteger    ticksMaximum = new PositiveInteger(5040);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(5041);

		/*
		 * Seconds running is less than timeout, so continue.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 4.0;

		boolean result = getMotor().isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning,
				ticksMoved);

		assertTrue(result);
	}


	@Override
	@Test
	public void testSetDirection() {
		getMotor().setDirection(DcMotorSimple.Direction.REVERSE);
	}

	@Test
	@Override
	public void runOpMode() {
	}

	protected MotorUnitTests() {
	}
}
