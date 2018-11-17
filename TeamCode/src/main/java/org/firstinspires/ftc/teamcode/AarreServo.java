package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This class wraps the FTC DcMotor interface / DcMotorImpl class to:
 *
 * <ul>
 *     <li>Provide stall detection</li>
 *     <li>Provide telemetry</li>
 * </ul>
 *
 */

class AarreServo {

    private final Servo      servo;
    private AarreTelemetry    telemetry;

    /**
     * Construct an instance of AarreMotor without telemetry.
     *
     * @param servoName The name of the motor.
     * @param hardwareMap The hardware map upon which the motor may be found.
     */
    @SuppressWarnings("unused")
    private AarreServo(HardwareMap hardwareMap, String servoName) {

        servo = hardwareMap.get(Servo.class, servoName);
    }


    /**
     * Construct an instance of AarreServo with telemetry.
     *
     * @param servoName The name of the servo for which to provide telemetry.
     * @param hardwareMap The hardware map upon which the servo may be found.
     * @param telemetry An instance of AarreTelemetry to associate with this instance.
     */
    AarreServo(HardwareMap hardwareMap, String servoName, AarreTelemetry telemetry) {

        // Call the other constructor to create the underlying Servo member
        this(hardwareMap, servoName);

        // Add a telemetry member
        this.telemetry = telemetry;

        servo.setDirection(Servo.Direction.FORWARD);

        // Upon construction, reset the servo to its full range of movement
        servo.scaleRange(0.0, 1.0);
    }

    /**
     * Get the current position of this servo.
     *
     * Despite its name, the {@link Servo} method <code>getPosition</code>
     * is often wrong about the servo's position, especially if there are mechanical obstacles stalling
     * the servo. Therefore, we use a different name here.
     *
     * @return The current (purported) position of this server in the interval [0,1]
     */
    @SuppressWarnings("WeakerAccess")
    public double getPurportedPosition() {

        return servo.getPosition();

    }


    /**
     * Turn the servo forward to its maximum position.
     *
     */
    void forward() {

        telemetry.log("Servo - Preparing to move forward");

        double startPosition = servo.getPosition();
        telemetry.log("Servo - Current position is %f", startPosition);

        double maxPosition = servo.MAX_POSITION;
        telemetry.log("Servo - Prescriptive max position is %f", maxPosition);

        setPosition(maxPosition);
        telemetry.log("Servo - Set to max");

        double currentPosition = servo.getPosition();
        telemetry.log("Servo - Purported max position is %f", currentPosition);

    }



    /**
     * Reverse the servo to its lower limit
     *
     * TODO: Avoid stalling the servo.
     *
     */
    void reverse() {

        telemetry.log("Preparing to reverse the servo");

        double startPosition = servo.getPosition();
        telemetry.log("Servo current position is %f", startPosition);

        double minPosition = servo.MIN_POSITION;
        telemetry.log("Servo prescriptive min position is %f", minPosition);

        setPosition(minPosition);
        telemetry.log("Servo set to min");

        double currentPosition = getPurportedPosition();
        telemetry.log("Servo purported min position is %f", currentPosition);

    }

    /**
     * Scale the range of the servo, for example to accommodate mechanical limits.
     *
     * @param min The minimum position in [0,1] that the servo can actually reach.
     * @param max The maximum position in [0,1] that the servo can actually reach.
     */
    void scaleRange(double min, double max){

        servo.scaleRange(min, max);

    }

    /**
     * Set the servo direction.
     *
     * The servo direction is set to @link{Servo.Direction.FORWARD} by default. If the servo is turning the
     * "wrong way," then set it to @link{Servo.Direction.REVERSE}
     *
     * @param direction An instance of @link{Servo.Direction}
     */
    @SuppressWarnings("SameParameterValue")
    void setDirection(Servo.Direction direction) {

        servo.setDirection(direction);

    }

    /**
     * Set the servo position.
     *
     * @param position Where to set the servo relative to its available range.
     *                 A value in the interval [0,1] where 0 is the minimum available position
     *                 and 1 is the maximum available position.
     */
    private void setPosition(double position) {

        telemetry.log("Setting servo to position %f", position);
        servo.setPosition(position);

        // Wait for the hardware to catch up
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            telemetry.log ("Sleep interrupted!");
        }

        telemetry.log("Done setting servo to position %f", position);

    }

}
