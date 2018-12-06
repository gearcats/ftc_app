package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Date;
import java.util.logging.*;

/**
 * A motor excursion is the travel of a motor in a single direction with acceleration (ramping up / speeding up) at the
 * beginning and deceleration (ramping down / slowing down) at the end.
 */
public class Excursion {

	static Logger excursionLog;

	private Mode mode;

	private Motor              motor;
	private SlowDown           slowdown;
	private SpeedUp            speedup;
	private NonNegativeInteger ticksToRotate;

	private LinearOpMode opMode;

	private PowerVector targetPower;

	private NonNegativeDouble timeoutSeconds;

	static {

		excursionLog = Logger.getLogger("Excursion");
		excursionLog.setUseParentHandlers(false);
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
		excursionLog.addHandler(handler);
		excursionLog.setLevel(Level.ALL);
	}

	public Excursion() {

		setTargetPower(new PowerVector(0.0));
		setTimeoutSeconds(new NonNegativeDouble(0.0));

		setSlowDown(new SlowDown());
		slowdown.setMotor(motor);

		setSpeedUp(new SpeedUp());
		speedup.setMotor(motor);

		setTicksToRotate(new NonNegativeInteger(1));
	}

	/**
	 * Run the excursion, ramping motor power up and down as needed
	 */
	public void execute() throws NoSuchMethodException {

		excursionLog.fine(String.format("Target power UP: %f", targetPower.doubleValue()));

		getSpeedUp().rampToPower(targetPower, getTicksToSpeedUp(), timeoutSeconds);

		excursionLog.fine(String.format("Target power DOWN: %f", 0.0));
		getSlowDown().rampToPower(new PowerVector(0.0), getTicksToSlowDown(), timeoutSeconds);

		getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		getMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

	}

	public void executeRunToStall() {

	}

	public void executeRunToTicks() {

	}

	public void executeRunToTime() {

	}

	public Mode getMode() {
		return mode;
	}

	public Motor getMotor() {
		return motor;
	}

	public LinearOpMode getOpMode() {
		return opMode;
	}

	public PowerVector getTargetPower() {
		return targetPower;
	}

	public NonNegativeDouble getTimeoutSeconds() {
		return timeoutSeconds;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	public SlowDown getSlowDown() {
		return slowdown;
	}

	public SpeedUp getSpeedUp() {
		return speedup;
	}


	public NonNegativeInteger getTicksToRotate() {
		return ticksToRotate;
	}

	public NonNegativeInteger getTicksToSlowDown() {
		return getSlowDown().getTicksToRotate();
	}

	public NonNegativeInteger getTicksToSpeedUp() {
		return getSpeedUp().getTicksToRotate();
	}

	public void setOpMode(LinearOpMode opMode) {
		this.opMode = opMode;
	}

	public void setTargetPower(PowerVector targetPower) {
		this.targetPower = targetPower;
	}

	public void setTimeoutSeconds(NonNegativeDouble timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}

	protected static enum Mode {
		RUN_TO_STALL, RUN_TO_TICKS, RUN_TO_TIME;
	}

	public void setSlowDown(SlowDown slowdown) {
		this.slowdown = slowdown;
	}

	public void setSpeedUp(SpeedUp speedup) {
		this.speedup = speedup;
	}

	public void setTicksToRotate(NonNegativeInteger ticksToRotate) {
		this.ticksToRotate = ticksToRotate;
		NonNegativeInteger ticksToSpeedUp = new NonNegativeInteger((int) Math.round(ticksToRotate.doubleValue() /
				2.0));
		NonNegativeInteger ticksToSlowDown = new NonNegativeInteger(ticksToRotate.intValue() - ticksToSpeedUp.intValue
				());
		getSpeedUp().setTicksToRotate(ticksToSpeedUp);
		getSlowDown().setTicksToRotate(ticksToSlowDown);
	}


}
