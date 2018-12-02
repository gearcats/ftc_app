package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.src.AarreDriveMotor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

class AarreDriveMotorUnitTests extends LinearOpMode {

	AarreDriveMotor motor;

	private final XLogger log = XLoggerFactory.getXLogger(this.getClass().getName());

	@BeforeEach
	void setUp() {
		motor = new AarreDriveMotor(this, "left");
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getTicksPerInch() {
	}

	@Test
	void createAarreMotorTorqueNADO() {
	}

	@Test
	void getRevolutionsPerMinute() {
	}

	@Test
	void getRevolutionsPerMinute1() {
	}

	@Test
	void setRevolutionsPerMinute() {
	}

	@Test
	void setTicksPerRevolution() {
	}

	@Test
	void getMillisecondsPerCycle() {
	}

	@Test
	void getTicksPerMinute() {
	}

	@Test
	void getTicksPerMinute1() {
	}

	@Test
	void getTicksPerRevolution() {
	}

	@Test
	void getTicksPerSecond() {
	}

	@Test
	void getTicksPerSecond1() {
	}

	@Test
	void getTicksPerMillisecond() {
	}

	@Test
	void getTicksPerMillisecond1() {
	}

	@Test
	void getTicksPerCycle() {
	}

	@Test
	void getTicksPerCycle1() {
	}

	@Test
	void getCurrentTickNumber() {
	}

	@Test
	void getPowerMagnitudeIncrementPerCycle() {
	}

	@Test
	void getPowerVectorCurrent() {
	}

	@Test
	void getPowerVectorNew() {
	}

	@Test
	void getPower() {
	}

	@Test
	void getTickNumberToStartRampDown() {
	}

	@Test
	void isRampUpToEncoderTicksDone() {
	}

	@Test
	void isRampDownToEncoderTicksRunning() {
	}

	@Test
	void isBusy() {
	}

	@Test
	void rampToEncoderTicks() {
	}

	@Test
	void rampToPower() {
	}

	@Test
	void rampToPower1() {
	}

	@Test
	void runByRevolutions() {
	}

	@Test
	void runByTime() {
	}

	@Test
	void runUntilStalled() {
	}

	@Test
	void setDirection() {
	}

	@Test
	void setMode() {
	}

	@Test
	void setPowerMagnitudeTolerance() {
	}

	@Test
	void setPowerMagnitudeIncrementPerCycle() {
	}

	@Test
	void setPowerVector() {
	}

	@Test
	void setStallDetectionToleranceInTicks() {
	}

	@Test
	void setStallTimeLimitInMilliseconds() {
	}

	@Test
	void setStallTimeLimitInSeconds() {
	}

	@Test
	void setTargetPosition() {
	}

	@Test
	void setZeroPowerBehavior() {
	}

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {
	}
}