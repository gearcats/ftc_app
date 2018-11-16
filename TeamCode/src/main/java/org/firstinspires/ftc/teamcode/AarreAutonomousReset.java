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
@Autonomous(name="Aarre Autonomous Reset", group="Aarre")
public class AarreAutonomousReset extends LinearOpMode {

    private final AarreRobot robot = new AarreRobot();
    private final AarreTelemetry betterTelemetry = new AarreTelemetry(telemetry, true);

    @Override
    public void runOpMode() {

        betterTelemetry.log("Status", "Initializing robot");

        robot.init(hardwareMap, betterTelemetry);

        // Wait for the driver to press PLAY
        waitForStart();

        betterTelemetry.log("Status", "Initializing hook");
        robot.initializeHook();

        betterTelemetry.log("Status", "Readying hook");
        robot.readyHook();

        betterTelemetry.log("Status", "Raising hook");
        robot.raiseHook();

        betterTelemetry.log("Status", "Lowering arm");
        robot.lowerArm();

        betterTelemetry.log("Status", "Reset complete");

        // Wait until the driver presses STOP
        //noinspection StatementWithEmptyBody
        while (opModeIsActive()) {

        }

    }

}
