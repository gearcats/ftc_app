package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.src.AarreRobot;
import org.firstinspires.ftc.teamcode.src.AarreTelemetry;

import java.util.logging.Logger;

/**
 * Autonomously raise the riser
 */

@Autonomous(name = "Aarre Autonomous Riser Raise", group = "Aarre")
@Disabled
public class AarreAutonomousRiserRaise extends LinearOpMode {

	private AarreTelemetry betterTelemetry;
	private AarreRobot     robot;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

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

		robot = new AarreRobot(this);

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
