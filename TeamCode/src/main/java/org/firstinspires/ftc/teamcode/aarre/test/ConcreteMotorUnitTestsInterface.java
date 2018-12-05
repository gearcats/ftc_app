package org.firstinspires.ftc.teamcode.aarre.test;

import org.junit.jupiter.api.Test;

/**
 * This interface declares revHDCoreHexMotor-related unit tests that depend on which kind of revHDCoreHexMotor is being
 * tested. For example, because the TorqueNADO revHDCoreHexMotor and the REV HD Core Hex revHDCoreHexMotor have
 * different characteristics (e.g., number of ticks per revolution), they need separate unit tests. Their tests should
 * both implement this interface, though. Motor-related tests whose results do not depend on the specific kind of
 * revHDCoreHexMotor are declared in {@link MotorUnitTestsInterface}.
 */
interface ConcreteMotorUnitTestsInterface extends MotorUnitTestsInterface {


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
	 * Test that isSlowDownToEncoderTicksRunning returns true when the revHDCoreHexMotor is close enough to the target
	 * tick number (negative numbers)
	 */
	@Test
	public void testGetTickNumberToStartSlowDown11();

	/**
	 * Test calculating when to slow down from 0.5 to 0.0 between 60 and 120 ticks.
	 */
	@Test
	public void testGetTickNumberToStartSlowDown12();

	@Test
	public void testGetTicksPerCycle01();

	@Test
	public void testGetTicksPerMinute01();

	/**
	 * Test that slowing down should be in effect if the current tick number is close enough to the target tick number.
	 * This test depends only on comparing the current and target tick numbers, so it does not depend on any particular
	 * kind of revHDCoreHexMotor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric01();

	/**
	 * Test that slowing down should not be in effect if the current tick number is not close enough to the target tick
	 * number. This test depends only on comparing the current and target tick numbers; it does not depend on any
	 * particular kind of revHDCoreHexMotor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric02();

	/**
	 * Test that slowing down should not be in effect when the current tick number exceeds the total number of ticks we
	 * were supposed to move. This test depends only on comparing the current and target tick numbers, so it does not
	 * depend on any particular kind of revHDCoreHexMotor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric03();

	/**
	 * Test that slowing down should stop when the current tick number exceeds the total number of ticks we were
	 * supposed to move. This test depends only on comparing the current and target tick numbers, so it does not depend
	 * on any particular kind of revHDCoreHexMotor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric04();

	/**
	 * Test that slowing down should be in progress when enough ticks have passed to start slowing but not enough have
	 * passed to reach the target.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric05();

	/**
	 * Test that slowing down should not be running when the target tick number is negative and the current tick number
	 * is negative but not sufficiently negative to have started the slowdown. This test depends only on comparing the
	 * current and target tick numbers, so it does not depend on any particular kind of revHDCoreHexMotor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric07();

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when enough ticks have passed (negative numbers)
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric09();

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when the revHDCoreHexMotor is not close enough to the
	 * target tick number (negative numbers)
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric10();

	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric11();

	@Test
	public void whenThereAreEnoughTicks_thenSlowDownStartsOnTime();

	@Test
	public void whenThereAreNotEnoughTicks_thenSlowDownStartsTooEarly();

	@Test
	public void whenTickNumberInRange_thenSlowDownIsRunning();

	@Test
	public void whenTickNumberOutsidePeriod_thenExceptionThrown();
}
