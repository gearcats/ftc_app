package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
public abstract class Motor implements MotorInterface {

	private static final int SECONDS_PER_MINUTE = 60;

	private static final int MILLISECONDS_PER_SECOND = 1000;

	private static final int MILLISECONDS_PER_CYCLE = 50;

	// How much to increment the motor power in each cycle of power ramping (slowing down / speeding up)
	private static final PowerMagnitude DEFAULT_POWER_INCREMENT_PER_CYCLE = new PowerMagnitude(0.1);

	// How much tolerance to allow when deciding whether we have reached a requested motor power
	private static final PowerMagnitude DEFAULT_PROPORTION_POWER_TOLERANCE = new PowerMagnitude(0.01);

	// How long to allow a motor operation to continue before timing out
	private static final double DEFAULT_SECONDS_TIMEOUT = 5.0;

	private PowerMagnitude powerMagnitudeIncrementPerCycle = DEFAULT_POWER_INCREMENT_PER_CYCLE;
	private PowerMagnitude powerMagnitudeTolerance         = DEFAULT_PROPORTION_POWER_TOLERANCE;
	private int            oldTickNumber                   = 0;

	// These are defaults. The user should customize them
	private int stallTimeLimitInMilliseconds   = 100;
	private int stallDetectionToleranceInTicks = 5;

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



	/**
	 * Get the current reading of the encoder for this getMotorRevHDCoreHex().
	 * <p>
	 * Despite its name, the {@link DcMotor} method {@code getCurrentPosition} provides almost no information about
	 * position. Therefore, we use a different name here.
	 *
	 * @return The current reading of the encoder for this motor, in ticks.
	 */
	public int getCurrentTickNumber() {
		DcMotor.RunMode mode = getMotor().getMode();
		if (mode != DcMotor.RunMode.RUN_USING_ENCODER) {
			log.warning("Motor is not using encoder!");
		}
		return getMotor().getCurrentPosition();
	}

