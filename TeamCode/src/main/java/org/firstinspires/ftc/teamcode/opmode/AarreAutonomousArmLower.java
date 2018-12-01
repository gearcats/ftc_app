package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.src.AarreRobot;
import org.firstinspires.ftc.teamcode.src.AarreTelemetry;

import java.util.logging.Logger;

/**
 * Autonomously lower the arm.
 */

@Autonomous(name = "Aarre Autonomous Arm Lower", group = "Aarre")
@Disabled
public class AarreAutonomousArmLower extends LinearOpMode {

	private AarreTelemetry betterTelemetry;
	private AarreRobot     robot;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());
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

		betterTelemetry.log("-- Lowering arm --");

		robot.lowerArm();

		betterTelemetry.log("-- Arm lowered --");

		// Wait until the driver presses STOP
		//noinspection StatementWithEmptyBody
		while (opModeIsActive()) {

		}

	}

}
