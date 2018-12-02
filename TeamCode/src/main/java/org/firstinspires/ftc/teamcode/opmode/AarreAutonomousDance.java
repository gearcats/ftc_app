package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.src.AarrePowerMagnitude;
import org.firstinspires.ftc.teamcode.src.AarreRobot;
import org.firstinspires.ftc.teamcode.src.AarreTelemetry;
// import org.slf4j.ext.XLogger;
// import org.slf4j.ext.XLoggerFactory;

/**
 * This file contains Aarre's experimental code to make the robot "dance" autonomously (that it, to make it exercise all
 * of its functions).
 * <p>
 * To avoid issuing an error on the phones, any OpMode class must be declared public.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
@Autonomous(name = "Aarre Autonomous Dance", group = "Aarre")
public class AarreAutonomousDance extends LinearOpMode {

	private AarreTelemetry betterTelemetry;
	private AarreRobot     robot;

	//static XLogger log;

	public AarreAutonomousDance() {
		//log = XLoggerFactory.getXLogger(getClass());
	}

	/**
	 * Properties and methods inherited from LinearOpMode include:
	 * <p>
	 * hardwareMap opModeIsActive telemetry
	 */
	@Override
	public final void runOpMode() {

		// log.entry();

		// 'telemetry' comes from FTC....
		// It is only available in runOpMode

		if (telemetry == null)
			throw new AssertionError("Unexpected null object: telemetry");
		betterTelemetry = new AarreTelemetry(telemetry);

		// 'hardwareMap comes from FTC....
		// It is only available in runOpMode

		if (hardwareMap == null)
			throw new AssertionError("Unexpected null object: hardwareMap");
		robot = new AarreRobot(this);

		// log.info("Initializing robot");

		// Wait for the driver to press PLAY
		waitForStart();

		// log.info("Starting play");

		final AarrePowerMagnitude drivePowerMagnitude = new AarrePowerMagnitude(0.5);
		// log.debug("Set drive power magnitude");
		final AarrePowerMagnitude turnPowerMagnitude = new AarrePowerMagnitude(0.5);
		// log.debug("Set turn power magnitude");
		final double inches  = 12.0;
		final double timeout = 5.0;

		// log.debug("Starting to drive");

		try {

			robot.drive(drivePowerMagnitude, inches, inches, timeout);
			robot.drive(drivePowerMagnitude, -inches, -inches, timeout);
			robot.drive(turnPowerMagnitude, inches, -inches, timeout);
			robot.drive(turnPowerMagnitude, -inches, inches, timeout);

			robot.raiseArm();
			robot.lowerArm();

			robot.raiseRiser();
			robot.lowerRiser();

			robot.lowerHook();
			robot.raiseHook();

		} catch (NoSuchMethodException e) {
			// log.error(e.toString());
		}
	}

}