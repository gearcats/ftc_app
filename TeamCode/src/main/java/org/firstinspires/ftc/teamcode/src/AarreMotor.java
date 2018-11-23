package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This class wraps the FTC DcMotor interface / DcMotorImpl class to:
 *
 * <ul>
 * <li>Provide stall detection</li>
 * <li>Provide telemetry</li>
 * </ul>
 * <p>
 * It might be preferable (e.g., more elegant, less wrapper code needed) to extend the FTC
 * DcMotorImpl class rather than wrap it. However, it seems that extending the DcMotorImpl class is
 * not recommended. See <a href=https://ftcforum.usfirst
 * .org/forum/ftc-technology/4717-how-to-extend-the-dcmotor-class
 * ></a> for a discussion.
 * <p>
 * Stall detection and telemetry code adapted from
 * <a href="https://github.com/TullyNYGuy/FTC8863_ftc_app/blob/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Lib/FTCLib/DcMotor8863.java"></a>
 */
public class AarreMotor {

	private static final int    MILLISECONDS_PER_SECOND    = 1000;
	private static final int    TICKS_PER_MOTOR_REVOLUTION = 1440;    // eg: TETRIX Motor Encoder,
	// TorqueNado
	private static final double DRIVE_GEAR_REDUCTION       = 1.0;     // This is 1.0 for our
	// direct-drive wheels
	private static final double WHEEL_DIAMETER_INCHES      = 5.5;     // For figuring
	// circumference; could be 5.625, also depends on treads

	static final double TICKS_PER_INCH =
			(TICKS_PER_MOTOR_REVOLUTION * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

	/**
	 * These fields relate to ramping motor power gradually
	 */
	private static final double DEFAULT_POWER_INCREMENT_ABSOLUTE   = 0.1;
	private static final int    DEFAULT_MILLISECONDS_CYCLE_LENGTH  = 50;
	private static final double DEFAULT_PROPORTION_POWER_TOLERANCE = 0.01;
	private static final double DEFAULT_SECONDS_TIMEOUT            = 5.0;

	private final DcMotor        motor;
	private final AarreTelemetry telemetry;
	private final LinearOpMode   opMode;
	private final HardwareMap    hardwareMap;
	private       int            oldTickNumber;
	private       int            stallTimeLimitInMilliseconds   = 0;
	private       int            stallDetectionToleranceInTicks = 5;
	private       ElapsedTime    timeStalledInMilliseconds;

	public AarreMotor(LinearOpMode opMode, final String motorName) {

		this.opMode = opMode;

		telemetry = new AarreTelemetry(opMode.telemetry);
		if (telemetry == null) {
			throw new AssertionError("Unexpected null object: telemetry");
		}

		/**
		 * hardwareMap will be null if we are running off-robot, but for testing purposes it is
		 * still helpful to instantiate this object (rather than throwing an exception, for
		 * example).
		 */
		hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			motor = null;
		} else {
			motor = hardwareMap.get(DcMotor.class, motorName);
			motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		}

		// These are defaults. The user should customize them
		stallDetectionToleranceInTicks = 5;
		setStallTimeLimitInMilliseconds(100);

	}


	/**
	 * Get counts per inch.
	 *
	 * @return The number of encoder ticks (counts) required to make the robot travel one inch.
	 */
	public static final double getTicksPerInch() {
		return TICKS_PER_INCH;
	}


	/**
	 * Get the current proportion of power
	 *
	 * @return The proportion of power at which the motor is operating. A value in the range [-1,
	 * 		1].
	 */
	public double getProportionPowerCurrent() {
		return motor.getPower();
	}

	/**
	 * Get the proportion of power to which to change the motor when ramping power up or down.
	 * <p>
	 * Must be public to allow access from unit test suite.
	 *
	 * @return The new proportion of power to apply to the motor.
	 */
	public double getProportionPowerNew(double proportionPowerCurrent, double
			proportionPowerRequested, double powerIncrementAbsolute) {

		double direction;
		double powerDelta;
		double powerIncrement;
		double proportionPowerNew;

		powerDelta = proportionPowerRequested - proportionPowerCurrent;
		direction = Math.signum(powerDelta);
		powerIncrement = powerIncrementAbsolute * direction;
		proportionPowerNew = proportionPowerCurrent + powerIncrement;
		//proportionPowerNew = Range.clip(proportionPowerNew, -1.0, 1.0);

		return proportionPowerNew;

	}

