package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * A pair of motors used for driving the robot
 */
public class AarreDriveMotors {

	static final double P_TURN_COEFF  = 0.1;     // Larger is more responsive, but also less stable
	static final double P_DRIVE_COEFF = 0.15;     // Larger is more responsive, but also less stable

	/**
	 * As tight as we can make it with an integer gyro
	 */
	static final double HEADING_THRESHOLD = 1.0;

	private static final double DEFAULT_POWER_INCREMENT_ABSOLUTE   = 0.1;
	private static final int    DEFAULT_MILLISECONDS_CYCLE_LENGTH  = 50;
	private static final double DEFAULT_PROPORTION_POWER_TOLERANCE = 0.01;
	AarreTelemetry        telemetry;
	AarreMotor            leftMotor;
	AarreMotor            rightMotor;
	HardwareMap           hardwareMap;
	LinearOpMode          opMode;
	ModernRoboticsI2cGyro gyro;

	public AarreDriveMotors(LinearOpMode opMode) {

		telemetry = new AarreTelemetry(opMode.telemetry);
		if (telemetry == null) {
			throw new AssertionError("Unexpected null object: telemetry");
		}

		hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			throw new AssertionError("Unexpected null object: hardwareMap");
		}

		this.opMode = opMode;
		if (opMode == null) {
			throw new AssertionError("Unexpected null object: opMode");
		}

		leftMotor = new AarreMotor(opMode, "left");
		rightMotor = new AarreMotor(opMode, "right");

		gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

		// Configure drive motors such that a positive power command moves them forwards
		// and causes the encoders to count UP. Note that, as in most robots, the drive
		// motors are mounted in opposite directions. May need to be reversed if using AndyMark motors.

		leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
		rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

		// Set all motors to zero power

		rampPowerTo(0.0);

		// This code REQUIRES that you have encoders on the wheel motors

		leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}

	/**
	 * Get desired steering force.
	 *
	 * @param error
	 * 		Error angle in robot relative degrees
	 * @param PCoeff
	 * 		Proportional Gain Coefficient
	 *
	 * @return Desired steering force.  +/- 1 range.  +ve = steer left
	 */
	public static double getSteer(final double error, final double PCoeff) {
		return Range.clip(error * PCoeff, -1.0, 1.0);
	}

	/**
	 * Perform a relative move, based on encoder counts.
	 * Encoders are not reset because the move is based on the current position.
	 * Move will stop if any of three conditions occur:
	 * 1) Move gets to the desired position
	 * 2) Move runs out of time
	 * 3) Driver stops the OpMode running.
	 */
	final void drive(final double proportionPower, final double inchesTravelLeft, final double inchesTravelRight, final double secondsTimeout) {

		final int newLeftTarget;
		final int newRightTarget;

		// Determine new target position, and pass to motor controller
		newLeftTarget = leftMotor.getCurrentTickNumber() + (int) (inchesTravelLeft * AarreMotor.getTicksPerInch());
		newRightTarget = rightMotor.getCurrentTickNumber() + (int) (inchesTravelRight * AarreMotor.getTicksPerInch());
		leftMotor.setTargetPosition(newLeftTarget);
		rightMotor.setTargetPosition(newRightTarget);

		// Turn On RUN_TO_POSITION
		leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		// reset the timeout time and start motion.
		final ElapsedTime runtime = new ElapsedTime();
		rampPowerTo(proportionPower);

		// keep looping while we are still active, and there is time left, and both motors are running.
		// Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
		// its target position, the motion will stop.  This is "safer" in the event that the robot will
		// always end the motion as soon as possible.
		// However, if you require that BOTH motors have finished their moves before the robot continues
		// onto the next step, use (isBusy() || isBusy()) in the loop test.
		while ((runtime.seconds() < secondsTimeout) && leftMotor.isBusy() && rightMotor.isBusy() && opMode.opModeIsActive()) {

			//telemetry.log("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
			//telemetry.log("Path2",  "Running at %7d :%7d", leftMotor.getCurrentTickNumber(), rightMotor.getCurrentTickNumber());

		}

		// Stop all motion;
		leftMotor.rampPowerTo(0.0);
		rightMotor.rampPowerTo(0.0);

		// Turn off RUN_TO_POSITION
		leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

	}

	/**
	 * getError determines the error between the target angle and the robot's current heading
	 *
	 * @param targetAngle
	 * 		Desired angle (relative to global reference established at last Gyro Reset).
	 *
	 * @return error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
	 * 		+ve error means the robot should turn LEFT (CCW) to reduce error.
	 */
	public double getError(final double targetAngle) {

		double robotError;

		// calculate error in -179 to +180 range  (
		robotError = targetAngle - gyro.getIntegratedZValue();
		while (robotError > 180.0) {
			robotError -= 360.0;
		}
		while (robotError <= -180.0) {
			robotError += 360.0;
		}
		return robotError;
	}

	/**
	 * Method to drive on a fixed compass bearing (angle), based on encoder counts.
	 * Move will stop if either of these conditions occur:
	 * 1) Move gets to the desired position
	 * 2) Driver stops the opmode running.
	 *
	 * @param speed
	 * 		Target speed for forward motion.  Should allow for _/- variance for adjusting heading
	 * @param distance
	 * 		Distance (in inches) to move from current position.  Negative distance means move
	 * 		backwards.
	 * @param angle
	 * 		Absolute Angle (in Degrees) relative to last gyro reset.
	 * 		0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
	 * 		If a relative angle is required, add/subtract from current heading.
	 */
	public void gyroDrive(final double speed, final double distance, final double angle) {

		final int newLeftTarget;
		final int newRightTarget;
		final int moveCounts;
		double    max;
		double    error;
		double    steer;
		double    leftSpeed;
		double    rightSpeed;

		// Ensure that the opmode is still active
		if (opMode.opModeIsActive()) {

			// Determine new target position, and pass to motor controller
			//noinspection NumericCastThatLosesPrecision
			moveCounts = (int) (distance * AarreMotor.TICKS_PER_INCH);
			newLeftTarget = leftMotor.getCurrentTickNumber() + moveCounts;
			newRightTarget = rightMotor.getCurrentTickNumber() + moveCounts;

			// Set Target and Turn On RUN_TO_POSITION
			leftMotor.setTargetPosition(newLeftTarget);
			rightMotor.setTargetPosition(newRightTarget);

			leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

			// start motion.
			final double adjustedSpeed = Range.clip(Math.abs(speed), 0.0, 1.0);
			leftMotor.rampPowerTo(adjustedSpeed);
			rightMotor.rampPowerTo(adjustedSpeed);

			// keep looping while we are still active, and BOTH motors are running.
			while (opMode.opModeIsActive() && (leftMotor.isBusy() && rightMotor.isBusy())) {

				// adjust relative speed based on heading error.
				error = getError(angle);
				steer = getSteer(error, P_DRIVE_COEFF);

				// if driving in reverse, the motor correction also needs to be reversed
				if (distance < 0.0) {
					steer *= -1.0;
				}

				leftSpeed = adjustedSpeed - steer;
				rightSpeed = adjustedSpeed + steer;

				// Normalize speeds if either one exceeds +/- 1.0;
				max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
				if (max > 1.0) {
					leftSpeed /= max;
					rightSpeed /= max;
				}

				leftMotor.rampPowerTo(leftSpeed);
				rightMotor.rampPowerTo(rightSpeed);

				// Display drive status for the driver.
				telemetry.addData("Err/St", "%5.1f/%5.1f", error, steer);
				telemetry.addData("Target", "%7d:%7d", newLeftTarget, newRightTarget);
				telemetry.addData("Actual", "%7d:%7d", leftMotor.getCurrentTickNumber(), rightMotor.getCurrentTickNumber());
				telemetry.addData("Speed", "%5.2f:%5.2f", leftSpeed, rightSpeed);
				telemetry.update();
			}

			// Stop all motion;
			leftMotor.rampPowerTo(0.0);
			rightMotor.rampPowerTo(0.0);

			// Turn off RUN_TO_POSITION
			leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}


	/**
	 * Method to obtain & hold a heading for a finite amount of time
	 * Move will stop once the requested time has elapsed
	 *
	 * @param proportionPower
	 * 		Desired speed of turn.
	 * @param angle
	 * 		Absolute Angle (in Degrees) relative to last gyro reset.
	 * 		0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
	 * 		If a relative angle is required, add/subtract from current heading.
	 * @param holdTime
	 * 		Length of time (in seconds) to hold the specified heading.
	 */
	public void gyroHold(final double proportionPower, final double angle, final double holdTime) {

		final ElapsedTime holdTimer = new ElapsedTime();

		// keep looping while we have time remaining.
		holdTimer.reset();
		while (opMode.opModeIsActive() && (holdTimer.time() < holdTime)) {
			// Update telemetry & Allow time for other processes to run.
			isOnHeading(proportionPower, angle, P_TURN_COEFF);
			telemetry.update();
		}

		// Stop all motion;
		leftMotor.rampPowerTo(0.0);
		rightMotor.rampPowerTo(0.0);
	}

	/**
	 * Method to spin on central axis to point in a new direction.
	 * Move will stop if either of these conditions occur:
	 * 1) Move gets to the heading (angle)
	 * 2) Driver stops the opmode running.
	 *
	 * @param proportionPowerRequested
	 * 		Desired speed of turn.
	 * @param angle
	 * 		Absolute Angle (in Degrees) relative to last gyro reset.
	 * 		0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
	 * 		If a relative angle is required, add/subtract from current heading.
	 */
	public void gyroTurn(final double proportionPowerRequested, final double angle) {

		// keep looping while we are still active, and not on heading.
		while (opMode.opModeIsActive() && !isOnHeading(proportionPowerRequested, angle, P_TURN_COEFF)) {
			// Update telemetry & Allow time for other processes to run.
			telemetry.update();
		}
	}

	/**
	 * Perform one cycle of closed loop heading control.
	 *
	 * @param speed
	 * 		Desired speed of turn.
	 * @param angle
	 * 		Absolute Angle (in Degrees) relative to last gyro reset.
	 * 		0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
	 * 		If a relative angle is required, add/subtract from current heading.
	 * @param PCoeff
	 * 		Proportional Gain coefficient
	 *
	 * @return {@code true} if the angular error is less than the heading threshold
	 * 		(i.e., the robot is on the right heading).
	 * 		{@code false} otherwise.
	 */
	boolean isOnHeading(final double speed, final double angle, final double PCoeff) {
		final double error;
		final double steer;
		boolean      onTarget = false;
		final double leftSpeed;
		final double rightSpeed;

		// determine turn power based on +/- error
		error = getError(angle);

		if (Math.abs(error) <= HEADING_THRESHOLD) {
			steer = 0.0;
			leftSpeed = 0.0;
			rightSpeed = 0.0;
			onTarget = true;
		} else {
			steer = getSteer(error, PCoeff);
			rightSpeed = speed * steer;
			leftSpeed = -rightSpeed;
		}

		// Send desired speeds to motors.
		leftMotor.rampPowerTo(leftSpeed);
		rightMotor.rampPowerTo(rightSpeed);

		// Display it for the driver.
		telemetry.addData("Target", "%5.2f", angle);
		telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
		telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

		return onTarget;
	}


	/**
	 * Ramp the motors to the same power level using default parameters
	 */
	public void rampPowerTo(double power) {
		rampPowerTo(power, power);

	}

	/**
	 * Ramp the motors to potentially different power levels using default parameters.
	 *
	 * @param proportionPowerRequestedLeft
	 * @param proportionPowerRequestedRight
	 */
	private void rampPowerTo(final double proportionPowerRequestedLeft, final double proportionPowerRequestedRight) {

		rampPowerTo(proportionPowerRequestedLeft, proportionPowerRequestedRight, DEFAULT_POWER_INCREMENT_ABSOLUTE, DEFAULT_MILLISECONDS_CYCLE_LENGTH, DEFAULT_PROPORTION_POWER_TOLERANCE);
	}


	/**
	 * Ramp the motor power up (or down) gradually to the requested amount.
	 * <p>
	 * The idea is to prevent slipping, sliding, jerking, wheelies, etc. due to
	 * excessive acceleration/deceleration.
	 *
	 * @param proportionPowerRequestedLeft
	 * 		The power to which the caller would like to ramp the left motor
	 * 		at the end of the ramp. Positive values indicate forward power;
	 * 		Negative values indicate reverse power. The value is expected to
	 * 		be in the interval [-1, 1].
	 * @param proportionPowerRequestedRight
	 * 		The power to which the caller would like to ramp the right motor
	 * 		at the end of the ramp. Positive values indicate forward power;
	 * 		Negative values indicate reverse power. The value is expected to
	 * 		be in the interval [-1, 1].
	 * @param powerIncrementAbsolute
	 * 		How much to increase or decrease the power during each cycle.
	 * 		The value is expected to be in the interval [0, 1].
	 * @param millisecondsCycleLength
	 * 		The length of each cycle in milliseconds.
	 * @param proportionPowerTolerance
	 * 		If the actual motor power is at least this close to the requested
	 * 		motor power, then we stop incrementing the power.
	 */
	public void rampPowerTo(final double proportionPowerRequestedLeft, final double proportionPowerRequestedRight, final double powerIncrementAbsolute, final int millisecondsCycleLength, final double proportionPowerTolerance) {

		ElapsedTime runtime;

		double proportionPowerCurrentLeft;
		double proportionPowerCurrentRight;

		double powerDeltaLeft;
		double greatestPowerDelta;
		double powerDeltaRight;

		double directionLeft;
		double directionRight;

		double powerIncrementLeft;
		double powerIncrementRight;

		double proportionPowerNewLeft;
		double proportionPowerNewRight;

		double millisecondsSinceChange;

		greatestPowerDelta = 1.0;

		while ((greatestPowerDelta > proportionPowerTolerance) && opMode.opModeIsActive()) {

			proportionPowerCurrentLeft = leftMotor.getProportionPowerCurrent();
			proportionPowerCurrentRight = rightMotor.getProportionPowerCurrent();

			proportionPowerNewLeft = leftMotor.getProportionPowerNew(proportionPowerRequestedLeft, proportionPowerCurrentLeft, powerIncrementAbsolute);
			proportionPowerNewRight = rightMotor.getProportionPowerNew(proportionPowerRequestedRight, proportionPowerCurrentRight, powerIncrementAbsolute);

			powerDeltaLeft = proportionPowerRequestedLeft - proportionPowerCurrentLeft;
			powerDeltaRight = proportionPowerRequestedRight - proportionPowerCurrentRight;

			greatestPowerDelta = Math.max(powerDeltaLeft, powerDeltaRight);

			leftMotor.setProportionPower(proportionPowerNewLeft);
			rightMotor.setProportionPower(proportionPowerNewRight);

			runtime = new ElapsedTime();
			millisecondsSinceChange = 0.0;

			while ((millisecondsSinceChange < (double) millisecondsCycleLength) && opMode.opModeIsActive()) {
				// Wait until it is time for next power increment
				opMode.sleep((long) millisecondsSinceChange);
				millisecondsSinceChange = runtime.milliseconds();
			}

		}

	}

}
