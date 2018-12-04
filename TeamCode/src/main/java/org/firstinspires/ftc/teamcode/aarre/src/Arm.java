package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.logging.Logger;

public class Arm {

	private static final PowerMagnitude DEFAULT_POWER_MAGNITUDE        = new PowerMagnitude(0.5);
	private static final double         SECONDS_TO_RUN_DEFAULT         = 1.0;
	private static final double         SECONDS_BEFORE_TIMEOUT_DEFAULT = 0.1;

	private Motor        motor;
	private Telemetry    telemetry;
	private HardwareMap  hardwareMap;
	private LinearOpMode opMode;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	private double currentPosition;

	/**
	 * This empty constructor is useful for testing.
	 */
	public Arm() {

	}

	/**
	 * Construct an instance of AarreArm with telemetry.
	 *
	 * @param nameOfRiserMotor
	 * 		The name of the motor that controls the riser.
	 * @param hardwareMap
	 * 		The hardware map upon which the motor may be found.
	 * @param telemetry
	 * 		An instance of AarreTelemetry to associate with the
	 */
	public Arm(final LinearOpMode opMode, final String nameOfArmMotor) {

		this.opMode = opMode;

		this.telemetry = new Telemetry(opMode.telemetry);

		/*
		  hardwareMap will be null if we are running off-robot, but for testing purposes it is
		  still helpful to instantiate this object (rather than throwing an exception, for
		  example).
		 */
		hardwareMap = opMode.hardwareMap;
		if (hardwareMap == null) {
			motor = null;
		}
		else {
			motor = MotorRevHDCoreHex.createAarreMotorRevHDCoreHex(opMode, nameOfArmMotor);
		}

		motor.rampToPower(new PowerVector(0.0));
		motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		motor.setDirection(DcMotorSimple.Direction.FORWARD);  // Positive power raises arm
		motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

	}

	/**
	 * Lower the arm by the default method.
	 */
	public final void lower() throws NoSuchMethodException {
		lowerByRamp();
	}

	private void lowerByRamp() throws NoSuchMethodException {
		PowerVector     powerVector           = new PowerVector(DEFAULT_POWER_MAGNITUDE, -1);
		PositiveInteger numberOfTicksToRotate = new PositiveInteger(120); // TODO: Eliminate magic #
		motor.rampToEncoderTicks(powerVector, numberOfTicksToRotate, SECONDS_BEFORE_TIMEOUT_DEFAULT);
	}

	/**
	 * Raise the arm using the default method.
	 */
	public final void raise() throws NoSuchMethodException {
		raiseByRamp();
	}

	private void raiseByRamp() throws NoSuchMethodException {
		PowerVector powerVector = new PowerVector(DEFAULT_POWER_MAGNITUDE, PowerVector.FORWARD);
		PositiveInteger numberOfTicksToRotate = new PositiveInteger(120); // TODO: Eliminate magic #
		motor.rampToEncoderTicks(powerVector, numberOfTicksToRotate, SECONDS_BEFORE_TIMEOUT_DEFAULT);
	}

}
