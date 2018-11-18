package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * This file contains Aarre's experimental code to make the robot "dance" autonomously (that it,
 * to make it exercise all of its functions).
 *
 * To avoid issuing an error on the phones, any OpMode class must be
 * declared public.
 *
 */

@SuppressWarnings({"WeakerAccess", "unused"})
@Autonomous(name="Aarre Dance", group="Aarre")
public class AarreDance extends LinearOpMode {

    private AarreTelemetry betterTelemetry;
    private AarreRobot robot;

    public AarreDance() {
    }

    /**
     * Properties inherited from LinearOpMode include:
     * <p>
     * hardwareMap
     * telemetry
     */
    @Override
    public void runOpMode() {

        // 'telemetry' comes from FTC....
        // It is only available in runOpMode

        if (telemetry == null)
            throw new AssertionError("Unexpected null object: telemetry");
        betterTelemetry = new AarreTelemetry(telemetry);

        // 'hardwareMap comes from FTC....
        // It is only available in runOpMode

        if (hardwareMap == null)
            throw new AssertionError("Unexpected null object: hardwareMap");
        robot = new AarreRobot(hardwareMap, betterTelemetry);

        betterTelemetry.log("Initializing robot");

        // Wait for the driver to press PLAY
        waitForStart();

        double DRIVE_SPEED = 0.5;
        double TURN_SPEED = 0.5;
        double INCHES = 12.0;
        double TIMEOUT = 5.0;

        robot.encoderDrive(DRIVE_SPEED, INCHES, INCHES, TIMEOUT);
        robot.encoderDrive(DRIVE_SPEED, -INCHES, -INCHES, TIMEOUT);
        robot.encoderDrive(TURN_SPEED, INCHES, -INCHES, TIMEOUT);
        robot.encoderDrive(TURN_SPEED, -INCHES, INCHES, TIMEOUT);

        robot.raiseArm();
        robot.lowerArm();

        robot.raiseRiser();
        robot.lowerRiser();

        robot.raiseHook();
        robot.lowerHook();

        betterTelemetry.log("Ready to run");    //

    }

}