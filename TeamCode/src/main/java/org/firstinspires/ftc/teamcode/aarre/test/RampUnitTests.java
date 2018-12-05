package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.Ramp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RampUnitTests {

	@Test
	public void whenConstructorIsCalled_thenTheResultIsNotNull() {
		Ramp ramp = new Ramp();
		assertNotNull(ramp);
	}

}