package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
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
class AarreRobot {

    private AarreTelemetry telemetry;

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
    @SuppressWarnings("WeakerAccess")
    CRServo scoopServo;

    /**
     *
     */
    AarreRobot(){

    }


    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     */
    void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;


        // Determine new target position, and pass to motor controller
        newLeftTarget = leftMotor.getCurrentTickNumber() + (int)(leftInches * leftMotor.COUNTS_PER_INCH);
        newRightTarget = rightMotor.getCurrentTickNumber() + (int)(rightInches * rightMotor.COUNTS_PER_INCH);
        leftMotor.setTargetPosition(newLeftTarget);
        rightMotor.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        ElapsedTime runtime = new ElapsedTime();
        leftMotor.setPower(Math.abs(speed));
        rightMotor.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while ((runtime.seconds() < timeoutS) && (leftMotor.isBusy() && rightMotor.isBusy())) {

            telemetry.log("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            telemetry.log("Path2",  "Running at %7d :%7d", leftMotor.getCurrentTickNumber(), rightMotor.getCurrentTickNumber());

        }

        // Stop all motion;
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // Turn off RUN_TO_POSITION
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }


    /**
     * Initialize hardware interfaces
     *
     * @param hardwareMap   An instance of {@link HardwareMap}
     * @param telemetry     An instance of {@link AarreTelemetry}
     *
     */
    void init(HardwareMap hardwareMap, AarreTelemetry telemetry) {

        // Define and initialize the motors. The strings used here as parameters
        // to 'get' must correspond to the names assigned in the robot configuration
        // in the FTC Robot Controller app on the phone

        leftMotor   = new AarreMotor(hardwareMap, "left", telemetry);
        rightMotor  = new AarreMotor(hardwareMap, "right", telemetry);
        armMotor    = new AarreMotor(hardwareMap, "arm", telemetry);
        riserMotor  = new AarreMotor(hardwareMap, "riser", telemetry);

        // Configure drive motors such that a positive power command moves them forwards
        // and causes the encoders to count UP. Note that, as in most robots, the drive
        // motors are mounted in opposite directions. May need to be reversed if using AndyMark motors.

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        armMotor.setPower(0);
        riserMotor.setPower(0);

        // This code REQUIRES that you have encoders on the wheel motors

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful encoder reset
        telemetry.log( "Drive motor encoders reset");
        telemetry.log("Left motor start position",  "%7d",  leftMotor.getCurrentTickNumber());
        telemetry.log("Right motor start position",  "%7d", rightMotor.getCurrentTickNumber());

        // Define the servos

        hookServo  = new AarreServo(hardwareMap, "hook", telemetry);
        initializeHook();

        scoopServo = hardwareMap.get(CRServo.class, "scoop");

        // Save reference to telemetry
        this.telemetry = telemetry;
    }

    /**
     * Set minimum position and maximum position of hook servo based on hardware limits.
     */
    private void initializeHook() {

        this.telemetry.log("Initializing hook");

        hookServo.setDirection(Servo.Direction.FORWARD);

        // With the hook up, the servo is at 0 degrees.
        // With the hook down, the servo is at about 100 degrees.

        double hook_down_degrees = 100.0;
        double hook_up_degrees = 0.0;
        double hook_maximum_degrees = 180.0;
        hookServo.scaleRange(hook_up_degrees/hook_maximum_degrees, hook_down_degrees/hook_maximum_degrees);

    }


    /**
     * Lower the arm to its downward position while avoiding stalling the arm motor
     */
    void lowerArm() {
        armMotor.setStallTimeLimitInMilliseconds(100);
        armMotor.runUntilStalled(-0.1);
    }

    void lowerHook() {
        telemetry.log("Hook servo - lowering hook");
        hookServo.forward();
        telemetry.log("Hook servo - hook lowered");
    }

    /**
     * Lower the riser to its downward position while avoiding stalling the riser motor
     */
    @SuppressWarnings("EmptyMethod")
    void lowerRiser() {
        riserMotor.setStallTimeLimitInMilliseconds(100);
        riserMotor.runUntilStalled(-0.1);
    }

    /**
     *  Move the riser based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     *
     *  This method does not do stall detection
     *
     *  @param revolutions The number of (motor shaft) revolutions to move the riser.
     *                     Positive values move the riser up.
     *                     Negative values move the riser down.
     */
    public void moveRiser(int revolutions) {

        int startPositionCounts;
        int startPositionRevolutions;

        int targetPositionCounts;
        int targetPositionRevolutions;

        final double riserSpeed         = 0.1;
        final int    timeoutS           = 1;

        // Determine new target position and pass to motor controller

        startPositionCounts = riserMotor.getCurrentTickNumber();
        startPositionRevolutions = startPositionCounts / riserMotor.COUNTS_PER_MOTOR_REVOLUTION;

        targetPositionRevolutions = startPositionRevolutions + 1;
        targetPositionCounts = targetPositionRevolutions * riserMotor.COUNTS_PER_MOTOR_REVOLUTION;

        //targetPositionInches = leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
        //newRightTarget = rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

        riserMotor.setTargetPosition(targetPositionCounts);

        // Turn On RUN_TO_POSITION
        riserMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time
        ElapsedTime runtime = new ElapsedTime();

        riserMotor.setPower(Math.abs(riserSpeed));

        // Keep looping while we are still active, and there is time left, and both motors are running.
        //
        //noinspection StatementWithEmptyBody
        while ((runtime.seconds() < timeoutS) && riserMotor.isBusy());

        // Stop all motion;
        riserMotor.setPower(0);

        // Turn off RUN_TO_POSITION
        riserMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    /**
     * Raise the arm to its upward position while avoiding stalling the arm motor
     */
    @SuppressWarnings("EmptyMethod")
    void raiseArm() {
        armMotor.setStallTimeLimitInMilliseconds(100);
        armMotor.runUntilStalled(0.1);
    }

    void raiseHook() {
        telemetry.log("Hook servo - raising hook");
        hookServo.reverse();
        telemetry.log("Hook servo - hook raised");
    }

    /**
     * Raise the riser to its upward position while avoiding stalling the riser motor
     */
    @SuppressWarnings("EmptyMethod")
    void raiseRiser() {
        riserMotor.setStallTimeLimitInMilliseconds(100);
        riserMotor.runUntilStalled(0.1);
    }
}

