package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.Date;
import java.util.logging.*;

/**
 * A pair of motors used for driving the robot
 */
public class AarreDriveMotors {

	// Defaults
	private static final double DEFAULT_HEADING_THRESHOLD          = 1.0;
	private static final double DEFAULT_P_DRIVE_COEFFICIENT        = 0.15; // Larger is more responsive but less stable
	private static final double DEFAULT_P_TURN_COEFFICIENT         = 0.1;  // Larger is more responsive but less stable
	private static final double DEFAULT_POWER_INCREMENT_ABSOLUTE   = 0.1;
	private static final double DEFAULT_PROPORTION_POWER_TOLERANCE = 0.01;
	private static final int    DEFAULT_MILLISECONDS_CYCLE_LENGTH  = 50;

	// Private fields
	private static AarrePowerMagnitude powerIncrementAbsolute    = new AarrePowerMagnitude
			(DEFAULT_POWER_INCREMENT_ABSOLUTE);
	private static int                 cycleLengthInMilliseconds = DEFAULT_MILLISECONDS_CYCLE_LENGTH;
	private static AarrePowerMagnitude powerMagnitudeTolerance   = new AarrePowerMagnitude
			(DEFAULT_PROPORTION_POWER_TOLERANCE);

	private AarreDriveMotor       leftMotor;
	private AarreDriveMotor       rightMotor;
	private AarreTelemetry        telemetry;
	private HardwareMap           hardwareMap;
	private LinearOpMode          opMode;
	private ModernRoboticsI2cGyro gyro;

	static Logger log;

