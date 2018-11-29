package org.firstinspires.ftc.teamcode.test;

import org.junit.jupiter.api.Test;

/**
 * This interface declares motor-related unit tests that apply to all motors. Motor-related unit tests whose results
 * depend on specific kinds of motors are declared in {@link AarreConcreteMotorUnitTestsInterface}.
 */
interface AarreMotorUnitTestsInterface {

	@Test
	void AarreMotorUnitTests();

	@Test
	void testGetProportionPowerNew01();

	@Test
	void testGetPowerVectorNew02();

	@Test
	void testGetPowerVectorNew03();

	@Test
	void testGetPowerVectorNew04();

	@Test
	void testGetPowerVectorNew05();

	@Test
	void testGetPowerVectorNew06();

	@Test
	void testGetPowerVectorNew07();

	@Test
	void testGetPowerVectorNew08();

	@Test
	void testGetPowerVectorNew09();

	/**
	 * Test that the new proportion of power increases correctly from 0 up.
	 */
	@Test
	void testGetPowerVectorNew10();


	/**
	 * Test that the new proportion of power does not increase beyond 1.0
	 */
	@Test
	void testGetPowerVectorNew11();

	/**
	 * Test that slowing down should be in effect if the current tick number is close enough to the target tick number.
	 * This test depends only on comparing the current and target tick numbers, so it does not depend on any particular
	 * kind of motor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric01();

	/**
	 * Test that slowing down should not be in effect if the current tick number is not close enough to the target tick
	 * number. This test depends only on comparing the current and target tick numbers; it does not depend on any
	 * particular kind of motor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric02();

	/**
	 * Test that slowing down should not be in effect when the current tick number exceeds the total number of ticks we
	 * were supposed to move. This test depends only on comparing the current and target tick numbers, so it does not
	 * depend on any particular kind of motor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric03();

	/**
	 * Test that slowing down should stop when the current tick number exceeds the total number of ticks we were
	 * supposed to move. This test depends only on comparing the current and target tick numbers, so it does not depend
	 * on any particular kind of motor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric04();


	/**
	 * Test that slowing down should stop when the target tick number is negative but the current tick number is
	 * positive. This test depends only on comparing the current and target tick numbers, so it does not depend on any
	 * particular kind of motor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric06();

	/**
	 * Test that slowing down should not be running when the target tick number is negative and the current tick number
	 * is negative but not sufficiently negative to have started the slowdown. This test depends only on comparing the
	 * current and target tick numbers, so it does not depend on any particular kind of motor.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric07();

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when enough ticks have passed (negative numbers)
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric09();

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns false when the motor is not close enough to the target tick
	 * number (negative numbers)
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric10();

	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric11();

	/**
	 * Test that isSlowDownToEncoderTicksRunning returns true when enough ticks have passed to start the ramp but not
	 * enough have passed to finish the required movement.
	 */
	@Test
	void testIsSlowDownToEncoderTicksRunningGeneric12();


	@Test
	void testIsRampUpToEncoderTicksDone01();

	@Test
	void testIsRampUpToEncoderTicksDone02();

	@Test
	void testIsRampUpToEncoderTicksDone03();

	/**
	 * Test that having reached the maximum power (having changes to power be within tolerance) does not cause the loop
	 * to stop - it should continue until the number of ticks is reached.
	 */
	@Test
	void testIsRampUpToEncoderTicksDone04();

	/**
	 * Test that moving exactly the right amount causes the check to stop.
	 */
	@Test
	void testIsRampUpToEncoderTicksDone05();

	/**
	 * Test that reaching the target power by itself does not cause the check to stop.
	 */
	@Test
	void testIsRampUpToEncoderTicksDone06();

	/**
	 * Test that a negative power delta by itself does not cause the check to stop.
	 */
	@Test
	void testIsRampUpToEncoderTicksDone07();

	/**
	 * Test that a negative power by itself does not cause the check to stop.
	 */
	@Test
	void testIsRampUpToEncoderTicksDone08();

	@Test
	void whenWeHaveNotMovedEnough_thenSpeedUpContinues();

	@Test
	void whenWeHaveNotMoved_thenSpeedUpContinues();

	@Test
	void whenWeHaveMovedMoreThanEnough_thenSpeedUpStops();

	@Test
	void testSetDirection();


	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	void runOpMode();
}
