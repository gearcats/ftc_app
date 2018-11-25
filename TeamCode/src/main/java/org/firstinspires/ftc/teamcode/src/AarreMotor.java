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
 * not recommended. See
 * <a href=https://ftcforum.usfirst.org/forum/ftc-technology/4717-how-to-extend-the-dcmotor-class
 * ></a>
 * for a discussion.
 * <p>
 * Stall detection and telemetry code adapted from
 * <a href="https://github.com/TullyNYGuy/FTC8863_ftc_app/blob/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Lib/FTCLib/DcMotor8863.java"></a>
 */
public class AarreMotor {


	private static final int    SECONDS_PER_MINUTE         = 60;

	private static final int    MILLISECONDS_PER_SECOND    = 1000;

	private static final int    MILLISECONDS_PER_CYCLE     = 50;

	private static final double POWER_INCREMENT_PER_CYCLE  = 0.1;
	private static final double PROPORTION_POWER_TOLERANCE = 0.01;
	private static final double DEFAULT_SECONDS_TIMEOUT    = 5.0;
	private static final int    RAMP_DIRECTION_DOWN        = -1;
	private static final int    RAMP_DIRECTION_UP          = 1;


	private final DcMotor        motor;
	private final AarreTelemetry telemetry;
	private final LinearOpMode   opMode;
	private final HardwareMap    hardwareMap;
	private       int            oldTickNumber                  = 0;
	private       int            stallTimeLimitInMilliseconds   = 0;
	private       int            stallDetectionToleranceInTicks = 5;
	private       ElapsedTime    timeStalledInMilliseconds      = null;

	private double revolutionsPerMinute;
	private double ticksPerRevolution;

	public AarreMotor(LinearOpMode opMode, final String motorName) {

		this.opMode = opMode;

		telemetry = new AarreTelemetry(opMode.telemetry);

		/**
		 * hardwareMap will be null if we are running off-robot, but for testing purposes it is
		 * still helpful to instantiate this object (rather than throwing an exception, for
		 * example).
		 */
		hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			motor = null;
		}
		else {
			motor = hardwareMap.get(DcMotor.class, motorName);
			motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		}

