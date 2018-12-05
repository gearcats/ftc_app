package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.Riser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class RiserUnitTests {

	Riser riser;

	RiserUnitTests() {
		riser = new Riser();
	}

	@Test
	public void testLowerMethod() {
		assertThrows(NullPointerException.class, () -> riser.lower());
	}

	@Test
	public void testNewAarreRiserObjectNotNull() {
		assertNotNull(riser);
	}

	@Test
	public void testRaiseMethod() {
		assertThrows(NullPointerException.class, () -> riser.raise());
	}

}