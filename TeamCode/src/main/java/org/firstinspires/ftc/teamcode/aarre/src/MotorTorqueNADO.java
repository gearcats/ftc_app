package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class MotorTorqueNADO extends Motor implements ConcreteMotorInterface {

	private static final double TORQUENADO_REVOLUTIONS_PER_MINUTE = 100;

	private static final int TORQUENADO_TICKS_PER_REVOLUTION = 1440;

	static MotorTorqueNADO motorTorqueNADO;

	private final  DcMotor      motor;
	static private Telemetry    telemetry;
	private final  LinearOpMode opMode;
	private final  HardwareMap  hardwareMap;


	public MotorTorqueNADO(LinearOpMode opMode, String motorName) {

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
			motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		}

	}

	public static MotorTorqueNADO createAarreMotorTorqueNADO(LinearOpMode opMode, String
			motorName) {
		motorTorqueNADO = new MotorTorqueNADO(opMode, motorName);
		return motorTorqueNADO;
	}

	public final int getCurrentTickNumber() {
		return motor.getCurrentPosition();
	}

	public final HardwareMap getHardwareMap() {
		return hardwareMap;
	}

	@Override
	public final DcMotor getMotor() {
		return motor;
	}

	@Override
	public LinearOpMode getOpMode() {
		return opMode;
	}

	public double getRevolutionsPerMinute() {
		return TORQUENADO_REVOLUTIONS_PER_MINUTE;
	}

	public double getRevolutionsPerMinute(PowerMagnitude powerMagnitude) {
		return getRevolutionsPerMinute() * powerMagnitude.doubleValue();
	}

	public double getTicksPerRevolution() {
		return TORQUENADO_TICKS_PER_REVOLUTION;
	}

	public void setPowerVector(PowerVector targetVector) {
		motor.setPower(targetVector.doubleValue());
	}

}
