package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.DriveMotors;
import org.firstinspires.ftc.teamcode.aarre.src.PowerMagnitude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DriveMotorsUnitTests extends LinearOpMode {

	private final Logger      javaLog = Logger.getLogger(this.getClass().getName());
	private       DriveMotors motors;

	@Test
	void drive() {
	}

	@Test
	void getCycleLengthInMilliseconds() {
	}

	@Test
	void getError() {
	}

	@Test
	void getPowerIncrementAbsolute() {
	}

	@Test
	void getPowerMagnitudeTolerance() {
	}

	@Test
	void getSteer() {
	}

	@Test
	void gyroDrive() {
	}

	@Test
	void gyroHold() {
	}

	@Test
	void gyroTurn() {
	}

	@Test
	void isOnHeading() {
	}

	@Test
	void rampPowerTo() {
	}

	@Test
	void rampPowerTo1() {
	}

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {
	}

	@Test
	void setCycleLengthInMilliseconds() {
	}

	@Test
	void setPowerIncrement() {

	}

	@Test
	void setPowerIncrement1() {
	}

	@Test
	void setPowerMagnitudeTolerance() {
		PowerMagnitude powerMagnitudeTolerance = new PowerMagnitude(0.0001);
		motors.setPowerMagnitudeTolerance(powerMagnitudeTolerance);
	}

	@BeforeEach
	void setUp() {
		motors = new DriveMotors(this);
	}

	@Test
	void testNewAarreArmObjectNotNull() {
		assertNotNull(motors);
	}
}