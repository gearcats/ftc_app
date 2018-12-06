package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExcursionUnitTests {

	private Excursion excursion;

	@Test
	public void whenExcursionIsCreated_thenPowerVectorIs0() {
		PowerVector powerVector = excursion.getTargetPower();
		assertNotNull(powerVector);
		assertEquals(0.0, powerVector.doubleValue());
	}

	@Test
	public void whenExcursionIsCreated_thenSecondsTimeoutIs1() {
		NonNegativeDouble secondsTimeout = excursion.getTimeoutSeconds();
		assertEquals(0.0, secondsTimeout.doubleValue());
	}

	@Test
	@BeforeEach
	public void whenExcursionIsCreated_thenTheResultIsNotNull() {
		excursion = new Excursion();
		assertNotNull(excursion);
	}

	@Test
	public void whenExcursionIsCreated_thenTicksToRotateIs1() {
		NonNegativeInteger ticksToRotate = excursion.getTicksToRotate();
		assertNotNull(ticksToRotate);
		assertEquals(1, ticksToRotate.intValue());
	}

	@Test
	public void whenExcursionIsCreated_thenTicksToSlowDownIsNotNull() {
		NonNegativeInteger actual = excursion.getTicksToSlowDown();
		assertNotNull(actual);
	}

	@Test
	public void whenExcursionIsCreated_thenTicksToSlowDownIsZero() {
		int expected = 0;
		int actual   = excursion.getTicksToSlowDown().intValue();
		assertEquals(expected, actual);
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanGetSlowDown() {
		assertNotNull(excursion.getSlowDown());
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanGetSpeedUp() {
		assertNotNull(excursion.getSpeedUp());
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanGetTicksToSlowDown() {
		NonNegativeInteger ticksToSlowDown = excursion.getTicksToSlowDown();
		assertNotNull(ticksToSlowDown);
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanGetTicksToSpeedUp() {
		NonNegativeInteger ticksToSpeedUp = excursion.getTicksToSpeedUp();
		assertNotNull(ticksToSpeedUp);
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanSetPowerVector() {
		PowerVector powerVector = new PowerVector(1.0);
		excursion.setTargetPower(powerVector);
		PowerVector powerVectorReturned = excursion.getTargetPower();
		assertEquals(powerVector, powerVectorReturned);
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanSetSlowDown() {
		SlowDown slowDown = new SlowDown();
		excursion.setSlowDown(slowDown);
		SlowDown returnedSlowDown = excursion.getSlowDown();
		assertEquals(slowDown, returnedSlowDown);
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanSetSpeedUp() {
		SpeedUp originalSpeedUp = new SpeedUp();
		excursion.setSpeedUp(originalSpeedUp);
		SpeedUp returnedSpeedUp = excursion.getSpeedUp();
		assertEquals(originalSpeedUp, returnedSpeedUp);
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanSetTicksToRotate() {
		NonNegativeInteger ticksToRotate = new NonNegativeInteger(1200);
		excursion.setTicksToRotate(ticksToRotate);
		NonNegativeInteger ticksToRotateReturned = excursion.getTicksToRotate();
		assertEquals(ticksToRotate, ticksToRotateReturned);
	}

	@Test
	public void whenTicksToRotateIsSet_thenYouCanGetTicksToSlowDown() {
		int                expected      = 600;
		int                total         = 2 * expected;
		NonNegativeInteger ticksToRotate = new NonNegativeInteger(total);
		excursion.setTicksToRotate(ticksToRotate);
		NonNegativeInteger result = excursion.getTicksToSlowDown();
		int                actual = result.intValue();
		assertEquals(expected, actual);
	}


}