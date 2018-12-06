package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SlowDownUnitTests extends RampUnitTests {

	private SlowDown slowdown;

	@Override
	public Ramp getRamp() {
		return slowdown;
	}

	@Test
	@BeforeEach
	public void slowDownUnitTests() {
		slowdown = new SlowDown();
	}

	@Test
	public void whenConstructorIsCalled_thenTheResultIsNotNull() {
		assertNotNull(slowdown);
	}

	@Test
	public void whenObjectIsConstructed_thenTicksToSlowDownIsZero() {
		NonNegativeInteger expected = new NonNegativeInteger(0);
		NonNegativeInteger actual   = slowdown.getTicksToRotate();
		assertEquals(expected.doubleValue(), actual.doubleValue());
	}

	@Test
	public void whenSlowDownIsConstructed_thenTicksToSlowDownIsNotNull() {
		NonNegativeInteger actual = slowdown.getTicksToRotate();
		assertNotNull(actual);
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

		slowdown.getMotor().setCurrentTickNumber(8);

		double result1 = slowdown.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerVectorAtStartOfPeriod, powerVectorAtEndOfPeriod);

		slowdown.getMotor().setCurrentTickNumber(15);

		double result2 = slowdown.getTickNumberToStartSlowDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerVectorAtStartOfPeriod, powerVectorAtEndOfPeriod);

		assertEquals(result1, result2);

	}

	@Test
	public final void whenSlowingDownAndMotorIsRev_thenTicsPerCycleIsCorrect() {
		MotorRevHDCoreHex motorRevHDCoreHex = new MotorRevHDCoreHex();
		SlowDown          slowdown          = new SlowDown();
		slowdown.setMotor(motorRevHDCoreHex);

		double ticksPerCycle = slowdown.getTicksPerCycle().doubleValue();
		assertEquals(13.44, ticksPerCycle, 0.001);
	}

	@Test
	public final void whenSlowingDownAndMotorIsTorque_thenTicsPerCycleIsCorrect() {
		MotorTorqueNADO motorTorqueNADO = new MotorTorqueNADO();
		SlowDown        slowdown        = new SlowDown();
		slowdown.setMotor(motorTorqueNADO);

		double ticksPerCycle = slowdown.getTicksPerCycle().doubleValue();
		assertEquals(13.44, ticksPerCycle, 0.001);
	}

	;

}