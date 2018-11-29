package org.firstinspires.ftc.teamcode.test;

import org.junit.jupiter.api.Test;

/**
 * This interface declares motor-related unit tests that depend on which kind of motor is being tested. For example,
 * because the TorqueNADO motor and the REV HD Core Hex motor have different characteristics (e.g., number of ticks per
 * revolution), they need separate unit tests. Their tests should both implement this interface, though. Motor-related
 * tests whose results do not depend on the specific kind of motor are declared in {@link
 * AarreMotorUnitTestsInterface}.
 */
interface AarreConcreteMotorUnitTestsInterface {

	@Test
	public void testGetNumberOfCycles01();

	@Test
	public void testGetNumberOfCycles02();

	@Test
	public void testGetNumberOfCycles03();

	@Test
	public void testGetNumberOfCycles04();

	/**
	 * Test calculating when to start a ramp down in a 2000-tick period.
	 */
	@Test
	public void testGetTickNumberToStartSlowDown01();

	/**
	 * Test calculating when to start slowing down in a 10000-tick period.
	 */
	@Test
	public void testGetTickNumberToStartSlowDown02();

	/**
	 * Test calculating when to start slowing down in a 120-tick period.
	 */
	@Test
	public void testGetTickNumberToStartSlowDown03();

	/**
	 * Test calculating when to start slowing down in a 10000-tick period.
	 */
	@Test
	public void testGetTickNumberToStartSlowDown04();

	/**
	 * Test slowing down from 0.5 to 0 between 0 and 120 ticks.
	 */
	@Test
	public void testGetTickNumberToStartSlowDown08();

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when the motor is close enough to the target tick number
	 * (negative numbers)
	 */
	@Test
	public void testGetTickNumberToStartSlowDown11();

	/**
	 * Test calculating when to slow down from 0.5 to 0.0 between 60 and 120 ticks.
	 */
	@Test
	public void testGetTickNumberToStartSlowDown12();

	@Test
	public void whenThereAreNotEnoughTicks_thenSlowDownStartsTooEarly();

	@Test
	public void whenThereAreEnoughTicks_thenSlowDownStartsOnTime();

	@Test
	public void testGetTicksPerCycle01();

	@Test
	public void testGetTicksPerMinute01();


}
