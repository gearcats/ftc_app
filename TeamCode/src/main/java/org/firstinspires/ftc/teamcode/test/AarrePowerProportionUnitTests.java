package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.src.AarrePowerProportion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AarrePowerProportionUnitTests {

	private AarrePowerProportion aarrePowerProportion;

	@Test
	final void testAarrePowerProportion01() {
		aarrePowerProportion = new AarrePowerProportion();
	}

	@Test
	final void testAarrePowerProportion02() {
		aarrePowerProportion = new AarrePowerProportion(0);
		double result = aarrePowerProportion.getProportion();
		assertEquals(0.0, result);
	}

	@Test
	final void testAarrePowerProportion03() {
		aarrePowerProportion = new AarrePowerProportion(1);
		double result = aarrePowerProportion.getProportion();
		assertEquals(1.0, result);
	}

	@Test
	final void testAarrePowerProportion04() {
		aarrePowerProportion = new AarrePowerProportion(-1);
		double result = aarrePowerProportion.getProportion();
		assertEquals(-1.0, result);
	}

	@Test
	final void testAarrePowerProportion05() {
		aarrePowerProportion = new AarrePowerProportion(0.0);
		double result = aarrePowerProportion.getProportion();
		assertEquals(0.0, result);
	}

	@Test
	final void testAarrePowerProportion06() {
		aarrePowerProportion = new AarrePowerProportion(1.0);
		double result = aarrePowerProportion.getProportion();
		assertEquals(1.0, result);
	}

	@Test
	final void testAarrePowerProportion07() {
		aarrePowerProportion = new AarrePowerProportion(-1.0);
		double result = aarrePowerProportion.getProportion();
		assertEquals(-1.0, result);
	}

	/**
	 * Test that attempting to set an integer proportion greater than 1 throws an exception.
	 */
	@Test
	final void testAarrePowerProportion08() {
		assertThrows(IllegalArgumentException.class, () -> {
			aarrePowerProportion = new AarrePowerProportion(2);
		});

	}

	/**
	 * Test that attempting to set an integer proportion less than -1 throws an exception.
	 */
	@Test
	final void testAarrePowerProportion09() {
		assertThrows(IllegalArgumentException.class, () -> {
			aarrePowerProportion = new AarrePowerProportion(-2);
		});

	}

	/**
	 * Test that attempting to set an double proportion greater than 1 throws an exception.
	 */
	@Test
	final void testAarrePowerProportion10() {
		assertThrows(IllegalArgumentException.class, () -> {
			aarrePowerProportion = new AarrePowerProportion(1.0001);
		});

	}

	/**
	 * Test that attempting to set an double proportion less than -1 throws an exception.
	 */
	@Test
	final void testAarrePowerProportion11() {
		assertThrows(IllegalArgumentException.class, () -> {
			aarrePowerProportion = new AarrePowerProportion(-1.0001);
		});

	}
}