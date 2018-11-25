package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.opmode.AarreAutonomous;
import org.firstinspires.ftc.teamcode.opmode.AarreAutonomousReady;

/**
 * This file contains Aarre's experimental code to initialize the robot. It defines all the specific
 * hardware for the 2018-2019 robot.
 * <p>
 * Pulling it out into a separate class makes it possible to use the same code from different
 * OpModes (such as {@link AarreAutonomous}and {@link AarreAutonomousReady}).
 * <p>
 * This is NOT an OpMode itself.
 */
public class AarreRobot {

	private final AarreTelemetry telemetry;

	/**
	 * These properties are package-private so methods of other classes in this package can use
	 * them.
	 * <p>
	 * TODO: Implement getters and setters to keep these properties private.
	 */


	AarreArm         arm;
	AarreDriveMotors driveMotors;
	AarreIMU         imu;
	AarreRiser       riser;
	AarreServo       hookServo;
	CRServo          scoopServo;
	HardwareMap      hardwareMap;


	/**
	 * Construct from opMode only
	 *
	 * @param opMode
	 * 		The FTC opMode inside which this robot is running.
	 */
	public AarreRobot(final LinearOpMode opMode) {

		telemetry = new AarreTelemetry(opMode.telemetry);

		hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			throw new AssertionError("Unexpected null object: hardwareMap");
		}

		// Define and initialize the motors. The strings used here as parameters
		// must correspond to the names assigned in the robot configuration
		// in the FTC Robot Controller app on the phone

		AarreDriveMotors driveMotors = new AarreDriveMotors(opMode);

		arm = new AarreArm(hardwareMap, "arm", telemetry, opMode);
		riser = new AarreRiser(hardwareMap, "riser", telemetry, opMode);

		// Define the servos

		hookServo = new AarreServo(hardwareMap, "hook", telemetry, opMode);

		telemetry.log("Initializing hook");

		hookServo.setDirection(Servo.Direction.FORWARD);

		// With the hook up, the servo is at 0 degrees.
		// With the hook down, the servo is at about 100 degrees.

		final double hookDownDegrees    = 100.0;
		final double hookUpDegrees      = 0.0;
		final double hookMaximumDegrees = 180.0;
		hookServo.scaleRange(hookUpDegrees / hookMaximumDegrees, hookDownDegrees / hookMaximumDegrees);

		/**
		 * Initialize the IMU.
		 */
		imu = new AarreIMU(opMode);

		// TODO: Initialize scoop servo
		scoopServo = hardwareMap.get(CRServo.class, "scoop");

	}


	public void drive(double proportionPower, double leftInches, double rightInches, double secondsTimeout) {

		driveMotors.drive(proportionPower, leftInches, rightInches, secondsTimeout);
	}

	public HardwareMap getHardwareMap() {
		return hardwareMap;
	}

	public AarreTelemetry getTelemetry() {
		return (telemetry);
	}

	public void gyroDrive(double proportionPower, double directionAndDistance, double secondsTime) {
		driveMotors.gyroDrive(proportionPower, directionAndDistance, secondsTime);
	}

	public void gyroHold(double proportionPower, double degreesHeading, double secondsTime) {
		driveMotors.gyroHold(proportionPower, degreesHeading, secondsTime);
	}

	public void gyroTurn(final double proportionPower, final double angle) {
		driveMotors.gyroTurn(proportionPower, angle);
	}

	/**
	 * Lower the arm to its downward position
	 */
	public void lowerArm() {
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
	public void lowerRiser() {
		riser.lower();
	}

	/**
	 * Raise the arm to its upward position
	 */
	public void raiseArm() {
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
	public void raiseRiser() {
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
	public void readyForAutonomousEndgame() {
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
	public void readyForAutonomousGame() {
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
	public void readyForTransportation() {
		lowerArm();
		lowerRiser();
		lowerHook();
	}


	public void updateIMUTelemetry() {
		imu.updateTelemetry();
	}


}

