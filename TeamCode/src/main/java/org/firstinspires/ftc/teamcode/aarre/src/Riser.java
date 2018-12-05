package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.logging.Logger;

/**
 * This class represents the riser on the robot.
 */
public class Riser {


	/**
	 * These values affect the methods that move the riser by using the encoder to run until stall.
	 */
	private static final NonNegativeInteger DEFAULT_MILLISECONDS_STALL_TIME_WINDOW = new NonNegativeInteger(200);
	private static final PowerMagnitude     DEFAULT_POWER_MAGNITUDE                = new PowerMagnitude(1.0);
	/**
	 * These values affect the methods that move the riser by using the encoder to run a fixed number of revolutions.
	 */
	private static final NonNegativeDouble  DEFAULT_REVOLUTIONS_LOWER              = new NonNegativeDouble(7.0);
	private static final NonNegativeDouble  DEFAULT_REVOLUTIONS_RAISE              = new NonNegativeDouble(7.0);

	/**
	 * These values affects all methods that move the riser.
	 */
	private static final NonNegativeDouble  DEFAULT_SECONDS_TO_RUN_MAXIMUM = new NonNegativeDouble(7.0);
	private static final NonNegativeInteger DEFAULT_TICKS_STALL_TOLERANCE  = new NonNegativeInteger(15);
	private              double             currentPosition;
	private final        Logger             log                            = Logger.getLogger(this.getClass().getName());
	private              MotorTorqueNADO    motor;
	private              LinearOpMode       opMode;
	private              TelemetryPlus      telemetry;

	/**
	 * This empty constructor is useful for testing.
	 */
	public Riser() {

	}

	/**
	 * Construct an instance of AarreRiser with telemetry.
	 *
	 * @param nameOfRiserMotor
	 * 		The name of the motor that controls the riser.
	 * @param hardwareMap
	 * 		The hardware map upon which the motor may be found.
	 * @param telemetry
	 * 		An instance of AarreTelemetry to associate with the
	 */
	Riser(final HardwareMap hardwareMap, final String nameOfRiserMotor, final TelemetryPlus telemetry, final LinearOpMode opMode) {

		currentPosition = 0.5; // We have no idea where the riser is

		// Make sure there is a hardwareMap parameter
		if (hardwareMap == null) {
			throw new IllegalArgumentException("Unexpected null parameter: hardwareMap");
		}


		// Make sure there is a telemetry parameter
		if (telemetry == null) {
			throw new AssertionError("Unexpected null parameter: telemetry");
		}

		this.telemetry = telemetry;

		this.opMode = opMode;

		motor = new MotorTorqueNADO(opMode, nameOfRiserMotor);

		motor.rampToPower(new PowerVector(0.0));
		motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		motor.setDirection(DcMotorSimple.Direction.FORWARD);  // Positive power raises riser
		motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

	}

	/**
	 * Best guess at current position of the riser. (We can't know exactly where the riser is because the riser starts
	 * in an unknown physical position upon software initialization and does not store any state information.) A value
	 * of 0.0 indicates that we believe the riser is fully lowered, and a value of 1.0 indicates that we believe the
	 * riser is fully raised. The riser generally should be either fully lowered or fully raised (except when it is in
	 * motion), so values in the middle when the riser is stationary suggest uncertainty about where the riser really
	 * is.
	 */
	public double getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Lower the riser using the default method.
	 */
	public final void lower() throws NoSuchMethodException {

		log.info("Riser - lowering riser");
		lowerByRevolutions();
		log.info("Riser - riser lowered");

		currentPosition = 0.0;
	}

	/**
	 * Lower the riser by revolutions using default parameters
	 */
	private void lowerByRevolutions() throws NoSuchMethodException {
		lowerByRevolutions(DEFAULT_POWER_MAGNITUDE, DEFAULT_REVOLUTIONS_LOWER, DEFAULT_SECONDS_TO_RUN_MAXIMUM);
	}

	/**
	 * Lower the riser by a certain number of revolutions of the motor shaft
	 *
	 * @param powerMagnitude
	 * 		Apply this proportion of power to the motor. In this method (where we have already specified by the method
	 * 		name
	 * 		that the intent is to "lowerUntilStalled" the riser), we expect this value to be non-negative. It tells us
	 * 		how
	 * 		much power to apply, not which direction to apply it.
	 * @param numberOfRevolutions
	 * 		Turn the motor shaft this number of revolutions. Also always non-negative.
	 * @param secondsTimeout
	 * 		Shut off the motor after this many seconds regardless of whether it has reached the requested number of
	 * 		revolutions. The idea is to prevent burning out motors by stalling them for long periods of time or
	 * 		breaking
	 * 		other components by applying too much force to them for too long.
	 */
	private void lowerByRevolutions(final PowerMagnitude powerMagnitude, final NonNegativeDouble numberOfRevolutions,
	                                final NonNegativeDouble secondsTimeout) throws NoSuchMethodException {

		log.fine(String.format("Riser - Lower by revolutions, power: %f", powerMagnitude.doubleValue()));
		PowerVector powerVector = new PowerVector(powerMagnitude, PowerVector.REVERSE);
		motor.runByRevolutions(powerVector, numberOfRevolutions, secondsTimeout);

	}

