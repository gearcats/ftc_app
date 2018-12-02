package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class AarreDriveMotor extends AarreMotorTorqueNADO implements AarreMotorInterface {

	/*
	 * This is 1.0 for direct-drive wheels
	 */
	private static final double DRIVE_GEAR_REDUCTION = 1.0;

	/*
	 * We use the diameter for calculating circumference
	 */
	private static final double WHEEL_DIAMETER_INCHES = 5.5;

	private final XLogger log = XLoggerFactory.getXLogger("TEMP");

	public AarreDriveMotor(LinearOpMode opMode, String motorName) {
		super(opMode, motorName);
	}

	public double getTicksPerInch() {
		return (getTicksPerRevolution() * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math
				.PI);
	}


}
