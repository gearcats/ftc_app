package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.SlowDown;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SlowDownUnitTests extends RampUnitTests {

	@Test
	public void whenConstructorIsCalled_thenTheResultIsNotNull() {
		SlowDown slowdown = new SlowDown();
		assertNotNull(slowdown);
	}

}