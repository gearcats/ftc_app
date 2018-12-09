package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

interface MotorInterface {

	// How much tolerance to allow when deciding whether we have reached a requested motor power
	static final PowerMagnitude DEFAULT_PROPORTION_POWER_TOLERANCE = new PowerMagnitude(0.01);

	// How long to allow a motor operation to continue before timing out
	static final NonNegativeDouble DEFAULT_SECONDS_TIMEOUT = new NonNegativeDouble(5.0);

	static final NonNegativeInteger MILLISECONDS_PER_SECOND = new NonNegativeInteger(1000);

	static final NonNegativeInteger SECONDS_PER_MINUTE = new NonNegativeInteger(60);

	public PowerVector getCurrentPowerVector();

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

	public NonNegativeDouble getRevolutionsPerMinute(PowerMagnitude powerMagnitude);

	public NonNegativeDouble getRevolutionsPerMinute();

	public NonNegativeDouble getTicksPerMillisecond(PowerMagnitude powerMagnitude);

	public NonNegativeDouble getTicksPerMillisecond();

	public NonNegativeDouble getTicksPerMinute();

	public NonNegativeDouble getTicksPerMinute(PowerMagnitude powerMagnitude);

	public NonNegativeDouble getTicksPerRevolution();

	public NonNegativeDouble getTicksPerSecond();

	public NonNegativeDouble getTicksPerSecond(PowerMagnitude powerMagnitude);

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
