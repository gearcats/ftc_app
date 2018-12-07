package org.firstinspires.ftc.teamcode.aarre.src;

import android.annotation.SuppressLint;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Date;
import java.util.logging.*;

/**
 * Track a ramp down (slowdown), that is, a gradual reduction in speed.
 */
public class SlowDown extends Ramp {

	/**
	 * Unique name to avoid hiding log field in superclass Ramp
	 */
	private static Logger slowDownLog;


	static {

		slowDownLog = Logger.getLogger("SlowDown");
		slowDownLog.setUseParentHandlers(false);
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
		slowDownLog.addHandler(handler);
		slowDownLog.setLevel(Level.ALL);
	}


	/**
	 * Calculate when (what tick number) to start a ramp down.
	 * <p>
	 * A slowdown can occur with either positive or negative power. Consider a slowdown from 0.5 to 0 versus a slowdown
	 * from -0.5 to 0. Thus we cannot assume that the power at the start of the period is greater than the power at the
	 * end of the period.
	 * <p>
	 * A slowdown also can occur without a stop at 0. For example, we could slow down from -0.5 to 0.5 or from 0.5 to
	 * -0.5. *
	 *
	 * @param tickNumberAtStartOfPeriod
	 * 		The motor encoder tick reading at the start of the period (which includes not only the ramp down at the end
	 * 		but	any time/ticks running at speed before the ramp). This value can be positive or negative depending on
	 * 		the
	 * 		position of the motor relative to the last time the encoder was reset.
	 * @param numberOfTicksInPeriod
	 * 		The total number of ticks in the period. This value must be positive. The direction of travel is
	 * 		determined by
	 * 		the power at the start and end of the period, not by the number of ticks.
	 * @param powerAtStartOfPeriod
	 * 		The motor power at the start of the period, in the range [-1,1].
	 * @param powerAtEndOfPeriod
	 * 		The power that should be applied to the motor at the end of the period, in the range [-1,1]. Because the
	 * 		motor
	 * 		is slowing down, the absolute	value of the power at the end of the period must be less than the absolute
	 * 		value
	 * 		of the power at the beginning of the period.
	 *
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	final public double getTickNumberToStartSlowDown() {

		slowDownLog.entering(getClass().getCanonicalName(), "getTickNumberToStartSlowDown");

		slowDownLog.finer(String.format("Tick number at start of period: %d", getInitialTickNumber()));
		slowDownLog.finer(String.format("Number of ticks in period: %d", getTicksToRotate().intValue()));
		slowDownLog.finer(String.format("Power vector at start of period: %f", getInitialPower().doubleValue()));
		slowDownLog.finer(String.format("Power vector at end of period: %f", getTargetPower().doubleValue()));
		slowDownLog.finer(String.format("Current tick number: %d", getCurrentTickNumber()));

		PowerMagnitude powerMagnitudeAtStartOfPeriod = getInitialPower().getMagnitude();
		slowDownLog.finer(String.format("Power magnitude at start of period: %f", powerMagnitudeAtStartOfPeriod
				.doubleValue()));

		PowerMagnitude powerMagnitudeAtEndOfPeriod = getTargetPower().getMagnitude();
		slowDownLog.finer(String.format("Power magnitude at end of period: %f", powerMagnitudeAtEndOfPeriod
				.doubleValue()));

		if (powerMagnitudeAtStartOfPeriod.doubleValue() <= powerMagnitudeAtEndOfPeriod.doubleValue()) {
			throw new IllegalArgumentException("When slowing down, the magnitude of the power at the start " + "of the" +
					" slowdown must be greater than the magnitude of the power at the end of the " + "slowdown.");
		}

		PowerVector powerChangeVector = getInitialPower().subtract(getTargetPower());
		slowDownLog.finer(String.format("Power change vector: %f", powerChangeVector.doubleValue()));

		PowerMagnitude magnitudeOfPowerChange = powerChangeVector.getMagnitude();
		slowDownLog.finer(String.format("Magnitude of power change: %f", magnitudeOfPowerChange.doubleValue()));

		int powerChangeDirection = powerChangeVector.getDirection();
		slowDownLog.finer(String.format("Power change direction: %d", powerChangeDirection));


		final NonNegativeDouble numberOfCyclesInSlowDown = new NonNegativeDouble(magnitudeOfPowerChange.divideBy
				(getPowerMagnitudeIncrementPerRampCycle()));
		slowDownLog.finer(String.format("Number of cycles in slowdown %f", numberOfCyclesInSlowDown.doubleValue()));

		final NonNegativeDouble numberOfTicksInSlowDown = new NonNegativeDouble(numberOfCyclesInSlowDown.doubleValue()
				* getTicksPerCycle().doubleValue());
		slowDownLog.finer(String.format("Number of ticks in slowdown: %f", numberOfTicksInSlowDown.doubleValue()));

		final double numberOfTicksToChange = powerChangeDirection * numberOfTicksInSlowDown.doubleValue();
		slowDownLog.finer(String.format("Number of ticks to change: %f", numberOfTicksToChange));

		final double tickNumberAtEndOfPeriod = getInitialTickNumber() + (getTicksToRotate().intValue() *
				powerChangeDirection);
		slowDownLog.finer(String.format("Tick number at end of period: %f", tickNumberAtEndOfPeriod));

		final double tickNumberToStartSlowDown = tickNumberAtEndOfPeriod - numberOfTicksToChange;
		slowDownLog.finer(String.format("Tick number to start slowdown: %f", tickNumberToStartSlowDown));

		slowDownLog.exiting(getClass().getCanonicalName(), "getTickNumberToStartSlowDown");

		return tickNumberToStartSlowDown;
	}


	public boolean isSlowDownToEncoderTicksRunning() {

		slowDownLog.entering(getClass().getCanonicalName(), "isSlowDownToEncoderTicksRunning");

		// Store the current tick number because, on the robot, it changes very rapidly
		final int currentTickNumber = getCurrentTickNumber();

		slowDownLog.finer(String.format("Tick number at start of travel: %d", getInitialTickNumber()));
		slowDownLog.finer(String.format("Current tick number: %d", getCurrentTickNumber()));
		slowDownLog.finer(String.format("Number of ticks to travel: %d", getTicksToRotate().intValue()));
		slowDownLog.finer(String.format("Power vector at start of travel: %f", getInitialPower().doubleValue()));
		slowDownLog.finer(String.format("Power vector at end of travel: %f", getTargetPower().doubleValue()));

		boolean result = false;

		double tickNumberToStartSlowDown = getTickNumberToStartSlowDown();

		slowDownLog.finer(String.format("Tick number to start slowdown: %f", tickNumberToStartSlowDown));

		if (((double) currentTickNumber >= getTickNumberToStartSlowDown()) && ((double) currentTickNumber <=
				getTargetTickNumber())) {
			result = true;
		}

		slowDownLog.exiting(getClass().getCanonicalName(), "isSlowDownToEncoderTicksRunning");
		return result;
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

		slowDownLog.entering(getClass().getCanonicalName(), "slowDownToPower");

		slowDownLog.finer(String.format("Target power at end: %f", powerVectorAtEnd.doubleValue()));

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

			setTargetPower(powerVectorNew);

			slowDownLog.finer(String.format("Milliseconds elapsed %f", runtimeFromStart.milliseconds()));
			slowDownLog.finer(String.format("Current tick number: %d", currentTickNumber));
			slowDownLog.finer(String.format("New power: %f", powerVectorNew.doubleValue()));

			/*
			 * Wait for next power change
			 */
			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = 0.0;
			while ((millisecondsSinceChange < getMillisecondsPerRampCycle().doubleValue()) && getOpMode()
					.opModeIsActive()) {
				// Wait until it is time for next power increment
				getOpMode().idle();
				millisecondsSinceChange = runtimeSinceChange.milliseconds();
			}

			secondsFromStart = runtimeFromStart.seconds();

			keepGoing = isSlowDownToEncoderTicksRunning();

		}
		slowDownLog.exiting(getClass().getCanonicalName(), "slowDownToPower");
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
			keepWaiting = !isSlowDownToEncoderTicksRunning();
		}
	}

}
