package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
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
class AarreRobot {

    private final AarreTelemetry telemetry;

    private static final double RISER_SPEED_PROPORTION = 0.2;
    private static final int RISER_TIMEOUT_MS = 100;
    private static final int RISER_STALL_TOLERANCE = 5;

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
    AarreRobot(HardwareMap hardwareMap, AarreTelemetry telemetry) {

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

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        armMotor.setPower(0);
        riserMotor.setPower(0);

        // This code REQUIRES that you have encoders on the wheel motors

        leftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        riserMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(RunMode.RUN_USING_ENCODER);
        riserMotor.setMode(RunMode.RUN_USING_ENCODER);

        // Define the servos

        hookServo = new AarreServo(hardwareMap, "hook", telemetry);

        this.telemetry.log("Initializing hook");

        hookServo.setDirection(Servo.Direction.FORWARD);

        // With the hook up, the servo is at 0 degrees.
        // With the hook down, the servo is at about 100 degrees.

        double hookDownDegrees = 100.0;
        double hookUpDegrees = 0.0;
        double hookMaximumDegrees = 180.0;
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
    final void encoderDrive(double speed,
                            double leftInches, double rightInches,
                            double timeoutS) {
        int newLeftTarget;
        int newRightTarget;


        // Determine new target position, and pass to motor controller
        newLeftTarget = leftMotor.getCurrentTickNumber() + (int) (leftInches * AarreMotor.getCountsPerInch());
        newRightTarget = rightMotor.getCurrentTickNumber() + (int) (rightInches * AarreMotor.getCountsPerInch());
        leftMotor.setTargetPosition(newLeftTarget);
        rightMotor.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        leftMotor.setMode(RunMode.RUN_TO_POSITION);
        rightMotor.setMode(RunMode.RUN_TO_POSITION);

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

            telemetry.log("Path1", "Running to %7d :%7d", Integer.valueOf(newLeftTarget), newRightTarget);
            telemetry.log("Path2",  "Running at %7d :%7d", leftMotor.getCurrentTickNumber(), rightMotor.getCurrentTickNumber());

        }

        // Stop all motion;
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // Turn off RUN_TO_POSITION
        leftMotor.setMode(RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(RunMode.RUN_USING_ENCODER);

    }

    /**
     * Lower the arm to its downward position while avoiding stalling the arm motor
     */
    void lowerArm() {
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

    /**
     * Lower the riser to its downward position while avoiding stalling the riser motor
     */
    void lowerRiser() {
        telemetry.log("Riser - lowering riser");
        riserMotor.setStallTimeLimitInMilliseconds(RISER_TIMEOUT_MS);
        riserMotor.setStallDetectionToleranceInTicks(RISER_STALL_TOLERANCE);
        riserMotor.runUntilStalled(-RISER_SPEED_PROPORTION);
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
     * Raise the riser to its upward position while avoiding stalling the riser motor
     */
    void raiseRiser() {
        telemetry.log("Riser - raising riser");
        riserMotor.setStallTimeLimitInMilliseconds(RISER_TIMEOUT_MS);
        riserMotor.setStallDetectionToleranceInTicks(RISER_STALL_TOLERANCE);
        riserMotor.runUntilStalled(RISER_SPEED_PROPORTION);
        telemetry.log("Riser - riser raised");
    }
}

