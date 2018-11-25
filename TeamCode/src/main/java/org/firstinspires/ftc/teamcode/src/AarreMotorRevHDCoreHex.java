package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AarreMotorRevHDCoreHex extends AarreMotor {

	/*
	 * Properties of the motor
	 *
	 * How many revolutions the shaft turns in one minute under no load
	 * For TorqueNADO motors, this is 100
	 * For REV HD Core Hex motors, it is 72 or 32.8 depending on gearing.
	 */
	private static final double REVOLUTIONS_PER_MINUTE = 100;

	/*
	 * How many encoder ticks in one revolution of the motor shaft
	 * For TETRIX motors and TorqueNADO motors, this is 1440
	 * For the REV HD Hex motor, it is 2240 (or 224???)
	 */
	private static final int TICKS_PER_REVOLUTION = 224;

	public AarreMotorRevHDCoreHex(LinearOpMode opMode, String motorName) {
		super(opMode, motorName);
	}
}