	/**
	 * Get the current reading of the encoder for this motor.
	 * <p>
	 * Despite its name, the {@link DcMotor} method {@code getCurrentPosition} provides almost no
	 * information about position. Therefore, we use a different name here.
	 *
	 * @return The current reading of the encoder for this motor, in ticks.
	 */
	@SuppressWarnings("WeakerAccess")
	public final int getCurrentTickNumber() {
		return motor.getCurrentPosition();
	}

	public double getPower() {
		return motor.getPower();
	}

	/**
	 * Get the number of milliseconds for which the motor has been stalled.
	 *
	 * @return The integer number of milliseconds during which the motor speed has been
	 * 		lowerUntilStalled than the stall detection tolerance.
	 */
	private int getTimeStalledInMilliseconds() {

		// Take the time stalled in (double) milliseconds, round to nearest long and cast to int
		final double msStalledDbl = timeStalledInMilliseconds.time();
		return (int) Math.round(msStalledDbl);
	}

	/**
	 * Determine whether the motor is busy.
	 *
	 * @return Returns true if the motor is currently advancing or retreating to a target position.
	 */
	final boolean isBusy() {
		return motor.isBusy();
	}


	/**
	 * Detect whether the motor is stalled.
	 * <p>
	 * The motor must not have moved more than a certain number of encoder clicks during a
	 * period of
	 * at least so many milliseconds before we consider it stalled.
	 *
	 * @return {@code true} if the motor is stalled; {@code false} otherwise.
	 */
	private boolean isStalled() {

		// TODO: Implement logging framework to allow logging by severity level
		telemetry.log("Time stalled = ", "%d ms", getTimeStalledInMilliseconds());
		telemetry.log("Stall time limit = ", "%d ms", stallTimeLimitInMilliseconds);

		boolean   stalled       = false;
		final int newTickNumber = getCurrentTickNumber();

		//telemetry.log("checking for a stall", "!");

		if (Math.abs(newTickNumber - oldTickNumber) < stallDetectionToleranceInTicks) {

			// The motor has not moved since the last time the position was read.

			if (timeStalledInMilliseconds.time() > stallTimeLimitInMilliseconds) {

				// The motor has been stalled for more than the time limit

				telemetry.log("Motor stalled");
				stalled = true;

			}

		} else {

			// The motor has moved since the last time we checked the position

			// Reset the timer

			timeStalledInMilliseconds.reset();
		}

		// Save the new tick number as baseline for the next iteration

		oldTickNumber = newTickNumber;

		// Notify caller whether or not the motor is not stalled

		return stalled;

	}


	/**
	 * Rotate this motor a certain number of ticks from its current position, ramping speed up at
	 * the beginning and down at the end.
	 *
	 * @param proportionPower
	 * 		The power at which to rotate, in the interval [-1.0, 1.0].
	 * @param numberOfTicksToRotate
	 * 		Maximum number of ticks to rotate. Must be non-negative.
	 * @param secondsTimeout
	 * 		Maximum number of seconds to rotate. Must be non-negative.
	 */
	final void rampToEncoderTicks(final double proportionPower, final int numberOfTicksToRotate,
	                              final double secondsTimeout) {

		if ((proportionPower < -1.0) || (proportionPower > 1.0)) {
			throw new AssertionError("Power out of range [-1.0, 1.0]");
		}

		if (secondsTimeout < 0.0) {
			throw new AssertionError("secondsTimeout must non-negative");
		}

		if (numberOfTicksToRotate < 0) {
			throw new AssertionError("numberOfTicksToRotate must be non-negative");
		}

		final int targetTicks = getCurrentTickNumber() +
		                        ((int) Math.signum(proportionPower) * numberOfTicksToRotate);

		setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		/*
		 * Ramp up for half of the ticks and down for the other half
		 *
		 * Ideally, the power profile should look something like this:
		 *
		 * ___/---\___
		 *
		 */
		int ticksToRampUp   = targetTicks / 2;
		int ticksToRampDown = targetTicks - ticksToRampUp;

		telemetry.log("Motor - Ramp to encoder ticks(3), target power: %f", proportionPower);
		rampToPower(proportionPower, ticksToRampUp, secondsTimeout);

		telemetry.log("Motor - Ramp to encoder ticks(3), target power: %f", 0.0);
		rampToPower(0.0, ticksToRampDown, secondsTimeout);

	}


