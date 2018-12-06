package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeedUpUnitTests extends RampUnitTests {

	SpeedUp speedup;

	@Test
	@BeforeEach
	public void SpeedUpUnitTests() {
		speedup = new SpeedUp();
		assertNotNull(speedup);
	}

	@Override
	public Ramp getRamp() {
		return speedup;
	}


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

		result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

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

		result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertTrue(result);
	}

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
		result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertTrue(result);
	}

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

		result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);

	}

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

		result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		assertTrue(result);
	}

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

		result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

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
		result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);
		assertFalse(result);
	}

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

		boolean result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

	@Test
	public final void whenSlowingDownAndMotorIsRev_thenTicsPerCycleIsCorrect() {
		MotorRevHDCoreHex motorRevHDCoreHex = new MotorRevHDCoreHex();
		SpeedUp           speedup           = new SpeedUp();
		speedup.setMotor(motorRevHDCoreHex);

		double ticksPerCycle = speedup.getTicksPerCycle().doubleValue();
		assertEquals(13.44, ticksPerCycle, 0.001);
	}

	@Test
	public final void whenSlowingDownAndMotorIsTorque_thenTicsPerCycleIsCorrect() {
		MotorTorqueNADO motorTorqueNADO = new MotorTorqueNADO();
		SpeedUp         speedup         = new SpeedUp();
		speedup.setMotor(motorTorqueNADO);

		double ticksPerCycle = speedup.getTicksPerCycle().doubleValue();
		assertEquals(13.44, ticksPerCycle, 0.001);
	}

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

		boolean result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertTrue(result);
	}

	@Test
	public void whenWeHaveNotMovedEnough_thenSpeedUpContinues() {

		NonNegativeInteger ticksMaximum   = new NonNegativeInteger(1440);
		NonNegativeInteger ticksMoved     = new NonNegativeInteger(190);
		NonNegativeDouble  secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble  secondsRunning = new NonNegativeDouble(4.0);

		boolean result = true;
		result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

	@Test
	public void whenWeHaveNotMoved_thenSpeedUpContinues() {

		NonNegativeInteger ticksMaximum = new NonNegativeInteger(5040);
		NonNegativeInteger ticksMoved   = new NonNegativeInteger(0);

		NonNegativeDouble secondsTimeout = new NonNegativeDouble(5.0);
		NonNegativeDouble secondsRunning = new NonNegativeDouble(4.0);

		boolean result = speedup.isSpeedUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved);

		assertFalse(result);
	}

	;
}