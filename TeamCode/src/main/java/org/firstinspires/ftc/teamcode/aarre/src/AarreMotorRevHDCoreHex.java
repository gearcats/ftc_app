package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AarreMotorRevHDCoreHex extends AarreMotor implements ConcreteMotorInterface {

	static final double REV_CORE_HEX_REVOLUTIONS_PER_MINUTE = 72;
	static final double REV_CORE_HEX_TICKS_PER_REVOLUTION = 224;

	private AarreMotorRevHDCoreHex(LinearOpMode opMode, String motorName) {
		super(opMode, motorName);
	}

	public static AarreMotorRevHDCoreHex createAarreMotorRevHDCoreHex(LinearOpMode opMode, String
			motorName) {
		AarreMotorRevHDCoreHex motor = new AarreMotorRevHDCoreHex(opMode, motorName);
		return motor;
	}


	public double getRevolutionsPerMinute() {
		return REV_CORE_HEX_REVOLUTIONS_PER_MINUTE;
	}

	public double getRevolutionsPerMinute(AarrePowerMagnitude powerMagnitude) {
		return getRevolutionsPerMinute() * powerMagnitude.asDouble();
	}

	public double getTicksPerRevolution() {
		return REV_CORE_HEX_TICKS_PER_REVOLUTION;
	}

}
