package org.firstinspires.ftc.teamcode.aarre.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.PowerMagnitude;
import org.firstinspires.ftc.teamcode.aarre.src.Robot;
import org.firstinspires.ftc.teamcode.aarre.src.Telemetry;

import java.util.logging.Logger;

/**
 * This file contains Aarre's experimental code to test that the robot drive autonomously.
 * <p>
 * To avoid issuing an error on the phones, any OpMode class must be declared public.
 */
@Autonomous(name = "Aarre Autonomous Drive Test", group = "Aarre")
@Disabled
public class AarreAutonomousDriveTest extends LinearOpMode {

	private Telemetry betterTelemetry;
	private Robot     robot;

	private final Logger log = Logger.getLogger(this.getClass().getName());

	public AarreAutonomousDriveTest() {
	}

	/**
	 * Properties and methods inherited from LinearOpMode include:
	 * <p>
	 * hardwareMap
	 * opModeIsActive
	 * telemetry
	 */
	@Override
	public final void runOpMode() {

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

		// Wait for the driver to press PLAY
		waitForStart();

		final PowerMagnitude driveSpeed = new PowerMagnitude(0.5);
		final PowerMagnitude turnSpeed  = new PowerMagnitude(0.5);
		final double         inches     = 12.0;
		final double         timeout    = 5.0;

		try {
			robot.drive(driveSpeed, inches, inches, timeout);
			robot.drive(driveSpeed, -inches, -inches, timeout);
			robot.drive(turnSpeed, inches, -inches, timeout);
			robot.drive(turnSpeed, -inches, inches, timeout);
		} catch (NoSuchMethodException e) {
			log.severe(e.toString());
		}


	}

}