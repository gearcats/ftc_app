package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.PositiveInteger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PositiveIntegerUnitTests {

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Test
	public void whenConstructorArgumentNegative_thenExceptionThrown() {
		int randomNegativeInteger = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, -1);
		assertThrows(IllegalArgumentException.class, () -> {
			new PositiveInteger(randomNegativeInteger);
		});
	}

	@Test
	public void whenConstructorArgumentPositive_thenNoProblem() {
		int randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		new PositiveInteger(randomPositiveInteger);
	}

	@Test
	public void whenDoubleValueRequested_thenReturnsCorrectValue() {
		int             randomPositiveInteger      = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		PositiveInteger randomAarrePositiveInteger = new PositiveInteger(randomPositiveInteger);
		double          expected                   = (double) randomPositiveInteger;
		double          actual                     = randomAarrePositiveInteger.doubleValue();
		assertEquals(expected, actual);
	}

	@Test
	public void whenIntValueRequested_thenReturnsCorrectValue() {
		int             randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		PositiveInteger positiveInteger       = new PositiveInteger(randomPositiveInteger);
		int             returnValue           = positiveInteger.intValue();
		assertEquals(randomPositiveInteger, returnValue);
	}

}
