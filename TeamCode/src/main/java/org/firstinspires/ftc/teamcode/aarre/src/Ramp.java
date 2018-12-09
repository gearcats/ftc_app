package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Date;
import java.util.logging.*;

/**
 * Track a ramp (gradual change) in speed, either a ramp up (a speedup) or a ramp down (a slowdown).
 * <p>
 * Note the distinction between a ramp (speed changes once, either up or down) and an {@link Excursion} (speed changes
 * twice, first up and then down.
 */
public abstract class Ramp {

	/**
	 * The number of milliseconds in each cycle of ramping. That is, a ramp will change (increase or decrease) motor
	 * power approximately every this many milliseconds.
	 */
	private static final NonNegativeInteger DEFAULT_MILLISECONDS_PER_RAMP_CYCLE = new NonNegativeInteger(50);

	/**
	 * How much to increment the motor power in each cycle of power ramping (slowing down / speeding up)
	 */
	private static final PowerMagnitude DEFAULT_POWER_INCREMENT_PER_RAMP_CYCLE = new PowerMagnitude(0.1);

	/**
	 * The motor's current power as set by the user for testing purposes.
	 */
	private PowerVector currentPowerVector;

	/**
	 * The motor's current position as set by the user for testing purposes.
	 */
	private int currentTickNumber;

	/**
	 * The power at the beginning of the ramp
	 */
	private PowerVector initialPower;

	/**
	 * The tick number at the beginning of the period
	 */
	private int initialTickNumber;

	private NonNegativeInteger millisecondsPerRampCycle = DEFAULT_MILLISECONDS_PER_RAMP_CYCLE;

	private Motor motor;

	private LinearOpMode opMode;

	private static PowerMagnitude powerMagnitudeIncrementPerRampCycle = DEFAULT_POWER_INCREMENT_PER_RAMP_CYCLE;

	/**
	 * Unique name to avoid shadowing log fields in subclasses
	 */
	private static Logger speedUpLog;

	/**
	 * The power at the end of the ramp
	 */
	private PowerVector targetPower;


	private NonNegativeInteger ticksToRotate;

	/**
	 * The maximum number of seconds to run the motor, even if the ramp has not reached its target
	 */
	private NonNegativeDouble timeoutSeconds;

	/**
	 * The magnitude within which we will consider a ramp operation to have been successful
	 */
	private PowerMagnitude tolerance;

	static {

		speedUpLog = Logger.getLogger("Ramp");
		speedUpLog.setUseParentHandlers(false);
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
		speedUpLog.addHandler(handler);
		speedUpLog.setLevel(Level.ALL);
	}

	public Ramp() {
		ticksToRotate = new NonNegativeInteger(0);
	}

	public void execute() throws NoSuchMethodException {

		final ElapsedTime runtime;
		double            secondsRunning;

		runtime = new ElapsedTime();
		secondsRunning = runtime.seconds();

		// TODO: Distinguish between ramping to goal and returning versus ramping to goal and holding before returning
		while (secondsRunning < getTimeoutSeconds().doubleValue() && getOpMode().opModeIsActive()) {
			rampToPower();
			secondsRunning = runtime.seconds();
		}
	}

	public PowerVector getCurrentPower() {
		return new PowerVector(getMotor().getCurrentPowerVector());
	}

	public PowerVector getCurrentPowerVector() {
		return currentPowerVector;
	}

	/**
	 * Get the current position of the motor.
	 *
	 * If running on-bot, then this method will return the actual tick number according to the encoder of the motor
	 * attached to this ramp. When running off-bot, it will return the last tick number that was set using the
	 * setCurrentTickNumber() method. This artificial current tick number is useful for testing.
	 *
	 * @return An integer that either indicates the actual position of the motor or represents
	 */
	public int getCurrentTickNumber() {
		int result;
		if (isOnBot()) {
			result = getMotor().getCurrentTickNumber();
		} else {
			result = currentTickNumber;
		}
		return result;
	}

	protected HardwareMap getHardwareMap() {
		return getOpMode().hardwareMap;
	}

