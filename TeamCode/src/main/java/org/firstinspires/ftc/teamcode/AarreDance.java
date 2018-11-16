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

    private final AarreRobot robot = new AarreRobot();
    private final AarreTelemetry betterTelemetry = new AarreTelemetry(telemetry);

    @Override
    public void runOpMode() {

        betterTelemetry.log("Initializing robot");

        robot.init(hardwareMap, betterTelemetry);

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