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
 * It might be preferable (e.g., more elegant, less wrapper code needed) to extend the FTC DcMotorImpl class rather than
 * wrap it. However, it seems that extending the DcMotorImpl class is not recommended. See <a
 * href=https://ftcforum.usfirst .org/forum/ftc-technology/4717-how-to-extend-the-dcmotor-class ></a> for a discussion.
 * <p>
 * Stall detection and telemetry code adapted from
 * <a href="https://github.com/TullyNYGuy/FTC8863_ftc_app/blob/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Lib/FTCLib/DcMotor8863.java"></a>
 */
public class AarreMotor implements AarreMotorInterface {


	private static final int SECONDS_PER_MINUTE = 60;

	private static final int MILLISECONDS_PER_SECOND = 1000;

	private static final int MILLISECONDS_PER_CYCLE = 50;

	// How much to increment the motor power in each cycle of power ramping
	private static final AarrePowerMagnitude DEFAULT_POWER_INCREMENT_PER_CYCLE = new AarrePowerMagnitude(0.1);
	// How much tolerance to allow when deciding whether we have reached a requested motor power
	private static final AarrePowerMagnitude DEFAULT_PROPORTION_POWER_TOLERANCE = new AarrePowerMagnitude(0.01);
	private AarrePowerMagnitude powerMagnitudeIncrementPerCycle = DEFAULT_POWER_INCREMENT_PER_CYCLE;
	private AarrePowerMagnitude powerMagnitudeTolerance = DEFAULT_PROPORTION_POWER_TOLERANCE;

	private static final double DEFAULT_SECONDS_TIMEOUT = 5.0;


	private final DcMotor motor;
	private final AarreTelemetry telemetry;
	private final LinearOpMode opMode;
	private final HardwareMap hardwareMap;
	private int oldTickNumber = 0;
	private int stallTimeLimitInMilliseconds;
	private int stallDetectionToleranceInTicks;
	private ElapsedTime timeStalledInMilliseconds = null;

	private double revolutionsPerMinute;
	private double ticksPerRevolution;

