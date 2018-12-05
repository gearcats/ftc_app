package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.Ramp;
import org.firstinspires.ftc.teamcode.aarre.src.SpeedUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}