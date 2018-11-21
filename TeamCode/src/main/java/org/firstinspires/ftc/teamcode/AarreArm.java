package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class AarreArm {

	private static final double PROPORTION_POWER_DEFAULT = 1.0;
	private static final double SECONDS_TO_RUN_DEFAULT = 1.0;
	private static final double SECONDS_BEFORE_TIMEOUT_DEFAULT = 0.1;

	private AarreMotor motor;
	private AarreTelemetry telemetry;

	private double currentPosition;

	/**
	 * This empty constructor is useful for testing.
	 */
	public AarreArm() {

	}

	/**
	 * Construct an instance of AarreArm with telemetry.
	 *
	 * @param nameOfRiserMotor
	 * 		The name of the motor that controls the riser.
	 * @param hardwareMap
	 * 		The hardware map upon which the motor may be found.
	 * @param telemetry
	 * 		An instance of AarreTelemetry to associate with the
	 */
	public AarreArm(final HardwareMap hardwareMap, final String nameOfRiserMotor, final AarreTelemetry telemetry) {

		currentPosition = 0.5; // We have no idea where the arm is

		// Make sure there is a hardwareMap parameter
		if (hardwareMap == null)
			throw new IllegalArgumentException("Unexpected null parameter: hardwareMap");


		// Make sure there is a telemetry parameter
		if (telemetry == null)
			throw new AssertionError("Unexpected null parameter: telemetry");

		this.telemetry = telemetry;

		motor = new AarreMotor(hardwareMap, nameOfRiserMotor, telemetry);

		motor.setPower(0.0);
		motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		motor.setDirection(DcMotorSimple.Direction.FORWARD);  // Positive power raises arm
		motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

	}

	/**
	 * Best guess at current position of the arm. (We can't know exactly where the arm is because
	 * the arm starts in an unknown physical position upon software initialization and does not
	 * store any state information.) A value of 0.0 indicates that we believe the arm is fully
	 * lowered, and a value of 1.0 indicates that we believe the arm is fully raised. The arm
	 * generally should be either fully lowered or fully raised (except when it is in motion), so
	 * values in the middle when the arm is stationary suggest uncertainty about where the arm
	 * really is.
	 */
	public double getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Lower the arm by the default method.
	 */
	public final void lower() {
		lowerByTime();
	}

	/**
	 * Lower the arm by the default amount of time.
	 */
	private void lowerByTime() {
		lowerByTime(PROPORTION_POWER_DEFAULT, SECONDS_TO_RUN_DEFAULT);
	}

	/**
	 * Lower the arm for a fixed amount of time.
	 *
	 * @param absolutePowerProportion
	 * 		Proportion of power to apply to motor. Must be non-negative. To raise the arm use
	 * 		raiseByTime().
	 * @param secondsToRun
	 * 		The number of seconds for which to raise the arm.
	 */
	private void lowerByTime(double absolutePowerProportion, double secondsToRun) {

		if (absolutePowerProportion < 0.0)
			throw new IllegalArgumentException("absolutePowerProportion expected to be non-negative");
		if (secondsToRun < 0.0)
			throw new IllegalArgumentException("secondsToRun expected to be non-negative");

		motor.runByTime(-absolutePowerProportion, secondsToRun);

		currentPosition = 0.0;
	}

	/**
	 * Lower the arm to its downward position while avoiding stalling the arm motor
	 */
	private void lowerUntilStalled() {
		lowerUntilStalled(PROPORTION_POWER_DEFAULT, SECONDS_TO_RUN_DEFAULT);
	}

	/**
	 * Lower the arm to its downward position while avoiding stalling the arm motor
	 */
	private void lowerUntilStalled(double absolutePowerProportion, double secondsToRun) {

		if (absolutePowerProportion < 0.0)
			throw new IllegalArgumentException("absolutePowerProportion expected to be non-negative");
		if (secondsToRun < 0.0)
			throw new IllegalArgumentException("secondsToRun expected to be non-negative");

		motor.setStallTimeLimitInSeconds(secondsToRun);
		motor.runUntilStalled(-absolutePowerProportion);
		currentPosition = 0.0;
	}

	/**
	 * Raise the arm using the default method.
	 */
	public final void raise() {
		raiseByTime();
	}

	/**
	 * Raise the arm by the default amount of time.
	 */
	private void raiseByTime() {
		raiseByTime(PROPORTION_POWER_DEFAULT, SECONDS_TO_RUN_DEFAULT);
	}

	/**
	 * Raise the arm for a fixed amount of time.
	 *
	 * @param absolutePowerProportion
	 * 		Proportion of power to apply to motor. Must be non-negative. To lower the arm use
	 * 		lowerByTime().
	 * @param secondsToRun
	 * 		The number of seconds for which to raise the arm. Must be non-negative.
	 */
	private void raiseByTime(double absolutePowerProportion, double secondsToRun) {

		if (absolutePowerProportion < 0.0)
			throw new IllegalArgumentException("absolutePowerProportion expected to be non-negative");
		if (secondsToRun < 0.0)
			throw new IllegalArgumentException("secondsToRun expected to be non-negative");

		motor.runByTime(absolutePowerProportion, secondsToRun);

		currentPosition = 1.0;
	}

	/**
	 * Raise the arm to its upward position while avoiding stalling the arm motor
	 */
	private void raiseUntilStalled() {
		raiseUntilStalled(PROPORTION_POWER_DEFAULT, SECONDS_BEFORE_TIMEOUT_DEFAULT);
	}

	/**
	 * Raise the arm to its upward position while avoiding stalling the arm motor
	 */
	private void raiseUntilStalled(double absolutePowerProportion, double secondsBeforeTimeout) {

		if (absolutePowerProportion < 0.0)
			throw new IllegalArgumentException("absolutePowerProportion expected to be non-negative");
		if (secondsBeforeTimeout < 0.0)
			throw new IllegalArgumentException("secondsBeforeTimeout expected to be non-negative");

		motor.setStallTimeLimitInSeconds(secondsBeforeTimeout);
		motor.runUntilStalled(absolutePowerProportion);
		currentPosition = 1.0;
	}

}
