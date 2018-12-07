package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class MotorTorqueNADO extends Motor implements ConcreteMotorInterface {

	private static final NonNegativeDouble TORQUENADO_REVOLUTIONS_PER_MINUTE = new NonNegativeDouble(100);

	private static final NonNegativeDouble TORQUENADO_TICKS_PER_REVOLUTION = new NonNegativeDouble(1440);
	private final        HardwareMap       hardwareMap;
	private final        DcMotor           motor;
	private final        LinearOpMode      opMode;
	static private       TelemetryPlus     telemetry;


	/**
	 * No-argument constructor for testing.
	 */
	public MotorTorqueNADO() {
		hardwareMap = null;
		motor = null;
		opMode = null;
	}

	public MotorTorqueNADO(LinearOpMode opMode, String motorName) {

		this.opMode = opMode;

		telemetry = new TelemetryPlus(opMode.telemetry);

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

	@Override
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

	@Override
	public NonNegativeDouble getRevolutionsPerMinute() {
		return TORQUENADO_REVOLUTIONS_PER_MINUTE;
	}

	@Override
	public NonNegativeDouble getRevolutionsPerMinute(PowerMagnitude powerMagnitude) {
		return new NonNegativeDouble(getRevolutionsPerMinute().doubleValue() * powerMagnitude.doubleValue());
	}

	@Override
	public NonNegativeDouble getTicksPerRevolution() {
		return TORQUENADO_TICKS_PER_REVOLUTION;
	}

	@Override
	public void setPowerVector(PowerVector targetVector) {
		motor.setPower(targetVector.doubleValue());
	}

}