	/**
	 * Get the number of cycles for which a ramp (slowing down/speeding up) should last
	 * <p>
	 * The current power is a parameter here (instead of using the getter for the corresponding class field) to allow
	 * for off-bot unit testing.
	 * <p>
	 */
	public int getNumberOfCycles(int ticksToMove, PowerVector powerVectorCurrent, PowerVector
			powerVectorRequested) {

		// The magnitude of the current and requested power
		PowerMagnitude powerMagnitudeCurrent   = powerVectorCurrent.getMagnitude();
		PowerMagnitude powerMagnitudeRequested = powerVectorRequested.getMagnitude();

		// The average number of ticks per cycle during the ramp
		double         currentMagnitude      = powerMagnitudeCurrent.doubleValue();
		double         requestedMagnitude    = powerMagnitudeRequested.doubleValue();
		double         averageMagnitude      = (currentMagnitude + requestedMagnitude) / 2;
		PowerMagnitude averagePowerMagnitude = new PowerMagnitude(averageMagnitude);
		double         averageTicksPerCycle  = averageMagnitude * getTicksPerCycle(averagePowerMagnitude);

		// The magnitude of the power change over the ramp
		PowerVector    powerVectorChangeOverRamp    = powerVectorRequested.subtract(powerVectorCurrent);
		PowerMagnitude powerMagnitudeChangeOverRamp = powerVectorChangeOverRamp.getMagnitude();

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


	public final PowerMagnitude getPowerMagnitudeIncrementPerCycle() {
		return this.powerMagnitudeIncrementPerCycle;
	}

	public PowerVector getPowerVectorCurrent() {
		return new PowerVector(getMotor().getPower());
	}

	public final PowerVector getPowerVectorNew(PowerVector powerVectorCurrent, PowerVector
			powerVectorRequested) {

		PowerVector powerVectorNew;

		/*
		 * Use a double here because the power change can be outside the range of a power vector.
		 * For example, if the power requested is -1 and the current power is 1, then the power
		 * change is -1 - 1 = -2.
		 *
		 * TODO: Create "AarrePowerChange" class???
		 */
		double powerChangeDouble = powerVectorRequested.doubleValue() - powerVectorCurrent.doubleValue();

		// The magnitude by which the power must change to reach the requested power
		double powerChangeMagnitude = Math.abs(powerChangeDouble);
		int    powerChangeDirection = (int) Math.signum(powerChangeDouble);

		if (powerChangeMagnitude <= getPowerMagnitudeIncrementPerCycle().doubleValue()) {
			// Within one cycle, give them what they really want...
			powerVectorNew = powerVectorRequested;
		} else {
			// Otherwise, give them what they need...
			PowerVector powerIncrementVector = new PowerVector(getPowerMagnitudeIncrementPerCycle(),
					powerChangeDirection);
			powerVectorNew = powerVectorCurrent.add(powerIncrementVector);
		}

		return powerVectorNew;

	}

	final public double getPower() {
		return getMotor().getPower();
	}

	final public double getTickNumberToStartSlowDown(final int tickNumberAtStartOfPeriod, final PositiveInteger
			numberOfTicksInPeriod, final PowerVector powerVectorAtStartOfPeriod, final PowerVector
			powerVectorAtEndOfPeriod) {

		log.entering(getClass().getCanonicalName(), "getTickNumberToStartSlowDown");

		log.finer(String.format("Tick number at start of period: %d", tickNumberAtStartOfPeriod));
		log.finer(String.format("Number of ticks in period: %d", numberOfTicksInPeriod.intValue()));
		log.finer(String.format("Power vector at start of period: %f", powerVectorAtStartOfPeriod.doubleValue()));
		log.finer(String.format("Power vector at end of period: %f", powerVectorAtEndOfPeriod.doubleValue()));

		PowerMagnitude powerMagnitudeAtStartOfPeriod = powerVectorAtStartOfPeriod.getMagnitude();
		log.finer(String.format("Power magnitude at start of period: %f", powerMagnitudeAtStartOfPeriod.doubleValue
				()));

		PowerMagnitude powerMagnitudeAtEndOfPeriod   = powerVectorAtEndOfPeriod.getMagnitude();
		log.finer(String.format("Power magnitude at end of period: %f", powerMagnitudeAtEndOfPeriod.doubleValue()));

		if (powerMagnitudeAtStartOfPeriod.doubleValue() <= powerMagnitudeAtEndOfPeriod.doubleValue()) {
			throw new IllegalArgumentException("When slowing down, the magnitude of the " + "power at the start " +
					"of the slowdown must be greater " + "than the magnitude of the power at the end " + "of "
					+ "the " + "slowdown" + ".");
		}

		PowerVector    powerChangeVector      = powerVectorAtStartOfPeriod.subtract(powerVectorAtEndOfPeriod);
		log.finer(String.format("Power change vector: %f", powerChangeVector.doubleValue()));

		PowerMagnitude magnitudeOfPowerChange = powerChangeVector.getMagnitude();
		log.finer(String.format("Magnitude of power change: %f", magnitudeOfPowerChange.doubleValue()));

		int            powerChangeDirection   = powerChangeVector.getDirection();
		log.finer(String.format("Power change direction: %d", powerChangeDirection));


		final double numberOfCyclesInSlowDown = magnitudeOfPowerChange.divideBy(getPowerMagnitudeIncrementPerCycle());
		log.finer(String.format("Number of cycles in slowdown %f", numberOfCyclesInSlowDown));

		final double numberOfTicksInSlowDown  = numberOfCyclesInSlowDown * getTicksPerCycle();
		log.finer(String.format("Number of ticks in slowdown: %f", numberOfTicksInSlowDown));

		final double numberOfTicksToChange    = powerChangeDirection * numberOfTicksInSlowDown;
		log.finer(String.format("Number of ticks to change: %f", numberOfTicksToChange));

		final double tickNumberAtEndOfPeriod = tickNumberAtStartOfPeriod + (numberOfTicksInPeriod.intValue() *
				powerChangeDirection);
		log.finer(String.format("Tick number at end of period: %f", tickNumberAtEndOfPeriod));

		final double tickNumberToStartSlowDown = tickNumberAtEndOfPeriod - numberOfTicksToChange;
		log.finer(String.format("Tick number to start slowdown: %f", tickNumberToStartSlowDown));

		log.exiting(getClass().getCanonicalName(), "getTickNumberToStartSlowDown");

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


	public boolean isSpeedUpToEncoderTicksDone(PositiveInteger ticksMaximum, double secondsTimeout, double secondsRunning, NonNegativeInteger ticksMoved) {

		log.entering(this.getClass().getCanonicalName(), "isSpeedUpToEncoderTicksDone");

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
		} else if (getHardwareMap() != null) {

			/*
			 * Only check whether Op Mode is active if hardware map is
			 * not null. Otherwise, we are running off-bot, and Op Mode
			 * will never be active. Returning false in that case allows
			 * us to use off-bot unit tests to test the other conditions.
			 */
			if (!getOpMode().opModeIsActive()) {
				log.finest("Loop done - On robot but op mode is not active");
				valueToReturn = true;
			}
		}
		log.exiting(this.getClass().getCanonicalName(), "isSpeedUpToEncoderTicksDone");
		return valueToReturn;
	}


	public boolean isSlowDownToEncoderTicksRunning(int tickNumberAtStartOfTravel, int tickNumberCurrent,
	                                               PositiveInteger numberOfTicksToTravel, PowerVector powerAtStartOfTravel, PowerVector powerAtEndOfTravel) {


		log.entering(getClass().getCanonicalName(), "isSlowDownToEncoderTicksRunning");

		log.finer(String.format("Tick number at start of travel: %d", tickNumberAtStartOfTravel));
		log.finer(String.format("Current tick number: %d", tickNumberCurrent));
		log.finer(String.format("Number of ticks to travel: %d", numberOfTicksToTravel.intValue()));
		log.finer(String.format("Power vector at start of travel: %f", powerAtStartOfTravel.doubleValue()));
		log.finer(String.format("Power vector at end of travel: %f", powerAtEndOfTravel.doubleValue()));

		double tickNumberAtEndOfTravel = tickNumberAtStartOfTravel + numberOfTicksToTravel.doubleValue();

		boolean result = false;

		double tickNumberToStartSlowDown = getTickNumberToStartSlowDown(tickNumberAtStartOfTravel,
				numberOfTicksToTravel, powerAtStartOfTravel, powerAtEndOfTravel);

		log.finer(String.format("Tick number to start slowdown: %f", tickNumberToStartSlowDown));

		if (((double) tickNumberCurrent >= tickNumberToStartSlowDown) && ((double) tickNumberCurrent <= tickNumberAtEndOfTravel)) {
			result = true;
		}

		log.exiting(getClass().getCanonicalName(), "isSlowDownToEncoderTicksRunning");
		return result;
	}

	/**
	 * Determine whether the motor is busy.
	 *
	 * @return Returns true if the motor is currently advancing or retreating to a target position.
	 */
	final boolean isBusy() {
		return getMotor().isBusy();
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

		log.fine(String.format("Time stalled = ", "%d ms", getTimeStalledInMilliseconds()));
		log.fine(String.format("Stall time limit = ", "%d ms", stallTimeLimitInMilliseconds));

		boolean   stalled       = false;
		final int newTickNumber = getCurrentTickNumber();

		//telemetry.log("checking for a stall", "!");

		if (Math.abs(newTickNumber - oldTickNumber) < stallDetectionToleranceInTicks) {

			// The motor has not moved since the last time the position was read.

			if (timeStalledInMilliseconds.time() > stallTimeLimitInMilliseconds) {

				// The motor has been stalled for more than the time limit

				log.info("Motor stalled");
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
	final void rampToEncoderTicks(final PowerVector powerVector, final PositiveInteger ticksToRotate, final
	double secondsTimeout) throws NoSuchMethodException {

		if (secondsTimeout < 0.0) {
			throw new IllegalArgumentException("secondsTimeout must non-negative");
		}

		setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		double          ticksToSpeedUpDouble = ticksToRotate.doubleValue() / 2.0;
		int             ticksToSpeedUpInt    = (int) Math.round(ticksToSpeedUpDouble);
		PositiveInteger ticksToSpeedUp       = new PositiveInteger(ticksToSpeedUpInt);

		int             ticksToSlowDownInt = ticksToRotate.intValue() - ticksToSpeedUp.intValue();
		PositiveInteger ticksToSlowDown    = new PositiveInteger(ticksToSlowDownInt);

		log.fine(String.format("Target power UP: %f", powerVector.doubleValue()));
		rampToPower(powerVector, ticksToSpeedUp, secondsTimeout);

		log.fine(String.format("Target power DOWN: %f", 0.0));
		rampToPower(new PowerVector(0.0), ticksToSlowDown, secondsTimeout);

		getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		getMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
	public void rampToPower(final PowerVector powerVectorRequested) {
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
	private void rampToPower(final PowerVector powerVectorRequested, final PositiveInteger ticksToMove,
	                         final double secondsTimeout) throws NoSuchMethodException {

		log.entering(getClass().getCanonicalName(), "rampToPower");

		log.finer(String.format("Ticks to move: %d", ticksToMove.intValue()));

		log.finer(String.format("Power vector requested: %f", powerVectorRequested.doubleValue()));
		PowerMagnitude powerMagnitudeRequested = powerVectorRequested.getMagnitude();
		log.finer(String.format("Power magnitude requested: %f", powerMagnitudeRequested.doubleValue()));

		PowerVector powerVectorCurrent = this.getPowerVectorCurrent();
		log.finer(String.format("Power vector current: %f", powerVectorCurrent.doubleValue()));
		PowerMagnitude powerMagnitudeCurrent = powerVectorCurrent.getMagnitude();
		log.finer(String.format("Power magnitude current: %f", powerMagnitudeCurrent.doubleValue()));

		PowerVector powerVectorChange = powerVectorRequested.subtract(powerVectorCurrent);
		log.finer(String.format("Power vector change: %f", powerVectorChange.doubleValue()));

		if (powerMagnitudeRequested.isGreaterThan(powerMagnitudeCurrent)) {
			speedUpToPower(powerVectorRequested, ticksToMove, secondsTimeout);
		} else if (powerMagnitudeRequested.isLessThan(powerMagnitudeCurrent)) {
			slowDownToPower(powerVectorRequested, ticksToMove, secondsTimeout);
		} else {
			// No change
		}
		log.exiting(getClass().getCanonicalName(), "rampToPower");
	}

	/**
	 * When ramping "down", the power change starts at the end of the motion, something like this:
	 *
	 * <pre>
	 * >_____
	 *       \
	 *        \
	 *         \
	 *          -|
	 * </pre>
	 * <p>
	 *
	 */
	private void slowDownToPower(final PowerVector powerVectorAtEnd, final PositiveInteger ticksToMove,
	                             final double secondsTimeout) {

		log.entering(getClass().getCanonicalName(), "slowDownToPower");

		log.finer(String.format("Target power at end: %f", powerVectorAtEnd.doubleValue()));

		boolean keepGoing;
		int     tickNumberStart;
		int     currentTickNumber;

		PowerVector powerVectorCurrent;
		PowerVector powerVectorNew;

		ElapsedTime      runtimeSinceChange;
		double           millisecondsSinceChange;

		ElapsedTime runtimeFromStart = new ElapsedTime();
		double      secondsFromStart = runtimeFromStart.seconds();

		tickNumberStart = getCurrentTickNumber();

		waitForSlowDown(powerVectorAtEnd, ticksToMove, tickNumberStart);

		/*
		 * Ramp down
		 */
		keepGoing = true;
		while (secondsFromStart < secondsTimeout && keepGoing && getOpMode().opModeIsActive()) {

			getOpMode().idle();
			currentTickNumber = getCurrentTickNumber();

			powerVectorCurrent = getPowerVectorCurrent();
			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorAtEnd);

			setPowerVector(powerVectorNew);

			log.finer(String.format("Milliseconds elapsed %f", runtimeFromStart.milliseconds()));
			log.finer(String.format("Current tick number: %d", currentTickNumber));
			log.finer(String.format("New power: %f", powerVectorNew.doubleValue()));

			/*
			 * Wait for next power change
			 */
			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = 0.0;
			while ((millisecondsSinceChange < (double) MILLISECONDS_PER_CYCLE) && getOpMode().opModeIsActive()) {
				// Wait until it is time for next power increment
				getOpMode().idle();
				millisecondsSinceChange = runtimeSinceChange.milliseconds();
			}

			secondsFromStart = runtimeFromStart.seconds();

			keepGoing = isSlowDownToEncoderTicksRunning(tickNumberStart, currentTickNumber, ticksToMove,
					powerVectorCurrent, powerVectorAtEnd);

		}
		log.exiting(getClass().getCanonicalName(), "slowDownToPower");
	}

	/**
	 * Wait until it is time to begin slowing down power to the motor.
	 *
	 * @param powerVectorAtEnd
	 * @param ticksToMove
	 * @param tickNumberStart
	 */
	private void waitForSlowDown(PowerVector powerVectorAtEnd, PositiveInteger ticksToMove, int
			tickNumberStart) {
		boolean     keepWaiting;
		int         tickNumberCurrent;
		PowerVector powerVectorCurrent;

		/*
		 * Wait for the right time to start slowing down
		 */
		keepWaiting = true;
		while (keepWaiting && getOpMode().opModeIsActive()) {
			getOpMode().idle();
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
	private void speedUpToPower(final PowerVector powerVectorRequested, final PositiveInteger ticksToMove,
	                            final double secondsTimeout) throws NoSuchMethodException {

		log.entering(getClass().getName(), "speedUpToPower");

		log.fine(String.format("Target power: %f", powerVectorRequested.doubleValue()));
		log.fine(String.format("Target ticks: %d", ticksToMove.intValue()));

		ElapsedTime runtimeSinceChange;

		ElapsedTime runtimeTotal;
		double secondsRunning;

		int    tickNumberCurrent;
		int    tickNumberStart;

		PowerVector powerVectorNew;
		double      millisecondsSinceChange;

		tickNumberStart = getCurrentTickNumber();
		log.fine(String.format("Starting tick number: %d", tickNumberStart));

		NonNegativeInteger ticksMoved = new NonNegativeInteger(0);

		runtimeTotal = new ElapsedTime();
		secondsRunning = runtimeTotal.seconds();

		while (!isSpeedUpToEncoderTicksDone(ticksToMove, secondsTimeout, secondsRunning, ticksMoved)) {

			PowerVector powerVectorCurrent = this.getPowerVectorCurrent();
			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
			setPowerVector(powerVectorNew);

			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = 0.0;

			// Wait until it is time for next power increment
			while ((millisecondsSinceChange < (double) MILLISECONDS_PER_CYCLE) && getOpMode().opModeIsActive()) {
				getOpMode().idle();
				millisecondsSinceChange = runtimeSinceChange.milliseconds();
			}

			tickNumberCurrent = getCurrentTickNumber();
			ticksMoved = new NonNegativeInteger(Math.abs(tickNumberCurrent - tickNumberStart));
			secondsRunning = runtimeTotal.seconds();

			log.fine(String.format("Seconds elapsed %f", secondsRunning));
			log.fine(String.format("Current tick number: %d", tickNumberCurrent));
			log.fine(String.format("Ticks moved: %d", ticksMoved.intValue()));
			log.fine(String.format("New power: %f", powerVectorNew.doubleValue()));
		}
		log.exiting(getClass().getName(), "speedUpToPower");

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
	public void rampToPower(final PowerVector powerVectorRequested, final PowerMagnitude powerIncrementMagnitude,
	                        final int millisecondsCycleLength, final PowerMagnitude
			powerToleranceMagnitude, final double secondsTimeout) {

		log.fine(String.format("Total target power: %f", powerVectorRequested.doubleValue()));
		if (getOpMode().opModeIsActive()) {
			log.fine("Op mode is active");
		} else {
			log.fine("Op mode is NOT active");
		}

		PowerVector    powerVectorCurrent;
		PowerVector    powerVectorNew;
		PowerVector    vectorOfLastPowerChange;
		PowerMagnitude magnitudeOfLastPowerChange;
		double         millisecondsSinceChange;

		magnitudeOfLastPowerChange = new PowerMagnitude(1.0);

		while ((magnitudeOfLastPowerChange.isGreaterThan(powerToleranceMagnitude)) && getOpMode().opModeIsActive()) {

			log.fine("Op mode is active");
			powerVectorCurrent = getPowerVectorCurrent();

			vectorOfLastPowerChange = powerVectorRequested.subtract(powerVectorCurrent);
			magnitudeOfLastPowerChange = vectorOfLastPowerChange.getMagnitude();

			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorRequested);

			log.fine(String.format("Ramp to power, current power target: %f", powerVectorNew.doubleValue()));

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

			while ((millisecondsSinceChange < (double) millisecondsCycleLength) && getOpMode().opModeIsActive() &&
					isMotorBusy && (secondsRunning < secondsTimeout)) {

				getOpMode().idle();
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
	final void runByRevolutions(final PowerVector proportionMotorPower, final double targetNumberOfRevolutions,
	                            final double secondsTimeout) throws NoSuchMethodException {

		log.entering(this.getClass().getCanonicalName(), "runByRevolutions");

		log.finer(String.format("Proportion motor power: %f", proportionMotorPower.doubleValue()));
		log.finer(String.format("Target number of revolutions: %f", targetNumberOfRevolutions));
		log.finer(String.format("Seconds timeout: %f", secondsTimeout));

		final double ticksPerRevolution = getTicksPerRevolution();

		log.finer(String.format("Ticks per revolution: %f", ticksPerRevolution));

		final int numberOfTicksToRunInt = (int) Math.round(getTicksPerRevolution() * targetNumberOfRevolutions);

		log.finer(String.format("Number of ticks to run (int): %d", numberOfTicksToRunInt));

		final PositiveInteger numberOfTicksToRun = new PositiveInteger(numberOfTicksToRunInt);

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
	final void runByTime(final PowerVector powerVector, final double secondsToRun) {

		if (secondsToRun < 0.0) {
			throw new IllegalArgumentException("secondsTimeout expected to be non-negative");
		}

		final ElapsedTime runtime;
		double            secondsRunning;

		setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		rampToPower(powerVector);
		runtime = new ElapsedTime();
		secondsRunning = runtime.seconds();

		while (secondsRunning < secondsToRun && getOpMode().opModeIsActive()) {
			secondsRunning = runtime.seconds();
		}

		rampToPower(new PowerVector(0.0));
		setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

	}


	/**
	 * Run this motor until it stalls
	 *
	 * @param power
	 * 		How much power to apply to the motor, in the interval [-1,1].
	 */
	void runUntilStalled(final PowerVector power) {
		timeStalledInMilliseconds = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
		rampToPower(power);
		while (!(isStalled()) && getOpMode().opModeIsActive()) {
			log.finest("Not stalled yet...");
			getOpMode().idle();
		}
		rampToPower(new PowerVector(0.0));
	}

	/**
	 * Set the logical direction in which this motor operates.
	 *
	 * @param direction
	 * 		The logical direction in which this motor operates.
	 */
	public void setDirection(final DcMotorSimple.Direction direction) {
		if (getMotor() != null) {
			getMotor().setDirection(direction);
		}
	}

	/**
	 * Set the run mode for this motor.
	 *
	 * @param mode
	 * 		the new current run mode for this motor
	 */
	void setMode(final DcMotor.RunMode mode) {
		getMotor().setMode(mode);
	}

	public void setPowerMagnitudeTolerance(PowerMagnitude powerMagnitude) {
		powerMagnitudeTolerance = powerMagnitude;
	}

	public void setPowerMagnitudeIncrementPerCycle(PowerMagnitude powerMagnitude) {
		powerMagnitudeIncrementPerCycle = powerMagnitude;
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
		getMotor().setTargetPosition(targetPositionTicks);
	}


	void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
		getMotor().setZeroPowerBehavior(zeroPowerBehavior);
	}

	public int getMillisecondsPerCycle() {
		return MILLISECONDS_PER_CYCLE;
	}

	public double getTicksPerMinute() {
		return getTicksPerRevolution() * getRevolutionsPerMinute();
	}

	public double getTicksPerMinute(PowerMagnitude powerMagnitude) {
		return getTicksPerRevolution() * getRevolutionsPerMinute(powerMagnitude);
	}


	public double getTicksPerSecond() {
		return getTicksPerMinute() / SECONDS_PER_MINUTE;
	}

	public double getTicksPerSecond(PowerMagnitude powerMagnitude) {
		return getTicksPerMinute(powerMagnitude) / SECONDS_PER_MINUTE;
	}

	public double getTicksPerMillisecond() {
		return getTicksPerSecond() / MILLISECONDS_PER_SECOND;
	}

	public double getTicksPerMillisecond(PowerMagnitude powerMagnitude) {
		return getTicksPerSecond(powerMagnitude) / MILLISECONDS_PER_SECOND;
	}

	public double getTicksPerCycle() {
		return getTicksPerMillisecond() * getMillisecondsPerCycle();
	}

	public double getTicksPerCycle(PowerMagnitude powerMagnitude) {
		return getTicksPerMillisecond(powerMagnitude) * getMillisecondsPerCycle();
	}




}
