package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

interface MotorInterface {

	int getCurrentTickNumber();

	HardwareMap getHardwareMap();

	/**
	 * Get the underlying DcMotor object.
	 * <p>
	 * The underlying DcMotor object is needed for operations such as setPower() and getCurrentPosition().
	 *
	 * @return
	 */
	DcMotor getMotor();

	LinearOpMode getOpMode();

	public PowerVector getCurrentPowerVector();

	NonNegativeDouble getRevolutionsPerMinute();

	NonNegativeDouble getRevolutionsPerMinute(PowerMagnitude powerMagnitude);


	/**
	 * @return The number of ticks in a millisecond at maximum power.
	 */
	NonNegativeDouble getTicksPerMillisecond();

	/**
	 * @param powerMagnitude
	 * 		The magnitude of motor power for which the caller wants to know the number of ticks in a power change
	 * 		cycle.
	 *
	 * @return The number of ticks in a power change cycle when the motor is operating at powerMagnitude
	 */
	NonNegativeDouble getTicksPerMillisecond(PowerMagnitude powerMagnitude);

	NonNegativeDouble getTicksPerMinute();

	NonNegativeDouble getTicksPerMinute(PowerMagnitude powerMagnitude);

	NonNegativeDouble getTicksPerRevolution();

	NonNegativeDouble getTicksPerSecond();

	NonNegativeDouble getTicksPerSecond(PowerMagnitude powerMagnitude);

	/**
	 * Set the power level of the motor without ramping.
	 * <p>
	 * Power is expressed as a fraction of the maximum possible power / speed supported according to the run mode in
	 * which the motor is operating.
	 * <p>
	 * Setting a power level of zero will brake the motor.
	 *
	 * @param powerVector
	 * 		The new power level of the motor, a value in the interval [-1.0, 1.0]
	 */
	void setPowerVector(PowerVector powerVector);

	void setStallDetectionToleranceInTicks(NonNegativeInteger ticks);

}
