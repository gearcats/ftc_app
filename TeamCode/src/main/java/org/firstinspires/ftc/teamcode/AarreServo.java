package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This class wraps the FTC DcMotor interface / DcMotorImpl class to:
 *
 * <ul>
 * <li>Provide stall detection</li>
 * <li>Provide telemetry</li>
 * </ul>
 */

public class AarreServo {

	private final Servo          servo;
	private       AarreTelemetry telemetry;
	private       LinearOpMode   opMode;

	/**
	 * Construct an instance of AarreMotor without telemetry.
	 *
	 * @param servoName
	 * 		The name of the motor.
	 * @param hardwareMap
	 * 		The hardware map upon which the motor may be found.
	 */
	public AarreServo(final HardwareMap hardwareMap, final String servoName, LinearOpMode opMode) {

		this.opMode = opMode;

		servo = hardwareMap.get(Servo.class, servoName);

		servo.setDirection(Servo.Direction.FORWARD);

		// Upon construction, reset the servo to its full range of movement
		servo.scaleRange(0.0, 1.0);
	}


	/**
	 * Construct an instance of AarreServo with telemetry.
	 *
	 * @param servoName
	 * 		The name of the servo for which to provide telemetry.
	 * @param hardwareMap
	 * 		The hardware map upon which the servo may be found.
	 * @param telemetry
	 * 		An instance of AarreTelemetry to associate with this instance.
	 */
	public AarreServo(final HardwareMap hardwareMap, final String servoName, final AarreTelemetry telemetry, final LinearOpMode opMode) {

		// Call the other constructor to create the underlying Servo member
		this(hardwareMap, servoName, opMode);

		this.telemetry = telemetry;

		this.opMode = opMode;

	}

	/**
	 * Get the current position of this servo.
	 * <p>
	 * Despite its name, the {@link Servo} method {@code getPosition}
	 * is often wrong about the servo's position, especially if there are mechanical obstacles
	 * stalling
	 * the servo. Therefore, we use a different name here.
	 *
	 * @return The current (purported) position of this server in the interval [0,1]
	 */
	@SuppressWarnings("WeakerAccess")
	public final double getPurportedPosition() {

		return servo.getPosition();

	}


	/**
	 * Turn the servo forward to its maximum position.
	 */
	final void forward() {

		telemetry.log("Servo - Preparing to move forward");

		final double startPosition = servo.getPosition();
		telemetry.log("Servo - Current position is %f", startPosition);

		final double maxPosition = servo.MAX_POSITION;
		telemetry.log("Servo - Prescriptive max position is %f", maxPosition);

		setPosition(maxPosition);
		telemetry.log("Servo - Set to max");

		final double currentPosition = servo.getPosition();
		telemetry.log("Servo - Purported max position is %f", currentPosition);

	}


	/**
	 * Reverse the servo to its lowerUntilStalled limit
	 * <p>
	 * TODO: Avoid stalling the servo.
	 */
	void reverse() {

		telemetry.log("Preparing to reverse the servo");

		final double startPosition = servo.getPosition();
		telemetry.log("Servo current position is %f", startPosition);

		final double minPosition = servo.MIN_POSITION;
		telemetry.log("Servo prescriptive min position is %f", minPosition);

		setPosition(minPosition);
		telemetry.log("Servo set to min");

		final double currentPosition = getPurportedPosition();
		telemetry.log("Servo purported min position is %f", currentPosition);

	}

	/**
	 * Scale the range of the servo, for example to accommodate mechanical limits.
	 *
	 * @param min
	 * 		The minimum position in [0,1] that the servo can actually reach.
	 * @param max
	 * 		The maximum position in [0,1] that the servo can actually reach.
	 */
	void scaleRange(final double min, final double max) {

		servo.scaleRange(min, max);

	}

	/**
	 * Set the servo direction.
	 * <p>
	 * The servo direction is set to @link{Servo.Direction.FORWARD} by default. If the servo is
	 * turning the
	 * "wrong way," then set it to @link{Servo.Direction.REVERSE}
	 *
	 * @param direction
	 * 		An instance of @link{Servo.Direction}
	 */
	@SuppressWarnings("SameParameterValue")
	void setDirection(final Servo.Direction direction) {

		servo.setDirection(direction);

	}

	/**
	 * Set the servo position.
	 *
	 * @param position
	 * 		Where to set the servo relative to its available range.
	 * 		A value in the interval [0,1] where 0 is the minimum available position
	 * 		and 1 is the maximum available position.
	 */
	private void setPosition(final double position) {

		//telemetry.log("Setting servo to position %f", position);
		servo.setPosition(position);

		long        millisecondsToWait;
		long        millisecondsInterval;
		ElapsedTime runtime;
		double      millisecondsElapsed;

		// Wait for the hardware to catch up

		millisecondsToWait = 1000;
		millisecondsInterval = 10;
		runtime = new ElapsedTime();
		millisecondsElapsed = runtime.milliseconds();

		while ((millisecondsElapsed < millisecondsToWait) && opMode.opModeIsActive()) {
			opMode.sleep(millisecondsInterval);
			millisecondsElapsed = runtime.milliseconds();
		}

		//telemetry.log("Done setting servo to position %f", position);

	}

}
