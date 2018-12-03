package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.logging.Logger;

public class AarreMotorTorqueNADO extends AarreMotor implements AarreMotorInterface {

	private static final double TORQUENADO_REVOLUTIONS_PER_MINUTE = 100;

	private static final int TORQUENADO_TICKS_PER_REVOLUTION = 1440;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	public AarreMotorTorqueNADO(LinearOpMode opMode, String motorName) {
		super(opMode, motorName);
	}

	public static AarreMotorTorqueNADO createAarreMotorTorqueNADO(LinearOpMode opMode, String
			motorName) {
		AarreMotorTorqueNADO motor = new AarreMotorTorqueNADO(opMode, motorName);
		return motor;
	}

	public double getRevolutionsPerMinute() {
		return TORQUENADO_REVOLUTIONS_PER_MINUTE;
	}

	public double getRevolutionsPerMinute(AarrePowerMagnitude powerMagnitude) {
		return getRevolutionsPerMinute() * powerMagnitude.asDouble();
	}

	public double getTicksPerRevolution() {
		return TORQUENADO_TICKS_PER_REVOLUTION;
	}

}
