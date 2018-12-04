package org.firstinspires.ftc.teamcode.aarre.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.Robot;
import org.firstinspires.ftc.teamcode.aarre.src.Telemetry;

import java.util.logging.Logger;


/**
 * This file contains Aarre's experimental code to test that we can read data from the IMU.
 * <p>
 * To avoid issuing an error on the phones, any OpMode class must be declared public.
 */
@Autonomous(name = "Aarre Autonomous IMU Test", group = "Aarre")
@Disabled
public class AarreAutonomousIMUTest extends LinearOpMode {

	Robot     robot;
	Telemetry betterTelemetry;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Override
	public void runOpMode() {

		// 'telemetry' comes from FTC....
		// It is only available in runOpMode

		if (telemetry == null) {
			throw new AssertionError("Unexpected null object: telemetry");
		}
		betterTelemetry = new Telemetry(telemetry);

		// 'hardwareMap comes from FTC....
		// It is only available in runOpMode

		if (hardwareMap == null) {
			throw new AssertionError("Unexpected null object: hardwareMap");
		}
		robot = new Robot(this);

		betterTelemetry.log("Initializing robot");

		while (opModeIsActive()) {
			robot.updateIMUTelemetry();
		}

	}
}
