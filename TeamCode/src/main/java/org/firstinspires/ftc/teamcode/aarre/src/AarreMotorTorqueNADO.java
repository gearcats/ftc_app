package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class AarreMotorTorqueNADO extends AarreMotor implements ConcreteMotorInterface {

	private static final double TORQUENADO_REVOLUTIONS_PER_MINUTE = 100;

	private static final int TORQUENADO_TICKS_PER_REVOLUTION = 1440;

	static AarreMotorTorqueNADO motorTorqueNADO;

	private final  DcMotor        motor;
	static private AarreTelemetry telemetry;
	private final  LinearOpMode   opMode;
	private final  HardwareMap    hardwareMap;


	public AarreMotorTorqueNADO(LinearOpMode opMode, String motorName) {

		this.opMode = opMode;

		telemetry = new AarreTelemetry(opMode.telemetry);

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

	public static AarreMotorTorqueNADO createAarreMotorTorqueNADO(LinearOpMode opMode, String
			motorName) {
		motorTorqueNADO = new AarreMotorTorqueNADO(opMode, motorName);
		return motorTorqueNADO;
	}

	public final int getCurrentTickNumber() {
		return motor.getCurrentPosition();
	}

	public final HardwareMap getHardwareMap() {
		return hardwareMap;
	}

	@Override
	public DcMotor getMotor() {
		return motor;
	}

	@Override
	public LinearOpMode getOpMode() {
		return opMode;
	}

	public double getRevolutionsPerMinute() {
		return TORQUENADO_REVOLUTIONS_PER_MINUTE;
	}

	public double getRevolutionsPerMinute(AarrePowerMagnitude powerMagnitude) {
		return getRevolutionsPerMinute() * powerMagnitude.doubleValue();
	}

	public double getTicksPerRevolution() {
		return TORQUENADO_TICKS_PER_REVOLUTION;
	}

	public void setPowerVector(AarrePowerVector targetVector) {
		motor.setPower(targetVector.doubleValue());
	}

}
