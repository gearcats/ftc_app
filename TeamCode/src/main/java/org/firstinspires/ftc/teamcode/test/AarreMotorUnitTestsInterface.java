package org.firstinspires.ftc.teamcode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

interface AarreMotorUnitTestsInterface {
	@BeforeEach
	void AarreMotorUnitTests();

	@Test
	void testGetNumberOfCycles01();

	@Test
	void testGetNumberOfCycles02();

	@Test
	void testGetNumberOfCycles03();

	@Test
	void testGetNumberOfCycles04();

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
	 * Test calculating when to start a ramp down
	 */
	void testGetTickNumberToStartRampDown01();

	void testGetTickNumberToStartRampDown02();

	void testGetTickNumberToStartRampDown03();

	void testGetTickNumberToStartRampDown04();

	void testGetTicksPerCycle01();

	void testGetTicksPerMinute01();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when the motor is close enough to the target tick number.
	 */
	@Test
	void testIsRampDownToEncoderTicksRunning01();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when the motor is not close enough to the target tick
	 * number.
	 */
	@Test
	void testIsRampDownToEncoderTicksRunning02();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when enough ticks have passed
	 */
	@Test
	void testIsRampDownToEncoderTicksRunning03();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when enough ticks have passed to finish the required
	 * movement.
	 */
	@Test
	void testIsRampDownToEncoderTicksRunning04();

	@Test
	void testIsRampDownToEncoderTicksRunning05();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when not enough ticks have passed to start the required
	 * movement.
	 */
	@Test
	void testIsRampDownToEncoderTicksRunning06();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when not enough ticks have passed to start the required
	 * movement (negative numbers).
	 */
	@Test
	void testIsRampDownToEncoderTicksRunning07();

	void testIsRampDownToEncoderTicksRunning08();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when enough ticks have passed (negative numbers)
	 */
	@Test
	void testIsRampDownToEncoderTicksRunning09();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when the motor is not close enough to the target tick
	 * number (negative numbers)
	 */
	@Test
	void testIsRampDownToEncoderTicksRunning10();

	void testIsRampDownToEncoderTicksRunning11();

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

	/**
	 * Test that a negative number of ticks does not cause the check to stop.
	 */
	@Test
	void testIsRampUpToEncoderTicksDone09();

	/**
	 * Test that a negative tick maximum does not cause the loop to stop
	 */
	@Test
	void testIsRampUpToEncoderTicksDone10();

	/**
	 * Test that a negative tick maximum does cause the loop to stop after it is exceeded
	 */
	@Test
	void testIsRampUpToEncoderTicksDone11();

	@Test
	void testSetDirection();

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	void runOpMode();
}
