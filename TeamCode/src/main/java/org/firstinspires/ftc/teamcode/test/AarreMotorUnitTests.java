package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AarreMotor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Autonomous(name = "Aarre Motor Unit Tests", group = "Aarre")
/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Disabled
public class AarreMotorUnitTests extends LinearOpMode {

	/**
	 * Test AarreRobot
	 * <p>
	 * We can't break this up into different methods because the tests depend on overriding the
	 * FTC runOpMode() method. Properties inherited from LinearOpMode include:
	 * - hardwareMap
	 * - telemetry
	 */

	AarreMotor motor;

	@BeforeEach
	public final void testConstructor() {
		motor = new AarreMotor(this, "left");
	}

	@Test
	public final void testGetProportionNew01() {
		double proportionPowerNew = motor.getProportionPowerNew(1.0, 0.0, 0.1);
		assertEquals(0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew02() {
		double proportionPowerNew = motor.getProportionPowerNew(-1.0, 0.0, 0.1);
		assertEquals(-0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew03() {
		double proportionPowerNew = motor.getProportionPowerNew(0.0, 1.0, 0.1);
		assertEquals(0.1, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew04() {
		double proportionPowerNew = motor.getProportionPowerNew(0.0, -1.0, 0.1);
		assertEquals(-0.1, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew05() {
		double proportionPowerNew = motor.getProportionPowerNew(1.0, -1.0, 0.1);
		assertEquals(0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew06() {
		double proportionPowerNew = motor.getProportionPowerNew(-1.0, 1.0, 0.1);
		assertEquals(-0.9, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew07() {
		double proportionPowerNew = motor.getProportionPowerNew(-1.0, -1.0, 0.1);
		assertEquals(-1.0, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew08() {
		double proportionPowerNew = motor.getProportionPowerNew(0.0, 0.0, 0.1);
		assertEquals(0.0, proportionPowerNew, "Wrong proportion power");
	}

	@Test
	public final void testGetProportionNew09() {
		double proportionPowerNew = motor.getProportionPowerNew(1.0, 1.0, 0.1);
		assertEquals(1.0, proportionPowerNew, "Wrong proportion power");
	}

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {

	}

}
