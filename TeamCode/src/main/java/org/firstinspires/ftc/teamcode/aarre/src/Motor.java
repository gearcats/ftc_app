package org.firstinspires.ftc.teamcode.aarre.src;

import android.annotation.SuppressLint;
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

	private static final NonNegativeInteger SECONDS_PER_MINUTE = new NonNegativeInteger(60);

	private static final NonNegativeInteger MILLISECONDS_PER_SECOND = new NonNegativeInteger(1000);

	private static final NonNegativeInteger MILLISECONDS_PER_CYCLE = new NonNegativeInteger(50);

	// How much to increment the motor power in each cycle of power ramping (slowing down / speeding up)
	private static final PowerMagnitude DEFAULT_POWER_INCREMENT_PER_CYCLE = new PowerMagnitude(0.1);

	// How much tolerance to allow when deciding whether we have reached a requested motor power
	private static final PowerMagnitude DEFAULT_PROPORTION_POWER_TOLERANCE = new PowerMagnitude(0.01);

	// How long to allow a motor operation to continue before timing out
	private static final NonNegativeDouble DEFAULT_SECONDS_TIMEOUT = new NonNegativeDouble(5.0);

	private PowerMagnitude powerMagnitudeIncrementPerCycle = DEFAULT_POWER_INCREMENT_PER_CYCLE;
	private PowerMagnitude powerMagnitudeTolerance         = DEFAULT_PROPORTION_POWER_TOLERANCE;

	/**
	 * When running on bot, callers can get the current tick number as reported by the motor encoder. For testing
	 * off-bot, however, the motor cannot report a tick number. This private field is used in conjuction with the
	 * getCurrentTickNumber() and setCurrentTickNumber() methods to artificially set the current tick number for the
	 * purposes of testing off-bot.
	 */
	private Integer currentTickNumber = new Integer(0);
	private Integer oldTickNumber     = new Integer(0);

	// These are defaults. The user should customize them
	private NonNegativeInteger stallTimeLimitInMilliseconds   = new NonNegativeInteger(100);
	private NonNegativeInteger stallDetectionToleranceInTicks = new NonNegativeInteger(5);

	private ElapsedTime timeStalledInMilliseconds = null;

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
				String formattedLogRecord = String.format(format, new Date(lr.getMillis()), lr.getLevel()
						.getLocalizedName(), lr.getLoggerName(), lr.getSourceClassName(), lr.getSourceMethodName(), lr
						.getMessage());
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
	 * <p>
	 * When running on bot, callers can get the current tick number as reported by the motor encoder. For testing
	 * off-bot, however, the motor cannot report a tick number. This private field is used in conjunction with the
	 * getCurrentTickNumber() and setCurrentTickNumber() methods to artificially set the current tick number for the
	 * purposes of testing off-bot.
	 *
	 * @return The current reading of the encoder for this motor, in ticks.
	 */
	public final int getCurrentTickNumber() {

		int result;

		DcMotor motor = getMotor();
		if (motor == null) {
			// Code is running off-bot, so return pretend number
			result = currentTickNumber;
		} else {
			DcMotor.RunMode mode = getMotor().getMode();
			if (mode != DcMotor.RunMode.RUN_USING_ENCODER) {
				log.warning("Motor is not using encoder!");
			}
			result = motor.getCurrentPosition();
			setCurrentTickNumber(Integer.valueOf(result));
		}

		return result;

	}


	/**
	 * Get the number of cycles for which a ramp (slowing down/speeding up) should last
	 * <p>
	 * The current power is a parameter here (instead of using the getter for the corresponding class field) to allow
	 * for off-bot unit testing.
	 * <p>
	 *
	 * @param ticksToMove
	 * @param powerVectorCurrent
	 * @param powerVectorRequested
	 * @return
	 */
	public NonNegativeInteger getNumberOfCycles(NonNegativeInteger ticksToMove, PowerVector powerVectorCurrent, PowerVector
			powerVectorRequested) {

		// The magnitude of the current and requested power
		PowerMagnitude powerMagnitudeCurrent   = powerVectorCurrent.getMagnitude();
		PowerMagnitude powerMagnitudeRequested = powerVectorRequested.getMagnitude();

		// Calculate the average number of ticks per cycle during the ramp
		double            currentMagnitude      = powerMagnitudeCurrent.doubleValue();
		double            requestedMagnitude    = powerMagnitudeRequested.doubleValue();
		double            averageMagnitude      = (currentMagnitude + requestedMagnitude) / 2;
		PowerMagnitude    averagePowerMagnitude = new PowerMagnitude(averageMagnitude);
		NonNegativeDouble ticksPerCycle         = getTicksPerCycle(averagePowerMagnitude);
		NonNegativeDouble averageTicksPerCycle = new NonNegativeDouble(ticksPerCycle.doubleValue() *
				averagePowerMagnitude.doubleValue());

		// The magnitude of the power change over the ramp
		PowerVector    powerVectorChangeOverRamp    = powerVectorRequested.subtract(powerVectorCurrent);
		PowerMagnitude powerMagnitudeChangeOverRamp = powerVectorChangeOverRamp.getMagnitude();

		// The number of cycles required to change power as much as requested
		NonNegativeDouble numCyclesRequiredToChangePower = new NonNegativeDouble(powerMagnitudeChangeOverRamp
				.doubleValue() / DEFAULT_POWER_INCREMENT_PER_CYCLE.doubleValue());

		// The number of ticks the motor would move if we changed the power that much
		NonNegativeDouble potentialTicksInRamp = averageTicksPerCycle.multiplyBy(numCyclesRequiredToChangePower);

		// Return the number of cycles to change power or number of cycles to reach ticks,
		// whichever is lower
		NonNegativeDouble cyclesToChange = numCyclesRequiredToChangePower;
		if (potentialTicksInRamp.doubleValue() > ticksToMove.doubleValue()) {
			// TODO: Doesn't this assume that the motor is operating at full power?
			NonNegativeDouble numCyclesRequiredToMoveTicks = new NonNegativeDouble(ticksToMove.doubleValue() /
					getTicksPerCycle().doubleValue());
			cyclesToChange = numCyclesRequiredToMoveTicks;
		}

		// TODO: Check on rounding here
		NonNegativeInteger wholeCyclesToChange = new NonNegativeInteger(cyclesToChange.intValue());
		return wholeCyclesToChange;

	}


	public final PowerMagnitude getPowerMagnitudeIncrementPerCycle() {
		return this.powerMagnitudeIncrementPerCycle;
	}

	public PowerVector getPowerVectorCurrent() {
		return new PowerVector(getMotor().getPower());
	}

	public final PowerVector getPowerVectorNew(PowerVector powerVectorCurrent, PowerVector powerVectorRequested) {

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

	@SuppressLint("DefaultLocale")
	final public double getTickNumberToStartSlowDown(final int tickNumberAtStartOfPeriod, final NonNegativeInteger
			numberOfTicksInPeriod, final PowerVector powerVectorAtStartOfPeriod, final PowerVector
			powerVectorAtEndOfPeriod) {

		log.entering(getClass().getCanonicalName(), "getTickNumberToStartSlowDown");

		log.finer(String.format("Tick number at start of period: %d", tickNumberAtStartOfPeriod));
		log.finer(String.format("Number of ticks in period: %d", numberOfTicksInPeriod.intValue()));
		log.finer(String.format("Power vector at start of period: %f", powerVectorAtStartOfPeriod.doubleValue()));
		log.finer(String.format("Power vector at end of period: %f", powerVectorAtEndOfPeriod.doubleValue()));
		log.finer(String.format("Current tick number: %d", getCurrentTickNumber()));

		PowerMagnitude powerMagnitudeAtStartOfPeriod = powerVectorAtStartOfPeriod.getMagnitude();
		log.finer(String.format("Power magnitude at start of period: %f", powerMagnitudeAtStartOfPeriod.doubleValue
				()));

		PowerMagnitude powerMagnitudeAtEndOfPeriod = powerVectorAtEndOfPeriod.getMagnitude();
		log.finer(String.format("Power magnitude at end of period: %f", powerMagnitudeAtEndOfPeriod.doubleValue()));

		if (powerMagnitudeAtStartOfPeriod.doubleValue() <= powerMagnitudeAtEndOfPeriod.doubleValue()) {
			throw new IllegalArgumentException("When slowing down, the magnitude of the " + "power at the start " +
					"of the slowdown must be greater " + "than the magnitude of the power at the end " + "of " + "the " +
					"" + "" + "" + "" + "" + "slowdown" + ".");
		}

		PowerVector powerChangeVector = powerVectorAtStartOfPeriod.subtract(powerVectorAtEndOfPeriod);
		log.finer(String.format("Power change vector: %f", powerChangeVector.doubleValue()));

		PowerMagnitude magnitudeOfPowerChange = powerChangeVector.getMagnitude();
		log.finer(String.format("Magnitude of power change: %f", magnitudeOfPowerChange.doubleValue()));

		int powerChangeDirection = powerChangeVector.getDirection();
		log.finer(String.format("Power change direction: %d", powerChangeDirection));


		final NonNegativeDouble numberOfCyclesInSlowDown = new NonNegativeDouble(magnitudeOfPowerChange.divideBy
				(getPowerMagnitudeIncrementPerCycle()));
		log.finer(String.format("Number of cycles in slowdown %f", numberOfCyclesInSlowDown.doubleValue()));

		final NonNegativeDouble numberOfTicksInSlowDown = new NonNegativeDouble(numberOfCyclesInSlowDown.doubleValue()
				* getTicksPerCycle().doubleValue());
		log.finer(String.format("Number of ticks in slowdown: %f", numberOfTicksInSlowDown.doubleValue()));

		final double numberOfTicksToChange = powerChangeDirection * numberOfTicksInSlowDown.doubleValue();
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


	public boolean isSpeedUpToEncoderTicksDone(NonNegativeInteger ticksMaximum, NonNegativeDouble secondsTimeout,
	                                           NonNegativeDouble secondsRunning, NonNegativeInteger ticksMoved) {

		log.entering(this.getClass().getCanonicalName(), "isSpeedUpToEncoderTicksDone");

		boolean valueToReturn = false;

		log.finest(String.format("ticksMaximum: %f", ticksMaximum.doubleValue()));
		log.finest(String.format("secondsTimeout: %f", secondsTimeout.doubleValue()));
		log.finest(String.format("secondsRunning: %f", secondsRunning.doubleValue()));
		log.finest(String.format("ticksMoved: %f", ticksMoved.doubleValue()));

		if (Math.abs(ticksMoved.intValue()) >= Math.abs(ticksMaximum.intValue())) {
			log.finest("Loop done - moved far enough");
			valueToReturn = true;
		} else if (secondsRunning.doubleValue() >= secondsTimeout.doubleValue()) {
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


	public boolean isSlowDownToEncoderTicksRunning(int tickNumberAtStartOfTravel, int tickNumberCurrent, NonNegativeInteger numberOfTicksToTravel, PowerVector
			                                               powerAtStartOfTravel, PowerVector powerAtEndOfTravel) {


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

		boolean                  stalled       = false;
		final int                newTickNumber = getCurrentTickNumber();
		final NonNegativeInteger ticksChanged  = new NonNegativeInteger(Math.abs(newTickNumber - getOldTickNumber()));

		//telemetry.log("checking for a stall", "!");

		if (ticksChanged.intValue() < stallDetectionToleranceInTicks.intValue()) {

			// The motor has not moved since the last time the position was read.

			if (timeStalledInMilliseconds.time() > stallTimeLimitInMilliseconds.doubleValue()) {

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

		setOldTickNumber(newTickNumber);

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
	final void rampToEncoderTicks(final PowerVector powerVector, final NonNegativeInteger ticksToRotate, final NonNegativeDouble secondsTimeout) throws NoSuchMethodException {

		setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		Excursion excursion = new Excursion();
		excursion.setPowerVector(powerVector);
		excursion.setTicksToRotate(ticksToRotate);
		excursion.setSecondsTimeout(secondsTimeout);

		double             ticksToSpeedUpDouble = ticksToRotate.doubleValue() / 2.0;
		int                ticksToSpeedUpInt    = (int) Math.round(ticksToSpeedUpDouble);
		NonNegativeInteger ticksToSpeedUp       = new NonNegativeInteger(ticksToSpeedUpInt);

		int                ticksToSlowDownInt = ticksToRotate.intValue() - ticksToSpeedUp.intValue();
		NonNegativeInteger ticksToSlowDown    = new NonNegativeInteger(ticksToSlowDownInt);

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
	private void rampToPower(final PowerVector powerVectorRequested, final NonNegativeInteger ticksToMove, final
	NonNegativeDouble secondsTimeout) throws NoSuchMethodException {

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
	 */
	private void slowDownToPower(final PowerVector powerVectorAtEnd, final NonNegativeInteger ticksToMove, final
	NonNegativeDouble secondsTimeout) {

		log.entering(getClass().getCanonicalName(), "slowDownToPower");

		log.finer(String.format("Target power at end: %f", powerVectorAtEnd.doubleValue()));

		boolean keepGoing;
		int     tickNumberStart;
		int     currentTickNumber;

		PowerVector powerVectorCurrent;
		PowerVector powerVectorNew;

		ElapsedTime runtimeSinceChange;
		double      millisecondsSinceChange;

		ElapsedTime runtimeFromStart = new ElapsedTime();
		double      secondsFromStart = runtimeFromStart.seconds();

		tickNumberStart = getCurrentTickNumber();

		waitForSlowDown(powerVectorAtEnd, ticksToMove, tickNumberStart);

		/*
		 * Ramp down
		 */
		keepGoing = true;
		while (secondsFromStart < secondsTimeout.doubleValue() && keepGoing && getOpMode().opModeIsActive()) {

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
			while ((millisecondsSinceChange < MILLISECONDS_PER_CYCLE.doubleValue()) && getOpMode().opModeIsActive()) {
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
	private void waitForSlowDown(PowerVector powerVectorAtEnd, NonNegativeInteger ticksToMove, int tickNumberStart) {
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
	private void speedUpToPower(final PowerVector powerVectorRequested, final NonNegativeInteger ticksToMove, final
	NonNegativeDouble secondsTimeout) throws NoSuchMethodException {

		log.entering(getClass().getName(), "speedUpToPower");

		log.fine(String.format("Target power: %f", powerVectorRequested.doubleValue()));
		log.fine(String.format("Target ticks: %d", ticksToMove.intValue()));

		ElapsedTime runtimeSinceChange;

		ElapsedTime       runtimeTotal;
		NonNegativeDouble secondsRunning;

		int tickNumberCurrent;
		int tickNumberStart;

		PowerVector       powerVectorNew;
		NonNegativeDouble millisecondsSinceChange;

		tickNumberStart = getCurrentTickNumber();
		log.fine(String.format("Starting tick number: %d", tickNumberStart));

		NonNegativeInteger ticksMoved = new NonNegativeInteger(0);

		runtimeTotal = new ElapsedTime();
		secondsRunning = new NonNegativeDouble(runtimeTotal.seconds());

		while (!isSpeedUpToEncoderTicksDone(ticksToMove, secondsTimeout, secondsRunning, ticksMoved)) {

			PowerVector powerVectorCurrent = this.getPowerVectorCurrent();
			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
			setPowerVector(powerVectorNew);

			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = new NonNegativeDouble(0.0);

			// Wait until it is time for next power increment
			while ((millisecondsSinceChange.doubleValue() < MILLISECONDS_PER_CYCLE.doubleValue()) && getOpMode()
					.opModeIsActive()) {
				getOpMode().idle();
				millisecondsSinceChange = new NonNegativeDouble(runtimeSinceChange.milliseconds());
			}

			tickNumberCurrent = getCurrentTickNumber();
			ticksMoved = new NonNegativeInteger(Math.abs(tickNumberCurrent - tickNumberStart));
			secondsRunning = new NonNegativeDouble(runtimeTotal.seconds());

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
	                        final NonNegativeInteger millisecondsCycleLength, final PowerMagnitude
			                        powerToleranceMagnitude, final NonNegativeDouble secondsTimeout) {

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

			while ((millisecondsSinceChange < millisecondsCycleLength.doubleValue()) && getOpMode().opModeIsActive() && isMotorBusy && (secondsRunning < secondsTimeout.doubleValue())) {

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
	final void runByRevolutions(final PowerVector proportionMotorPower, final NonNegativeDouble targetNumberOfRevolutions, final NonNegativeDouble secondsTimeout) throws NoSuchMethodException {

		log.entering(this.getClass().getCanonicalName(), "runByRevolutions");

		log.finer(String.format("Proportion motor power: %f", proportionMotorPower.doubleValue()));
		log.finer(String.format("Target number of revolutions: %f", targetNumberOfRevolutions));
		log.finer(String.format("Seconds timeout: %f", secondsTimeout));

		final NonNegativeDouble ticksPerRevolution = getTicksPerRevolution();

		log.finer(String.format("Ticks per revolution: %f", ticksPerRevolution));

		final NonNegativeInteger numberOfTicksToRun = new NonNegativeInteger((int) Math.round(getTicksPerRevolution()
				.doubleValue() * targetNumberOfRevolutions.doubleValue()));

		log.finer(String.format("Number of ticks to run (int): %d", numberOfTicksToRun));

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
	final void runByTime(final PowerVector powerVector, final NonNegativeDouble secondsToRun) {

		final ElapsedTime runtime;
		double            secondsRunning;

		setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		rampToPower(powerVector);
		runtime = new ElapsedTime();
		secondsRunning = runtime.seconds();

		while (secondsRunning < secondsToRun.doubleValue() && getOpMode().opModeIsActive()) {
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
	 * When running on bot, callers can get the current tick number as reported by the motor encoder. For testing
	 * off-bot, however, the motor cannot report a tick number. This private field is used in conjunction with the
	 * getCurrentTickNumber() and setCurrentTickNumber() methods to artificially set the current tick number for the
	 * purposes of testing off-bot.
	 *
	 * @param tickNumber
	 */
	public void setCurrentTickNumber(Integer tickNumber) {
		currentTickNumber = tickNumber;
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
	public void setStallDetectionToleranceInTicks(final NonNegativeInteger ticks) {
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
	void setStallTimeLimitInMilliseconds(final NonNegativeInteger milliseconds) {
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
	void setStallTimeLimitInSeconds(final NonNegativeDouble seconds) {
		final NonNegativeInteger milliseconds = new NonNegativeInteger(seconds.multiplyBy(MILLISECONDS_PER_SECOND).intValue());
		setStallTimeLimitInMilliseconds(milliseconds);
	}

	void setTargetPosition(final int targetPositionTicks) {
		getMotor().setTargetPosition(targetPositionTicks);
	}


	void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
		getMotor().setZeroPowerBehavior(zeroPowerBehavior);
	}

	public NonNegativeInteger getMillisecondsPerCycle() {
		return MILLISECONDS_PER_CYCLE;
	}

	public NonNegativeDouble getTicksPerMinute() {
		return getTicksPerRevolution().multiplyBy(getRevolutionsPerMinute());
	}

	public NonNegativeDouble getTicksPerMinute(PowerMagnitude powerMagnitude) {
		return getTicksPerRevolution().multiplyBy(getRevolutionsPerMinute(powerMagnitude));
	}


	public NonNegativeDouble getTicksPerSecond() {
		return new NonNegativeDouble(getTicksPerMinute().doubleValue() / SECONDS_PER_MINUTE.doubleValue());
	}

	public NonNegativeDouble getTicksPerSecond(PowerMagnitude powerMagnitude) {
		return new NonNegativeDouble(getTicksPerMinute(powerMagnitude).doubleValue() / SECONDS_PER_MINUTE.doubleValue
				());
	}

	public NonNegativeDouble getTicksPerMillisecond() {
		return new NonNegativeDouble(getTicksPerSecond().doubleValue() / MILLISECONDS_PER_SECOND.doubleValue());
	}

	public NonNegativeDouble getTicksPerMillisecond(PowerMagnitude powerMagnitude) {
		return new NonNegativeDouble(getTicksPerSecond(powerMagnitude).doubleValue() / MILLISECONDS_PER_SECOND
				.doubleValue());
	}

	public NonNegativeDouble getTicksPerCycle() {
		return new NonNegativeDouble(getTicksPerMillisecond().doubleValue() * getMillisecondsPerCycle().doubleValue());
	}

	public NonNegativeDouble getTicksPerCycle(PowerMagnitude powerMagnitude) {
		return new NonNegativeDouble(getTicksPerMillisecond(powerMagnitude).doubleValue() * getMillisecondsPerCycle()
				.doubleValue());
	}


	public Integer getOldTickNumber() {
		return oldTickNumber;
	}

	public void setOldTickNumber(Integer oldTickNumber) {
		this.oldTickNumber = oldTickNumber;
	}
}
