package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Date;
import java.util.logging.*;



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
public abstract class AarreMotor implements AarreMotorInterface, ConcreteMotorInterface {

	private static final int SECONDS_PER_MINUTE = 60;

	private static final int MILLISECONDS_PER_SECOND = 1000;

	private static final int MILLISECONDS_PER_CYCLE = 50;

	// How much to increment the motor power in each cycle of power ramping (slowing down / speeding up)
	private static final AarrePowerMagnitude DEFAULT_POWER_INCREMENT_PER_CYCLE = new AarrePowerMagnitude(0.1);

	// How much tolerance to allow when deciding whether we have reached a requested motor power
	private static final AarrePowerMagnitude DEFAULT_PROPORTION_POWER_TOLERANCE = new AarrePowerMagnitude(0.01);

	// How long to allow a motor operation to continue before timing out
	private static final double DEFAULT_SECONDS_TIMEOUT = 5.0;

	private final  DcMotor             motor;
	static private AarreTelemetry      telemetry;
	private final  LinearOpMode        opMode;
	private final  HardwareMap         hardwareMap;
	private        AarrePowerMagnitude powerMagnitudeIncrementPerCycle = DEFAULT_POWER_INCREMENT_PER_CYCLE;
	private        AarrePowerMagnitude powerMagnitudeTolerance         = DEFAULT_PROPORTION_POWER_TOLERANCE;
	private        int                 oldTickNumber                   = 0;
	private        int                 stallTimeLimitInMilliseconds;
	private        int                 stallDetectionToleranceInTicks;
	private        ElapsedTime         timeStalledInMilliseconds       = null;

	static Logger log;

