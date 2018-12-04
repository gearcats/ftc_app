package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorRevHDCoreHex extends Motor implements ConcreteMotorInterface {

	static final double REV_CORE_HEX_REVOLUTIONS_PER_MINUTE = 72;
	static final double REV_CORE_HEX_TICKS_PER_REVOLUTION = 224;

	private final  DcMotor           motor;
	static private Telemetry         telemetry;
	private final  LinearOpMode      opMode;
	private final  HardwareMap       hardwareMap;
	static         MotorRevHDCoreHex motorRevHDCoreHex;

	public MotorRevHDCoreHex(LinearOpMode opMode, final String motorName) {

		this.opMode = opMode;

		telemetry = new Telemetry(opMode.telemetry);

		/*
		  hardwareMap will be null if we are running off-robot, but for testing purposes it is
		  still helpful to instantiate this object (rather than throwing an exception, for
		  example).
		 */
		hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			motor = null;
		} else {
			motor = hardwareMap.get(DcMotor.class, motorName);
			getMotor().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		}

	}

	public static MotorRevHDCoreHex createAarreMotorRevHDCoreHex(LinearOpMode opMode, String
			motorName) {
		motorRevHDCoreHex = new MotorRevHDCoreHex(opMode, motorName);
		return motorRevHDCoreHex;
	}

	public final int getCurrentTickNumber() {
		return motor.getCurrentPosition();
	}

	@Override
	public final DcMotor getMotor() {
		return motor;
	}

	@Override
	public LinearOpMode getOpMode() {
		return opMode;
	}

	public final HardwareMap getHardwareMap() {
		return hardwareMap;
	}

	public double getRevolutionsPerMinute() {
		return REV_CORE_HEX_REVOLUTIONS_PER_MINUTE;
	}

	public double getRevolutionsPerMinute(PowerMagnitude powerMagnitude) {
		return getRevolutionsPerMinute() * powerMagnitude.doubleValue();
	}

	public double getTicksPerRevolution() {
		return REV_CORE_HEX_TICKS_PER_REVOLUTION;
	}

	public final void setPowerVector(final PowerVector powerVector) {
		motor.setPower(powerVector.doubleValue());
	}
}
