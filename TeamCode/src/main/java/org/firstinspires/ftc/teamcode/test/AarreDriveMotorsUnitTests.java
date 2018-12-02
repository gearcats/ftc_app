package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.src.AarreDriveMotors;
import org.firstinspires.ftc.teamcode.src.AarrePowerMagnitude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AarreDriveMotorsUnitTests extends LinearOpMode {

	private AarreDriveMotors motors;

	private final XLogger log = XLoggerFactory.getXLogger("TEMP");


	@BeforeEach
	void setUp() {
		motors = new AarreDriveMotors(this);
	}

	@Test
	void testNewAarreArmObjectNotNull() {
		assertNotNull(motors);
	}

	@Test
	void getPowerIncrementAbsolute() {
	}

	@Test
	void getCycleLengthInMilliseconds() {
	}

	@Test
	void getPowerMagnitudeTolerance() {
	}

	@Test
	void getSteer() {
	}

	@Test
	void drive() {
	}

	@Test
	void getError() {
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

	@Test
	void setPowerIncrement() {

	}

	@Test
	void setPowerIncrement1() {
	}

	@Test
	void setCycleLengthInMilliseconds() {
	}

	@Test
	void setPowerMagnitudeTolerance() {
		AarrePowerMagnitude powerMagnitudeTolerance = new AarrePowerMagnitude(0.0001);
		motors.setPowerMagnitudeTolerance(powerMagnitudeTolerance);
	}

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {
	}
}