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
    public final void runOpMode() {

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

        final double driveSpeed = 0.5;
        final double turnSpeed = 0.5;
        final double inches = 12.0;
        final double timeout = 5.0;

        robot.encoderDrive(driveSpeed, inches, inches, timeout);
        robot.encoderDrive(driveSpeed, -inches, -inches, timeout);
        robot.encoderDrive(turnSpeed, inches, -inches, timeout);
        robot.encoderDrive(turnSpeed, -inches, inches, timeout);

        robot.raiseArm();
        robot.lowerArm();

        //robot.raiseRiserUntilStall();
        //robot.lowerRiserUntilStall();

        //robot.raiseRiserByTicks();
        //robot.lowerRiserByTicks();

        robot.raiseRiserByRevolutions();
        robot.lowerRiserByRevolutions();

        robot.lowerHook();
        robot.raiseHook();

        betterTelemetry.log("Ready to run");    //

    }

}