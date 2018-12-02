package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.src.AarreRobot;
import org.firstinspires.ftc.teamcode.src.AarreTelemetry;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Autonomously raise the arm
 */

@SuppressWarnings("unused")
@Autonomous(name = "Aarre Autonomous Arm Raise", group = "Aarre")
@Disabled
public class AarreAutonomousArmRaise extends LinearOpMode {

	private AarreTelemetry betterTelemetry;
	private AarreRobot     robot;

	private final XLogger log = XLoggerFactory.getXLogger("TEMP");

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

		betterTelemetry.log("-- Raising arm --");

		try {
			robot.raiseArm();
		} catch (NoSuchMethodException e) {
			// log.error(e.toString());
		}

		betterTelemetry.log("-- Arm raised --");

		// Wait until the driver presses STOP
		//noinspection StatementWithEmptyBody
		while (opModeIsActive()) {

		}

	}

}