	/**
	 * Lower the riser for the default amount of time.
	 */
	private void lowerByTime() {
		lowerByTime(DEFAULT_POWER_MAGNITUDE, DEFAULT_SECONDS_TO_RUN_MAXIMUM);
	}

	/**
	 * Lower the riser for a fixed amount of time.
	 *
	 * @param powerMagnitude
	 * 		Proportion of power to apply to motor. Must be non-negative. To raise the riser use raiseByTime().
	 * @param secondsToRun
	 * 		The number of seconds for which to raise the arm.
	 */
	private void lowerByTime(PowerMagnitude powerMagnitude, NonNegativeDouble secondsToRun) {

		PowerVector powerVector = new PowerVector(powerMagnitude, -1);

		motor.runByTime(powerVector, secondsToRun);

		currentPosition = 0.0;
	}

	/**
	 * Lower the riser to its downward position while avoiding stalling the riser motor
	 */
	private void lowerUntilStall() {
		motor.setStallTimeLimitInMilliseconds(DEFAULT_MILLISECONDS_STALL_TIME_WINDOW);
		motor.setStallDetectionToleranceInTicks(DEFAULT_TICKS_STALL_TOLERANCE);
		PowerVector powerVector = new PowerVector(DEFAULT_POWER_MAGNITUDE, PowerVector.REVERSE);
		motor.runUntilStalled(powerVector);
	}


	/**
	 * Raise the riser using the default method.
	 */
	public void raise() throws NoSuchMethodException {
		log.entering(this.getClass().getCanonicalName(), "raise");
		raiseByRevolutions();
		log.exiting(this.getClass().getCanonicalName(), "raise");

		/*
		 * Hold the riser motor at the top so that gravity will not gently pull it down.
		 */
		motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		currentPosition = 1.0;
	}

	/**
	 * Raise the riser by revolutions using default parameters.
	 */
	private void raiseByRevolutions() throws NoSuchMethodException {
		raiseByRevolutions(DEFAULT_POWER_MAGNITUDE, DEFAULT_REVOLUTIONS_RAISE, DEFAULT_SECONDS_TO_RUN_MAXIMUM);
	}

	/**
	 * Raise the riser by a certain number of motor shaft revolutions.
	 *
	 * @param powerMagnitude
	 * 		Apply this proportion of power to the motor. In this method (to "raiseUntilStalled" the riser), we expect
	 * 		this
	 * 		value to be non-negative.
	 * @param numberOfRevolutions
	 * 		Turn the motor shaft this number of revolutions.
	 * @param secondsTimeout
	 * 		Shut off the motor shuts off after this many seconds regardless of whether it has reached the requested
	 * 		number
	 * 		of revolutions. The idea is to prevent burning out motors by stalling them for long periods of time or
	 * 		breaking
	 * 		other components by applying too much force to them for too long.
	 */
	private void raiseByRevolutions(final PowerMagnitude powerMagnitude, final NonNegativeDouble numberOfRevolutions,
	                                final NonNegativeDouble secondsTimeout) throws NoSuchMethodException {

		log.entering(this.getClass().getCanonicalName(), "raiseByRevolutions");

		log.fine(String.format("Power magnitude: %f", powerMagnitude.doubleValue()));
		log.fine(String.format("Number of revolutions: %f", numberOfRevolutions.doubleValue()));
		log.fine(String.format("Seconds timeout: %f", secondsTimeout.doubleValue()));
		log.fine(String.format("Power magnitude: %f", powerMagnitude.doubleValue()));

		PowerVector powerVector = new PowerVector(powerMagnitude, PowerVector.FORWARD);

		log.fine(String.format("Power vector %f", powerVector.doubleValue()));

		motor.runByRevolutions(powerVector, numberOfRevolutions, secondsTimeout);

		log.exiting(this.getClass().getCanonicalName(), "raiseByRevolutions");

	}

	/**
	 * Raise the riser for the default amount of time.
	 */
	private void raiseByTime() {
		raiseByTime(DEFAULT_POWER_MAGNITUDE, DEFAULT_SECONDS_TO_RUN_MAXIMUM);
	}

	/**
	 * Raise the riser for a fixed amount of time.
	 *
	 * @param powerMagnitude
	 * 		Proportion of power to apply to motor. Must be non-negative. To lower the arm use lowerByTime().
	 * @param secondsToRun
	 * 		The number of seconds for which to raise the arm. Must be non-negative.
	 */
	private void raiseByTime(PowerMagnitude powerMagnitude, NonNegativeDouble secondsToRun) {

		PowerVector powerVector = new PowerVector(powerMagnitude, PowerVector.FORWARD);
		motor.runByTime(powerVector, secondsToRun);

		currentPosition = 1.0;
	}

	/**
	 * Raise the riser to its upward position while avoiding stalling the riser motor.
	 * <p>
	 * This method does not work very well. The riser motor does not really 'stall'. Instead, it continues to run
	 * irregularly as it attempts to push the riser higher than it can go.
	 */
	private void raiseUntilStall() {
		motor.setStallTimeLimitInMilliseconds(DEFAULT_MILLISECONDS_STALL_TIME_WINDOW);
		motor.setStallDetectionToleranceInTicks(DEFAULT_TICKS_STALL_TOLERANCE);
		PowerVector powerVector = new PowerVector(DEFAULT_POWER_MAGNITUDE, PowerVector.FORWARD);
		motor.runUntilStalled(powerVector);
	}

}
