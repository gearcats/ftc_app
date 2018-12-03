package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.AarreRobot;
import org.firstinspires.ftc.teamcode.aarre.src.AarreTelemetry;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for AarreRobot class
 * <p>
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Robot Unit Tests", group = "Aarre")
@Disabled
public class AarreRobotUnitTests extends LinearOpMode {

	/**
	 * Test AarreRobot
	 * <p>
	 * We can't break this up into different methods because the tests depend on overriding the FTC
	 * runOpMode() method. Properties inherited from LinearOpMode include: - hardwareMap -
	 * telemetry
	 */

	AarreRobot robot;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Test
	final void testConstructor() {

		try {
			robot = new AarreRobot(this);
		} catch (AssertionError e) {
			//
		}

	}

	@Test
	final void testTelemetryExists() {

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
