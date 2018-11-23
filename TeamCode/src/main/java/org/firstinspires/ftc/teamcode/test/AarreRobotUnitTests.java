package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AarreRobot;
import org.firstinspires.ftc.teamcode.AarreTelemetry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for AarreRobot class
 *
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 *
 */
@Autonomous(name = "Aarre Robot Unit Tests", group = "Aarre")
@Disabled
public class AarreRobotUnitTests extends LinearOpMode {

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
