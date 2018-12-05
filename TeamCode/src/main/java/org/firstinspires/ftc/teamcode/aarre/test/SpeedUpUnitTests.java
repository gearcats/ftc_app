package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.SpeedUp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpeedUpUnitTests extends RampUnitTests {

	@Test
	public void whenConstructorIsCalled_thenTheResultIsNotNull() {
		SpeedUp speedup = new SpeedUp();
		assertNotNull(speedup);
	}

}