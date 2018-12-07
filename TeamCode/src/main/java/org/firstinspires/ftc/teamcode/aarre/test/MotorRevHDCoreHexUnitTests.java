package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.aarre.src.MotorRevHDCoreHex;
import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeDouble;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor Rev HD Core Hex Unit Tests", group = "Aarre")
@Disabled
public class MotorRevHDCoreHexUnitTests extends MotorUnitTests implements ConcreteMotorUnitTestsInterface {

	Logger            javaLog           = Logger.getLogger(this.getClass().getName());
	/**
	 * Test AarreRobot
	 * <p>
	 * We can't break this up into different methods because the tests depend on overriding the FTC runOpMode() method.
	 * Properties inherited from LinearOpMode include: - hardwareMap - telemetry
	 */

	MotorRevHDCoreHex motorRevHDCoreHex = new MotorRevHDCoreHex(this, "arm");

	@Override
	public MotorRevHDCoreHex getMotor() {
		return motorRevHDCoreHex;
	}


	@Test
	public final void testGetTicksPerMillisecond01() {
		double ticksPerMillisecond = motorRevHDCoreHex.getTicksPerMillisecond().doubleValue();
		assertEquals(0.2688, ticksPerMillisecond, 0.00001);
	}

	@Test
	public final void testGetTicksPerMinute01() {
		NonNegativeDouble ticksPerMinute = motorRevHDCoreHex.getTicksPerMinute();
		assertEquals(16128.0, ticksPerMinute.doubleValue());
	}

	@Test
	public final void testGetTicksPerSecond01() {
		double ticksPerSecond = motorRevHDCoreHex.getTicksPerSecond().doubleValue();
		assertEquals(268.8, ticksPerSecond);
	}

}
