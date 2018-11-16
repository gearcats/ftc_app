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
    Servo   hookServo;
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

        hookServo  = hardwareMap.get(Servo.class, "hook");
        scoopServo = hardwareMap.get(CRServo.class, "scoop");

        // Save reference to telemetry
        this.telemetry = telemetry;
    }

    /**
     * Determine minimum position and maximum position of hook servo based on hardware limits
     */
    void initializeHook() {

        // Is the hook servo installed "upside down"? If so, uncomment this line.

        //hookServo.setDirection(Servo.Direction.REVERSE);

        // Reset the servo to its full range of movement
        hookServo.scaleRange(0.0, 1.0);

        raiseHook();
        double maximumServoPosition = hookServo.getPosition();

        lowerHook();
        double minimumServoPosition = hookServo.getPosition();

        hookServo.scaleRange(minimumServoPosition, maximumServoPosition);

    }

    /**
     * Lower the arm to its downward position while avoiding stalling the arm motor
     */
    void lowerArm() {
        armMotor.setStallTimeLimitInMilliseconds(100);
        armMotor.runUntilStalled(-0.1);
    }

    /**
     * Lower the hook to its downward position.
     *
     * TODO: Avoid stalling the hook servo.
     *
     */
    void lowerHook() {

        telemetry.log("Preparing to lower hook");

        double downPosition = hookServo.MIN_POSITION;
        telemetry.log("Hook servo prescriptive down position is %f", downPosition);

        hookServo.setPosition(downPosition);

        telemetry.log("Hook lowered");

        double currentPosition = hookServo.getPosition();
        telemetry.log("Hook servo actual down position is %f", currentPosition);

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

    /**
     * Raise the hook to its upward position.
     *
     * TODO: Avoid stalling the hook servo.
     *
     */
    void raiseHook() {

        telemetry.log("Preparing to raise hook");

        double upPosition = hookServo.MAX_POSITION;
        telemetry.log("Hook servo prescribed up position is %f", upPosition);

        hookServo.setPosition(upPosition);

        telemetry.log("Hook raised");

        double currentPosition = hookServo.getPosition();
        telemetry.log("Hook servo actual up position is %f", currentPosition);

    }

    /**
     * Put the hook in its middle position so that we can both lower and raise it.
     *
     * TODO: Avoid stalling the hook servo.
     */
    void readyHook() {

        telemetry.log("Hook servo preparing to move to middle position");

        double middlePosition = 0.5;

        telemetry.log("Hook servo prescribed middle position is %f", middlePosition);

        hookServo.setPosition(middlePosition);

        telemetry.log("Hook servo moved to middle position");

        double currentPosition = hookServo.getPosition();
        telemetry.log("Hook servo actual middle position is %f", currentPosition);

    }

    /**
     * TODO Raise the riser to its upward position while avoiding stalling the riser motor
     */
    @SuppressWarnings("EmptyMethod")
    void raiseRiser() {

    }
}

