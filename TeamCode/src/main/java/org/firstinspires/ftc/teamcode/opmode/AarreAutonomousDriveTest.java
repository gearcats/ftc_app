package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.src.AarrePowerMagnitude;
import org.firstinspires.ftc.teamcode.src.AarreRobot;
import org.firstinspires.ftc.teamcode.src.AarreTelemetry;
// import org.slf4j.ext.XLogger;
// import org.slf4j.ext.XLoggerFactory;

/**
 * This file contains Aarre's experimental code to test that the robot drive autonomously.
 * <p>
 * To avoid issuing an error on the phones, any OpMode class must be declared public.
 */
@Autonomous(name = "Aarre Autonomous Drive Test", group = "Aarre")
@Disabled
public class AarreAutonomousDriveTest extends LinearOpMode {

	private AarreTelemetry betterTelemetry;
	private AarreRobot     robot;

	// private final XLogger log = XLoggerFactory.getXLogger("TEMP");

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

		final AarrePowerMagnitude driveSpeed = new AarrePowerMagnitude(0.5);
		final AarrePowerMagnitude           turnSpeed  = new AarrePowerMagnitude(0.5);
		final double           inches     = 12.0;
		final double           timeout    = 5.0;

		try {
			robot.drive(driveSpeed, inches, inches, timeout);
			robot.drive(driveSpeed, -inches, -inches, timeout);
			robot.drive(turnSpeed, inches, -inches, timeout);
			robot.drive(turnSpeed, -inches, inches, timeout);
		} catch (NoSuchMethodException e) {
			// log.error(e.toString());
		}


	}

}