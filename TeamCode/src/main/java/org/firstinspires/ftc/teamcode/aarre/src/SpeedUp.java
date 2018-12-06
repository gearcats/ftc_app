package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Date;
import java.util.logging.*;

/**
 * Track a ramp up (speedup), that is, a gradual increase in speed.
 */
public class SpeedUp extends Ramp {

	/**
	 * Unique name to avoid hiding log field in superclass Ramp
	 */
	private static Logger speedUpLog;


	static {

		speedUpLog = Logger.getLogger("SpeedUp");
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


	public boolean isSpeedUpToEncoderTicksDone(NonNegativeInteger ticksMaximum, NonNegativeDouble secondsTimeout,
	                                           NonNegativeDouble secondsRunning, NonNegativeInteger ticksMoved) {

		speedUpLog.entering(this.getClass().getCanonicalName(), "isSpeedUpToEncoderTicksDone");

		boolean valueToReturn = false;

		speedUpLog.finest(String.format("ticksMaximum: %f", ticksMaximum.doubleValue()));
		speedUpLog.finest(String.format("secondsTimeout: %f", secondsTimeout.doubleValue()));
		speedUpLog.finest(String.format("secondsRunning: %f", secondsRunning.doubleValue()));
		speedUpLog.finest(String.format("ticksMoved: %f", ticksMoved.doubleValue()));

		if (Math.abs(ticksMoved.intValue()) >= Math.abs(ticksMaximum.intValue())) {
			speedUpLog.finest("Loop done - moved far enough");
			valueToReturn = true;
		} else if (secondsRunning.doubleValue() >= secondsTimeout.doubleValue()) {
			speedUpLog.finest("Loop done - timed out");
			valueToReturn = true;
		} else if (getHardwareMap() != null) {

			/*
			 * Only check whether Op Mode is active if hardware map is
			 * not null. Otherwise, we are running off-bot, and Op Mode
			 * will never be active. Returning false in that case allows
			 * us to use off-bot unit tests to test the other conditions.
			 */
			if (!getOpMode().opModeIsActive()) {
				speedUpLog.finest("Loop done - On robot but op mode is not active");
				valueToReturn = true;
			}
		}
		speedUpLog.exiting(this.getClass().getCanonicalName(), "isSpeedUpToEncoderTicksDone");
		return valueToReturn;
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

		speedUpLog.entering(getClass().getName(), "speedUpToPower");

		speedUpLog.fine(String.format("Target power: %f", powerVectorRequested.doubleValue()));
		speedUpLog.fine(String.format("Target ticks: %d", ticksToMove.intValue()));

		ElapsedTime runtimeSinceChange;

		ElapsedTime       runtimeTotal;
		NonNegativeDouble secondsRunning;

		int tickNumberCurrent;
		int tickNumberStart;

		PowerVector       powerVectorNew;
		NonNegativeDouble millisecondsSinceChange;

		tickNumberStart = getCurrentTickNumber();
		speedUpLog.fine(String.format("Starting tick number: %d", tickNumberStart));

		NonNegativeInteger ticksMoved = new NonNegativeInteger(0);

		runtimeTotal = new ElapsedTime();
		secondsRunning = new NonNegativeDouble(runtimeTotal.seconds());

		while (!isSpeedUpToEncoderTicksDone(ticksToMove, secondsTimeout, secondsRunning, ticksMoved)) {

			PowerVector powerVectorCurrent = this.getPowerVectorCurrent();
			powerVectorNew = getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
			setTargetPower(powerVectorNew);

			runtimeSinceChange = new ElapsedTime();
			millisecondsSinceChange = new NonNegativeDouble(0.0);

			// Wait until it is time for next power increment
			while ((millisecondsSinceChange.doubleValue() < getMillisecondsPerRampCycle().doubleValue()) && getOpMode
					().opModeIsActive()) {
				getOpMode().idle();
				millisecondsSinceChange = new NonNegativeDouble(runtimeSinceChange.milliseconds());
			}

			tickNumberCurrent = getCurrentTickNumber();
			ticksMoved = new NonNegativeInteger(Math.abs(tickNumberCurrent - tickNumberStart));
			secondsRunning = new NonNegativeDouble(runtimeTotal.seconds());

			speedUpLog.fine(String.format("Seconds elapsed %f", secondsRunning));
			speedUpLog.fine(String.format("Current tick number: %d", tickNumberCurrent));
			speedUpLog.fine(String.format("Ticks moved: %d", ticksMoved.intValue()));
			speedUpLog.fine(String.format("New power: %f", powerVectorNew.doubleValue()));
		}
		speedUpLog.exiting(getClass().getName(), "speedUpToPower");

	}

}
