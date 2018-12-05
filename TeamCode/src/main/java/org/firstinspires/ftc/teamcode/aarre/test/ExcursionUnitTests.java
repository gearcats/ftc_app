package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExcursionUnitTests {

	private Excursion excursion;

	@Test
	@BeforeEach
	public void whenExcursionIsCreated_thenTheResultIsNotNull() {
		excursion = new Excursion();
		assertNotNull(excursion);
	}

	/*
	 *TEST GETTERS
	 */

	@Test
	public void whenExcursionIsCreated_thenPowerVectorIs0() {
		PowerVector powerVector = excursion.getPowerVector();
		assertNotNull(powerVector);
		assertEquals(0.0, powerVector.doubleValue());
	}

	@Test
	public void whenExcursionIsCreated_thenSecondsTimeoutIs1() {
		NonNegativeDouble secondsTimeout = excursion.getSecondsTimeout();
		assertEquals(0.0, secondsTimeout.doubleValue());
	}

	@Test
	public void whenExcursionIsCreated_thenTicksToRotateIs1() {
		NonNegativeInteger ticksToRotate = excursion.getTicksToRotate();
		assertNotNull(ticksToRotate);
		assertEquals(1, ticksToRotate.intValue());
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanGetSlowDown() {
		assertNotNull(excursion.getSlowDown());
	}

	@Test
	public void whenExcursionIsCreated_thenYouCanGetSpeedUp() {
		assertNotNull(excursion.getSpeedUp());
	}

	/*
	 *TEST SETTERS
	 */

	@Test
	public void whenExcursionIsCreated_thenYouCanSetPowerVector() {
		PowerVector powerVector = new PowerVector(1.0);
		excursion.setPowerVector(powerVector);
		PowerVector powerVectorReturned = excursion.getPowerVector();
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



}