	/**
	 * Ramp the motor power up (or down) gradually to the requested proportion.
	 * <p>
	 * The idea is to prevent slipping, sliding, jerking, wheelies, etc. due to excessive
	 * acceleration/deceleration.
	 * <p>
	 * This method uses default values for parameters that are likely not of interest to most
	 * callers.
	 *
	 * @param proportionPowerRequested
	 */
	public void rampToPower(final double proportionPowerRequested) {
		rampToPower(proportionPowerRequested, DEFAULT_POWER_INCREMENT_ABSOLUTE,
		            DEFAULT_MILLISECONDS_CYCLE_LENGTH, DEFAULT_PROPORTION_POWER_TOLERANCE,
		            DEFAULT_SECONDS_TIMEOUT);
	}

	/**
	 * Ramp the motor power up (or down) gradually to the requested amount until ticksMaximum or
	 * secondsTimeout has been reached.
	 *
	 * @param proportionPowerRequested
	 * 		The power to which the caller would like to ramp the motor at the end of the ramp
	 * 		time. Positive values indicate forward power; Negative values indicate reverse power.
	 * 		The value is expected to be in the interval [-1, 1].
	 * @param ticksToMove
	 * 		The number of encoder ticks to turn. The method will stop when this number has
	 * 		been reached, unless it times out first.
	 * @param secondsTimeout
	 * 		The maximum number of seconds to run. The method will stop when this number has been
	 * 		reached, unless it moves enough ticks first.
	 */
	private void rampToPower(final double proportionPowerRequested, final int ticksToMove, final
	double secondsTimeout) {

		telemetry.log("Motor - Ramp to power(3), target power: %f", proportionPowerRequested);
		telemetry.log("Motor - Ramp to power(3), target ticks: %d", ticksToMove);

		ElapsedTime runtime;

		double secondsRunning;
		int    tickNumberCurrent;
		int    ticksMoved;
		int    tickNumberStart;

		double proportionPowerCurrent;
		double proportionPowerNew;
		double powerDelta;
		double millisecondsSinceChange;

		tickNumberStart = getCurrentTickNumber();
		telemetry.log("Motor - Ramp to power(3), starting tick number: %d", tickNumberStart);

		powerDelta = 1.0;
		proportionPowerCurrent = 0.0;
		secondsRunning = 0.0;
		ticksMoved = 0;
		tickNumberCurrent = 0;

		while (!isRampToEncoderTicksDone(ticksToMove, secondsTimeout, secondsRunning, ticksMoved,
		                                 powerDelta, proportionPowerCurrent)) {

			telemetry.log("Motor - Ramp to power(3), current tick number: %d", tickNumberCurrent);
			proportionPowerCurrent = getProportionPowerCurrent();
			powerDelta = proportionPowerRequested - proportionPowerCurrent;

			proportionPowerNew = getProportionPowerNew(proportionPowerCurrent,
			                                           proportionPowerRequested,
			                                           DEFAULT_POWER_INCREMENT_ABSOLUTE);
			telemetry.log("Motor - Ramp power to, power: %f", proportionPowerNew);
			setProportionPower(proportionPowerNew);
			runtime = new ElapsedTime();
			millisecondsSinceChange = 0.0;

			while ((millisecondsSinceChange < (double) DEFAULT_MILLISECONDS_CYCLE_LENGTH) &&
			       opMode.opModeIsActive()) {
				// Wait until it is time for next power increment
				opMode.sleep((long) millisecondsSinceChange);
				millisecondsSinceChange = runtime.milliseconds();
			}

			tickNumberCurrent = getCurrentTickNumber();
			ticksMoved = tickNumberCurrent - tickNumberStart;

		}

	}