	/**
	 * The power at the start of the ramp
	 */
	public PowerVector getInitialPower() {
		return initialPower;
	}

	/**
	 * The tick number at the beginning of the ramp
	 */
	public int getInitialTickNumber() {
		return initialTickNumber;
	}

	public NonNegativeInteger getMillisecondsPerRampCycle() {
		return millisecondsPerRampCycle;
	}

	/**
	 * The motor that we are ramping
	 */
	public Motor getMotor() {
		return motor;
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
	 *
	 * @return
	 */
	public NonNegativeInteger getNumberOfCycles() {

		// The magnitude of the current and requested power
		PowerMagnitude powerMagnitudeCurrent   = getCurrentPower().getMagnitude();
		PowerMagnitude powerMagnitudeRequested = getTargetPower().getMagnitude();

		// Calculate the average number of ticks per cycle during the ramp
		double            currentMagnitude      = powerMagnitudeCurrent.doubleValue();
		double            requestedMagnitude    = powerMagnitudeRequested.doubleValue();
		double            averageMagnitude      = (currentMagnitude + requestedMagnitude) / 2;
		PowerMagnitude    averagePowerMagnitude = new PowerMagnitude(averageMagnitude);
		NonNegativeDouble ticksPerCycle         = getTicksPerCycle(averagePowerMagnitude);
		NonNegativeDouble averageTicksPerCycle = new NonNegativeDouble(ticksPerCycle.doubleValue() *
				averagePowerMagnitude.doubleValue());

		// The magnitude of the power change over the ramp
		PowerVector    powerVectorChangeOverRamp    = getTargetPower().subtract(getCurrentPower());
		PowerMagnitude powerMagnitudeChangeOverRamp = powerVectorChangeOverRamp.getMagnitude();

		// The number of cycles required to change power as much as requested
		NonNegativeDouble numCyclesRequiredToChangePower = new NonNegativeDouble(powerMagnitudeChangeOverRamp
				.doubleValue() / getPowerMagnitudeIncrementPerRampCycle().doubleValue());

		// The number of ticks the motor would move if we changed the power that much
		NonNegativeDouble potentialTicksInRamp = averageTicksPerCycle.multiplyBy(numCyclesRequiredToChangePower);

		// Return the number of cycles to change power or number of cycles to reach ticks,
		// whichever is lower
		NonNegativeDouble cyclesToChange = numCyclesRequiredToChangePower;
		if (potentialTicksInRamp.doubleValue() > getTicksToRotate().doubleValue()) {
			// TODO: Doesn't this assume that the motor is operating at full power?
			NonNegativeDouble numCyclesRequiredToMoveTicks = new NonNegativeDouble(getTicksToRotate().doubleValue() /
					getTicksPerCycle().doubleValue());
			cyclesToChange = numCyclesRequiredToMoveTicks;
		}

		// TODO: Check on rounding here
		NonNegativeInteger wholeCyclesToChange = new NonNegativeInteger(cyclesToChange.intValue());
		return wholeCyclesToChange;

	}

	public LinearOpMode getOpMode() {
		return opMode;
	}

	public static PowerMagnitude getPowerMagnitudeIncrementPerRampCycle() {
		return powerMagnitudeIncrementPerRampCycle;
	}

	/**
	 * Get the proportion of power to which to change the motor when ramping power up or down.
	 * <p>
	 * Must be public to allow access from unit test suite.
	 *
	 * @param powerVectorCurrent
	 * 		The current power vector.
	 * @param powerVectorRequested
	 * 		The power vector that the caller has requested.
	 * @param powerIncrementMagnitude
	 * 		The maximum magnitude by which we are allowed to change the power.
	 *
	 * @return The new proportion of power to apply to the motor.
	 */
	public static PowerVector getPowerVectorNew(PowerVector powerVectorCurrent, PowerVector powerVectorRequested) {

		PowerVector    powerVectorNew;
		PowerMagnitude powerMagnitudeIncrementPerRampCycle = getPowerMagnitudeIncrementPerRampCycle();

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

		if (powerChangeMagnitude <= getPowerMagnitudeIncrementPerRampCycle().doubleValue()) {
			// Within one cycle, give them what they really want...
			powerVectorNew = powerVectorRequested;
		} else {
			// Otherwise, give them what they need...
			PowerVector powerIncrementVector = new PowerVector(getPowerMagnitudeIncrementPerRampCycle(),
					powerChangeDirection);
			powerVectorNew = powerVectorCurrent.add(powerIncrementVector);
		}

		return powerVectorNew;

	}

	/**
	 * The desired power at the end of the ramp.
	 */
	public PowerVector getTargetPower() {
		return targetPower;
	}


	public int getTargetTickNumber() {
		return getInitialTickNumber() + getTicksToRotate().intValue();
	}


	public NonNegativeDouble getTicksPerCycle() {
		return new NonNegativeDouble(getMotor().getTicksPerMillisecond().doubleValue() * getMillisecondsPerRampCycle()
				.doubleValue());
	}

	public NonNegativeDouble getTicksPerCycle(PowerMagnitude powerMagnitude) {
		return new NonNegativeDouble(getMotor().getTicksPerMillisecond(powerMagnitude).doubleValue() *
				getMillisecondsPerRampCycle().doubleValue());
	}


	public NonNegativeInteger getTicksToRotate() {
		return ticksToRotate;
	}

	/**
	 * How many seconds to run the motor before timing out, even if ramp has not been achieved
	 */
	public NonNegativeDouble getTimeoutSeconds() {
		return timeoutSeconds;
	}

	/**
	 * The power margin within which we will consider a power ramp to have finished
	 */
	public PowerMagnitude getTolerance() {
		return tolerance;
	}

	/**
	 * Is the program running on the robot?
	 *
	 * @return True if we are running on the robot, false otherwise (typically in a test environment).
	 */
	public boolean isOnBot() {
		// TODO: Figure out how to know whether we are running on the robot
		return true;
	}

	void rampToPower() throws NoSuchMethodException {
		rampToPower(targetPower, ticksToRotate, timeoutSeconds);
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
	void rampToPower(final PowerVector powerVectorRequested, final NonNegativeInteger ticksToMove, final
	NonNegativeDouble secondsTimeout) throws NoSuchMethodException {

		speedUpLog.entering(getClass().getCanonicalName(), "rampToPower");

		speedUpLog.finer(String.format("Ticks to move: %d", ticksToMove.intValue()));

		speedUpLog.finer(String.format("Power vector requested: %f", powerVectorRequested.doubleValue()));
		PowerMagnitude powerMagnitudeRequested = powerVectorRequested.getMagnitude();
		speedUpLog.finer(String.format("Power magnitude requested: %f", powerMagnitudeRequested.doubleValue()));

		PowerVector powerVectorCurrent = this.getCurrentPower();
		speedUpLog.finer(String.format("Power vector current: %f", powerVectorCurrent.doubleValue()));
		PowerMagnitude powerMagnitudeCurrent = powerVectorCurrent.getMagnitude();
		speedUpLog.finer(String.format("Power magnitude current: %f", powerMagnitudeCurrent.doubleValue()));

		PowerVector powerVectorChange = powerVectorRequested.subtract(powerVectorCurrent);
		speedUpLog.finer(String.format("Power vector change: %f", powerVectorChange.doubleValue()));

		if (powerMagnitudeRequested.isGreaterThan(powerMagnitudeCurrent)) {
			SpeedUp speedUp = new SpeedUp();
			speedUp.setTargetPower(powerVectorRequested);
			speedUp.setTicksToRotate(ticksToMove);
			speedUp.setTimeoutSeconds(secondsTimeout);
			speedUp.execute();
		} else if (powerMagnitudeRequested.isLessThan(powerMagnitudeCurrent)) {
			SlowDown slowDown = new SlowDown();
			slowDown.setTargetPower(powerVectorRequested);
			slowDown.setTicksToRotate(ticksToMove);
			slowDown.setTimeoutSeconds(secondsTimeout);
			slowDown.execute();
		} else {
			// No change
		}
		speedUpLog.exiting(getClass().getCanonicalName(), "rampToPower");
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

		speedUpLog.fine(String.format("Total target power: %f", powerVectorRequested.doubleValue()));
		if (getOpMode().opModeIsActive()) {
			speedUpLog.fine("Op mode is active");
		} else {
			speedUpLog.fine("Op mode is NOT active");
		}

		PowerVector    powerVectorCurrent;
		PowerVector    powerVectorNew;
		PowerVector    vectorOfLastPowerChange;
		PowerMagnitude magnitudeOfLastPowerChange;
		double         millisecondsSinceChange;

		magnitudeOfLastPowerChange = new PowerMagnitude(1.0);

		while ((magnitudeOfLastPowerChange.isGreaterThan(powerToleranceMagnitude)) && getOpMode().opModeIsActive()) {

			speedUpLog.fine("Op mode is active");
			powerVectorCurrent = getCurrentPower();

			vectorOfLastPowerChange = powerVectorRequested.subtract(powerVectorCurrent);
			magnitudeOfLastPowerChange = vectorOfLastPowerChange.getMagnitude();

			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorRequested);

			speedUpLog.fine(String.format("Ramp to power, current power target: %f", powerVectorNew.doubleValue()));

			setTargetPower(powerVectorNew);

			/*
			 * Wait for: (1) the cycle period to elapse, (2) the Op Mode to go inactive, (3) the
			 * motor to stop, or (4) the timeout period to expire.
			 */

			ElapsedTime elapsedTimeTotal       = new ElapsedTime();
			ElapsedTime elapsedTimeSinceChange = new ElapsedTime();

			millisecondsSinceChange = 0.0;
			double  secondsRunning = 0.0;
			boolean isMotorBusy    = true;

			while ((millisecondsSinceChange < millisecondsCycleLength.doubleValue()) && getOpMode().opModeIsActive()
					&& isMotorBusy && (secondsRunning < secondsTimeout.doubleValue())) {

				getOpMode().idle();
				millisecondsSinceChange = elapsedTimeSinceChange.milliseconds();
				secondsRunning = elapsedTimeTotal.seconds();
				isMotorBusy = motor.isBusy();
			}

		}

	}

	public void setCurrentPower(PowerVector powerVector) {
		this.setCurrentPowerVector(powerVector);
	}

	public void setCurrentPowerVector(PowerVector currentPowerVector) {
		this.currentPowerVector = currentPowerVector;
	}

	/**
	 * Set the current position of the motor for testing purposes.
	 *
	 * Note this does not actually turn the motor. Rather it sets an internal field that holds the last value set,
	 * which
	 * can be retrieved by the getCurrentTickNumber() method. The purpose of this is to allow for off-bot testing of
	 * methods that require a current tick number.
	 *
	 * @param currentTickNumber
	 */
	public void setCurrentTickNumber(int tickNumber) {
		this.currentTickNumber = tickNumber;
	}

	public void setInitialPower(PowerVector initialPower) {
		this.initialPower = initialPower;
		this.setCurrentPower(initialPower);
	}

	public void setInitialTickNumber(int initialTickNumber) {
		this.initialTickNumber = initialTickNumber;
	}

	public void setMillisecondsPerRampCycle(NonNegativeInteger millisecondsPerRampCycle) {
		this.millisecondsPerRampCycle = millisecondsPerRampCycle;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	public void setOpMode(LinearOpMode opMode) {
		this.opMode = opMode;
	}


	public void setPowerMagnitudeIncrementPerRampCycle(PowerMagnitude powerMagnitude) {
		powerMagnitudeIncrementPerRampCycle = powerMagnitude;
	}

	public void setTargetPower(PowerVector powerVector) {
		targetPower = powerVector;
	}

	public void setTicksToRotate(NonNegativeInteger ticksToRotate) {
		this.ticksToRotate = ticksToRotate;
	}

	public void setTimeoutSeconds(NonNegativeDouble timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}

	public void setTolerance(PowerMagnitude tolerance) {
		this.tolerance = tolerance;
	}


}
