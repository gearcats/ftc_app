package org.firstinspires.ftc.teamcode.aarre.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.Robot;
import org.firstinspires.ftc.teamcode.aarre.src.TelemetryPlus;

import java.util.logging.Logger;


/**
 * This file contains Aarre's experimental code to test that we can read data from the IMU.
 * <p>
 * To avoid issuing an error on the phones, any OpMode class must be declared public.
 */
@Autonomous(name = "Aarre Autonomous IMU Test", group = "Aarre")
@Disabled
public class AarreAutonomousIMUTest extends LinearOpMode {

	TelemetryPlus betterTelemetryPlus;
	private final Logger javaLog = Logger.getLogger(this.getClass().getName());
	Robot robot;

	@Override
	public void runOpMode() {

		// 'telemetry' comes from FTC....
		// It is only available in runOpMode

		if (telemetry == null) {
			throw new AssertionError("Unexpected null object: telemetry");
		}
		betterTelemetryPlus = new TelemetryPlus(telemetry);

		// 'hardwareMap comes from FTC....
		// It is only available in runOpMode

		if (hardwareMap == null) {
			throw new AssertionError("Unexpected null object: hardwareMap");
		}
		robot = new Robot(this);

		betterTelemetryPlus.log("Initializing robot");

		while (opModeIsActive()) {
			robot.updateIMUTelemetry();
		}

	}
}
