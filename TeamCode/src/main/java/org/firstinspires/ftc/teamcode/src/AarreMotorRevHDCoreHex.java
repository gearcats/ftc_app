package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.logging.Logger;

public class AarreMotorRevHDCoreHex extends AarreMotor implements AarreMotorInterface {

	/*
	 * Properties of the motor
	 *
	 * How many revolutions the shaft turns in one minute under no load
	 * For TorqueNADO motors, this is 100
	 * For REV HD Core Hex motors, it is 72 or 32.8 depending on gearing.
	 */
	static final double REV_CORE_HEX_REVOLUTIONS_PER_MINUTE = 72;

	/*
	 * How many encoder ticks in one revolution of the motor shaft
	 * For TETRIX motors and TorqueNADO motors, this is 1440
	 * For the REV HD Hex motor, it is 2240 (or 224???)
	 */
	static final double REV_CORE_HEX_TICKS_PER_REVOLUTION = 224;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	private AarreMotorRevHDCoreHex(LinearOpMode opMode, String motorName) {
		super(opMode, motorName);
	}

	public static AarreMotorRevHDCoreHex createAarreMotorRevHDCoreHex(LinearOpMode opMode, String
			motorName) {
		AarreMotorRevHDCoreHex motor = new AarreMotorRevHDCoreHex(opMode, motorName);
		motor.setRevolutionsPerMinute(REV_CORE_HEX_REVOLUTIONS_PER_MINUTE);
		motor.setTicksPerRevolution(REV_CORE_HEX_TICKS_PER_REVOLUTION);
		return motor;
	}


}