	static {
		log = Logger.getLogger(AarreMotor.class.getName());
		log.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		handler.setFormatter(new SimpleFormatter() {

			private static final String format = "%1$tF %1$tT [%2$s] %4$s::%5$s - %6$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				String formattedLogRecord = String.format(format, new Date(lr.getMillis()), lr.getLevel()
						.getLocalizedName(), lr.getLoggerName(), lr.getSourceClassName(), lr.getSourceMethodName(), lr
						.getMessage());
				return formattedLogRecord;
			}

		});
		log.addHandler(handler);
		log.setLevel(Level.ALL);
	}

	public AarreDriveMotors(LinearOpMode opMode) {

		this.opMode = opMode;

		telemetry = new AarreTelemetry(opMode.telemetry);

		log.setLevel(Level.ALL);

		/*
		  hardwareMap will be null if we are running off-robot, but for testing purposes it is
		  still helpful to instantiate this object (rather than throwing an exception, for
		  example).
		 */
		hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			leftMotor = null;
			rightMotor = null;
		} else {
			leftMotor = new AarreDriveMotor(opMode, "left");
			rightMotor = new AarreDriveMotor(opMode, "right");

			this.setPowerIncrement(DEFAULT_POWER_INCREMENT_ABSOLUTE);

			// Configure drive motors such that a positive power command moves them forwards
			// and causes the encoders to count UP. Note that, as in most robots, the drive
			// motors are mounted in opposite directions. May need to be reversed if using AndyMark motors.

			leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
			rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

			// Set all motors to zero power

			try {
				rampPowerTo(new AarrePowerVector(0.0));
			} catch (NoSuchMethodException e) {
				log.severe(e.toString());
			}

			// This code REQUIRES that you have encoders on the wheel motors

			leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
			rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

			leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}



	}

	public static AarrePowerMagnitude getPowerIncrementAbsolute() {
		return powerIncrementAbsolute;
	}

	public static int getCycleLengthInMilliseconds() {
		return cycleLengthInMilliseconds;
	}

	public static AarrePowerMagnitude getPowerMagnitudeTolerance() {
		return powerMagnitudeTolerance;
	}

	/**
	 * Get desired steering force.
	 *
	 * @param error
	 * 		Error angle in robot relative degrees
	 * @param proportionalGainCoefficient
	 * 		Proportional Gain Coefficient
	 *
	 * @return Desired steering force.  +/- 1 range.  +ve = steer left
	 * 		<p>
	 * 		TODO: Eliminate use of Range.clip here
	 */
	public static AarrePowerVector getSteer(final double error, final double proportionalGainCoefficient) {
		return new AarrePowerVector(Range.clip(error * proportionalGainCoefficient, -1.0, 1.0));
	}

	/**
	 * Perform a relative move, based on encoder counts. Encoders are not reset because the move is based on the
	 * current
	 * position. Move will stop if any of three conditions occur: 1) Move gets to the desired position 2) Move runs out
	 * of time 3) Driver stops the OpMode running.
	 *
	 * @param powerMagnitude
	 * 		The magnitude of power to apply to the wheels.
	 * @param inchesTravelLeft
	 * 		How many inches to move the left wheel. Negative values move the left wheel in reverse.
	 * @param inchesTravelRight
	 * 		How many inches to move the right wheel. Negative values move the right wheel in reverse.
	 * @param secondsTimeout
	 * 		TODO: Catch impossible combinations of inchesTravelLeft and inchesTravelRight.
	 */
	final void drive(final AarrePowerMagnitude powerMagnitude, final double inchesTravelLeft, final double inchesTravelRight, final double secondsTimeout) throws NoSuchMethodException {

		log.entering(this.getClass().getCanonicalName(), "drive");

		final int newLeftTarget;
		final int newRightTarget;

		// Determine new target position, and pass to motor controller
		newLeftTarget = leftMotor.getCurrentTickNumber() + (int) (inchesTravelLeft * leftMotor.getTicksPerInch());
		newRightTarget = rightMotor.getCurrentTickNumber() + (int) (inchesTravelRight * rightMotor.getTicksPerInch());
		leftMotor.setTargetPosition(newLeftTarget);
		rightMotor.setTargetPosition(newRightTarget);

		// Turn On RUN_TO_POSITION
		leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		// reset the timeout time and start motion.
		final ElapsedTime runtime = new ElapsedTime();

		int              leftPowerDirection  = (int) Math.signum(inchesTravelLeft);
		int              rightPowerDirection = (int) Math.signum(inchesTravelRight);
		AarrePowerVector leftPowerVector     = new AarrePowerVector(powerMagnitude, leftPowerDirection);
		AarrePowerVector rightPowerVector    = new AarrePowerVector(powerMagnitude, rightPowerDirection);

		log.fine("Calling rampPowerTo");
		rampPowerTo(leftPowerVector, rightPowerVector);
		log.fine("Returned from rampPowerTo");

		// keep looping while we are still active, and there is time left, and both motors are running.
		// Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
		// its target position, the motion will stop.  This is "safer" in the event that the robot will
		// always end the motion as soon as possible.
		// However, if you require that BOTH motors have finished their moves before the robot continues
		// onto the next step, use (isBusy() || isBusy()) in the loop test.
		while ((runtime.seconds() < secondsTimeout) && leftMotor.isBusy() && rightMotor.isBusy() && opMode
				.opModeIsActive()) {

			//telemetry.log("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
			//telemetry.log("Path2",  "Running at %7d :%7d", leftMotor.getCurrentTickNumber(), rightMotor
			// .getCurrentTickNumber());
			opMode.idle();

		}

		// Stop all motion;
		leftMotor.rampToPower(new AarrePowerVector(0.0));
		rightMotor.rampToPower(new AarrePowerVector(0.0));

		// Turn off RUN_TO_POSITION
		leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		log.exiting(this.getClass().getCanonicalName(), "drive");

	}

	/**
	 * getError determines the error between the target angle and the robot's current heading
	 *
	 * @param targetAngle
	 * 		Desired angle (relative to global reference established at last Gyro Reset).
	 *
	 * @return error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference +ve error means
	 * the
	 * 		robot should turn LEFT (CCW) to reduce error.
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
	 * Method to drive on a fixed compass bearing (angle), based on encoder counts. Move will stop if either of these
	 * conditions occur: 1) Move gets to the desired position 2) Driver stops the opmode running.
	 *
	 * @param powerMagnitude
	 * 		Target power vector for forward motion.  Should allow for _/- variance for adjusting heading
	 * @param inchesTravelDistanceAndDirection
	 * 		Distance (in inches) to move from current position.  Negative distance means move backwards.
	 * @param angle
	 * 		Absolute Angle (in Degrees) relative to last gyro reset. 0 = fwd. +ve is CCW from fwd. -ve is CW from
	 * 		forward.
	 * 		If a relative angle is required, add/subtract from current heading.
	 * 		<p>
	 * 		TODO: This is buggy, half is RUN_BY_ENCODER, half not
	 */
	public void gyroDrive(final AarrePowerMagnitude powerMagnitude, final double inchesTravelDistanceAndDirection, final double angle) throws NoSuchMethodException {

		final int        tickNumberTargetLeft;
		final int        tickNumberTargetRight;
		final int        numberOfTicksToMove;
		double           max;
		double           error;
		AarrePowerVector steer;
		AarrePowerVector leftPowerVector;
		AarrePowerVector rightPowerVector;

		// Ensure that the opmode is still active
		if (opMode.opModeIsActive()) {

			// Determine new target position, and pass to motor controller
			numberOfTicksToMove = (int) (inchesTravelDistanceAndDirection * leftMotor.getTicksPerInch());
			tickNumberTargetLeft = leftMotor.getCurrentTickNumber() + numberOfTicksToMove;
			tickNumberTargetRight = rightMotor.getCurrentTickNumber() + numberOfTicksToMove;

			int                    powerDirection      = (int) Math.signum(inchesTravelDistanceAndDirection);
			final AarrePowerVector powerVectorAdjusted = new AarrePowerVector(powerMagnitude, powerDirection);

			// TODO: Need to write a function to determine when to stop this loop
			boolean continueLoop = false;

			// keep looping while we are still active, and BOTH motors are running.
			while (opMode.opModeIsActive() && continueLoop) {

				// adjust relative powerMagnitude based on heading error.
				error = getError(angle);
				steer = getSteer(error, DEFAULT_P_DRIVE_COEFFICIENT);

				// if driving in reverse, the motor correction also needs to be reversed
				if (inchesTravelDistanceAndDirection < 0.0) {
					steer.reverseDirection();
				}

				leftPowerVector = powerVectorAdjusted.subtract(steer);
				rightPowerVector = powerVectorAdjusted.add(steer);

				// Normalize speeds if either one exceeds +/- 1.0;
				// TODO: Avoid allowing speeds to go outside the bounds of +/- 1.0
				max = Math.max(leftPowerVector.asDouble(), rightPowerVector.asDouble());
				if (max > 1.0) {
					leftPowerVector = leftPowerVector.divideBy(max);
					rightPowerVector = rightPowerVector.divideBy(max);
				}

				this.rampPowerTo(leftPowerVector, rightPowerVector);


				// Display drive status for the driver.
				telemetry.addData("Err/St", "%5.1f/%5.1f", error, steer);
				telemetry.addData("Target", "%7d:%7d", tickNumberTargetLeft, tickNumberTargetRight);
				telemetry.addData("Actual", "%7d:%7d", leftMotor.getCurrentTickNumber(), rightMotor
						.getCurrentTickNumber());
				telemetry.addData("Speed", "%5.2f:%5.2f", leftPowerVector, rightPowerVector);
				telemetry.update();
			}

			// Stop all motion;
			this.rampPowerTo(new AarrePowerVector(0.0));

			// Turn off RUN_TO_POSITION
			leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}


	/**
	 * Method to obtain & hold a heading for a finite amount of time Move will stop once the requested time has elapsed
	 *
	 * @param powerVector
	 * 		Desired speed of turn.
	 * @param angle
	 * 		Absolute Angle (in Degrees) relative to last gyro reset. 0 = fwd. +ve is CCW from fwd. -ve is CW from
	 * 		forward.
	 * 		If a relative angle is required, add/subtract from current heading.
	 * @param holdTime
	 * 		Length of time (in seconds) to hold the specified heading.
	 */
	public void gyroHold(final AarrePowerVector powerVector, final double angle, final double holdTime) throws
			NoSuchMethodException {

		final ElapsedTime holdTimer = new ElapsedTime();

		// keep looping while we have time remaining.
		holdTimer.reset();
		while (opMode.opModeIsActive() && (holdTimer.time() < holdTime)) {
			// Update telemetry & Allow time for other processes to run.
			isOnHeading(powerVector, angle, DEFAULT_P_TURN_COEFFICIENT);
			telemetry.update();
		}

		// Stop all motion;
		leftMotor.rampToPower(new AarrePowerVector(0.0));
		rightMotor.rampToPower(new AarrePowerVector(0.0));
	}

	/**
	 * Method to spin on central axis to point in a new direction. Move will stop if either of these conditions occur:
	 * 1) Move gets to the heading (angle) 2) Driver stops the opmode running.
	 *
	 * @param powerVectorRequested
	 * 		Desired speed of turn.
	 * @param angle
	 * 		Absolute Angle (in Degrees) relative to last gyro reset. 0 = fwd. +ve is CCW from fwd. -ve is CW from
	 * 		forward.
	 * 		If a relative angle is required, add/subtract from current heading.
	 */
	public void gyroTurn(final AarrePowerVector powerVectorRequested, final double angle) throws
			NoSuchMethodException {

		// keep looping while we are still active, and not on heading.
		while (opMode.opModeIsActive() && !isOnHeading(powerVectorRequested, angle, DEFAULT_P_TURN_COEFFICIENT)) {
			// Update telemetry & Allow time for other processes to run.
			telemetry.update();
		}
	}

	/**
	 * Perform one cycle of closed loop heading control.
	 *
	 * @param powerVector
	 * 		Desired speed of turn.
	 * @param angle
	 * 		Absolute Angle (in Degrees) relative to last gyro reset. 0 = fwd. +ve is CCW from fwd. -ve is CW from
	 * 		forward.
	 * 		If a relative angle is required, add/subtract from current heading.
	 * @param proportionalGainCoefficient
	 * 		Proportional Gain coefficient
	 *
	 * @return {@code true} if the angular error is less than the heading threshold (i.e., the robot is on the right
	 * 		heading). {@code false} otherwise.
	 */
	boolean isOnHeading(final AarrePowerVector powerVector, final double angle, final double proportionalGainCoefficient) throws NoSuchMethodException {
		final double           error;
		final AarrePowerVector steer;

		boolean                onTarget = false;
		final AarrePowerVector leftPowerVector;
		final AarrePowerVector rightPowerVector;

		// determine turn power based on +/- error
		error = getError(angle);

		if (Math.abs(error) <= DEFAULT_HEADING_THRESHOLD) {
			// Keep on truckin'...
			leftPowerVector = powerVector;
			rightPowerVector = powerVector;
			onTarget = true;
		} else {
			steer = getSteer(error, proportionalGainCoefficient);
			rightPowerVector = powerVector.multiplyBy(steer);
			leftPowerVector = new AarrePowerVector(rightPowerVector);
			leftPowerVector.reverseDirection();
		}

		// Send desired speeds to motors.
		this.rampPowerTo(leftPowerVector, rightPowerVector);

		// Display it for the driver.
		telemetry.addData("Target", "%5.2f", angle);
		telemetry.addData("Err/St", "%5.2f/%5.2f", error, powerVector);
		telemetry.addData("Speed.", "%5.2f:%5.2f", leftPowerVector, rightPowerVector);

		return onTarget;
	}


	/**
	 * Ramp the motors to the same power level using default parameters
	 */
	public void rampPowerTo(AarrePowerVector power) throws NoSuchMethodException {
		rampPowerTo(power, power);

	}


	/**
	 * Ramp the motors gradually up (or down) to potentially different power levels.
	 * <p>
	 * The idea is to prevent slipping, sliding, jerking, wheelies, etc. due to excessive acceleration/deceleration.
	 *
	 * @param powerVectorRequestedLeft
	 * 		The power to which the caller would like the left motor to reach by the end of the ramp. Positive values
	 * 		indicate forward power; negative values indicate reverse power. The value is expected to be in the interval
	 * 		[-1, 1].
	 * @param powerVectorRequestedRight
	 * 		The power to which the caller would like the right motor to reach by the end of the ramp. Positive values
	 * 		indicate forward power; Negative values indicate reverse power. The value is expected to be in the interval
	 * 		[-1, 1].
	 * @param powerIncrementAbsolute
	 * 		How much to increase or decrease the power during each cycle. The value is expected to be in the interval
	 * 		[0,	1].
	 * @param millisecondsCycleLength
	 * @param powerMagnitudeTolerance
	 * 		If the actual motor power is at least this close to the requested motor power, then we stop incrementing
	 * 		the	power.
	 */
	public void rampPowerTo(final AarrePowerVector powerVectorRequestedLeft, final AarrePowerVector powerVectorRequestedRight) throws NoSuchMethodException {

		log.entering(getClass().getCanonicalName(), getClass().getMethod("rampPowerTo", AarrePowerVector.class,
				AarrePowerVector.class).getName());

		AarrePowerMagnitude powerMagnitudeTolerance = getPowerMagnitudeTolerance();

		AarrePowerVector powerVectorCurrentLeft;
		AarrePowerVector powerVectorCurrentRight;

		AarrePowerVector powerDeltaLeftVector;
		AarrePowerVector powerDeltaRightVector;

		AarrePowerMagnitude powerDeltaLeftMagnitude;
		AarrePowerMagnitude powerDeltaRightMagnitude;

		double              greatestPowerDeltaDouble;
		AarrePowerMagnitude greatestPowerDeltaMagnitude;

		AarrePowerVector powerVectorNewLeft;
		AarrePowerVector powerVectorNewRight;

		greatestPowerDeltaMagnitude = new AarrePowerMagnitude(1.0);

		while ((greatestPowerDeltaMagnitude.isGreaterThan(powerMagnitudeTolerance)) && opMode.opModeIsActive()) {

			powerVectorCurrentLeft = leftMotor.getPowerVectorCurrent();
			powerVectorCurrentRight = rightMotor.getPowerVectorCurrent();

			powerVectorNewLeft = leftMotor.getPowerVectorNew(powerVectorRequestedLeft, powerVectorCurrentLeft);
			powerVectorNewRight = rightMotor.getPowerVectorNew(powerVectorRequestedRight, powerVectorCurrentRight);

			powerDeltaLeftVector = powerVectorRequestedLeft.subtract(powerVectorCurrentLeft);
			powerDeltaRightVector = powerVectorRequestedRight.subtract(powerVectorCurrentRight);

			powerDeltaLeftMagnitude = powerDeltaLeftVector.getMagnitude();
			powerDeltaRightMagnitude = powerDeltaRightVector.getMagnitude();

			greatestPowerDeltaDouble = Math.max(powerDeltaLeftMagnitude.asDouble(), powerDeltaRightMagnitude.asDouble
					());
			greatestPowerDeltaMagnitude = new AarrePowerMagnitude(greatestPowerDeltaDouble);

			leftMotor.setPowerVector(powerVectorNewLeft);
			rightMotor.setPowerVector(powerVectorNewRight);

			waitForNextIncrement();

		}

		log.exiting(getClass().getName(), getClass().getMethod("rampPowerTo", AarrePowerVector.class, AarrePowerVector
				.class).getName());


	}


	public void setPowerIncrement(double increment) {
		AarrePowerMagnitude powerMagnitudeIncrement = new AarrePowerMagnitude(increment);
		setPowerIncrement(powerMagnitudeIncrement);
	}

	public void setPowerIncrement(AarrePowerMagnitude powerIncrementAbsolute) {
		this.powerIncrementAbsolute = powerIncrementAbsolute;
	}

	public void setCycleLengthInMilliseconds(int cycleLengthInMilliseconds) {
		this.cycleLengthInMilliseconds = cycleLengthInMilliseconds;
	}

	public void setPowerMagnitudeTolerance(AarrePowerMagnitude powerMagnitudeTolerance) {
		this.powerMagnitudeTolerance = powerMagnitudeTolerance;
	}

	private void waitForNextIncrement() {
		log.entering(getClass().getCanonicalName(), "waitForNextIncrement");

		ElapsedTime elapsedTime             = new ElapsedTime();
		double      millisecondsSinceChange = elapsedTime.milliseconds();
		while ((millisecondsSinceChange < getCycleLengthInMilliseconds()) && opMode.opModeIsActive()) {
			opMode.idle();
			millisecondsSinceChange = elapsedTime.milliseconds();
		}
		log.exiting(getClass().getCanonicalName(), "waitForNextIncrement");

	}
}