	static {

		log = Logger.getLogger("AarreMotor");
		log.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		handler.setFormatter(new SimpleFormatter() {

			private static final String format = "%1$tF %1$tT [%2$s] %4$s::%5$s - %6$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				String formattedLogRecord = String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(), lr.getLoggerName(), lr.getSourceClassName(), lr.getSourceMethodName(), lr.getMessage());
				return formattedLogRecord;
			}

		});
		log.addHandler(handler);
		log.setLevel(Level.ALL);
	}

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

	/**
	 * Get the current reading of the encoder for this motor.
	 * <p>
	 * Despite its name, the {@link DcMotor} method {@code getCurrentPosition} provides almost no information about
	 * position. Therefore, we use a different name here.
	 *
	 * @return The current reading of the encoder for this motor, in ticks.
	 */
	public final int getCurrentTickNumber() {
		return motor.getCurrentPosition();
	}

	/**
	 * Get the number of cycles for which a ramp (slowing down/speeding up) should last
	 * <p>
	 * The current power is a parameter here (instead of using the getter for the corresponding class field) to allow
	 * for off-bot unit testing.
	 * <p>
	 */
	public int getNumberOfCycles(int ticksToMove, AarrePowerVector powerVectorCurrent, AarrePowerVector
			powerVectorRequested) {

		// The magnitude of the current and requested power
		AarrePowerMagnitude powerMagnitudeCurrent   = powerVectorCurrent.getMagnitude();
		AarrePowerMagnitude powerMagnitudeRequested = powerVectorRequested.getMagnitude();

		// The average number of ticks per cycle during the ramp
		double              currentMagnitude      = powerMagnitudeCurrent.asDouble();
		double              requestedMagnitude    = powerMagnitudeRequested.asDouble();
		double              averageMagnitude      = (currentMagnitude + requestedMagnitude) / 2;
		AarrePowerMagnitude averagePowerMagnitude = new AarrePowerMagnitude(averageMagnitude);
		double              averageTicksPerCycle  = averageMagnitude * getTicksPerCycle(averagePowerMagnitude);

		// The magnitude of the power change over the ramp
		AarrePowerVector    powerVectorChangeOverRamp    = powerVectorRequested.subtract(powerVectorCurrent);
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
		int    powerChangeDirection = (int) Math.signum(powerChangeDouble);

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

	final public double getPower() {
		return motor.getPower();
	}

	final public double getTickNumberToStartSlowDown(final int tickNumberAtStartOfPeriod, final AarrePositiveInteger
			numberOfTicksInPeriod, final AarrePowerVector powerVectorAtStartOfPeriod, final AarrePowerVector
			powerVectorAtEndOfPeriod) {

		AarrePowerMagnitude powerMagnitudeAtStartOfPeriod = powerVectorAtStartOfPeriod.getMagnitude();
		AarrePowerMagnitude powerMagnitudeAtEndOfPeriod   = powerVectorAtEndOfPeriod.getMagnitude();
		if (powerMagnitudeAtStartOfPeriod.asDouble() <= powerMagnitudeAtEndOfPeriod.asDouble()) {
			throw new IllegalArgumentException("When slowing down, the absolute value of the " + "power at the start "
					+ "of the slowdown must be greater " + "than the absolute value of the power at the end " + "of "
					+ "the " + "slowdown" + ".");
		}

		AarrePowerVector    powerChangeVector      = powerVectorAtStartOfPeriod.subtract(powerVectorAtEndOfPeriod);
		AarrePowerMagnitude magnitudeOfPowerChange = powerChangeVector.getMagnitude();
		int                 powerChangeDirection   = powerChangeVector.getDirection();

		final double numberOfCyclesInSlowDown = magnitudeOfPowerChange.divideBy(getPowerMagnitudeIncrementPerCycle());
		final double numberOfTicksInSlowDown  = numberOfCyclesInSlowDown * getTicksPerCycle();
		final double numberOfTicksToChange    = powerChangeDirection * numberOfTicksInSlowDown;
		final double tickNumberAtEndOfPeriod = tickNumberAtStartOfPeriod + (numberOfTicksInPeriod.intValue() *
				powerChangeDirection);
		final double tickNumberToStartSlowDown = tickNumberAtEndOfPeriod - numberOfTicksToChange;

		return tickNumberToStartSlowDown;
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


	public boolean isSpeedUpToEncoderTicksDone(AarrePositiveInteger ticksMaximum, double secondsTimeout, double
			secondsRunning, AarreNonNegativeInteger ticksMoved) throws NoSuchMethodException {

		log.entering(this.getClass().getCanonicalName(), this.getClass().getMethod("isSpeedUpToEncoderTicksDone",
				AarrePositiveInteger.class, double.class, double.class, AarreNonNegativeInteger.class).getName());

		boolean valueToReturn = false;

		log.finest(String.format("ticksMaximum: %f", ticksMaximum.doubleValue()));
		log.finest(String.format("secondsTimeout: %f", secondsTimeout));
		log.finest(String.format("secondsRunning: %f", secondsRunning));
		log.finest(String.format("ticksMoved: %f", ticksMoved.doubleValue()));

		if (Math.abs(ticksMoved.intValue()) >= Math.abs(ticksMaximum.intValue())) {
			log.finest("Loop done - moved far enough");
			valueToReturn = true;
		} else if (secondsRunning >= secondsTimeout) {
			log.finest("Loop done - timed out");
			valueToReturn = true;
		} else if (hardwareMap != null) {

			/*
			 * Only check whether Op Mode is active if hardware map is
			 * not null. Otherwise, we are running off-bot, and Op Mode
			 * will never be active. Returning false in that case allows
			 * us to use off-bot unit tests to test the other conditions.
			 */
			if (!opMode.opModeIsActive()) {
				log.finest("Loop done - On robot but op mode is not active");
				valueToReturn = true;
			}
		}
		log.exiting(this.getClass().getCanonicalName(), this.getClass().getMethod("isSpeedUpToEncoderTicksDone",
				AarrePositiveInteger.class, double.class, double.class, AarreNonNegativeInteger.class).getName());
		return valueToReturn;
	}


	public boolean isSlowDownToEncoderTicksRunning(int tickNumberAtStartOfTravel, int tickNumberCurrent,
	                                               AarrePositiveInteger numberOfTicksToTravel, AarrePowerVector
			                                               powerAtStartOfTravel, AarrePowerVector powerAtEndOfTravel) {

		double tickNumberAtEndOfPeriod = tickNumberAtStartOfTravel + numberOfTicksToTravel.doubleValue();

		boolean result = false;

		double tickNumberToStartSlowDown = getTickNumberToStartSlowDown(tickNumberAtStartOfTravel,
				numberOfTicksToTravel, powerAtStartOfTravel, powerAtEndOfTravel);

		if (tickNumberCurrent < tickNumberAtStartOfTravel) {
			throw new IllegalArgumentException("The current tick number must be within the period of travel");
		}

		if (((double) tickNumberCurrent >= tickNumberToStartSlowDown) && ((double) tickNumberCurrent <=
				tickNumberAtEndOfPeriod)) {
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
	 * Rotate this motor a certain number of ticks from its current position, speeding up at the beginning and slowing
	 * down at the end.
	 * <p>
	 * We want to speed up and hold max speed for half of the ticks and then slow down and hold min speed for the other
	 * half of the ticks.
	 * <p>
	 * In other words, ideally, the power profile should look something like this:
	 * <p>
	 * ___/---\___
	 * <p>
	 * Things are complicated, however, because there may not be enough ticks to do a full speed up and slow down.
	 * </p>
	 *
	 * @param powerVector
	 * 		The power at which to rotate, in the interval [-1.0, 1.0]. Positive values indicate rotation forward
	 * 		(increasing tick number). Negative values indicate rotation backward (decreasing tick number).
	 * @param ticksToRotate
	 * 		Maximum number of ticks to rotate. Must be non-negative.
	 * @param secondsTimeout
	 * 		Maximum number of seconds to rotate. Must be non-negative.
	 */
	final void rampToEncoderTicks(final AarrePowerVector powerVector, final AarrePositiveInteger ticksToRotate, final
	double secondsTimeout) throws NoSuchMethodException {

		if (secondsTimeout < 0.0) {
			throw new IllegalArgumentException("secondsTimeout must non-negative");
		}

		setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		double               ticksToSpeedUpDouble = ticksToRotate.doubleValue() / 2.0;
		int                  ticksToSpeedUpInt    = (int) Math.round(ticksToSpeedUpDouble);
		AarrePositiveInteger ticksToSpeedUp       = new AarrePositiveInteger(ticksToSpeedUpInt);

		int                  ticksToSlowDownInt = ticksToRotate.intValue() - ticksToSpeedUp.intValue();
		AarrePositiveInteger ticksToSlowDown    = new AarrePositiveInteger(ticksToSlowDownInt);

		log.fine(String.format("Motor - Ramp to encoder ticks(3), target power UP: %f" + powerVector.asDouble()));
		rampToPower(powerVector, ticksToSpeedUp, secondsTimeout);

		log.fine(String.format("Motor - Ramp to encoder ticks(3), target power DOWN: %f", 0.0));
		rampToPower(new AarrePowerVector(0.0), ticksToSlowDown, secondsTimeout);

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
	 * 		The number of encoder ticks to move. The method will stop when this number has been reached, unless it
	 * 		times
	 * 		out first.
	 * @param secondsTimeout
	 * 		The maximum number of seconds to run. The method will stop when this number has been reached, unless it
	 * 		moves
	 * 		enough ticks first.
	 */
	private void rampToPower(final AarrePowerVector powerVectorRequested, final AarrePositiveInteger ticksToMove,
	                         final double secondsTimeout) throws NoSuchMethodException {

		AarrePowerVector powerVectorCurrent = this.getPowerVectorCurrent();
		AarrePowerVector powerVectorChange  = powerVectorCurrent.subtract(powerVectorRequested);

		int powerChangeDirection = powerVectorChange.getDirection();

		if (powerChangeDirection == AarrePowerVector.FORWARD) {
			speedUpToPower(powerVectorRequested, ticksToMove, secondsTimeout);
		} else if (powerChangeDirection == AarrePowerVector.REVERSE) {
			slowDownToPower(powerVectorRequested, ticksToMove, secondsTimeout);
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
	private void slowDownToPower(final AarrePowerVector powerVectorAtEnd, final AarrePositiveInteger ticksToMove,
	                             final double secondsTimeout) {

		telemetry.log("Motor::slowDownToPower(3) - Target power: %f", powerVectorAtEnd);

		boolean keepWaiting;
		boolean keepGoing;
		int     tickNumberStart;
		int     tickNumberCurrent;

		AarrePowerVector powerVectorCurrent;
		AarrePowerVector powerVectorNew;
		double           millisecondsSinceChange;

		ElapsedTime runtimeFromStart;
		ElapsedTime runtimeSinceChange;

		runtimeFromStart = new ElapsedTime();
		tickNumberStart = getCurrentTickNumber();

		waitForSlowDown(powerVectorAtEnd, ticksToMove, tickNumberStart);

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

			telemetry.log("Motor::slowDownToPower(3) - Milliseconds elapsed %f", runtimeFromStart.milliseconds());
			telemetry.log("Motor::slowDownToPower(3) - Current tick number: %d", tickNumberCurrent);
			telemetry.log("Motor::slowDownToPower(3) - New power: %f", powerVectorNew);

			/*
			 * Wait for next power change
			 */
			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = 0.0;
			while ((millisecondsSinceChange < (double) MILLISECONDS_PER_CYCLE) && opMode.opModeIsActive()) {
				// Wait until it is time for next power increment
				opMode.idle();
				millisecondsSinceChange = runtimeSinceChange.milliseconds();
			}

			keepGoing = isSlowDownToEncoderTicksRunning(tickNumberStart, tickNumberCurrent, ticksToMove,
					powerVectorCurrent, powerVectorAtEnd);

		}
	}

	/**
	 * Wait until it is time to begin slowing down power to the motor.
	 *
	 * @param powerVectorAtEnd
	 * @param ticksToMove
	 * @param tickNumberStart
	 */
	private void waitForSlowDown(AarrePowerVector powerVectorAtEnd, AarrePositiveInteger ticksToMove, int
			tickNumberStart) {
		boolean          keepWaiting;
		int              tickNumberCurrent;
		AarrePowerVector powerVectorCurrent;

		/*
		 * Wait for the right time to start slowing down
		 */
		keepWaiting = true;
		while (keepWaiting && opMode.opModeIsActive()) {
			opMode.idle();
			tickNumberCurrent = getCurrentTickNumber();
			powerVectorCurrent = getPowerVectorCurrent();
			keepWaiting = !isSlowDownToEncoderTicksRunning(tickNumberStart, tickNumberCurrent, ticksToMove,
					powerVectorCurrent, powerVectorAtEnd);
		}
	}

	/**
	 * When speeding up, the power change starts at the beginning of the motion, something like this:
	 *
	 * <pre>
	 *          _____>
	 *         /
	 *        /
	 *     |_/
	 * </pre>
	 */
	private void speedUpToPower(final AarrePowerVector powerVectorRequested, final AarrePositiveInteger ticksToMove,
	                            final double secondsTimeout) throws NoSuchMethodException {

		log.fine(String.format("Target power: %f", powerVectorRequested));
		log.fine(String.format("Target ticks: %d", ticksToMove));

		ElapsedTime runtimeSinceChange;
		ElapsedTime runtimeTotal;

		double secondsRunning;
		int    tickNumberCurrent;
		int    tickNumberStart;

		AarrePowerVector powerVectorNew;
		double           millisecondsSinceChange;

		tickNumberStart = getCurrentTickNumber();
		log.fine(String.format("Motor::speedUpToPower(3) - Starting tick number: %d", tickNumberStart));

		secondsRunning = 0.0;
		AarreNonNegativeInteger ticksMoved = new AarreNonNegativeInteger(0);

		runtimeTotal = new ElapsedTime();

		while (!isSpeedUpToEncoderTicksDone(ticksToMove, secondsTimeout, secondsRunning, ticksMoved)) {

			AarrePowerVector powerVectorCurrent = this.getPowerVectorCurrent();
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
			ticksMoved = new AarreNonNegativeInteger(tickNumberCurrent - tickNumberStart);
			log.fine(String.format("Motor::speedUpToPower(3) - Milliseconds elapsed %f", runtimeTotal
					.milliseconds()));
			log.fine(String.format("Motor::speedUpToPower(3) - Current tick number: %d", tickNumberCurrent));
			log.fine(String.format("Motor::speedUpToPower(3) - New power: %f", powerVectorNew));

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
	public void rampToPower(final AarrePowerVector powerVectorRequested, final AarrePowerMagnitude
			powerIncrementMagnitude, final int millisecondsCycleLength, final AarrePowerMagnitude
			powerToleranceMagnitude, final double secondsTimeout) {

		log.fine(String.format("Ramp to power (5), total target power: %f", powerVectorRequested.asDouble()));
		if (opMode.opModeIsActive()) {
			log.fine("Op mode is active");
		} else {
			log.fine("Op mode is NOT active");
		}

		AarrePowerVector    powerVectorCurrent;
		AarrePowerVector    powerVectorNew;
		AarrePowerVector    vectorOfLastPowerChange;
		AarrePowerMagnitude magnitudeOfLastPowerChange;
		double              millisecondsSinceChange;

		magnitudeOfLastPowerChange = new AarrePowerMagnitude(1.0);

		while ((magnitudeOfLastPowerChange.isGreaterThan(powerToleranceMagnitude)) && opMode.opModeIsActive()) {

			log.fine("Op mode is active");
			powerVectorCurrent = getPowerVectorCurrent();

			vectorOfLastPowerChange = powerVectorRequested.subtract(powerVectorCurrent);
			magnitudeOfLastPowerChange = vectorOfLastPowerChange.getMagnitude();

			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorRequested);

			log.fine(String.format("Ramp to power, current power target: %f", powerVectorNew.asDouble()));

			setPowerVector(powerVectorNew);

			/*
			 * Wait for: (1) the cycle period to elapse, (2) the Op Mode to go inactive, (3) the
			 * motor to stop, or (4) the timeout period to expire.
			 */

			ElapsedTime elapsedTimeTotal       = new ElapsedTime();
			ElapsedTime elapsedTimeSinceChange = new ElapsedTime();

			millisecondsSinceChange = 0.0;
			double  secondsRunning = 0.0;
			boolean isMotorBusy    = true;

			while ((millisecondsSinceChange < (double) millisecondsCycleLength) && opMode.opModeIsActive() &&
					isMotorBusy && (secondsRunning < secondsTimeout)) {

				opMode.idle();
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
	 * 		The proportion of power to apply to the motor. If positive, then the motor will run forward. If negative,
	 * 		then
	 * 		the motor will run in reverse.
	 * @param targetNumberOfRevolutions
	 * 		The number of revolutions to turn the motor. Must be positive.
	 * @param secondsTimeout
	 * 		The operation will time out after this many seconds even if the target number of revolutions has not been
	 * 		reached.
	 */
	final void runByRevolutions(final AarrePowerVector proportionMotorPower, final double targetNumberOfRevolutions,
	                            final double secondsTimeout) throws NoSuchMethodException {

		log.entering(this.getClass().getCanonicalName(), "runByRevolutions");

		log.finer(String.format("Proportion motor power: %f", proportionMotorPower.asDouble()));
		log.finer(String.format("Target number of revolutions: %f", targetNumberOfRevolutions));
		log.finer(String.format("Seconds timeout: %f", secondsTimeout));

		final double ticksPerRevolution = getTicksPerRevolution();

		log.finer(String.format("Ticks per revolution: %f", ticksPerRevolution));

		final int numberOfTicksToRunInt = (int) Math.round(getTicksPerRevolution() * targetNumberOfRevolutions);

		log.finer(String.format("Number of ticks to run (int): %d", numberOfTicksToRunInt));

		final AarrePositiveInteger numberOfTicksToRun = new AarrePositiveInteger(numberOfTicksToRunInt);

		log.finer(String.format("Number of ticks to run (AarrePositiveInteger): %f", numberOfTicksToRun.doubleValue
				()));

		rampToEncoderTicks(proportionMotorPower, numberOfTicksToRun, secondsTimeout);

		log.exiting(this.getClass().getCanonicalName(), "runByRevolutions");
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
		double            secondsRunning;

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
			log.finest("Not stalled yet...");
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
	public void setDirection(final DcMotorSimple.Direction direction) {
		if (motor != null) {
			motor.setDirection(direction);
		}
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

	public void setPowerMagnitudeTolerance(AarrePowerMagnitude powerMagnitude) {
		powerMagnitudeTolerance = powerMagnitude;
	}

	public void setPowerMagnitudeIncrementPerCycle(AarrePowerMagnitude powerMagnitude) {
		powerMagnitudeIncrementPerCycle = powerMagnitude;
	}

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

	public int getMillisecondsPerCycle() {
		return MILLISECONDS_PER_CYCLE;
	}

	public double getTicksPerMinute() {
		return getTicksPerRevolution() * getRevolutionsPerMinute();
	}

	public double getTicksPerMinute(AarrePowerMagnitude powerMagnitude) {
		return getTicksPerRevolution() * getRevolutionsPerMinute(powerMagnitude);
	}


	public double getTicksPerSecond() {
		return getTicksPerMinute() / SECONDS_PER_MINUTE;
	}

	public double getTicksPerSecond(AarrePowerMagnitude powerMagnitude) {
		return getTicksPerMinute(powerMagnitude) / SECONDS_PER_MINUTE;
	}

	public double getTicksPerMillisecond() {
		return getTicksPerSecond() / MILLISECONDS_PER_SECOND;
	}

	public double getTicksPerMillisecond(AarrePowerMagnitude powerMagnitude) {
		return getTicksPerSecond(powerMagnitude) / MILLISECONDS_PER_SECOND;
	}

	public double getTicksPerCycle() {
		return getTicksPerMillisecond() * getMillisecondsPerCycle();
	}

	public double getTicksPerCycle(AarrePowerMagnitude powerMagnitude) {
		return getTicksPerMillisecond(powerMagnitude) * getMillisecondsPerCycle();
	}




}
