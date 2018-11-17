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

    private final AarreTelemetry betterTelemetry = new AarreTelemetry(telemetry);
    private final AarreRobot robot = new AarreRobot(hardwareMap, betterTelemetry);

    @Override
    public void runOpMode() {

        betterTelemetry.log("-------------------------------------------------------------------------------");
        betterTelemetry.log("Resetting robot for autonomous mode");

        // Wait for the driver to press PLAY
        waitForStart();

        robot.raiseHook();
        robot.lowerArm();
        robot.lowerRiser();

        betterTelemetry.log("Reset complete - robot is ready for autonomous mode");

        // Wait until the driver presses STOP
        //noinspection StatementWithEmptyBody
        while (opModeIsActive()) {

        }

    }

}
