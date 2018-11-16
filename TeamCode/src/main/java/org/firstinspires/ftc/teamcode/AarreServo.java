package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This class wraps the FTC DcMotor interface / DcMotorImpl class to:
 *
 * <ul>
 *     <li>Provide stall detection</li>
 *     <li>Provide telemetry</li>
 * </ul>
 *
 * TODO: Refactor so both this and AarreMotor inherit from something more general like "AarreActuator" -- these two classes share a lot of code
 */

public class AarreServo {

    private int oldTickNumber;

    private final Servo      servo;
    private int          stallTimeLimitInMilliseconds = 0;
    private double          stallDetectionToleranceRange = 0.01;
    private AarreTelemetry    telemetry;
    private final ElapsedTime  timeStalledInMilliseconds;
    private double previousPosition;


    /**
     * Construct an instance of AarreMotor without telemetry.
     *
     * @param servoName The name of the motor.
     * @param hardwareMap The hardware map upon which the motor may be found.
     */
    @SuppressWarnings("unused")
    public AarreServo(HardwareMap hardwareMap, String servoName) {

        servo = hardwareMap.get(Servo.class, servoName);
        timeStalledInMilliseconds = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        setDefaults();
    }


    /**
     * Construct an instance of AarreServo with telemetry.
     *
     * @param servoName The name of the servo for which to provide telemetry.
     * @param hardwareMap The hardware map upon which the servo may be found.
     * @param telemetry An instance of AarreTelemetry to associate with this instance.
     * @param servoDirection Set to Servo.Direction.REVERSE if the servo moves in the wrong direction.
     */
    public AarreServo(HardwareMap hardwareMap, String servoName, AarreTelemetry telemetry) {

        // Call the other constructor to create the underlying Servo member
        this(hardwareMap, servoName);

        // Add a telemetry member
        this.telemetry = telemetry;

        servo.setDirection(Servo.Direction.FORWARD);

        // Upon construction, reset the servo to its full range of movement
        servo.scaleRange(0.0, 1.0);
    }



    /**
     * Set some reasonable defaults for a servo. The user should then set the real values.
     */
    private void setDefaults() {

        setStallDetectionToleranceInRange(0.05);
        setStallTimeLimitInMilliseconds(0);

    }

    /**
     * Get the number of milliseconds for which the servo has been stalled.
     *
     * @return  The integer number of milliseconds during which the servo speed has been
     *          lower than the stall detection tolerance.
     */
    private int getTimeStalledInMilliseconds() {

        // Take the time stalled in (double) milliseconds, round to nearest long and cast to int
        return (int) Math.round(timeStalledInMilliseconds.time());
    }

    /**
     * Set the stall detection time limit
     *
     * @param stallTimeLimitInMilliseconds The number of milliseconds during which the servo must
     *                                     not have moved more than the stall detection tolerance
     *                                     before we call it a stall.
     */
    @SuppressWarnings("WeakerAccess")
    public void setStallTimeLimitInMilliseconds(int stallTimeLimitInMilliseconds) {
        this.stallTimeLimitInMilliseconds = stallTimeLimitInMilliseconds;
    }

    /**
     * Set the stall detection tolerance
     *
     * @param stallDetectionToleranceInRange    An integer number of encoder clicks such that,
     *                                          if the encoder changes fewer than this number of
     *                                          clicks over a period of time defined by
     *                                          stallTimeLimitInMilliseconds, then we consider the
     *                                          servo stalled.
     */
    @SuppressWarnings("WeakerAccess")
    public void setStallDetectionToleranceInRange(double stallDetectionToleranceInRange) {
        this.stallDetectionToleranceRange = stallDetectionToleranceInRange;
    }

    /**
     * Set up stall detection.
     *
     * @param   stallTimeLimitInMilliseconds      How long does the servo have to be still before we consider it stalled?
     * @param   stallDetectionToleranceInRange    An portion of the range in [0,1] such that,
     *                                            if the servo changes less than this over a
     *                                            period of stallTimeLimitInMilliseconds, then we call it a stall.
     */
    public void setupStallDetection(int stallTimeLimitInMilliseconds, double stallDetectionToleranceInRange) {
        setStallDetectionToleranceInRange(stallDetectionToleranceInRange);
        setStallTimeLimitInMilliseconds(stallTimeLimitInMilliseconds);
        timeStalledInMilliseconds.reset();
        previousPosition = this.getPurportedPosition();
    }

    /**
     * Detect whether the servo is stalled.
     *
     * The servo must not have moved more than a certain number of encoder clicks during a period
     * of at least so many milliseconds before we consider it stalled.
     *
     * @return  <code>true</code> if the servo is stalled;
     *          <code>false</code> otherwise.
     */
    private boolean isStalled() {

        telemetry.log("Time stalled = ", "%d ms", getTimeStalledInMilliseconds());
        telemetry.log("Stall time limit = ", "%d ms", stallTimeLimitInMilliseconds);

        double newPosition = this.getPurportedPosition();

        telemetry.log("Checking for a servo stall", "!");

        // if the servo has not moved since the last time the position was read

        if (Math.abs(newPosition - previousPosition) < stallDetectionToleranceRange) {
            telemetry.log("Servo is not moving", "!");

            // Servo has not moved, checking to see how long the servo has been stalled for
            if (timeStalledInMilliseconds.time() > stallTimeLimitInMilliseconds) {

                telemetry.log("Servo stall timer has expired", "!");

                // it has been stalled for more than the time limit
                return true;

            } else {

                telemetry.log("Servo stall time has NOT expired", "!");

            }
        } else {

            if(telemetry != null) {
                telemetry.log("Servo is still moving.", " Resetting stall timer.");
            }

            // reset the timer because the servo is not stalled
            timeStalledInMilliseconds.reset();
        }

        // Save the new tick number as baseline for the next iteration
        this.previousPosition = newPosition;

        return false;
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
     * TODO: Avoid stalling the hook servo.
     *
     */
    void forwardServo() {

        telemetry.log("Preparing to forward the servo");

        double startPosition = servo.getPosition();
        telemetry.log("Servo current position is %f", startPosition);

        double maxPosition = servo.MAX_POSITION;
        telemetry.log("Servo prescriptive max position is %f", maxPosition);

        servo.setPosition(maxPosition);
        telemetry.log("Servo set to max");

        double currentPosition = servo.getPosition();
        telemetry.log("Servo purported max position is %f", currentPosition);

    }



    /**
     * Reverse the servo to its lower limit
     *
     * TODO: Avoid stalling the servo.
     *
     */
    void reverseServo() {

        telemetry.log("Preparing to reverse the servo");

        double startPosition = servo.getPosition();
        telemetry.log("Servo current position is %f", startPosition);

        double minPosition = servo.MIN_POSITION;
        telemetry.log("Servo prescriptive min position is %f", minPosition);

        servo.setPosition(minPosition);
        telemetry.log("Servo set to min");

        double currentPosition = getPurportedPosition();
        telemetry.log("Servo purported min position is %f", currentPosition);

    }


    /**
     * Put the servo in its middle position so that we can both lower and raise it.
     *
     * TODO: Avoid stalling the servo.
     */
    void readyServo() {

        telemetry.log("Servo preparing to move to middle position");

        double middlePosition = 0.5;

        telemetry.log("Servo prescribed middle position is %f", middlePosition);

        servo.setPosition(middlePosition);

        telemetry.log("Servo moved to middle position");

        double currentPosition = servo.getPosition();
        telemetry.log("Servo actual middle position is %f", currentPosition);

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
    void setDirection(Servo.Direction direction) {

        servo.setDirection(direction);

    }

}
