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

@SuppressWarnings("WeakerAccess")
@Autonomous(name="Aarre Autonomous Reset", group="Aarre")
public class AarreAutonomousReset extends LinearOpMode {

    private AarreRobot robot = new AarreRobot();

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initializing robot");
        telemetry.update();

        robot.init(hardwareMap, telemetry);

        // Wait for the driver to press PLAY
        waitForStart();

        robot.lowerArm();
        robot.lowerRiser();
        robot.raiseHook();

        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

    }

}