		// These are defaults. The user should customize them
		stallDetectionToleranceInTicks = 5;
		stallTimeLimitInMilliseconds = 100;

	}

	public double getRevolutionsPerMinute() {
		return revolutionsPerMinute;
	}

	public void setRevolutionsPerMinute(double revolutionsPerMinute) {
		this.revolutionsPerMinute = revolutionsPerMinute;
	}

	public void setTicksPerRevolution(double ticksPerRevolution) {
		this.ticksPerRevolution = ticksPerRevolution;
	}

	/**
	 * The number of milliseconds in a ramp up/ramp down cycle.
	 * <p>
	 * It is useful to have this public getter for testing purposes.
	 *
	 * @return The number of milliseconds in a ramp up/ramp down cycle.
	 */
	public int getMillisecondsPerCycle() {
		return MILLISECONDS_PER_CYCLE;
	}

	public double getTicksPerMinute() {
		return getTicksPerRevolution() * getRevolutionsPerMinute();
	}

	public double getTicksPerRevolution() {
		return ticksPerRevolution;
	}

	public double getTicksPerSecond() {
		return getTicksPerMinute() / SECONDS_PER_MINUTE;
	}

	public double getTicksPerMillisecond() {
		return getTicksPerSecond() / MILLISECONDS_PER_SECOND;
	}

	public double getTicksPerCycle() {
		return getTicksPerMillisecond() * getMillisecondsPerCycle();
	}

	/**
	 * Get the current reading of the encoder for this motor.
	 * <p>
	 * Despite its name, the {@link DcMotor} method {@code getCurrentPosition} provides almost no
	 * information about position. Therefore, we use a different name here.
	 *
	 * @return The current reading of the encoder for this motor, in ticks.
	 */
	public final int getCurrentTickNumber() {
		return motor.getCurrentPosition();
	}

	/**
	 * Get the number of cycles for which a ramp should last
	 * <p>
	 * The current power is a parameter here (instead of using the getter for the corresponding
	 * class field) to allow for off-bot unit testing.
	 */
	public int getNumberOfCycles(int ticksToMove, double proportionPowerCurrent, double
			proportionPowerRequested) {

		double proportionPowerChange;
		int    cyclesToChange;

		proportionPowerChange = Math.abs(proportionPowerRequested - proportionPowerCurrent);
		cyclesToChange = (int) Math.round(proportionPowerChange / POWER_INCREMENT_PER_CYCLE);

		return cyclesToChange;

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

		if (Math.abs(powerDelta) < PROPORTION_POWER_TOLERANCE) {
			proportionPowerNew = proportionPowerRequested;
		}
		else {
			direction = Math.signum(powerDelta);
			powerIncrement = powerIncrementAbsolute * direction;
			proportionPowerNew = proportionPowerCurrent + powerIncrement;
		}

		return proportionPowerNew;

	}

	public double getPower() {
		return motor.getPower();
	}

	/**
	 * Calculate when (what tick number) to start a ramp down.
	 */
	/**
	 * @param tickNumberAtStartOfPeriod
	 * 		The motor encoder tick reading at the start of the period (which includes not only the
	 * 		ramp
	 * 		down at the end but any time/ticks running at speed before the ramp).
	 * @param numberOfTicksInPeriod
	 * 		The total number of ticks in the period.
	 * @param powerAtStart
	 * 		The motor power at the start of the period, in the range [-1,1].
	 * @param powerAtEnd
	 * 		The power that should be applied to the motor at the end of the period, in the range
	 * 		[-1,1].
	 *
	 * @return
	 */
	public double getTickNumberToStartRampDown(final int tickNumberAtStartOfPeriod, final int
			numberOfTicksInPeriod, final double powerAtStart, final double powerAtEnd) {

		/*
		 * A ramp down can occur with either positive or negative power
		 *
		 * Consider a ramp down from 0.5 to 0 versus a ramp down from -0.5 to 0.
		 *
		 * Thus we cannot assume that powerAtStart is greater than powerAtEnd
		 */
		if (Math.abs(powerAtStart) <= Math.abs(powerAtEnd)) {
			throw new IllegalArgumentException("When ramping down, the absolute value of the " +
			                                   "power at the start of the ramp must be greater " +
			                                   "than the absolute value of the power at the end " +
			                                   "of the ramp.");
		}

		double powerChangeAbsolute;
		double powerChangeDirection;

		final double numberOfCyclesInRamp;
		final double numberOfTicksToChange;
		final double tickNumberAtEndOfPeriod;
		final double tickNumberToStartRampdown;
		final double numberOfTicksInRamp;

		powerChangeAbsolute = Math.abs(powerAtStart - powerAtEnd);
		powerChangeDirection = Math.signum(powerAtStart - powerAtEnd);

		numberOfCyclesInRamp = powerChangeAbsolute / POWER_INCREMENT_PER_CYCLE;
		numberOfTicksInRamp = numberOfCyclesInRamp * getTicksPerCycle();
		numberOfTicksToChange = powerChangeDirection * numberOfTicksInRamp;
		tickNumberAtEndOfPeriod =
				tickNumberAtStartOfPeriod + (numberOfTicksInPeriod * powerChangeDirection);
		tickNumberToStartRampdown = tickNumberAtEndOfPeriod - numberOfTicksToChange;

		return tickNumberToStartRampdown;
	}

	/**
	 * Calculate how many encoder ticks are in an interval of time at a particular speed (power).
	 *
	 * @param power
	 * 		The proportion of power applied to the motor.
	 * @param millisecondsInInterval
	 * 		The interval the caller wants to know about, in milliseconds.
	 *
	 * @return The number of ticks in that time interval at that power.
	 */
	public int getTicksInInterval(double power, int millisecondsInInterval) {
		return -1;
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
	 * @param powerCurrent
	 *      The current power applied to the motor.
	 *
	 * @return True if the loop in rampUpToEncoderTicks should end.
	 */
	public boolean isRampUpToEncoderTicksDone(int ticksMaximum, double secondsTimeout, double
			secondsRunning, int ticksMoved, double powerDelta, double powerCurrent) {

		boolean valueToReturn = false;

		if (Math.abs(ticksMoved) >= Math.abs(ticksMaximum)) {
			telemetry.log("Loop done - moved far enough");
			valueToReturn = true;
		}
		else if (secondsRunning >= secondsTimeout) {
			telemetry.log("Loop done - timed out");
			valueToReturn = true;
		}
		else if (hardwareMap != null) {

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

	/**
	 * @param tickNumberAtStartOfPeriod
	 * 		The motor encoder tick number at which the period (including both the ramp at the
	 * 		portion at speed before the ramp) started.
	 * @param tickNumberCurrent
	 * 		The current motor encoder tick number.
	 * @param numberOfTicksInPeriod
	 * 		The number of motor encoder ticks over which the ramp should extend. Must be
	 * 		non-negative.
	 * @param powerAtStart
	 * 		The motor power at which the ramp started.
	 * @param powerAtEnd
	 * 		The power the motor should be at when the ramp ends.
	 *
	 * @return True if changing the power should start or continue. False otherwise.
	 */
	public boolean isRampDownToEncoderTicksRunning(int tickNumberAtStartOfPeriod, int
			tickNumberCurrent, int numberOfTicksInPeriod, double powerAtStart, double powerAtEnd) {

		if (numberOfTicksInPeriod < 0) {
			throw new IllegalArgumentException("Number of ticks in period must be non-negative");
		}

		/*
		 * The ramp down should start when we are at a tick number equal to (or
		 * greater than) the tick number to start the ramp down and continue until we are at a
		 * tick number equal to (or greater than) the tick number to end the ramp down.
		 */

		boolean result = false;

		int tickNumberGoal = tickNumberAtStartOfPeriod + numberOfTicksInPeriod;
		double tickToStartRampdown = getTickNumberToStartRampDown(tickNumberAtStartOfPeriod,
		                                                          numberOfTicksInPeriod,
		                                                          powerAtStart, powerAtEnd);

		if ((tickNumberCurrent >= tickToStartRampdown) &&
		    (tickNumberCurrent <= numberOfTicksInPeriod)) {
			result = true;
		}

		return result;
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

		}
		else {

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
	 * 		The power at which to rotate, in the interval [-1.0, 1.0]. Positive values indicate
	 * 		rotation forward (increasing tick number). Negative values indicate rotation backward
	 * 		(decreasing tick number).
	 * @param numberOfTicksToRotate
	 * 		Maximum number of ticks to rotate. Must be non-negative.
	 * @param secondsTimeout
	 * 		Maximum number of seconds to rotate. Must be non-negative.
	 */
	final void rampToEncoderTicks(final double proportionPower, final int numberOfTicksToRotate,
	                              final double secondsTimeout) {

		if ((proportionPower < -1.0) || (proportionPower > 1.0)) {
			throw new IllegalArgumentException("Power out of range [-1.0, 1.0]");
		}
		else if (secondsTimeout < 0.0) {
			throw new IllegalArgumentException("secondsTimeout must non-negative");
		}
		else if (numberOfTicksToRotate < 0) {
			throw new IllegalArgumentException("numberOfTicksToRotate must be non-negative");
		}

		final int targetTickNumber = getCurrentTickNumber() +
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
		int ticksToRampUp   = numberOfTicksToRotate / 2;
		int ticksToRampDown = numberOfTicksToRotate - ticksToRampUp;

		telemetry.log("Motor - Ramp to encoder ticks(3), target power UP: %f", proportionPower);
		rampToPower(proportionPower, ticksToRampUp, secondsTimeout);

		telemetry.log("Motor - Ramp to encoder ticks(3), target power DOWN: %f", 0.0);
		rampToPower(0.0, ticksToRampDown, secondsTimeout);

		motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
		rampToPower(proportionPowerRequested, POWER_INCREMENT_PER_CYCLE, MILLISECONDS_PER_CYCLE,
		            PROPORTION_POWER_TOLERANCE, DEFAULT_SECONDS_TIMEOUT);
	}

	/**
	 * Ramp the motor power up (or down) gradually to the requested amount until ticksMaximum or
	 * secondsTimeout has been reached.
	 *
	 * @param proportionPowerRequested
	 * 		The power to which the caller would like to ramp the motor at the end of the ramp time.
	 * 		Positive values indicate forward power; Negative values indicate reverse power. The
	 * 		value
	 * 		is expected to be in the interval [-1, 1].
	 * @param ticksToMove
	 * 		The number of encoder ticks to turn. The method will stop when this number has been
	 * 		reached, unless it times out first.
	 * @param secondsTimeout
	 * 		The maximum number of seconds to run. The method will stop when this number has been
	 * 		reached, unless it moves enough ticks first.
	 */
	private void rampToPower(final double proportionPowerRequested, final int ticksToMove, final
	double secondsTimeout) {

		if ((proportionPowerRequested > 0) && (proportionPowerRequested <= 1)) {
			rampUpToPower(proportionPowerRequested, ticksToMove, secondsTimeout);
		}
		else if ((proportionPowerRequested < 0) && (proportionPowerRequested >= -1)) {
			rampDownToPower(proportionPowerRequested, ticksToMove, secondsTimeout);
		}
		else if (proportionPowerRequested == 0) {
			return;
		}
		else {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * When ramping "down", the power change starts at the beginning of the motion, something like
	 * this:
	 *
	 * <pre>
	 * >_____
	 *       \
	 *        \
	 *         \
	 *          -|
	 * </pre>
	 */
	private void rampDownToPower(final double proportionPowerAtEndAbsolute, final int ticksToMove, final
	double secondsTimeout) {

		telemetry.log("Motor::rampDownToPower(3) - Target power: %f",
		              proportionPowerAtEndAbsolute);

		if ((proportionPowerAtEndAbsolute <= 0) || (proportionPowerAtEndAbsolute > 1)) {
			throw new IllegalArgumentException("Expected a value in [0,1]");
		}

		boolean keepWaiting;
		boolean keepGoing;
		int     tickNumberStart;
		int     tickNumberCurrent;

		double proportionPowerCurrent;
		double proportionPowerNew;
		double millisecondsSinceChange;

		ElapsedTime runtimeFromStart;
		ElapsedTime runtimeSinceChange;

		runtimeFromStart = new ElapsedTime();
		tickNumberStart = getCurrentTickNumber();

		/*
		 * Wait for the right time to start ramping down
		 */
		keepWaiting = true;
		while (keepWaiting && opMode.opModeIsActive()) {
			opMode.idle();
			tickNumberCurrent = getCurrentTickNumber();
			proportionPowerCurrent = getProportionPowerCurrent();
			keepWaiting = !isRampDownToEncoderTicksRunning(tickNumberStart, tickNumberCurrent,
			                                               ticksToMove, proportionPowerCurrent,
			                                               proportionPowerAtEndAbsolute);
		}

		/*
		 * Ramp down
		 */
		keepGoing = true;
		while (keepGoing && opMode.opModeIsActive()) {

			opMode.idle();
			tickNumberCurrent = getCurrentTickNumber();

			proportionPowerCurrent = getProportionPowerCurrent();
			proportionPowerNew = getProportionPowerNew(proportionPowerCurrent,
			                                           proportionPowerAtEndAbsolute,
			                                           POWER_INCREMENT_PER_CYCLE);

			setProportionPower(proportionPowerNew);

			telemetry.log("Motor::rampDownToPower(3) - Milliseconds elapsed %f", runtimeFromStart
					.milliseconds());
			telemetry.log("Motor::rampDownToPower(3) - Current tick number: %d",
			              tickNumberCurrent);
			telemetry.log("Motor::rampDownToPower(3) - New power: %f", proportionPowerNew);

			/*
			 * Wait for next power change
			 */
			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = 0.0;
			while ((millisecondsSinceChange < (double) MILLISECONDS_PER_CYCLE) &&
			       opMode.opModeIsActive()) {
				// Wait until it is time for next power increment
				//opMode.idle();
				millisecondsSinceChange = runtimeSinceChange.milliseconds();
			}

			keepGoing = isRampDownToEncoderTicksRunning(tickNumberStart, tickNumberCurrent,
			                                            ticksToMove,
			                                            proportionPowerCurrent,
			                                            proportionPowerAtEndAbsolute);

		}
	}

	/**
	 * When ramping "up", the power change starts at the beginning of the motion, something like
	 * this:
	 *
	 * <pre>
	 *          _____>
	 *         /
	 *        /
	 *     |_/
	 * </pre>
	 */
	private void rampUpToPower(final double proportionPowerRequestedAbsolute, final int
			ticksToMove, final double secondsTimeout) {

		if ((proportionPowerRequestedAbsolute <= 0) || (proportionPowerRequestedAbsolute > 1)) {
			throw new IllegalArgumentException("Expected a value in [0,1]");
		}

		telemetry.log("Motor::rampUpToPower(3) - Target power: %f",
		              proportionPowerRequestedAbsolute);
		telemetry.log("Motor::rampUpToPower(3) - Target ticks: %d", ticksToMove);

		ElapsedTime runtimeSinceChange;
		ElapsedTime runtimeTotal;

		double secondsRunning;
		int    tickNumberCurrent;
		int    ticksMoved;
		int    tickNumberStart;

		double proportionPowerCurrent;
		double proportionPowerNew;
		double powerDelta;
		double millisecondsSinceChange;

		tickNumberStart = getCurrentTickNumber();
		telemetry.log("Motor::rampUpToPower(3) - Starting tick number: %d", tickNumberStart);

		powerDelta = 1.0;
		proportionPowerCurrent = 0.0;
		secondsRunning = 0.0;
		ticksMoved = 0;
		tickNumberCurrent = 0;

		runtimeTotal = new ElapsedTime();

		while (!isRampUpToEncoderTicksDone(ticksToMove, secondsTimeout, secondsRunning,
		                                   ticksMoved, powerDelta, proportionPowerCurrent)) {


			proportionPowerCurrent = getProportionPowerCurrent();
			powerDelta = proportionPowerRequestedAbsolute - proportionPowerCurrent;

			proportionPowerNew = getProportionPowerNew(proportionPowerCurrent,
			                                           proportionPowerRequestedAbsolute,
			                                           POWER_INCREMENT_PER_CYCLE);

			setProportionPower(proportionPowerNew);


			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = 0.0;

			while ((millisecondsSinceChange < (double) MILLISECONDS_PER_CYCLE) &&
			       opMode.opModeIsActive()) {
				// Wait until it is time for next power increment
				opMode.idle();
				millisecondsSinceChange = runtimeSinceChange.milliseconds();
			}

			tickNumberCurrent = getCurrentTickNumber();
			ticksMoved = tickNumberCurrent - tickNumberStart;
			telemetry.log("Motor::rampUpToPower(3) - Milliseconds elapsed %f", runtimeTotal
					.milliseconds());
			telemetry.log("Motor::rampUpToPower(3) - Current tick number: %d", tickNumberCurrent);
			telemetry.log("Motor::rampUpToPower(3) - New power: %f", proportionPowerNew);

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

		telemetry.log("Motor - Ramp to power (5), total target power: %f",
		              proportionPowerRequested);

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

			while ((millisecondsSinceChange < (double) millisecondsCycleLength) &&
			       opMode.opModeIsActive() && isMotorBusy && (secondsRunning < secondsTimeout)) {

				opMode.sleep((long) millisecondsSinceChange);
				millisecondsSinceChange = elapsedTimeSinceChange.milliseconds();
				secondsRunning = elapsedTimeTotal.seconds();
				isMotorBusy = isBusy();
			}

		}

	}

	/**
	 * Run the motor a certain number of revolutions.
	 *
	 * @param proportionMotorPower
	 * @param numberOfRevolutions
	 * @param secondsTimeout
	 */
	final void runByRevolutions(final double proportionMotorPower, final double
			numberOfRevolutions, final double secondsTimeout) {

		final int numberOfTicks = (int) Math.round(getTicksPerRevolution() * numberOfRevolutions);
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

}
