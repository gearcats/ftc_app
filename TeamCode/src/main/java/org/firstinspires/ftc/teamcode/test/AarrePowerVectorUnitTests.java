package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.src.AarrePowerVector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AarrePowerVectorUnitTests {

	private AarrePowerVector aarrePowerVector;

	@Test
	final void testAarrePowerVector01() {
		aarrePowerVector = new AarrePowerVector();
	}

	@Test
	final void testAarrePowerVector02() {
		aarrePowerVector = new AarrePowerVector(0);
		double result = aarrePowerVector.asDouble();
		assertEquals(0.0, result);
	}

	@Test
	final void testAarrePowerVector03() {
		aarrePowerVector = new AarrePowerVector(1);
		double result = aarrePowerVector.asDouble();
		assertEquals(1.0, result);
	}

	@Test
	final void testAarrePowerVector04() {
		aarrePowerVector = new AarrePowerVector(-1);
		double result = aarrePowerVector.asDouble();
		assertEquals(-1.0, result);
	}

	@Test
	final void testAarrePowerVector05() {
		aarrePowerVector = new AarrePowerVector(0.0);
		double result = aarrePowerVector.asDouble();
		assertEquals(0.0, result);
	}

	@Test
	final void testAarrePowerVector06() {
		aarrePowerVector = new AarrePowerVector(1.0);
		double result = aarrePowerVector.asDouble();
		assertEquals(1.0, result);
	}

	@Test
	final void testAarrePowerVector07() {
		aarrePowerVector = new AarrePowerVector(-1.0);
		double result = aarrePowerVector.asDouble();
		assertEquals(-1.0, result);
	}

	/**
	 * Test that attempting to set an integer Vector greater than 1 throws an exception.
	 */
	@Test
	final void testAarrePowerVector08() {
		assertThrows(IllegalArgumentException.class, () -> aarrePowerVector = new AarrePowerVector
				(2));

	}

	/**
	 * Test that attempting to set an integer Vector less than -1 throws an exception.
	 */
	@Test
	final void testAarrePowerVector09() {
		assertThrows(IllegalArgumentException.class, () -> aarrePowerVector = new AarrePowerVector(-2));

	}


	/**
	 * Test that attempting to set an double Vector greater than 1 throws an exception.
	 */
	@Test
	final void testAarrePowerVector10() {
		assertThrows(IllegalArgumentException.class, () -> aarrePowerVector = new AarrePowerVector
				(1.0001));

	}

	/**
	 * Test that attempting to set an double Vector less than -1 throws an exception.
	 */
	@Test
	final void testAarrePowerVector11() {
		assertThrows(IllegalArgumentException.class, () -> aarrePowerVector = new AarrePowerVector(-1.0001));

	}

	/**
	 * Test subtracting power vectors
	 */
	@Test
	final void testAarrePowerVector12() {
		AarrePowerVector aarrePowerVector1 = new AarrePowerVector(0.79);
		AarrePowerVector aarrePowerVector2 = new AarrePowerVector(0.45);
		AarrePowerVector aarrePowerVector3 = aarrePowerVector1.subtract(aarrePowerVector2);
		double result = aarrePowerVector3.asDouble();
		assertEquals(0.34, result);
	}
}