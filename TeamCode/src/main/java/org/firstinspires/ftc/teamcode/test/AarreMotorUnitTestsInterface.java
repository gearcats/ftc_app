package org.firstinspires.ftc.teamcode.test;

interface AarreMotorUnitTestsInterface {

	void AarreMotorUnitTests();

	void testGetProportionPowerNew01();

	void testGetPowerVectorNew02();

	void testGetPowerVectorNew03();

	void testGetPowerVectorNew04();

	void testGetPowerVectorNew05();

	void testGetPowerVectorNew06();

	void testGetPowerVectorNew07();

	void testGetPowerVectorNew08();

	void testGetPowerVectorNew09();

	/**
	 * Test that the new proportion of power increases correctly from 0 up.
	 */
	void testGetPowerVectorNew10();

	/**
	 * Test that the new proportion of power does not increase beyond 1.0
	 */
	void testGetPowerVectorNew11();


	/**
	 * Test that isRampDownToEncoderTicksRunning returns true when the motor is close enough to the target tick number.
	 */
	void testIsRampDownToEncoderTicksRunning01();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when the motor is not close enough to the target tick
	 * number.
	 */
	void testIsRampDownToEncoderTicksRunning02();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when enough ticks have passed
	 */
	void testIsRampDownToEncoderTicksRunning03();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when enough ticks have passed to finish the required
	 * movement.
	 */
	void testIsRampDownToEncoderTicksRunning04();

	void testIsRampDownToEncoderTicksRunning05();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when not enough ticks have passed to start the required
	 * movement.
	 */
	void testIsRampDownToEncoderTicksRunning06();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when not enough ticks have passed to start the required
	 * movement (negative numbers).
	 */
	void testIsRampDownToEncoderTicksRunning07();

	void testIsRampDownToEncoderTicksRunning08();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when enough ticks have passed (negative numbers)
	 */
	void testIsRampDownToEncoderTicksRunning09();

	/**
	 * Test that isRampDownToEncoderTicksRunning returns false when the motor is not close enough to the target tick
	 * number (negative numbers)
	 */
	void testIsRampDownToEncoderTicksRunning10();

	void testIsRampDownToEncoderTicksRunning11();

	void testIsRampUpToEncoderTicksDone01();

	void testIsRampUpToEncoderTicksDone02();

	void testIsRampUpToEncoderTicksDone03();

	/**
	 * Test that having reached the maximum power (having changes to power be within tolerance) does not cause the loop
	 * to stop - it should continue until the number of ticks is reached.
	 */
	void testIsRampUpToEncoderTicksDone04();

	/**
	 * Test that moving exactly the right amount causes the check to stop.
	 */
	void testIsRampUpToEncoderTicksDone05();

	/**
	 * Test that reaching the target power by itself does not cause the check to stop.
	 */
	void testIsRampUpToEncoderTicksDone06();

	/**
	 * Test that a negative power delta by itself does not cause the check to stop.
	 */
	void testIsRampUpToEncoderTicksDone07();

	/**
	 * Test that a negative power by itself does not cause the check to stop.
	 */
	void testIsRampUpToEncoderTicksDone08();

	/**
	 * Test that a negative number of ticks does not cause the check to stop.
	 */
	void testIsRampUpToEncoderTicksDone09();

	/**
	 * Test that a negative tick maximum does not cause the loop to stop
	 */
	void testIsRampUpToEncoderTicksDone10();

	/**
	 * Test that a negative tick maximum does cause the loop to stop after it is exceeded
	 */
	void testIsRampUpToEncoderTicksDone11();

	void testSetDirection();

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	void runOpMode();
}
