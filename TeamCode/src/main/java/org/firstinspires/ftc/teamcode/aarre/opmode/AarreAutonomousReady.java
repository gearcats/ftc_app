package org.firstinspires.ftc.teamcode.aarre.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.Robot;
import org.firstinspires.ftc.teamcode.aarre.src.TelemetryPlus;

import java.util.logging.Logger;

/**
 * This file contains Aarre's experimental code to autonomously "reset" the robot to a state suitable for running the
 * autonomous game mode
 * <p>
 * To avoid issuing an error on the phones, any OpMode class must be declared public.
 */

@Autonomous(name = "Aarre Autonomous Ready", group = "Aarre")
@Disabled
public class AarreAutonomousReady extends LinearOpMode {

	private       TelemetryPlus betterTelemetryPlus;
	private final Logger        log = Logger.getLogger(this.getClass().getName());
	private       Robot         robot;

	/**
	 * Properties inherited from LinearOpMode include:
	 * <p>
	 * hardwareMap telemetry
	 */
	@Override
	public final void runOpMode() {

		// 'telemetry' comes from FTC....
		// It is only available in runOpMode

		if (telemetry == null)
			throw new AssertionError("Unexpected null object: telemetry");
		betterTelemetryPlus = new TelemetryPlus(telemetry);

		// 'hardwareMap comes from FTC....
		// It is only available in runOpMode

		if (hardwareMap == null)
			throw new AssertionError("Unexpected null object: hardwareMap");

		robot = new Robot(this);

		betterTelemetryPlus.log("Initializing robot");

		// Wait for the driver to press PLAY
		waitForStart();

		try {
			robot.readyForAutonomousGame();
		} catch (NoSuchMethodException e) {
			log.severe(e.toString());
		}

		betterTelemetryPlus.log("Reset complete - robot is ready for autonomous mode");

		// Wait until the driver presses STOP
		//noinspection StatementWithEmptyBody
		while (opModeIsActive()) {

		}

	}

}
