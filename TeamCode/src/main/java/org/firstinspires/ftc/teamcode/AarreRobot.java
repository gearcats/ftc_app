package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains Aarre's experimental code to initialize the robot. It defines
 * all the specific hardware for the 2018-2019 robot.
 *
 * Pulling it out into a separate class makes it possible to use the same code from
 * different OpModes (such as {@link AarreAutonomous}and {@link AarreAutonomousReset}).
 *
 * This is NOT an OpMode itself.
 *
 */
public class AarreRobot {

    private final AarreTelemetry telemetry;

    private static final double DEFAULT_PROPORTION_RISER_POWER = 1.0;
    private static final int DEFAULT_MILLISECONDS_RISER_TIMEOUT = 5000;
    private static final double DEFAULT_SECONDS_RISER_TIMEOUT = (double) DEFAULT_MILLISECONDS_RISER_TIMEOUT / 1000.0;
    private static final int DEFAULT_TICKS_RISER_STALL_TOLERANCE = 15;
    private static final double DEFAULT_REVOLUTIONS_RISER_RANGE = 5.0;

    /** These properties are package-private so methods of other classes in this package can use them.
     *
     *  TODO: Implement getters and setters to keep these properties private.
     *
     */
    @SuppressWarnings("WeakerAccess")
    AarreMotor leftMotor;
    @SuppressWarnings("WeakerAccess")
    AarreMotor rightMotor;
    @SuppressWarnings("WeakerAccess")
    AarreMotor armMotor;
    @SuppressWarnings("WeakerAccess")
    AarreMotor riserMotor;
    @SuppressWarnings("WeakerAccess")
    AarreServo hookServo;
    @SuppressWarnings({"WeakerAccess", "unused"})
    CRServo scoopServo;

