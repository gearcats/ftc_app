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


	private PowerVector currentPower = new PowerVector(0);

	/**
	 * When running on bot, callers can get the current tick number as reported by the motor encoder. For testing
	 * off-bot, however, the motor cannot report a tick number. This private field is used in conjunction with the
	 * getCurrentTickNumber() and setCurrentTickNumber() methods to artificially set the current tick number for the
	 * purposes of testing off-bot.
	 *
	 * TODO: Consider using Mockito for this purpose instead
	 */
	private Integer currentTickNumber = new Integer(0);

	static Logger log;

	private Integer oldTickNumber = new Integer(0);

	private PowerMagnitude powerMagnitudeTolerance = DEFAULT_PROPORTION_POWER_TOLERANCE;

	private NonNegativeInteger stallDetectionToleranceInTicks = new NonNegativeInteger(5);

	// These are defaults. The user should customize them
	private NonNegativeInteger stallTimeLimitInMilliseconds = new NonNegativeInteger(100);

	private ElapsedTime timeStalledInMilliseconds = null;

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
	 * When running on-bot, callers can get the current power as reported by the motor. For testing off-bot, however,
	 * the motor cannot report its power. This private field is used in conjunction with the getCurrentPower() and
	 * setCurrentPower() methods to artificially set the power for the purposes of testing off-bot.
	 *
	 * TODO: Consider using Mockito for this purpose instead
	 */
	public final PowerVector getCurrentPowerVector() {

		PowerVector result;
		DcMotor     motor = getMotor();

		if (motor == null) {
			result = currentPower;
		} else {
			double powerDbl = motor.getPower();
			result = new PowerVector(powerDbl);
		}
		return result;
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
	 * @param powerMagnitude
	 * 		The magnitude of motor power for which the caller wants to know the number of ticks in a power change
	 * 		cycle.
	 *
	 * @return The number of ticks in a power change cycle when the motor is operating at powerMagnitude
	 */
	public NonNegativeDouble getTicksPerMillisecond(PowerMagnitude powerMagnitude) {
		return new NonNegativeDouble(getTicksPerSecond(powerMagnitude).doubleValue() / MILLISECONDS_PER_SECOND
				.doubleValue());
	}

	/**
	 * @return The number of ticks in a millisecond at maximum power.
	 */
	public NonNegativeDouble getTicksPerMillisecond() {
		return new NonNegativeDouble(getTicksPerSecond().doubleValue() / MILLISECONDS_PER_SECOND.doubleValue());
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
	public Integer getOldTickNumber() {
		return oldTickNumber;
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
	final void rampToEncoderTicks(final PowerVector powerVector, final NonNegativeInteger ticksToRotate, final
	NonNegativeDouble secondsTimeout) throws NoSuchMethodException {

		log.entering(getClass().getCanonicalName(), "rampToEncoderTicks");
		setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		Excursion excursion = new Excursion();
		excursion.setMotor(this);
		excursion.setTargetPower(powerVector);
		excursion.setTicksToRotate(ticksToRotate);
		excursion.setTimeoutSeconds(secondsTimeout);

		log.finer(String.format("Ticks to speed up: %d", excursion.getTicksToSpeedUp()));
		log.finer(String.format("Ticks to slow down: %d", excursion.getTicksToSlowDown()));

		excursion.execute();

		log.exiting(getClass().getCanonicalName(), "rampToEncoderTicks");
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
	final void runByRevolutions(final PowerVector proportionMotorPower, final NonNegativeDouble
			targetNumberOfRevolutions, final NonNegativeDouble secondsTimeout) throws NoSuchMethodException {

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
	final void runByTime(final PowerVector targetPower, final NonNegativeDouble secondsToRun) throws
			NoSuchMethodException {

		setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

		Excursion excursion = new Excursion();
		excursion.setTargetPower(targetPower);
		excursion.setTimeoutSeconds(secondsToRun);
		excursion.setMode(Excursion.Mode.RUN_TO_TIME);
		excursion.execute();

		setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

	}

	/**
	 * Run this motor until it stalls
	 *
	 * @param power
	 * 		How much power to apply to the motor, in the interval [-1,1].
	 */
	void runUntilStalled(final PowerVector power) throws NoSuchMethodException {

		timeStalledInMilliseconds = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

		SpeedUp speedup = new SpeedUp();
		speedup.setTargetPower(power);
		speedup.execute();
		while (!(isStalled()) && getOpMode().opModeIsActive()) {
			log.finest("Not stalled yet...");
			getOpMode().idle();
		}
		setPowerVector(new PowerVector(0.0));
	}


	/**
	 * When running on bot, callers can get the true current motor power. For testing off-bot, however, we cannot
	 * set or
	 * get power from the motor.
	 *
	 * TODO: Consider using Mockito for this instead.
	 */
	public void setCurrentPower(PowerVector currentPower) {
		this.currentPower = currentPower;
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

	public void setOldTickNumber(Integer oldTickNumber) {
		this.oldTickNumber = oldTickNumber;
	}


	public void setPowerMagnitudeTolerance(PowerMagnitude powerMagnitude) {
		powerMagnitudeTolerance = powerMagnitude;
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
		final NonNegativeInteger milliseconds = new NonNegativeInteger(seconds.multiplyBy(MILLISECONDS_PER_SECOND)
				.intValue());
		setStallTimeLimitInMilliseconds(milliseconds);
	}

	void setTargetPosition(final int targetPositionTicks) {
		getMotor().setTargetPosition(targetPositionTicks);
	}

	void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
		getMotor().setZeroPowerBehavior(zeroPowerBehavior);
	}


}