	/**
	 * Ramp the motor power up (or down) gradually to the requested amount.
	 * <p>
	 * The idea is to prevent slipping, sliding, jerking, wheelies, etc. due to excessive
	 * acceleration/deceleration.
	 *
	 * @param proportionPowerRequested
	 * 		The power to which the caller would like to ramp the motor at the end of the ramp time.
	 * 		Positive values indicate forward power; Negative values indicate reverse power. The
	 * 		value
	 * 		is expected to be in the interval [-1, 1].
	 * @param powerIncrementAbsolute
	 * 		How much to increase or decrease the power during each cycle. The value is expected
	 * 		to be
	 * 		in the interval [0, 1].
	 * @param millisecondsCycleLength
	 * 		The length of each cycle in milliseconds.
	 * @param proportionPowerTolerance
	 * 		If the actual motor power is at least this close to the requested motor power, then we
	 * 		stop
	 * 		incrementing the power.
	 */
	public void rampToPower(final double proportionPowerRequested, final double
			powerIncrementAbsolute, final int millisecondsCycleLength, final double
			                        proportionPowerTolerance, final double secondsTimeout) {

		telemetry.log("Motor - Ramp to power (5), total target power: %f", proportionPowerRequested);

		double proportionPowerCurrent;
		double proportionPowerNew;
		double powerDelta;
		double millisecondsSinceChange;

		powerDelta = 1.0;

		while ((powerDelta > proportionPowerTolerance) && opMode.opModeIsActive()) {

			proportionPowerCurrent = getProportionPowerCurrent();
			powerDelta = proportionPowerRequested - proportionPowerCurrent;

			proportionPowerNew = getProportionPowerNew(proportionPowerCurrent,
			                                           proportionPowerRequested,
			                                           powerIncrementAbsolute);

			telemetry.log("Motor - Ramp power to, current power target: %f", proportionPowerNew);

			setProportionPower(proportionPowerNew);

			/*
			 * Wait for: (1) the cycle period to elapse, (2) the Op Mode to go inactive, (3) the
			 * motor to stop, or (4) the timeout period to expire.
			 */

			ElapsedTime elapsedTimeTotal       = new ElapsedTime();
			ElapsedTime elapsedTimeSinceChange = new ElapsedTime();

			millisecondsSinceChange = 0.0;
			double  secondsRunning = 0.0;
			boolean isMotorBusy    = true;

			while ((millisecondsSinceChange < (double) millisecondsCycleLength)
			       && opMode.opModeIsActive()
			       && isMotorBusy
			       && (secondsRunning < secondsTimeout)) {

				opMode.sleep((long) millisecondsSinceChange);
				millisecondsSinceChange = elapsedTimeSinceChange.milliseconds();
				secondsRunning = elapsedTimeTotal.seconds();
				isMotorBusy = isBusy();
			}

		}

	}


	final void runByRevolutions(final double proportionMotorPower, final double
			numberOfRevolutions, final double secondsTimeout) {

		final int numberOfTicks = (int) Math.round(
				(double) TICKS_PER_MOTOR_REVOLUTION * numberOfRevolutions);
		telemetry.log("Motor - Run by revolutions, power: %f", proportionMotorPower);
		rampToEncoderTicks(proportionMotorPower, numberOfTicks, secondsTimeout);
	}

	/**
	 * Run the motor for a fixed amount of time. This method of moving the motor does not depend on
	 * an encoder.
	 *
	 * @param proportionPower
	 * 		Proportion of power to apply to the motor, in the range [-1.0, 1.0]. Negative values
	 * 		run
	 * 		the motor backward.
	 * @param secondsToRun
	 * 		Number of seconds for which to run the motor.
	 */
	final void runByTime(final double proportionPower, final double secondsToRun) {

		if ((proportionPower < -1.0) || (proportionPower > 1.0)) {
			throw new IllegalArgumentException("Power expected to be in range [-1.0, 1.0]");
		}

		if (secondsToRun < 0.0) {
			throw new IllegalArgumentException("secondsTimeout expected to be non-negative");
		}

		final ElapsedTime runtime;
		double            secondsRunning;

		setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		rampToPower(proportionPower);
		runtime = new ElapsedTime();
		secondsRunning = runtime.seconds();

		while (secondsRunning < secondsToRun && opMode.opModeIsActive()) {
			secondsRunning = runtime.seconds();
		}

		rampToPower(0.0);
		setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

	}


	/**
	 * Run this motor until it stalls
	 *
	 * @param power
	 * 		How much power to apply to the motor, in the interval [-1,1].
	 */
	void runUntilStalled(final double power) {
		timeStalledInMilliseconds = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
		rampToPower(power);
		while (!(isStalled()) && opMode.opModeIsActive()) {
			//telemetry.log("Not stalled yet...");
		}
		rampToPower(0.0);
	}

	/**
	 * Set the logical direction in which this motor operates.
	 *
	 * @param direction
	 * 		The logical direction in which this motor operates.
	 */
	void setDirection(final DcMotorSimple.Direction direction) {
		motor.setDirection(direction);
	}

	/**
	 * Set the run mode for this motor.
	 *
	 * @param mode
	 * 		the new current run mode for this motor
	 */
	@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
	void setMode(final DcMotor.RunMode mode) {
		motor.setMode(mode);
	}

	/**
	 * Set the power level of the motor without ramping.
	 * <p>
	 * Power is expressed as a fraction of the maximum possible power / speed supported
	 * according to
	 * the run mode in which the motor is operating.
	 * <p>
	 * Setting a power level of zero will brake the motor.
	 *
	 * @param power
	 * 		The new power level of the motor, a value in the interval [-1.0, 1.0]
	 */
	public void setProportionPower(final double power) {
		motor.setPower(power);
	}


