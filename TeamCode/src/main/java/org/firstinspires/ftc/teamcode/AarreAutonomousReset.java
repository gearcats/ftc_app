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

        telemetry.addData("Status", "Raising hook");
        telemetry.update();
        robot.raiseHook();

//        telemetry.addData("Status", "Lowering arm");
//        robot.lowerArm();
//
//        telemetry.addData("Status", "Lowering riser");
//        robot.lowerRiser();

        telemetry.addData("Status", "Reset complete");
        telemetry.update();

        // Wait until the driver presses STOP
        //noinspection StatementWithEmptyBody
        while (opModeIsActive()) {

        }

    }

}