	public AarreMotor(LinearOpMode opMode, final String motorName) {

		this.opMode = opMode;

		telemetry = new AarreTelemetry(opMode.telemetry);

		/*
		  hardwareMap will be null if we are running off-robot, but for testing purposes it is
		  still helpful to instantiate this object (rather than throwing an exception, for
		  example).
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
		stallTimeLimitInMilliseconds = 100;

	}

	@Override
	public double getRevolutionsPerMinute() {
		return revolutionsPerMinute;
	}

	@Override
	public double getRevolutionsPerMinute(AarrePowerMagnitude powerMagnitude) {
		return revolutionsPerMinute * powerMagnitude.asDouble();
	}

	@Override
	public void setRevolutionsPerMinute(double revolutionsPerMinute) {
		this.revolutionsPerMinute = revolutionsPerMinute;
	}

	@Override
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
	@Override
	public int getMillisecondsPerCycle() {
		return MILLISECONDS_PER_CYCLE;
	}

	@Override
	public double getTicksPerMinute() {
		return getTicksPerRevolution() * getRevolutionsPerMinute();
	}

	@Override
	public double getTicksPerMinute(AarrePowerMagnitude powerMagnitude) {
		return getTicksPerRevolution() * getRevolutionsPerMinute(powerMagnitude);
	}

	@Override
	public double getTicksPerRevolution() {
		return ticksPerRevolution;
	}

	@Override
	public double getTicksPerSecond() {
		return getTicksPerMinute() / SECONDS_PER_MINUTE;
	}

	@Override
	public double getTicksPerSecond(AarrePowerMagnitude powerMagnitude) {
		return getTicksPerMinute(powerMagnitude) / SECONDS_PER_MINUTE;
	}

	@Override
	public double getTicksPerMillisecond() {
		return getTicksPerSecond() / MILLISECONDS_PER_SECOND;
	}

	@Override
	public double getTicksPerMillisecond(AarrePowerMagnitude powerMagnitude) {
		return getTicksPerSecond(powerMagnitude) / MILLISECONDS_PER_SECOND;
	}

	@Override
	public double getTicksPerCycle() {
		return getTicksPerMillisecond() * getMillisecondsPerCycle();
	}

	@Override
	public double getTicksPerCycle(AarrePowerMagnitude powerMagnitude) {
		return getTicksPerMillisecond(powerMagnitude) * getMillisecondsPerCycle();
	}

	/**
	 * Get the current reading of the encoder for this motor.
	 * <p>
	 * Despite its name, the {@link DcMotor} method {@code getCurrentPosition} provides almost no information about
	 * position. Therefore, we use a different name here.
	 *
	 * @return The current reading of the encoder for this motor, in ticks.
	 */
	@Override
	public final int getCurrentTickNumber() {
		return motor.getCurrentPosition();
	}

	/**
	 * Get the number of cycles for which a ramp should last
	 * <p>
	 * The current power is a parameter here (instead of using the getter for the corresponding class field) to allow
	 * for off-bot unit testing.
	 * <p>
	 */
	@Override
	public int getNumberOfCycles(int ticksToMove, AarrePowerVector powerVectorCurrent, AarrePowerVector
			powerVectorRequested) {

		// The magnitude of the current and requested power
		AarrePowerMagnitude powerMagnitudeCurrent = powerVectorCurrent.getMagnitude();
		AarrePowerMagnitude powerMagnitudeRequested = powerVectorRequested.getMagnitude();

		// The average number of ticks per cycle during the ramp
		double currentMagnitude = powerMagnitudeCurrent.asDouble();
		double requestedMagnitude = powerMagnitudeRequested.asDouble();
		double averageMagnitude = (currentMagnitude + requestedMagnitude) / 2;
		AarrePowerMagnitude averagePowerMagnitude = new AarrePowerMagnitude(averageMagnitude);
		double averageTicksPerCycle = averageMagnitude * getTicksPerCycle(averagePowerMagnitude);

		// The magnitude of the power change over the ramp
		AarrePowerVector powerVectorChangeOverRamp = powerVectorRequested.subtract(powerVectorCurrent);
		AarrePowerMagnitude powerMagnitudeChangeOverRamp = powerVectorChangeOverRamp.getMagnitude();

		// The number of cycles required to change power as much as requested
		double numCyclesRequiredToChangePower = powerMagnitudeChangeOverRamp.divideBy
				(DEFAULT_POWER_INCREMENT_PER_CYCLE);

		// The number of ticks the motor would move if we changed the power that much
		double potentialTicksInRamp = averageTicksPerCycle * numCyclesRequiredToChangePower;

		// Return the number of cycles to change power or number of cycles to reach ticks,
		// whichever is lower
		double cyclesToChange = numCyclesRequiredToChangePower;
		if (potentialTicksInRamp > ticksToMove) {
			// TODO: Doesn't this assume that the motor is operating at full power?
			double numCyclesRequiredToMoveTicks = ticksToMove / getTicksPerCycle();
			cyclesToChange = numCyclesRequiredToMoveTicks;
		}

		int wholeCyclesToChange = (int) Math.round(cyclesToChange);
		return wholeCyclesToChange;

	}


	public final AarrePowerMagnitude getPowerMagnitudeIncrementPerCycle() {
		return this.powerMagnitudeIncrementPerCycle;
	}

	public AarrePowerVector getPowerVectorCurrent() {
		return new AarrePowerVector(motor.getPower());
	}

	@Override
	public final AarrePowerVector getPowerVectorNew(AarrePowerVector powerVectorCurrent, AarrePowerVector
			powerVectorRequested) {

		AarrePowerVector powerVectorNew;

		/*
		 * Use a double here because the power change can be outside the range of a power vector.
		 * For example, if the power requested is -1 and the current power is 1, then the power
		 * change is -1 - 1 = -2.
		 *
		 * TODO: Create "AarrePowerChange" class???
		 */
		double powerChangeDouble = powerVectorRequested.asDouble() - powerVectorCurrent.asDouble();

		// The magnitude by which the power must change to reach the requested power
		double powerChangeMagnitude = Math.abs(powerChangeDouble);
		int powerChangeDirection = (int) Math.signum(powerChangeDouble);

		if (powerChangeMagnitude <= getPowerMagnitudeIncrementPerCycle().asDouble()) {
			// Within one cycle, give them what they really want...
			powerVectorNew = powerVectorRequested;
		} else {
			// Otherwise, give them what they need...
			AarrePowerVector powerIncrementVector = new AarrePowerVector(getPowerMagnitudeIncrementPerCycle(),
					powerChangeDirection);
			powerVectorNew = powerVectorCurrent.add(powerIncrementVector);
		}

		return powerVectorNew;

	}

	@Override
	public double getPower() {
		return motor.getPower();
	}

	/**
	 * Calculate when (what tick number) to start a ramp down.
	 *
	 * @param tickNumberAtStartOfPeriod
	 * 		The motor encoder tick reading at the start of the period (which includes not only the ramp down at the
	 * 		end but
	 * 		any time/ticks running at speed before the ramp).
	 * @param numberOfTicksInPeriod
	 * 		The total number of ticks in the period.
	 * @param powerAtStartOfPeriod
	 * 		The motor power at the start of the period, in the range [-1,1].
	 * @param powerAtEndOfPeriod
	 * 		The power that should be applied to the motor at the end of the period, in the range [-1,1].
	 *
	 * @return
	 */
	@Override
	public double getTickNumberToStartRampDown(final int tickNumberAtStartOfPeriod, final int numberOfTicksInPeriod,
	                                           final AarrePowerVector powerAtStartOfPeriod, final AarrePowerVector
			                                               powerAtEndOfPeriod) {

		/*
		 * A ramp down can occur with either positive or negative power
		 *
		 * Consider a ramp down from 0.5 to 0 versus a ramp down from -0.5 to 0.
		 *
		 * Thus we cannot assume that powerAtStartOfPeriod is greater than powerAtEndOfPeriod
		 */
		if (powerAtStartOfPeriod.asDouble() <= powerAtEndOfPeriod.asDouble()) {
			throw new IllegalArgumentException("When ramping down, the absolute value of the " + "power at the start "
					+ "of the ramp must be greater " + "than the absolute value of the power at the end " + "of the " +
					"ramp" + ".");
		}

		final double numberOfCyclesInRamp;
		final double numberOfTicksInRamp;
		final double numberOfTicksToChange;
		final double tickNumberAtEndOfPeriod;
		final double tickNumberToStartRampDown;

		AarrePowerVector powerChangeVector = powerAtStartOfPeriod.subtract(powerAtEndOfPeriod);
		AarrePowerMagnitude magnitudeOfPowerChange = powerChangeVector.getMagnitude();
		int powerChangeDirection = powerChangeVector.getDirection();

		numberOfCyclesInRamp = magnitudeOfPowerChange.asDouble() / DEFAULT_POWER_INCREMENT_PER_CYCLE.asDouble();
		numberOfTicksInRamp = numberOfCyclesInRamp * getTicksPerCycle();
		numberOfTicksToChange = powerChangeDirection * numberOfTicksInRamp;
		tickNumberAtEndOfPeriod = tickNumberAtStartOfPeriod + (numberOfTicksInPeriod * powerChangeDirection);
		tickNumberToStartRampDown = tickNumberAtEndOfPeriod - numberOfTicksToChange;

		return tickNumberToStartRampDown;
	}


	/**
	 * Get the number of milliseconds for which the motor has been stalled.
	 *
	 * @return The integer number of milliseconds during which the motor speed has been lowerUntilStalled than the
	 * stall
	 * 		detection tolerance.
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
	 * The code could be part of the rampToEncoderTicks itself but isolating it here (1) simplifies rampToEncoderTicks
	 * and (2) allows unit testing this method, which does not depend on Op Mode.
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
	 * 		The current power applied to the motor.
	 *
	 * @return True if the loop in rampUpToEncoderTicks should end.
	 */
	@Override
	public boolean isRampUpToEncoderTicksDone(int ticksMaximum, double secondsTimeout, double secondsRunning, int
			ticksMoved, AarrePowerVector powerDelta, AarrePowerVector powerCurrent) {

		boolean valueToReturn = false;

		if (Math.abs(ticksMoved) >= Math.abs(ticksMaximum)) {
			telemetry.log("Loop done - moved far enough");
			valueToReturn = true;
		} else if (secondsRunning >= secondsTimeout) {
			telemetry.log("Loop done - timed out");
			valueToReturn = true;
		} else if (hardwareMap != null) {

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
	 * Determine whether a ramp down should be running
	 *
	 * @param tickNumberAtStartOfPeriod
	 * 		The motor encoder tick number at which the period (including both the ramp and the portion at speed before
	 * 		the
	 * 		ramp) started.
	 * @param tickNumberCurrent
	 * 		The current motor encoder tick number.
	 * @param numberOfTicksInPeriod
	 * 		The number of motor encoder ticks over which the ramp should extend. Must be non-negative.
	 * @param powerAtStart
	 * 		The motor power at which the ramp started.
	 * @param powerAtEnd
	 * 		The power the motor should be at when the ramp ends.
	 *
	 * @return True if changing the power should start or continue. False otherwise.
	 */
	@Override
	public boolean isRampDownToEncoderTicksRunning(int tickNumberAtStartOfPeriod, int tickNumberCurrent, int
			numberOfTicksInPeriod, AarrePowerVector powerAtStart, AarrePowerVector powerAtEnd) {

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
		double tickToStartRampDown = getTickNumberToStartRampDown(tickNumberAtStartOfPeriod, numberOfTicksInPeriod,
				powerAtStart, powerAtEnd);

		if ((tickNumberCurrent >= tickToStartRampDown) && (tickNumberCurrent <= numberOfTicksInPeriod)) {
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
	 * The motor must not have moved more than a certain number of encoder clicks during a period of at least so many
	 * milliseconds before we consider it stalled.
	 *
	 * @return {@code true} if the motor is stalled; {@code false} otherwise.
	 */
	private boolean isStalled() {

		// TODO: Implement logging framework to allow logging by severity level
		telemetry.log("Time stalled = ", "%d ms", getTimeStalledInMilliseconds());
		telemetry.log("Stall time limit = ", "%d ms", stallTimeLimitInMilliseconds);

		boolean stalled = false;
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
	 * Rotate this motor a certain number of ticks from its current position, ramping speed up at the beginning and
	 * down
	 * at the end.
	 *
	 * @param powerVector
	 * 		The power at which to rotate, in the interval [-1.0, 1.0]. Positive values indicate rotation forward
	 * 		(increasing tick number). Negative values indicate rotation backward (decreasing tick number).
	 * @param numberOfTicksToRotate
	 * 		Maximum number of ticks to rotate. Must be non-negative.
	 * @param secondsTimeout
	 * 		Maximum number of seconds to rotate. Must be non-negative.
	 */
	final void rampToEncoderTicks(final AarrePowerVector powerVector, final int numberOfTicksToRotate, final double
			secondsTimeout) {

		if (secondsTimeout < 0.0) {
			throw new IllegalArgumentException("secondsTimeout must non-negative");
		} else if (numberOfTicksToRotate < 0) {
			throw new IllegalArgumentException("numberOfTicksToRotate must be non-negative");
		}

		setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		/*
		 * Ramp up for half of the ticks and down for the other half
		 *
		 * Ideally, the power profile should look something like this:
		 *
		 * ___/---\___
		 *
		 */
		int ticksToRampUp = numberOfTicksToRotate / 2;
		int ticksToRampDown = numberOfTicksToRotate - ticksToRampUp;

		telemetry.log("Motor - Ramp to encoder ticks(3), target power UP: %f", powerVector);
		rampToPower(powerVector, ticksToRampUp, secondsTimeout);

		telemetry.log("Motor - Ramp to encoder ticks(3), target power DOWN: %f", 0.0);
		rampToPower(new AarrePowerVector(0.0), ticksToRampDown, secondsTimeout);

		motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	}


	/**
	 * Ramp the motor power up (or down) gradually to the requested proportion.
	 * <p>
	 * The idea is to prevent slipping, sliding, jerking, wheelies, etc. due to excessive acceleration/deceleration.
	 * <p>
	 * This method uses default values for parameters that are likely not of interest to most callers.
	 *
	 * @param powerVectorRequested
	 */
	@Override
	public void rampToPower(final AarrePowerVector powerVectorRequested) {
		rampToPower(powerVectorRequested, DEFAULT_POWER_INCREMENT_PER_CYCLE, MILLISECONDS_PER_CYCLE,
				DEFAULT_PROPORTION_POWER_TOLERANCE, DEFAULT_SECONDS_TIMEOUT);
	}

	/**
	 * Ramp the motor power up (or down) gradually to the requested amount until ticksMaximum or secondsTimeout has
	 * been
	 * reached.
	 *
	 * @param powerVectorRequested
	 * 		The power to which the caller would like to ramp the motor at the end of the ramp time. Positive values
	 * 		indicate forward power; Negative values indicate reverse power. The value is expected to be in the interval
	 * 		[-1, 1].
	 * @param ticksToMove
	 * 		The number of encoder ticks to turn. The method will stop when this number has been reached, unless it
	 * 		times
	 * 		out first.
	 * @param secondsTimeout
	 * 		The maximum number of seconds to run. The method will stop when this number has been reached, unless it
	 * 		moves
	 * 		enough ticks first.
	 */
	private void rampToPower(final AarrePowerVector powerVectorRequested, final int ticksToMove, final double secondsTimeout) {

		AarrePowerVector powerVectorCurrent = this.getPowerVectorCurrent();
		AarrePowerVector powerVectorChange = powerVectorCurrent.subtract(powerVectorRequested);

		int powerChangeDirection = powerVectorChange.getDirection();

		if (powerChangeDirection == AarrePowerVector.FORWARD) {
			rampUpToPower(powerVectorRequested, ticksToMove, secondsTimeout);
		} else if (powerChangeDirection == AarrePowerVector.REVERSE) {
			rampDownToPower(powerVectorRequested, ticksToMove, secondsTimeout);
		} else {
			//No change
			if (powerChangeDirection != 0) {
				throw new RuntimeException("Power change direction should be -1, 0, or 1");
			}
		}

	}

	/**
	 * When ramping "down", the power change starts at the beginning of the motion, something like this:
	 *
	 * <pre>
	 * >_____
	 *       \
	 *        \
	 *         \
	 *          -|
	 * </pre>
	 * <p>
	 * TODO: This is seriously buggy, does not distinguish +/- from increase/decrease
	 */
	private void rampDownToPower(final AarrePowerVector powerVectorAtEnd, final int ticksToMove, final double secondsTimeout) {

		telemetry.log("Motor::rampDownToPower(3) - Target power: %f", powerVectorAtEnd);

		boolean keepWaiting;
		boolean keepGoing;
		int tickNumberStart;
		int tickNumberCurrent;

		AarrePowerVector powerVectorCurrent;
		AarrePowerVector powerVectorNew;
		double millisecondsSinceChange;

		ElapsedTime runtimeFromStart;
		ElapsedTime runtimeSinceChange;

		runtimeFromStart = new ElapsedTime();
		tickNumberStart = getCurrentTickNumber();

		waitForRampDown(powerVectorAtEnd, ticksToMove, tickNumberStart);

		/*
		 * Ramp down
		 */
		keepGoing = true;
		while (keepGoing && opMode.opModeIsActive()) {

			opMode.idle();
			tickNumberCurrent = getCurrentTickNumber();

			powerVectorCurrent = getPowerVectorCurrent();
			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorAtEnd);

			setPowerVector(powerVectorNew);

			telemetry.log("Motor::rampDownToPower(3) - Milliseconds elapsed %f", runtimeFromStart.milliseconds());
			telemetry.log("Motor::rampDownToPower(3) - Current tick number: %d", tickNumberCurrent);
			telemetry.log("Motor::rampDownToPower(3) - New power: %f", powerVectorNew);

			/*
			 * Wait for next power change
			 */
			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = 0.0;
			while ((millisecondsSinceChange < (double) MILLISECONDS_PER_CYCLE) && opMode.opModeIsActive()) {
				// Wait until it is time for next power increment
				//opMode.idle();
				millisecondsSinceChange = runtimeSinceChange.milliseconds();
			}

			keepGoing = isRampDownToEncoderTicksRunning(tickNumberStart, tickNumberCurrent, ticksToMove,
					powerVectorCurrent, powerVectorAtEnd);

		}
	}

	/**
	 * Wait until it is time to begin ramping down power to the motor.
	 *
	 * @param powerVectorAtEnd
	 * @param ticksToMove
	 * @param tickNumberStart
	 */
	private void waitForRampDown(AarrePowerVector powerVectorAtEnd, int ticksToMove, int tickNumberStart) {
		boolean keepWaiting;
		int tickNumberCurrent;
		AarrePowerVector powerVectorCurrent;

		/*
		 * Wait for the right time to start ramping down
		 */
		keepWaiting = true;
		while (keepWaiting && opMode.opModeIsActive()) {
			opMode.idle();
			tickNumberCurrent = getCurrentTickNumber();
			powerVectorCurrent = getPowerVectorCurrent();
			keepWaiting = !isRampDownToEncoderTicksRunning(tickNumberStart, tickNumberCurrent, ticksToMove,
					powerVectorCurrent, powerVectorAtEnd);
		}
	}

	/**
	 * When ramping "up", the power change starts at the beginning of the motion, something like this:
	 *
	 * <pre>
	 *          _____>
	 *         /
	 *        /
	 *     |_/
	 * </pre>
	 */
	private void rampUpToPower(final AarrePowerVector powerVectorRequested, final int ticksToMove, final double
			secondsTimeout) {

		telemetry.log("Motor::rampUpToPower(3) - Target power: %f", powerVectorRequested);
		telemetry.log("Motor::rampUpToPower(3) - Target ticks: %d", ticksToMove);

		ElapsedTime runtimeSinceChange;
		ElapsedTime runtimeTotal;

		double secondsRunning;
		int tickNumberCurrent;
		int ticksMoved;
		int tickNumberStart;

		AarrePowerVector powerVectorCurrent;
		AarrePowerVector powerVectorNew;
		AarrePowerVector powerDelta;
		double millisecondsSinceChange;

		tickNumberStart = getCurrentTickNumber();
		telemetry.log("Motor::rampUpToPower(3) - Starting tick number: %d", tickNumberStart);

		powerDelta = new AarrePowerVector(1.0);
		powerVectorCurrent = this.getPowerVectorCurrent();
		secondsRunning = 0.0;
		ticksMoved = 0;
		tickNumberCurrent = 0;

		runtimeTotal = new ElapsedTime();

		while (!isRampUpToEncoderTicksDone(ticksToMove, secondsTimeout, secondsRunning, ticksMoved, powerDelta,
				powerVectorCurrent)) {


			powerVectorCurrent = this.getPowerVectorCurrent();
			powerDelta = powerVectorRequested.subtract(powerVectorCurrent);

			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorRequested);

			setPowerVector(powerVectorNew);


			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = 0.0;

			while ((millisecondsSinceChange < (double) MILLISECONDS_PER_CYCLE) && opMode.opModeIsActive()) {
				// Wait until it is time for next power increment
				opMode.idle();
				millisecondsSinceChange = runtimeSinceChange.milliseconds();
			}

			tickNumberCurrent = getCurrentTickNumber();
			ticksMoved = tickNumberCurrent - tickNumberStart;
			telemetry.log("Motor::rampUpToPower(3) - Milliseconds elapsed %f", runtimeTotal.milliseconds());
			telemetry.log("Motor::rampUpToPower(3) - Current tick number: %d", tickNumberCurrent);
			telemetry.log("Motor::rampUpToPower(3) - New power: %f", powerVectorNew);

		}
	}

	/**
	 * Ramp the motor power up (or down) gradually to the requested amount.
	 * <p>
	 * The idea is to prevent slipping, sliding, jerking, wheelies, etc. due to excessive acceleration/deceleration.
	 *
	 * @param powerVectorRequested
	 * 		The power to which the caller would like to ramp the motor at the end of the ramp time. Positive values
	 * 		indicate forward power; Negative values indicate reverse power. The value is expected to be in the interval
	 * 		[-1, 1].
	 * @param powerIncrementMagnitude
	 * 		How much to increase or decrease the power during each cycle. The value is expected to be in the interval
	 * 		[0,
	 * 		1].
	 * @param millisecondsCycleLength
	 * 		The length of each cycle in milliseconds.
	 * @param powerToleranceMagnitude
	 * 		If the actual motor power is at least this close to the requested motor power, then we stop incrementing
	 * 		the
	 * 		power.
	 */
	@Override
	public void rampToPower(final AarrePowerVector powerVectorRequested, final AarrePowerMagnitude
			powerIncrementMagnitude, final int millisecondsCycleLength, final AarrePowerMagnitude
			powerToleranceMagnitude, final double secondsTimeout) {

		telemetry.log("Motor - Ramp to power (5), total target power: %f", powerVectorRequested);

		AarrePowerVector powerVectorCurrent;
		AarrePowerVector powerVectorNew;
		AarrePowerVector vectorOfLastPowerChange;
		AarrePowerMagnitude magnitudeOfLastPowerChange;
		double millisecondsSinceChange;

		magnitudeOfLastPowerChange = new AarrePowerMagnitude(1.0);

		while ((magnitudeOfLastPowerChange.isGreaterThan(powerToleranceMagnitude)) && opMode.opModeIsActive()) {

			powerVectorCurrent = getPowerVectorCurrent();

			vectorOfLastPowerChange = powerVectorRequested.subtract(powerVectorCurrent);
			magnitudeOfLastPowerChange = vectorOfLastPowerChange.getMagnitude();

			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorRequested);

			telemetry.log("Motor - Ramp power to, current power target: %f", powerVectorNew);

			setPowerVector(powerVectorNew);

			/*
			 * Wait for: (1) the cycle period to elapse, (2) the Op Mode to go inactive, (3) the
			 * motor to stop, or (4) the timeout period to expire.
			 */

			ElapsedTime elapsedTimeTotal = new ElapsedTime();
			ElapsedTime elapsedTimeSinceChange = new ElapsedTime();

			millisecondsSinceChange = 0.0;
			double secondsRunning = 0.0;
			boolean isMotorBusy = true;

			while ((millisecondsSinceChange < (double) millisecondsCycleLength) && opMode.opModeIsActive() &&
					isMotorBusy && (secondsRunning < secondsTimeout)) {

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
	final void runByRevolutions(final AarrePowerVector proportionMotorPower, final double numberOfRevolutions, final
	double secondsTimeout) {

		final int numberOfTicks = (int) Math.round(getTicksPerRevolution() * numberOfRevolutions);
		telemetry.log("Motor - Run by revolutions, power: %f", proportionMotorPower);
		rampToEncoderTicks(proportionMotorPower, numberOfTicks, secondsTimeout);
	}

	/**
	 * Run the motor for a fixed amount of time. This method of moving the motor does not depend on an encoder.
	 *
	 * @param powerVector
	 * 		Proportion of power to apply to the motor, in the range [-1.0, 1.0]. Negative values run the motor
	 * 		backward.
	 * @param secondsToRun
	 * 		Number of seconds for which to run the motor.
	 */
	final void runByTime(final AarrePowerVector powerVector, final double secondsToRun) {

		if (secondsToRun < 0.0) {
			throw new IllegalArgumentException("secondsTimeout expected to be non-negative");
		}

		final ElapsedTime runtime;
		double secondsRunning;

		setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		rampToPower(powerVector);
		runtime = new ElapsedTime();
		secondsRunning = runtime.seconds();

		while (secondsRunning < secondsToRun && opMode.opModeIsActive()) {
			secondsRunning = runtime.seconds();
		}

		rampToPower(new AarrePowerVector(0.0));
		setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

	}


	/**
	 * Run this motor until it stalls
	 *
	 * @param power
	 * 		How much power to apply to the motor, in the interval [-1,1].
	 */
	void runUntilStalled(final AarrePowerVector power) {
		timeStalledInMilliseconds = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
		rampToPower(power);
		while (!(isStalled()) && opMode.opModeIsActive()) {
			//telemetry.log("Not stalled yet...");
			opMode.idle();
		}
		rampToPower(new AarrePowerVector(0.0));
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
	void setMode(final DcMotor.RunMode mode) {
		motor.setMode(mode);
	}

	@Override
	public void setPowerMagnitudeTolerance(AarrePowerMagnitude powerMagnitude) {
		powerMagnitudeTolerance = powerMagnitude;
	}

	@Override
	public void setPowerMagnitudeIncrementPerCycle(AarrePowerMagnitude powerMagnitude) {
		powerMagnitudeIncrementPerCycle = powerMagnitude;
	}

	@Override
	public void setPowerVector(final AarrePowerVector powerVector) {
		motor.setPower(powerVector.asDouble());
	}


	/**
	 * Set the stall detection tolerance
	 *
	 * @param ticks
	 * 		An integer number of encoder clicks such that, if the encoder changes fewer than this number of clicks
	 * 		over a
	 * 		period of time defined by stallTimeLimitInMilliseconds, then we consider the motor stalled.
	 */
	@Override
	public void setStallDetectionToleranceInTicks(final int ticks) {
		stallDetectionToleranceInTicks = ticks;
	}

	/**
	 * Set the stall detection time limit in milliseconds.
	 *
	 * @param milliseconds
	 * 		The number of milliseconds during which the motor must not have moved more than the stall detection
	 * 		tolerance
	 * 		before we call it a stall.
	 */
	void setStallTimeLimitInMilliseconds(final int milliseconds) {
		stallTimeLimitInMilliseconds = milliseconds;
	}

	/**
	 * Set the stall detection time limit in seconds.
	 *
	 * @param seconds
	 * 		The number of seconds during which the motor must not have moved more than the stall detection tolerance
	 * 		before
	 * 		we call it a stall.
	 */
	void setStallTimeLimitInSeconds(final double seconds) {
		final int milliseconds = (int) (seconds * (double) MILLISECONDS_PER_SECOND);
		setStallTimeLimitInMilliseconds(milliseconds);
	}

	void setTargetPosition(final int targetPositionTicks) {
		motor.setTargetPosition(targetPositionTicks);
	}


	void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
		motor.setZeroPowerBehavior(zeroPowerBehavior);
	}

}
