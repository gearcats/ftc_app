package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * A pair of motors used for driving the robot
 */
public class AarreDrive {

	AarreTelemetry telemetry;
	AarreMotor     leftMotor;
	AarreMotor     rightMotor;
	HardwareMap    hardwareMap;

	public AarreDrive(final LinearOpMode opMode) {
		this.telemetry = new AarreTelemetry(opMode.telemetry);
		if (telemetry == null) {
			throw new AssertionError("Unexpected null object: telemetry");
		}

		this.hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			throw new AssertionError("Unexpected null object: hardwareMap");
		}

		leftMotor = new AarreMotor(opMode, "left");
		rightMotor = new AarreMotor(opMode, "right");
	}

}