	/**
	 * Set the stall detection tolerance
	 *
	 * @param ticks
	 * 		An integer number of encoder clicks such that, if the encoder changes fewer than this
	 * 		number of clicks over a period of time defined by stallTimeLimitInMilliseconds, then we
	 * 		consider the motor stalled.
	 */
	@SuppressWarnings("WeakerAccess")
	public void setStallDetectionToleranceInTicks(final int ticks) {
		stallDetectionToleranceInTicks = ticks;
	}

	/**
	 * Set the stall detection time limit in milliseconds.
	 *
	 * @param milliseconds
	 * 		The number of milliseconds during which the motor must not have moved more than the
	 * 		stall
	 * 		detection tolerance before we call it a stall.
	 */
	void setStallTimeLimitInMilliseconds(final int milliseconds) {
		stallTimeLimitInMilliseconds = milliseconds;
	}

	/**
	 * Set the stall detection time limit in seconds.
	 *
	 * @param seconds
	 * 		The number of seconds during which the motor must not have moved more than the stall
	 * 		detection tolerance before we call it a stall.
	 */
	void setStallTimeLimitInSeconds(final double seconds) {
		final int milliseconds = (int) (seconds * (double) MILLISECONDS_PER_SECOND);
		setStallTimeLimitInMilliseconds(milliseconds);
	}

	/**
	 * Set up stall detection.
	 *
	 * @param timeLimitMs
	 * 		How long does the motor have to be still before we consider it stalled?
	 * @param toleranceTicks
	 * 		An integer number of encoder clicks such that, if the encoder changes less than this
	 * 		over a
	 * 		period of stallTimeLimitInMilliseconds, then we call it a stall.
	 */
	public void setupStallDetection(final int timeLimitMs, final int toleranceTicks) {

		setStallDetectionToleranceInTicks(toleranceTicks);
		setStallTimeLimitInMilliseconds(timeLimitMs);
		timeStalledInMilliseconds.reset();
		oldTickNumber = getCurrentTickNumber();

	}


	void setTargetPosition(final int targetPositionTicks) {
		motor.setTargetPosition(targetPositionTicks);
	}


	void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
		motor.setZeroPowerBehavior(zeroPowerBehavior);
	}

	/**
	 * Returns true if the loop in rampToEncoderTicks should end.
	 * <p>
	 * The code could be simplified and shortened but it is verbose for comprehensibility.
	 * <p>
	 * The code could be part of the rampToEncoderTicks itself but isolating it here (1) simplifies
	 * rampToEncoderTicks and (2) allows unit testing this method, which does not depend on Op
	 * Mode.
	 *
	 * @param ticksMaximum
	 * 		The maximum number of ticks that rampToEncoderTicks is supposed to move.
	 * @param secondsTimeout
	 * 		The maximum number of seconds for which rampToEncoderTicks is supposed to run.
	 * @param secondsRunning
	 * 		The number of seconds for which rampToEncoderTicks has been running.
	 * @param ticksMoved
	 * 		The number of ticks by which rampToEncoderTicks has already moved.
	 * @param powerDelta
	 * 		The difference between the current power and the previous power on the ramp.
	 *
	 * @return True if the loop in rampToEncoderTicks should end.
	 */
	public boolean isRampToEncoderTicksDone(int ticksMaximum, double secondsTimeout, double secondsRunning, int ticksMoved, double powerDelta, double powerCurrent) {

		boolean valueToReturn = false;

		if (ticksMoved >= Math.abs(ticksMaximum)) {
			telemetry.log("Loop done - moved far enough");
			valueToReturn = true;
		} else if (secondsRunning >= secondsTimeout) {
			telemetry.log("Loop done - timed out");
			valueToReturn = true;
		} else if (Math.abs(powerDelta) < DEFAULT_PROPORTION_POWER_TOLERANCE) {
			telemetry.log("Loop done - power reached");
			valueToReturn = true;
		} else if (hardwareMap != null) {

			telemetry.log("Loop check - Running on robot");

			/*
			 * Only check whether Op Mode is active if hardware map is
			 * not null. Otherwise, we are running off-bot, and Op Mode
			 * will never be active. Returning false in that case allows
			 * us to use off-bot unit tests to test the other conditions.
			 */
			if (!opMode.opModeIsActive()) {
				telemetry.log("Loop done - On robot but op mode is not active");
				valueToReturn = true;
			}
		}
		return valueToReturn;
	}

}
