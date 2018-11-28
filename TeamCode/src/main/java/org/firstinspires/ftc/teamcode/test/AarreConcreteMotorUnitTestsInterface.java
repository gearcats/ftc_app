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

	@Test
	public void testGetTickNumberToStartRampDown01();

	@Test
	public void testGetTickNumberToStartRampDown02();

	@Test
	public void testGetTickNumberToStartRampDown03();

	@Test
	public void testGetTickNumberToStartRampDown04();

	@Test
	public void testGetTicksPerCycle01();

	@Test
	public void testGetTicksPerMinute01();


}
