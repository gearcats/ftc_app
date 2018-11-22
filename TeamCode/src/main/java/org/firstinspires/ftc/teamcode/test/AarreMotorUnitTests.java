package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AarreRobot;
import org.firstinspires.ftc.teamcode.AarreTelemetry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Autonomous(name = "Aarre Motor Unit Tests", group = "Aarre")
public class AarreMotorUnitTests extends LinearOpMode {

	/**
	 * Test AarreRobot
	 * <p>
	 * We can't break this up into different methods because the tests depend on overriding the
	 * FTC runOpMode() method. Properties inherited from LinearOpMode include:
	 * - hardwareMap
	 * - telemetry
	 */

	AarreRobot robot;

	@Test
	public final void testConstructor() {

		try {
			robot = new AarreRobot(this);
		} catch (AssertionError e) {
			//
		}

	}

	@Test
	public final void testTelemetryExists() {

		assertNotNull(telemetry);
		AarreTelemetry aarreTelemetry = new AarreTelemetry(telemetry);
		assertNotNull(aarreTelemetry);

	}

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {

	}

}