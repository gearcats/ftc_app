package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.PowerVector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PowerVectorUnitTests {

	private PowerVector powerVector;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Test
	final void testAarrePowerVector01() {
		powerVector = new PowerVector();
	}

	@Test
	final void testAarrePowerVector02() {
		powerVector = new PowerVector(0);
		double result = powerVector.doubleValue();
		assertEquals(0.0, result);
	}

	@Test
	final void testAarrePowerVector03() {
		powerVector = new PowerVector(1);
		double result = powerVector.doubleValue();
		assertEquals(1.0, result);
	}

	@Test
	final void testAarrePowerVector04() {
		powerVector = new PowerVector(-1);
		double result = powerVector.doubleValue();
		assertEquals(-1.0, result);
	}

	@Test
	final void testAarrePowerVector05() {
		powerVector = new PowerVector(0.0);
		double result = powerVector.doubleValue();
		assertEquals(0.0, result);
	}

	@Test
	final void testAarrePowerVector06() {
		powerVector = new PowerVector(1.0);
		double result = powerVector.doubleValue();
		assertEquals(1.0, result);
	}

	@Test
	final void testAarrePowerVector07() {
		powerVector = new PowerVector(-1.0);
		double result = powerVector.doubleValue();
		assertEquals(-1.0, result);
	}

	/**
	 * Test that attempting to set an integer Vector greater than 1 throws an exception.
	 */
	@Test
	final void testAarrePowerVector08() {
		assertThrows(IllegalArgumentException.class, () -> powerVector = new PowerVector
				(2));

	}

	/**
	 * Test that attempting to set an integer Vector less than -1 throws an exception.
	 */
	@Test
	final void testAarrePowerVector09() {
		assertThrows(IllegalArgumentException.class, () -> powerVector = new PowerVector(-2));

	}


	/**
	 * Test that attempting to set an double Vector greater than 1 throws an exception.
	 */
	@Test
	final void testAarrePowerVector10() {
		assertThrows(IllegalArgumentException.class, () -> powerVector = new PowerVector
				(1.0001));

	}

	/**
	 * Test that attempting to set an double Vector less than -1 throws an exception.
	 */
	@Test
	final void testAarrePowerVector11() {
		assertThrows(IllegalArgumentException.class, () -> powerVector = new PowerVector(-1.0001));

	}

	/**
	 * Test subtracting power vectors
	 */
	@Test
	final void testAarrePowerVector12() {
		PowerVector powerVector1 = new PowerVector(0.79);
		PowerVector powerVector2 = new PowerVector(0.45);
		PowerVector powerVector3 = powerVector1.subtract(powerVector2);
		double      result       = powerVector3.doubleValue();
		assertEquals(0.34, result);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testAdd() {
	}

	@Test
	void testDivideBy() {
	}

	@Test
	void testDivideBy1() {
	}

	@Test
	void testGetDirection() {
	}

	@Test
	void testIsGreaterThan() {
	}

	@Test
	void testAsDouble() {
	}

	@Test
	void testGetMagnitude() {
	}

	@Test
	void testMultiplyBy() {
	}

	@Test
	void testReverseDirection() {
	}

	@Test
	void testSubtract() {
	}
}