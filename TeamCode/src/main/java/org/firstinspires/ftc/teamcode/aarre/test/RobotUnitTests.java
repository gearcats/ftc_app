package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.Robot;
import org.firstinspires.ftc.teamcode.aarre.src.TelemetryPlus;
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
public class RobotUnitTests extends LinearOpMode {

	/**
	 * Test AarreRobot
	 * <p>
	 * We can't break this up into different methods because the tests depend on overriding the FTC
	 * runOpMode() method. Properties inherited from LinearOpMode include: - hardwareMap -
	 * telemetry
	 */

	Robot robot;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	@Test
	final void testConstructor() {

		try {
			robot = new Robot(this);
		} catch (AssertionError e) {
			//
		}

	}

	@Test
	final void testTelemetryExists() {

		assertNotNull(this.telemetry);
		TelemetryPlus telemetry = new TelemetryPlus(this.telemetry);
		assertNotNull(telemetry);

	}

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {

	}

}
