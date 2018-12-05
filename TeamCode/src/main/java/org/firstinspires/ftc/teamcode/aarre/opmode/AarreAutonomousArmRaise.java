package org.firstinspires.ftc.teamcode.aarre.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.Robot;
import org.firstinspires.ftc.teamcode.aarre.src.TelemetryPlus;

import java.util.logging.Logger;

/**
 * Autonomously raise the arm
 */

@Autonomous(name = "Aarre Autonomous Arm Raise", group = "Aarre")
public class AarreAutonomousArmRaise extends LinearOpMode {

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

		// Wait for the driver to press PLAY
		waitForStart();

		betterTelemetryPlus.log("-- Raising arm --");

		try {
			robot.raiseArm();
		} catch (NoSuchMethodException e) {
			log.severe(e.toString());
		}

		betterTelemetryPlus.log("-- Arm raised --");

		// Wait until the driver presses STOP
		//noinspection StatementWithEmptyBody
		while (opModeIsActive()) {

		}

	}

}
