package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.Excursion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExcursionUnitTests {

	private Excursion excursion;

	@Test
	@BeforeEach
	public void whenExcursionIsCreated_thenTheResultIsNotNull() {
		excursion = new Excursion();
		assertNotNull(excursion);
	}

	@Test
	public void whenExcursionIsCreated_thenItHasASlowDown() {
		assertNotNull(excursion.getSlowDown());
	}

	@Test
	public void whenExcursionIsCreated_thenItHasASpeedUp() {
		assertNotNull(excursion.getSpeedUp());
	}


}