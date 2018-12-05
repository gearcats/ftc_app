package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeInteger;
import org.firstinspires.ftc.teamcode.aarre.src.Ramp;
import org.firstinspires.ftc.teamcode.aarre.src.SlowDown;
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
	public void whenConstructorIsCalled_thenTheResultIsNotNull() {
		assertNotNull(slowdown);
	}

	@Test
	@BeforeEach
	public void slowDownUnitTests() {
		slowdown = new SlowDown();
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

}