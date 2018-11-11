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

                                HardwareMap hardwareMap     =  null;

    private ElapsedTime period          = new ElapsedTime();

    public DcMotor leftMotor;
    public DcMotor rightMotor;
    public AarreMotor armMotor;
    public DcMotor riserMotor;
    public Servo   hookServo;
    public CRServo scoopServo;

    /**
     *
     */
    public AarreRobot(){

    }





    /**
     * Initialize hardware interfaces
     *
     * @param hardwareMap   An instance of {@link HardwareMap}
     * @param telemetry     An instance of {@Link Telemetry}
     *
     */
    public void init(HardwareMap hardwareMap, Telemetry telemetry) {

        // Define and initialize the motors. The strings used here as parameters
        // to 'get' must correspond to the names assigned in the robot configuration
        // in the FTC Robot Controller app on the phone

        leftMotor   = hardwareMap.get(DcMotor.class, "left");
        rightMotor  = hardwareMap.get(DcMotor.class, "right");
        armMotor    = new AarreMotor(hardwareMap, "arm", telemetry);
        riserMotor  = hardwareMap.get(DcMotor.class, "riser");
        hookServo   = hardwareMap.get(Servo.class,   "hook");
        scoopServo  = hardwareMap.get(CRServo.class, "scoop");

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

        hookServo.setPosition(MID_SERVO);
        //scoopServo.setPosition(MID_SERVO);

        // Save reference to Hardware map
        this.hardwareMap = hardwareMap;

        lowerArm();
        lowerRiser();
        raiseHook();

    }

    /**
     * Lower the arm to its downward position while avoiding stalling the arm motor
     */
    public void lowerArm() {
        armMotor.setStallTimeLimitInMilliseconds(100);
        //armMotor.runUntilStalled();
    }

    /**
     * Raise the hook to its upward position while avoiding stalling the hook servo
     */
    public void raiseHook() {
    }

    /**
     * Lower the riser to its downward position while avoiding stalling the riser motor
     */
    public void lowerRiser() {
    }
}

