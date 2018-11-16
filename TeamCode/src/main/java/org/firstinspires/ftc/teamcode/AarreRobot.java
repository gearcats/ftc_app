package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
    DcMotor leftMotor;
    @SuppressWarnings("WeakerAccess")
    DcMotor rightMotor;
    @SuppressWarnings("WeakerAccess")
    AarreMotor armMotor;
    @SuppressWarnings("WeakerAccess")
    DcMotor riserMotor;
    @SuppressWarnings("WeakerAccess")
    AarreServo   hookServo;
    @SuppressWarnings("WeakerAccess")
    CRServo scoopServo;

    /**
     *
     */
    AarreRobot(){

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

        leftMotor   = hardwareMap.get(DcMotor.class, "left");
        rightMotor  = hardwareMap.get(DcMotor.class, "right");
        armMotor    = new AarreMotor(hardwareMap, "arm", telemetry);
        riserMotor  = hardwareMap.get(DcMotor.class, "riser");

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
        telemetry.log("Left motor start position",  "%7d",  leftMotor.getCurrentPosition());
        telemetry.log("Right motor start position",  "%7d", rightMotor.getCurrentPosition());

        // Define the servos

        hookServo  = new AarreServo(hardwareMap, "hook", telemetry);
        scoopServo = hardwareMap.get(CRServo.class, "scoop");

        // Save reference to telemetry
        this.telemetry = telemetry;
    }

    /**
     * Set minimum position and maximum position of hook servo based on hardware limits, then
     * exercise the hook by raising it and lowering it.
     */
    void initializeHook() {

        telemetry.log("Initializing hook");

        hookServo.setDirection(Servo.Direction.REVERSE);

        // The hook servo is constrained by hardware
        // When the hook is down, the arm attached to the servo is at about 110 degrees
        // compared to 0 when the hook is up. Looking at it the other way, the arm attached
        // to the servo is at about 70 degrees compared to 180 when the hook is up.

        double hook_down_degrees = 80.0;
        double hook_up_degrees = 180.0;
        double hook_maximum_degrees = 180.0;
        hookServo.scaleRange(hook_down_degrees/hook_maximum_degrees, hook_up_degrees/hook_maximum_degrees);

        raiseHook();
        lowerHook();

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
        hookServo.reverseServo();
        telemetry.log("Hook servo - hook lowered");
    }

    /**
     * TODO Lower the riser to its downward position while avoiding stalling the riser motor
     */
    @SuppressWarnings("EmptyMethod")
    void lowerRiser() {

    }

    /**
     * TODO Raise the arm to its upward position while avoiding stalling the arm motor
     */
    @SuppressWarnings("EmptyMethod")
    void raiseArm() {

    }


    void raiseHook() {
        telemetry.log("Hook servo - raising hook");
        hookServo.forwardServo();
        telemetry.log("Hook servo - hook raised");
    }

    /**
     * TODO Raise the riser to its upward position while avoiding stalling the riser motor
     */
    @SuppressWarnings("EmptyMethod")
    void raiseRiser() {

    }
}

