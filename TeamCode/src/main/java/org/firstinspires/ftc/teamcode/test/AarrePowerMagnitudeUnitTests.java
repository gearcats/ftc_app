package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.src.AarrePowerMagnitude;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AarrePowerMagnitudeUnitTests {

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void compareTo() {
	}

	@Test
	void asDouble() {
	}

	@Test
	void testDivideBy() {
		AarrePowerMagnitude testMagnitude1 = new AarrePowerMagnitude(1.0);
		AarrePowerMagnitude testMagnitude2 = new AarrePowerMagnitude(0.5);
		double result = testMagnitude1.divideBy(testMagnitude2);
		assertEquals(2.0, result);
	}

	@Test
	void isGreaterThan() {
	}

	@Test
	void isLessThan() {
	}

	@Test
	void subtract() {
	}
}