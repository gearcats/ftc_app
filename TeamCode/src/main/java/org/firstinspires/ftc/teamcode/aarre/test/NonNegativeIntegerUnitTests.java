package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeInteger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class NonNegativeIntegerUnitTests {


	@Test
	public void whenConstructorArgumentZero_thenNoProblem() {
		new NonNegativeInteger(0);
	}

	@Test
	public void whenConstructorArgumentPositive_thenNoProblem() {
		int randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		new NonNegativeInteger(randomPositiveInteger);
	}

	@Test
	public void whenConstructorArgumentNegative_thenExceptionThrown() {
		int randomNegativeInteger = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, 0);
		assertThrows(IllegalArgumentException.class, () -> {
			new NonNegativeInteger(randomNegativeInteger);
		});
	}

	@Test
	public void whenDoubleValueRequested_thenReturnsCorrectValue() {
		int                randomPositiveInteger      = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		NonNegativeInteger randomAarrePositiveInteger = new NonNegativeInteger(randomPositiveInteger);
		double             expected                   = (double) randomPositiveInteger;
		double             actual                     = randomAarrePositiveInteger.doubleValue();
		assertEquals(expected, actual);
	}

	@Test
	public void whenIntValueRequested_thenReturnsCorrectValue() {
		int                randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		NonNegativeInteger aarrePositiveInteger  = new NonNegativeInteger(randomPositiveInteger);
		int                returnValue           = aarrePositiveInteger.intValue();
		assertEquals(randomPositiveInteger, returnValue);
	}

	@Test
	public void whenToStringCalled_thenReturnsCorrectValue() {
		int                testValue  = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
		NonNegativeInteger testObject = new NonNegativeInteger(testValue);
		String             expected   = String.format("%d", testValue);
		String             result     = testObject.toString();
		assertEquals(expected, result);
	}


}
