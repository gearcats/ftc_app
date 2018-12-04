package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class AarreDriveMotor extends AarreMotorTorqueNADO implements AarreMotorInterface, ConcreteMotorInterface {

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

	@Override
	public DcMotor getMotor() {
		return (DcMotor) motorTorqueNADO;
	}
	public double getTicksPerInch() {
		return (getTicksPerRevolution() * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math
				.PI);
	}

	@Override
	public void setPowerVector(AarrePowerVector targetVector) {
		motorTorqueNADO.setPowerVector(targetVector);
	}


}
