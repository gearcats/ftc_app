package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AarreMotorTorqueNADO extends AarreMotor implements AarreMotorInterface {

	private static final double TORQUENADO_REVOLUTIONS_PER_MINUTE = 100;

	private static final int TORQUENADO_TICKS_PER_REVOLUTION = 1440;

	public AarreMotorTorqueNADO(LinearOpMode opMode, String motorName) {
		super(opMode, motorName);
	}

	public static AarreMotorTorqueNADO createAarreMotorTorqueNADO(LinearOpMode opMode, String
			motorName) {
		AarreMotorTorqueNADO motor = new AarreMotorTorqueNADO(opMode, motorName);
		motor.setRevolutionsPerMinute(TORQUENADO_REVOLUTIONS_PER_MINUTE);
		motor.setTicksPerRevolution(TORQUENADO_TICKS_PER_REVOLUTION);
		return motor;
	}
}
