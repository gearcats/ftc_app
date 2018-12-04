package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.AarrePowerMagnitude;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class AarrePowerMagnitudeUnitTests {

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Test
	void whenValueIsNegative_thenCannotConstructMagnitude() {
		double testValue = -1.0;
		assertThrows(IllegalArgumentException.class, () -> {
			new AarrePowerMagnitude(testValue);
		});
	}

	@Test
	void whenValueIsPositive_thenCanConstructMagnitude() {
		double testValue = 1.0;
		new AarrePowerMagnitude(testValue);
	}

	@Test
	void whenValueIsZero_thenCanConstructMagnitude() {
		double testValue = 0.0;
		new AarrePowerMagnitude(testValue);
	}

	@Test
	void compareTo() {
	}

	@Test
	void whenTheMagnitudeIsOne_thenAsDoubleReturnsOne() {
		double              expected      = 1.0;
		AarrePowerMagnitude testMagnitude = new AarrePowerMagnitude(expected);
		double              actual        = testMagnitude.doubleValue();
		assertEquals(expected, actual);
	}

	@Test
	void whenTheMagnitudeIsZero_thenAsDoubleReturnsZero() {
		double              expected      = 0.0;
		AarrePowerMagnitude testMagnitude = new AarrePowerMagnitude(expected);
		double              actual        = testMagnitude.doubleValue();
		assertEquals(expected, actual);
	}


	@Test
	final void whenMagnitudesAre0and1_thenComparisonIsCorrect() {
		double              zero          = 0.0;
		double              one           = 1.0;
		AarrePowerMagnitude zeroMagnitude = new AarrePowerMagnitude(zero);
		AarrePowerMagnitude oneMagnitude  = new AarrePowerMagnitude(one);

		assertTrue(oneMagnitude.doubleValue() > zeroMagnitude.doubleValue());
	}

	@Test
	final void whenMagnitudesAre1and0_thenComparisonIsCorrect() {
		double              zero          = 0.0;
		double              one           = 1.0;
		AarrePowerMagnitude zeroMagnitude = new AarrePowerMagnitude(zero);
		AarrePowerMagnitude oneMagnitude  = new AarrePowerMagnitude(one);

		assertTrue(zeroMagnitude.doubleValue() < oneMagnitude.doubleValue());
	}


	@Test
	void testDivideBy() {
		AarrePowerMagnitude testMagnitude1 = new AarrePowerMagnitude(1.0);
		AarrePowerMagnitude testMagnitude2 = new AarrePowerMagnitude(0.5);
		double result = testMagnitude1.divideBy(testMagnitude2);
		assertEquals(2.0, result);
	}

	@Test
	void whenValuesAre0and1_thenIsGreaterThanIsCorrect() {
		double              zero          = 0.0;
		double              one           = 1.0;
		AarrePowerMagnitude zeroMagnitude = new AarrePowerMagnitude(zero);
		AarrePowerMagnitude oneMagnitude  = new AarrePowerMagnitude(one);

		assertTrue(oneMagnitude.isGreaterThan(zeroMagnitude));
	}

	@Test
	void whenValuesAre0and1_thenIsLessThanIsCorrect() {
		double              zero          = 0.0;
		double              one           = 1.0;
		AarrePowerMagnitude zeroMagnitude = new AarrePowerMagnitude(zero);
		AarrePowerMagnitude oneMagnitude  = new AarrePowerMagnitude(one);

		assertTrue(zeroMagnitude.isLessThan(oneMagnitude));
	}

	@Test
	void isLessThan() {
	}

	@Test
	void subtract() {
	}
}