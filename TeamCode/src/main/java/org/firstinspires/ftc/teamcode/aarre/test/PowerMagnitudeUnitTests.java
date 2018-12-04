package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.PowerMagnitude;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class PowerMagnitudeUnitTests {

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Test
	void whenValueIsNegative_thenCannotConstructMagnitude() {
		double testValue = -1.0;
		assertThrows(IllegalArgumentException.class, () -> {
			new PowerMagnitude(testValue);
		});
	}

	@Test
	void whenValueIsPositive_thenCanConstructMagnitude() {
		double testValue = 1.0;
		new PowerMagnitude(testValue);
	}

	@Test
	void whenValueIsZero_thenCanConstructMagnitude() {
		double testValue = 0.0;
		new PowerMagnitude(testValue);
	}

	@Test
	void compareTo() {
	}

	@Test
	void whenTheMagnitudeIsOne_thenAsDoubleReturnsOne() {
		double         expected      = 1.0;
		PowerMagnitude testMagnitude = new PowerMagnitude(expected);
		double         actual        = testMagnitude.doubleValue();
		assertEquals(expected, actual);
	}

	@Test
	void whenTheMagnitudeIsZero_thenAsDoubleReturnsZero() {
		double         expected      = 0.0;
		PowerMagnitude testMagnitude = new PowerMagnitude(expected);
		double         actual        = testMagnitude.doubleValue();
		assertEquals(expected, actual);
	}


	@Test
	final void whenMagnitudesAre0and1_thenComparisonIsCorrect() {
		double         zero          = 0.0;
		double         one           = 1.0;
		PowerMagnitude zeroMagnitude = new PowerMagnitude(zero);
		PowerMagnitude oneMagnitude  = new PowerMagnitude(one);

		assertTrue(oneMagnitude.doubleValue() > zeroMagnitude.doubleValue());
	}

	@Test
	final void whenMagnitudesAre1and0_thenComparisonIsCorrect() {
		double         zero          = 0.0;
		double         one           = 1.0;
		PowerMagnitude zeroMagnitude = new PowerMagnitude(zero);
		PowerMagnitude oneMagnitude  = new PowerMagnitude(one);

		assertTrue(zeroMagnitude.doubleValue() < oneMagnitude.doubleValue());
	}


	@Test
	void testDivideBy() {
		PowerMagnitude testMagnitude1 = new PowerMagnitude(1.0);
		PowerMagnitude testMagnitude2 = new PowerMagnitude(0.5);
		double         result         = testMagnitude1.divideBy(testMagnitude2);
		assertEquals(2.0, result);
	}

	@Test
	void whenValuesAre0and1_thenIsGreaterThanIsCorrect() {
		double         zero          = 0.0;
		double         one           = 1.0;
		PowerMagnitude zeroMagnitude = new PowerMagnitude(zero);
		PowerMagnitude oneMagnitude  = new PowerMagnitude(one);

		assertTrue(oneMagnitude.isGreaterThan(zeroMagnitude));
	}

	@Test
	void whenValuesAre0and1_thenIsLessThanIsCorrect() {
		double         zero          = 0.0;
		double         one           = 1.0;
		PowerMagnitude zeroMagnitude = new PowerMagnitude(zero);
		PowerMagnitude oneMagnitude  = new PowerMagnitude(one);

		assertTrue(zeroMagnitude.isLessThan(oneMagnitude));
	}

	@Test
	void isLessThan() {
	}

	@Test
	void subtract() {
	}
}