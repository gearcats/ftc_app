package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * This file contains Aarre's experimental code to autonomously set the robot to a state
 * suitable for transportation (i.e., with the arm down, the riser down, and the hook down).
 * <p>
 * To avoid issuing an error on the phones, any OpMode class must be
 * declared public.
 */

@Autonomous(name = "Aarre Autonomous Transport", group = "Aarre")
public class AarreAutonomousTransport extends LinearOpMode {

    private AarreTelemetry betterTelemetry;
    private AarreRobot robot;

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

        betterTelemetry.log("Status", "Initializing robot");

        // Wait for the driver to press PLAY
        waitForStart();

        robot.readyForTransportation();

        betterTelemetry.log("Status", "Reset complete - robot is ready for autonomous mode");

        // Wait until the driver presses STOP
        //noinspection StatementWithEmptyBody
        while (opModeIsActive()) {

        }

    }

}
