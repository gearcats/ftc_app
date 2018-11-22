package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

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
	AarreMotor   leftMotor;
	AarreMotor   rightMotor;
	AarreArm     arm;
	AarreRiser   riser;
	AarreServo   hookServo;
	CRServo      scoopServo;
	LinearOpMode opMode;
	HardwareMap  hardwareMap;


	/**
	 * Construct from opMode only
	 *
	 * @param opMode
	 * 		The FTC opMode inside which this robot is running.
	 */
	public AarreRobot(final LinearOpMode opMode) {

		this.telemetry = new AarreTelemetry(opMode.telemetry);
		if (telemetry == null) {
			throw new AssertionError("Unexpected null object: telemetry");
		}

		this.hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			throw new AssertionError("Unexpected null object: hardwareMap");
		}

		// Define and initialize the motors. The strings used here as parameters
		// must correspond to the names assigned in the robot configuration
		// in the FTC Robot Controller app on the phone

		AarreDrive drive = new AarreDrive(opMode);
		arm = new AarreArm(hardwareMap, "arm", telemetry, opMode);
		riser = new AarreRiser(hardwareMap, "riser", telemetry, opMode);

		// Configure drive motors such that a positive power command moves them forwards
		// and causes the encoders to count UP. Note that, as in most robots, the drive
		// motors are mounted in opposite directions. May need to be reversed if using AndyMark motors.

		leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
		rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);


		// Set all motors to zero power

		// TODO: Ramp power on the motors simultaneously to avoid swerving
		leftMotor.rampPowerTo(0.0);
		rightMotor.rampPowerTo(0.0);

		// This code REQUIRES that you have encoders on the wheel motors

		leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		// Define the servos

		hookServo = new AarreServo(hardwareMap, "hook", telemetry, opMode);

		this.telemetry.log("Initializing hook");

		hookServo.setDirection(Servo.Direction.FORWARD);

		// With the hook up, the servo is at 0 degrees.
		// With the hook down, the servo is at about 100 degrees.

		final double hookDownDegrees    = 100.0;
		final double hookUpDegrees      = 0.0;
		final double hookMaximumDegrees = 180.0;
		hookServo.scaleRange(hookUpDegrees / hookMaximumDegrees, hookDownDegrees / hookMaximumDegrees);

		// TODO: Initialize scoop servo
		scoopServo = hardwareMap.get(CRServo.class, "scoop");

	}


	/**
	 *  Perform a relative move, based on encoder counts.
	 *  Encoders are not reset because the move is based on the current position.
	 *  Move will stop if any of three conditions occur:
	 *  1) Move gets to the desired position
	 *  2) Move runs out of time
	 *  3) Driver stops the OpMode running.
	 */
	final void encoderDrive(final double speed, final double leftInches, final double rightInches, final double timeoutS) {
		final int newLeftTarget;
		final int newRightTarget;


		// Determine new target position, and pass to motor controller
		newLeftTarget = leftMotor.getCurrentTickNumber() + (int) (leftInches * AarreMotor.getTicksPerInch());
		newRightTarget = rightMotor.getCurrentTickNumber() + (int) (rightInches * AarreMotor.getTicksPerInch());
		leftMotor.setTargetPosition(newLeftTarget);
		rightMotor.setTargetPosition(newRightTarget);

		// Turn On RUN_TO_POSITION
		leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		// reset the timeout time and start motion.
		final ElapsedTime runtime = new ElapsedTime();
		leftMotor.rampPowerTo(Math.abs(speed));
		rightMotor.rampPowerTo(Math.abs(speed));

		// keep looping while we are still active, and there is time left, and both motors are running.
		// Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
		// its target position, the motion will stop.  This is "safer" in the event that the robot will
		// always end the motion as soon as possible.
		// However, if you require that BOTH motors have finished their moves before the robot continues
		// onto the next step, use (isBusy() || isBusy()) in the loop test.
		while ((runtime.seconds() < timeoutS) && leftMotor.isBusy() && rightMotor.isBusy() && opMode.opModeIsActive()) {

			//telemetry.log("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
			//telemetry.log("Path2",  "Running at %7d :%7d", leftMotor.getCurrentTickNumber(), rightMotor.getCurrentTickNumber());

		}

		// Stop all motion;
		leftMotor.rampPowerTo(0.0);
		rightMotor.rampPowerTo(0.0);

		// Turn off RUN_TO_POSITION
		leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

	}

	public HardwareMap getHardwareMap() {
		return hardwareMap;
	}

	public AarreMotor getLeftMotor() {
		return leftMotor;
	}

	public AarreMotor getRightMotor() {
		return rightMotor;
	}

	public AarreTelemetry getTelemetry() {
		return (telemetry);
	}

	/**
	 * Lower the arm to its downward position
	 */
	void lowerArm() {
		arm.lower();
	}

	/**
	 * Lower the hook to its downward position
	 */
	void lowerHook() {
		telemetry.log("Hook servo - lowering hook");
		hookServo.forward();
		telemetry.log("Hook servo - hook lowered");
	}

	/**
	 * Lower the riser to its downward (least extended) position. This is the position that it will
	 * need to be in at the beginning of the autonomous game when it is hanging from the lander.
	 */
	void lowerRiser() {
		riser.lower();
	}

	/**
	 * Raise the arm to its upward position
	 */
	void raiseArm() {
		arm.raise();
	}

	/**
	 * Raise the hook to its upward position
	 */
	void raiseHook() {
		telemetry.log("Hook servo - raising hook");
		hookServo.reverse();
		telemetry.log("Hook servo - hook raised");
	}

	/**
	 * Raise the riser to its upward (most extended) position. This is the position that it will
	 * need to be in near the end of the game just before it latches on to the lander.
	 */
	void raiseRiser() {
		riser.raise();
	}

	/**
	 * Put the robot in the state it should be in at the end of the
	 * game, when it is preparing to latch to the lander.
	 * <p>
	 * <ul>
	 * <li>The arm is lowered.</li>
	 * <li>The riser is raised, as it would need to be when attaching to the lander.</li>
	 * <li>The hook is raised, as it would need to be when attaching to the lander.</li>
	 * </ul>
	 * </p>
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
	 * <p>
	 * <ul>
	 * <li>The arm is lowered, as it would be when attached to the lander.</li>
	 * <li>The riser is lowered, as it would be when attached to the lander.</li>
	 * <li>The hook is raised, as it would be when attached to the lander.</li>
	 * </ul>
	 * </p>
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
	 * <p>
	 * <ul>
	 * <li>The arm is lowered, which is its most compact position.</li>
	 * <li>The riser is lowered, which is its most compact position.</li>
	 * <li>The hook is lowered, which is its most compact position.</li>
	 * </ul>
	 * </p>
	 * The difference between this and {@code readyForAutonomousGame} is that
	 * this mode lowers the hook.
	 */
	public void readyForTransportation() {
		lowerArm();
		lowerRiser();
		lowerHook();
	}


}

