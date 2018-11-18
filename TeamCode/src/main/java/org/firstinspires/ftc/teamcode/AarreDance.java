package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * This file contains Aarre's experimental code to autonomously "reset" the robot to a state
 * suitable for running the autonomous game mode
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

    @Override
    public void runOpMode() {

        // 'telemetry' comes from FTC....
        // It is only available in runOpMode

        if (telemetry == null) throw new AssertionError("Unexpected null object: telemetry");
        AarreTelemetry betterTelemetry = new AarreTelemetry(telemetry);
        if (betterTelemetry == null)
            throw new AssertionError("Unexpected null object: betterTelemetry");

        // 'hardwareMap comes from FTC....
        // It is only available in runOpMode

        if (hardwareMap == null) throw new AssertionError("Unexpected null object: hardwareMap");
        AarreRobot robot = new AarreRobot(hardwareMap, betterTelemetry);
        if (robot == null) throw new AssertionError("Unexpected null object: robot");

        betterTelemetry.log("Initializing robot");

        // Wait for the driver to press PLAY
        waitForStart();

        robot.raiseArm();
        robot.lowerArm();

        robot.raiseRiser();
        robot.lowerRiser();

        robot.raiseHook();
        robot.lowerHook();

        betterTelemetry.log("Ready to run");    //

    }

}