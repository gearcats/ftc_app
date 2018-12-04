package org.firstinspires.ftc.teamcode.aarre.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.Robot;
import org.firstinspires.ftc.teamcode.aarre.src.Telemetry;

import java.util.logging.Logger;

/**
 * This file contains Aarre's experimental code to autonomously put the robot in
 * the state that it should be in at the end of the user period just before
 * latching to the lander. This includes:
 * <p>
 * 1. The arm must be down
 * 2. The riser must be up
 * 3. The hook must be up
 * <p>
 * To avoid issuing an error on the phones, any OpMode class must be
 * declared public.
 */

@Autonomous(name = "Aarre Autonomous Endgame", group = "Aarre")
@Disabled
public class AarreAutonomousEndgame extends LinearOpMode {

	private Telemetry betterTelemetry;
	private Robot     robot;


	private final Logger log = Logger.getLogger(this.getClass().getName());

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

		try {
			robot.readyForAutonomousEndgame();
		} catch (NoSuchMethodException e) {
			log.severe(e.toString());
		}

		betterTelemetry.log("Reset complete - robot is ready for autonomous mode");

		// Wait until the driver presses STOP
		//noinspection StatementWithEmptyBody
		while (opModeIsActive()) {

		}

	}

}
