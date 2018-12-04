package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.aarre.opmode.AarreAutonomous;
import org.firstinspires.ftc.teamcode.aarre.opmode.AarreAutonomousReady;

import java.util.Date;
import java.util.logging.*;

/**
 * This file contains Aarre's experimental code to initialize the robot. It defines all the specific
 * hardware for the 2018-2019 robot.
 * <p>
 * Pulling it out into a separate class makes it possible to use the same code from different
 * OpModes (such as {@link AarreAutonomous}and {@link AarreAutonomousReady}).
 * <p>
 * This is NOT an OpMode itself.
 */
public class Robot {

	private final TelemetryPlus telemetry;

	/**
	 * These properties are package-private so methods of other classes in this package can use
	 * them.
	 * <p>
	 * TODO: Implement getters and setters to keep these properties private.
	 */


	Arm         arm;
	DriveMotors driveMotors;
	IMU         imu;
	Riser       riser;
	Servo       hookServo;
	CRServo     scoopServo;
	HardwareMap hardwareMap;

	static Logger log;

	static {
		log = Logger.getLogger(Robot.class.getName());
		log.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		handler.setFormatter(new SimpleFormatter() {

			private static final String format = "%1$tF %1$tT [%2$s] %3$s : %4$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				String formattedLogRecord = String.format(format, new Date(lr.getMillis()), lr.getLevel()
						.getLocalizedName(), lr.getLoggerName(), lr.getMessage());
				//telemetry.log(formattedLogRecord);
				return formattedLogRecord;
			}

		});
		log.addHandler(handler);
		log.setLevel(Level.ALL);
	}

	/**
	 * Construct from opMode only
	 *
	 * @param opMode
	 * 		The FTC opMode inside which this robot is running.
	 */
	public Robot(final LinearOpMode opMode) {

		telemetry = new TelemetryPlus(opMode.telemetry);

		hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			throw new AssertionError("Unexpected null object: hardwareMap");
		}

		// Define and initialize the motors. The strings used here as parameters
		// must correspond to the names assigned in the robot configuration
		// in the FTC Robot Controller app on the phone

		driveMotors = new DriveMotors(opMode);

		arm = new Arm(opMode, "arm");
		riser = new Riser(hardwareMap, "riser", telemetry, opMode);

		// Define the servos

		hookServo = new Servo(hardwareMap, "hook", telemetry, opMode);

		telemetry.log("Initializing hook");

		hookServo.setDirection(com.qualcomm.robotcore.hardware.Servo.Direction.FORWARD);

		// With the hook up, the servo is at 0 degrees.
		// With the hook down, the servo is at about 100 degrees.

		final double hookDownDegrees    = 100.0;
		final double hookUpDegrees      = 0.0;
		final double hookMaximumDegrees = 180.0;
		hookServo.scaleRange(hookUpDegrees / hookMaximumDegrees, hookDownDegrees / hookMaximumDegrees);


		/*
		  Initialize the IMU.
		 */
		imu = new IMU(opMode);

		// TODO: Initialize scoop servo
		scoopServo = hardwareMap.get(CRServo.class, "scoop");

	}

	/**
	 * Move the robot.
	 *
	 * @param powerMagnitude The magnitude of power to apply to the wheels.
	 * @param leftInches The number of inches to move the left wheel. Negative values will move
	 *                      the left wheel in reverse.
	 * @param rightInches The number of inches to move the right wheel. Negative values will move
	 *                      the right wheel in reverse.
	 * @param secondsTimeout Stop after this many seconds even if move not fully executed.
	 */
	public void drive(PowerMagnitude powerMagnitude, double leftInches, double rightInches, double secondsTimeout)
			throws NoSuchMethodException {

		log.fine(String.format("drive: powerMagnitude: %f", powerMagnitude.doubleValue()));
		log.fine(String.format("drive: leftInches: %f", leftInches));
		log.fine(String.format("drive: rightInches: %f", rightInches));
		log.fine(String.format("drive: secondsTimeout: %f", secondsTimeout));
		driveMotors.drive(powerMagnitude, leftInches, rightInches, secondsTimeout);
	}

	public HardwareMap getHardwareMap() {
		return hardwareMap;
	}

	public TelemetryPlus getTelemetry() {
		return (telemetry);
	}

	public void gyroDrive(PowerMagnitude powerMagnitude, double inchesDirectionAndDistance,
	                      double secondsTime) throws NoSuchMethodException {
		driveMotors.gyroDrive(powerMagnitude
				, inchesDirectionAndDistance, secondsTime);
	}

	public void gyroHold(PowerVector powerVector, double degreesHeading, double secondsTime) throws
			NoSuchMethodException {
		driveMotors.gyroHold(powerVector, degreesHeading, secondsTime);
	}

	public void gyroTurn(final PowerVector powerVector, final double angle) throws NoSuchMethodException {
		driveMotors.gyroTurn(powerVector, angle);
	}

	/**
	 * Lower the arm to its downward position
	 */
	public void lowerArm() throws NoSuchMethodException {
		arm.lower();
	}

	/**
	 * Lower the hook to its downward position
	 */
	public void lowerHook() {
		telemetry.log("Hook servo - lowering hook");
		hookServo.forward();
		telemetry.log("Hook servo - hook lowered");
	}

	/**
	 * Lower the riser to its downward (least extended) position. This is the position that it will
	 * need to be in at the beginning of the autonomous game when it is hanging from the lander.
	 */
	public void lowerRiser() throws NoSuchMethodException {
		riser.lower();
	}

	/**
	 * Raise the arm to its upward position
	 */
	public void raiseArm() throws NoSuchMethodException {
		arm.raise();
	}

	/**
	 * Raise the hook to its upward position
	 */
	public void raiseHook() {
		telemetry.log("Hook servo - raising hook");
		hookServo.reverse();
		telemetry.log("Hook servo - hook raised");
	}

	/**
	 * Raise the riser to its upward (most extended) position. This is the position that it will
	 * need to be in near the end of the game just before it latches on to the lander.
	 */
	public void raiseRiser() throws NoSuchMethodException {
		riser.raise();
	}

	/**
	 * Put the robot in the state it should be in at the end of the
	 * game, when it is preparing to latch to the lander.
	 *
	 * <ul>
	 * <li>The arm is lowered.</li>
	 * <li>The riser is raised, as it would need to be when attaching to the lander.</li>
	 * <li>The hook is raised, as it would need to be when attaching to the lander.</li>
	 * </ul>
	 *
	 * The difference between this and {@code readyForTransportation} is that this
	 * mode raises the hook.
	 */
	public void readyForAutonomousEndgame() throws NoSuchMethodException {
		lowerArm();
		raiseRiser();
		raiseHook();
	}

	/**
	 * Prepare the robot to play the autonomous portion of the game.
	 *
	 * <ul>
	 * <li>The arm is lowered, as it would be when attached to the lander.</li>
	 * <li>The riser is lowered, as it would be when attached to the lander.</li>
	 * <li>The hook is raised, as it would be when attached to the lander.</li>
	 * </ul>
	 *
	 * The difference between this and {@code readyForTransportation} is that this
	 * mode raises the hook.
	 */
	public void readyForAutonomousGame() throws NoSuchMethodException {
		lowerArm();
		lowerRiser();
		raiseHook();
	}

	/**
	 * Prepare the robot for transportation by putting it in its most compact state.
	 *
	 * <ul>
	 * <li>The arm is lowered, which is its most compact position.</li>
	 * <li>The riser is lowered, which is its most compact position.</li>
	 * <li>The hook is lowered, which is its most compact position.</li>
	 * </ul>
	 *
	 * The difference between this and {@code readyForAutonomousGame} is that
	 * this mode lowers the hook.
	 */
	public void readyForTransportation() throws NoSuchMethodException {
		lowerArm();
		lowerRiser();
		lowerHook();
	}


	public void updateIMUTelemetry() {
		imu.updateTelemetry();
	}


}
