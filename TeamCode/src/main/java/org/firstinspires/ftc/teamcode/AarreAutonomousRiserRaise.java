package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Autonomously raise the riser
 */

@Autonomous(name = "Aarre Autonomous Riser Raise", group = "Aarre")
public class AarreAutonomousRiserRaise extends LinearOpMode {

	private AarreTelemetry betterTelemetry;
	private AarreRobot     robot;

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

		if (telemetry == null) {
			throw new AssertionError("Unexpected null object: telemetry");
		}
		betterTelemetry = new AarreTelemetry(telemetry);

		// 'hardwareMap comes from FTC....
		// It is only available in runOpMode

		if (hardwareMap == null) {
			throw new AssertionError("Unexpected null object: hardwareMap");
		}

		robot = new AarreRobot(hardwareMap, betterTelemetry, this);

		betterTelemetry.log("Initializing robot");

		// Wait for the driver to press PLAY
		waitForStart();

		robot.raiseRiser();

		betterTelemetry.log("Riser raised");

		// Wait until the driver presses STOP
		//noinspection StatementWithEmptyBody
		while (opModeIsActive()) {

		}

	}

}
