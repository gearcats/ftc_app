package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeDouble;
import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonNegativeDoubleUnitTests {

	@Test
	@BeforeEach
	public void whenNonNegativeDoubleConstructed_thenValueIs0() {
		NonNegativeDouble actual = new NonNegativeDouble();
		assertEquals(0.0, actual.doubleValue());
	}

	@Test
	public void whenGettingIntValue_thenAnswerIsCorrect() {
		int               expected   = 1;
		NonNegativeDouble testObject = new NonNegativeDouble(expected);
		int               actual     = testObject.intValue();
		assertEquals(expected, actual);
	}

	@Test
	public void whenMultiplyingNonNegativeDoubles_thenAnswerIsCorrect() {
		double            double1       = 5.0;
		double            double2       = 6.0;
		NonNegativeDouble multiplicand1 = new NonNegativeDouble(double1);
		NonNegativeDouble multiplicand2 = new NonNegativeDouble(double2);
		NonNegativeDouble actual        = multiplicand1.multiplyBy(multiplicand2);
		NonNegativeDouble expected      = new NonNegativeDouble(double1 * double2);
		assertEquals(expected, actual);
	}

	@Test
	public void whenMultiplyingByNonNegativeInteger_thenAnswerIsCorrect() {
		double             doubleMultiplicand = 5.0;
		int                intMultiplicand    = 6;
		NonNegativeDouble  multiplicand1      = new NonNegativeDouble(doubleMultiplicand);
		NonNegativeInteger multiplicand2      = new NonNegativeInteger(intMultiplicand);
		NonNegativeDouble  actual             = multiplicand1.multiplyBy(multiplicand2);
		NonNegativeDouble  expected           = new NonNegativeDouble(doubleMultiplicand * intMultiplicand);
		assertEquals(expected, actual);

	}


}