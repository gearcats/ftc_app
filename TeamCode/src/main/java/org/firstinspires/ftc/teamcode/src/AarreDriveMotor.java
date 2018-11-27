package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AarreDriveMotor extends AarreMotorTorqueNADO {

	/*
	 * This is 1.0 for direct-drive wheels
	 */
	private static final double DRIVE_GEAR_REDUCTION = 1.0;

	/*
	 * We use the diameter for calculating circumference
	 */
	private static final double WHEEL_DIAMETER_INCHES = 5.5;

	public AarreDriveMotor(LinearOpMode opMode, String motorName) {
		super(opMode, motorName);
	}

	public double getTicksPerInch() {
		return (getTicksPerRevolution() * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math
				.PI);
	}


}
