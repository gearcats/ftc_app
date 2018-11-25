package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AarreMotorTorqueNADO extends AarreMotor {

	/*
	 * How many encoder ticks in one revolution of the motor shaft
	 * For TETRIX motors and TorqueNADO motors, this is 1440
	 * For the REV HD Hex motor, it is 2240
	 */
	private static final int TICKS_PER_REVOLUTION = 1440;

	public AarreMotorTorqueNADO(LinearOpMode opMode, String motorName) {
		super(opMode, motorName);
	}
}
