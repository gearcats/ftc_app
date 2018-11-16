package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This file contains Aarre's experimental code to initialize the robot. It defines
 * all the specific hardware for the 2018-2019 robot.
 *
 * Pulling it out into a separate class makes it possible to use the same code from
 * different opmodes (such as {@link AarreAutonomous}and {@link AarreAutonomousReset}).
 *
 * This is NOT an opmode itself.
 *
 */
class AarreRobot {

    private static final double MID_SERVO                    =  0.5 ;
    public static final double ARM_UP_POWER                 =  0.45 ;
    public static final double ARM_DOWN_POWER               = -0.45 ;

    private HardwareMap hardwareMap     =  null;

    private Telemetry telemetry = null;

    private ElapsedTime period          = new ElapsedTime();

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
     * @param telemetry     An instance of {@link Telemetry}
     *
     */
    void init(HardwareMap hardwareMap, Telemetry telemetry) {

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
        telemetry.addData("Status", "Drive motor encoders reset");
        telemetry.addData("Left motor start position",  "%7d",  leftMotor.getCurrentPosition());
        telemetry.addData("Right motor start position",  "%7d", rightMotor.getCurrentPosition());
        telemetry.update();

        // Define the servos

        hookServo  = hardwareMap.get(Servo.class, "hook");
        scoopServo = hardwareMap.get(CRServo.class, "scoop");

        // Initialize the servos

        //hookServo.setPosition(MID_SERVO);
        //scoopServo.setPosition(MID_SERVO);

        // Save reference to Hardware map
        this.hardwareMap = hardwareMap;

        // Save reference to telemetry
        this.telemetry = telemetry;


    }

    /**
     * Lower the arm to its downward position while avoiding stalling the arm motor
     */
    void lowerArm() {
        armMotor.setStallTimeLimitInMilliseconds(100);
        armMotor.runUntilStalled(-0.1);
    }

    /**
     * TODO Lower the hook to its downward position while avoiding stalling the hook servo
     */
    void lowerHook() {

    }

    /**
     * TODO Lower the riser to its downward position while avoiding stalling the riser motor
     */
    void lowerRiser() {

    }

    /**
     * TODO Raise the arm to its upward position while avoiding stalling the arm motor
     */
    void raiseArm() {

    }

    /**
     * TODO Raise the hook to its upward position while avoiding stalling the hook servo
     */
    void raiseHook() {
        this.telemetry.addData("Status", "Raising hook");
        this.telemetry.update();

        double maximumPosition = hookServo.MAX_POSITION;
        this.telemetry.addData("Hook servo maximum position is %f", maximumPosition);
        this.telemetry.update();

        this.telemetry.addData("Status", "Raising hook");
        this.telemetry.update();

        hookServo.setPosition(maximumPosition);

        this.telemetry.addData("Status", "Hook raised");
        this.telemetry.update();

        double currentPosition = hookServo.getPosition();
        this.telemetry.addData("Hook servo current position is %f", currentPosition);
        this.telemetry.update();
    }

    /**
     * TODO Raise the riser to its upward position while avoiding stalling the riser motor
     */
    void raiseRiser() {

    }
}

