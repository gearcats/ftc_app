package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.AarreNonNegativeInteger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AarreNonNegativeIntegerUnitTests {


	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Test
	public void whenConstructorArgumentZero_thenNoProblem() {
		new AarreNonNegativeInteger(0);
	}

	@Test
	public void whenConstructorArgumentPositive_thenNoProblem() {
		int randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		new AarreNonNegativeInteger(randomPositiveInteger);
	}

	@Test
	public void whenConstructorArgumentNegative_thenExceptionThrown() {
		int randomNegativeInteger = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, 0);
		assertThrows(IllegalArgumentException.class, () -> {
			new AarreNonNegativeInteger(randomNegativeInteger);
		});
	}

	@Test
	public void whenDoubleValueRequested_thenReturnsCorrectValue() {
		int                     randomPositiveInteger      = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		AarreNonNegativeInteger randomAarrePositiveInteger = new AarreNonNegativeInteger(randomPositiveInteger);
		double                  expected                   = (double) randomPositiveInteger;
		double                  actual                     = randomAarrePositiveInteger.doubleValue();
		assertEquals(expected, actual);
	}

	@Test
	public void whenIntValueRequested_thenReturnsCorrectValue() {
		int                     randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		AarreNonNegativeInteger aarrePositiveInteger  = new AarreNonNegativeInteger(randomPositiveInteger);
		int                     returnValue           = aarrePositiveInteger.intValue();
		assertEquals(randomPositiveInteger, returnValue);
	}


}