    /**
     * Constructor
     *
     * @param hardwareMap   An instance of {@link HardwareMap}
     * @param telemetry     An instance of {@link AarreTelemetry}
     *
     */
    public AarreRobot(final HardwareMap hardwareMap, @SuppressWarnings("ParameterHidesMemberVariable") final AarreTelemetry telemetry) {

        if (telemetry == null)
            throw new AssertionError("Unexpected null object: telemetry");

        this.telemetry = telemetry;

        // Define and initialize the motors. The strings used here as parameters
        // must correspond to the names assigned in the robot configuration
        // in the FTC Robot Controller app on the phone

        leftMotor = new AarreMotor(hardwareMap, "left", telemetry);
        rightMotor = new AarreMotor(hardwareMap, "right", telemetry);
        armMotor = new AarreMotor(hardwareMap, "arm", telemetry);
        riserMotor = new AarreMotor(hardwareMap, "riser", telemetry);

        // Configure drive motors such that a positive power command moves them forwards
        // and causes the encoders to count UP. Note that, as in most robots, the drive
        // motors are mounted in opposite directions. May need to be reversed if using AndyMark motors.

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        riserMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set all motors to zero power

        leftMotor.setPower(0.0);
        rightMotor.setPower(0.0);
        armMotor.setPower(0.0);
        riserMotor.setPower(0.0);

        // This code REQUIRES that you have encoders on the wheel motors

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        riserMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        riserMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define the servos

        hookServo = new AarreServo(hardwareMap, "hook", telemetry);

        this.telemetry.log("Initializing hook");

        hookServo.setDirection(Servo.Direction.FORWARD);

        // With the hook up, the servo is at 0 degrees.
        // With the hook down, the servo is at about 100 degrees.

        final double hookDownDegrees = 100.0;
        final double hookUpDegrees = 0.0;
        final double hookMaximumDegrees = 180.0;
        hookServo.scaleRange(hookUpDegrees / hookMaximumDegrees, hookDownDegrees / hookMaximumDegrees);

        // TODO: Initialize scoop servo
        scoopServo = hardwareMap.get(CRServo.class, "scoop");

    }


    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     */
    final void encoderDrive(final double speed,
                            final double leftInches, final double rightInches,
                            final double timeoutS) {
        final int newLeftTarget;
        final int newRightTarget;


        // Determine new target position, and pass to motor controller
        newLeftTarget = leftMotor.getCurrentTickNumber() + (int) (leftInches * AarreMotor.getCountsPerInch());
        newRightTarget = rightMotor.getCurrentTickNumber() + (int) (rightInches * AarreMotor.getCountsPerInch());
        leftMotor.setTargetPosition(newLeftTarget);
        rightMotor.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        final ElapsedTime runtime = new ElapsedTime();
        leftMotor.setPower(Math.abs(speed));
        rightMotor.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while ((runtime.seconds() < timeoutS) && (leftMotor.isBusy() && rightMotor.isBusy())) {

            telemetry.log("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
            telemetry.log("Path2",  "Running at %7d :%7d", leftMotor.getCurrentTickNumber(), rightMotor.getCurrentTickNumber());

        }

        // Stop all motion;
        leftMotor.setPower(0.0);
        rightMotor.setPower(0.0);

        // Turn off RUN_TO_POSITION
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public AarreMotor getLeftMotor() {
        return leftMotor;
    }

    public AarreMotor getRightMotor() {
        return rightMotor;
    }


    /**
     * Lower the arm to its downward position while avoiding stalling the arm motor
     */
    final void lowerArm() {
        armMotor.setStallTimeLimitInMilliseconds(100);
        armMotor.runUntilStalled(-0.1);
    }

    /**
     * Lower the hook to its downward position
     */
    void lowerHook() {
        telemetry.log("Hook servo - lowering hook");
        hookServo.forward();
        telemetry.log("Hook servo - hook lowered");
    }

    public void lowerRiser() {
        lowerRiserByRevolutions();
    }

    /**
     * Lower the riser by default parameters
     */
    private void lowerRiserByRevolutions() {
        lowerRiserByRevolutions(DEFAULT_PROPORTION_RISER_POWER, DEFAULT_REVOLUTIONS_RISER_RANGE, DEFAULT_MILLISECONDS_RISER_TIMEOUT);
    }

    /**
     * Lower the riser by a certain number of revolutions of the motor shaft
     *
     * @param proportionMotorPower
     * @param numberOfRevolutions
     * @param secondsTimeout
     */
    private void lowerRiserByRevolutions(final double proportionMotorPower, final double numberOfRevolutions, final double secondsTimeout) {
        riserMotor.runByRevolutions(proportionMotorPower, -numberOfRevolutions, secondsTimeout);
    }


    /**
     * Lower the riser to its downward position while avoiding stalling the riser motor
     */
    void lowerRiserUntilStall() {
        telemetry.log("Riser - lowering riser");
        riserMotor.setStallTimeLimitInMilliseconds(200);
        riserMotor.setStallDetectionToleranceInTicks(15);
        riserMotor.runUntilStalled(-1.0);
        telemetry.log("Riser - riser lowered");
    }

    /**
     * Raise the arm to its upward position while avoiding stalling the arm motor
     */
    void raiseArm() {
        armMotor.setStallTimeLimitInMilliseconds(100);
        armMotor.runUntilStalled(0.1);
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
     * Raise the riser. This public method will use the best available method to raise the
     * riser at any given point in code development.
     */
    public void raiseRiser() {
        raiseRiserByRevolutions();
    }

    /**
     * Raise the riser by revolutions using default parameters.
     */
    private void raiseRiserByRevolutions() {
        raiseRiserByRevolutions(DEFAULT_PROPORTION_RISER_POWER, DEFAULT_REVOLUTIONS_RISER_RANGE, DEFAULT_SECONDS_RISER_TIMEOUT);
    }

    /**
     * Raise the riser by a certain number of motor shaft numberOfRevolutions.
     *
     * @param proportionMotorPower Apply this proportion of power to the motor.
     * @param numberOfRevolutions  Turn the motor shaft this number of revolutions.
     * @param secondsTimeout       Shut off the motor shuts off after this many seconds
     *                             regardless of whether it has reached the requested number
     *                             of revolutions. The idea is to prevent burning out motors
     *                             by stalling them for long periods of time or breaking other
     *                             components by applying too much force to them for too long.
     */
    private final void raiseRiserByRevolutions(final double proportionMotorPower, final double numberOfRevolutions, final double secondsTimeout) {
        riserMotor.runByRevolutions(proportionMotorPower, numberOfRevolutions, secondsTimeout);
    }


    /**
     * Raise the riser to its upward position while avoiding stalling the riser motor.
     *
     * This method does not work very well. The riser motor does not really 'stall'. Instead, it
     * continues to run irregularly as it attempts to push the riser higher than it can go.
     */
    private void raiseRiserUntilStall() {
        telemetry.log("Riser - raising riser");
        riserMotor.setStallTimeLimitInMilliseconds(DEFAULT_MILLISECONDS_RISER_TIMEOUT);
        riserMotor.setStallDetectionToleranceInTicks(DEFAULT_TICKS_RISER_STALL_TOLERANCE);
        riserMotor.runUntilStalled(1.0);
        telemetry.log("Riser - riser raised");
    }

}

