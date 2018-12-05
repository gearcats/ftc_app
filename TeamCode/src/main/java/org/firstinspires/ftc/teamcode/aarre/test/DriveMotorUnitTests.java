package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.DriveMotor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

class DriveMotorUnitTests extends LinearOpMode {

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());
	DriveMotor motor;

	@Test
	void createAarreMotorTorqueNADO() {
	}

	@Test
	void getCurrentTickNumber() {
	}

	@Test
	void getMillisecondsPerCycle() {
	}

	@Test
	void getPower() {
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
	void getRevolutionsPerMinute() {
	}

	@Test
	void getRevolutionsPerMinute1() {
	}

	@Test
	void getTickNumberToStartRampDown() {
	}

	@Test
	void getTicksPerCycle() {
	}

	@Test
	void getTicksPerCycle1() {
	}

	@Test
	void getTicksPerInch() {
	}

	@Test
	void getTicksPerMillisecond() {
	}

	@Test
	void getTicksPerMillisecond1() {
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
	void isBusy() {
	}

	@Test
	void isRampDownToEncoderTicksRunning() {
	}

	@Test
	void isRampUpToEncoderTicksDone() {
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

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {
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
	void setPowerMagnitudeIncrementPerCycle() {
	}

	@Test
	void setPowerMagnitudeTolerance() {
	}

	@Test
	void setPowerVector() {
	}

	@Test
	void setRevolutionsPerMinute() {
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
	void setTicksPerRevolution() {
	}

	@BeforeEach
	void setUp() {
		motor = new DriveMotor(this, "left");
	}

	@Test
	void setZeroPowerBehavior() {
	}

	@AfterEach
	void tearDown() {
	}
}