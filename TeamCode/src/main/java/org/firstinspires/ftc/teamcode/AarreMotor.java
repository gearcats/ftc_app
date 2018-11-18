package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This class wraps the FTC DcMotor interface / DcMotorImpl class to:
 *
 * <ul>
 *     <li>Provide stall detection</li>
 *     <li>Provide telemetry</li>
 * </ul>
 *
 * It might be preferable (e.g., more elegant, less wrapper code needed)
 * to extend the FTC DcMotorImpl class rather than wrap it. However, it seems that
 * extending the DcMotorImpl class is not recommended. See
 * <a href=https://ftcforum.usfirst.org/forum/ftc-technology/4717-how-to-extend-the-dcmotor-class></a>
 * for a discussion.
 *
 * Stall detection and telemetry code adapted from
 * <a href="https://github.com/TullyNYGuy/FTC8863_ftc_app/blob/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Lib/FTCLib/DcMotor8863.java"></a>
 */
class AarreMotor {

   private final DcMotor      motor;
   private int oldTickNumber;
   private int          stallTimeLimitInMilliseconds = 0;
   private int          stallDetectionToleranceInTicks = 5;
   private AarreTelemetry    telemetry;
   private ElapsedTime  timeStalledInMilliseconds;

    private final int COUNTS_PER_MOTOR_REVOLUTION        = 1440 ;    // eg: TETRIX Motor Encoder, TorqueNado
    private static final double DRIVE_GEAR_REDUCTION            = 1.0 ;     // This is 1.0 for our direct-drive wheels
    private static final double WHEEL_DIAMETER_INCHES           = 5.5 ;     // For figuring circumference; could be 5.625, also depends on treads
    final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REVOLUTION * AarreMotor.DRIVE_GEAR_REDUCTION) / (AarreMotor.WHEEL_DIAMETER_INCHES * 3.1415);

    /**
     * Construct an instance of AarreMotor without telemetry.
     *
     * @param motorName The name of the motor.
     * @param hardwareMap The hardware map upon which the motor may be found.
     */
    @SuppressWarnings("unused")
    private AarreMotor(HardwareMap hardwareMap, String motorName) {

        motor = hardwareMap.get(DcMotor.class, motorName);
        setDefaults();
    }

    /**
     * Construct an instance of AarreMotor with telemetry.
     *
     * @param motorName The name of the motor for which to provide telemetry.
     * @param hardwareMap The hardware map upon which the motor may be found.
     * @param telemetry An instance of AarreTelemetry to associate with the
     */
    AarreMotor(HardwareMap hardwareMap, String motorName, AarreTelemetry telemetry) {

        // Call the other constructor to create the underlying DcMotor member
        this(hardwareMap, motorName);

        // Add a telemetry member
        this.telemetry = telemetry;
    }

    /**
     * Get the current reading of the encoder for this motor.
     *
     * Despite its name, the {@link DcMotor} method <code>getCurrentPosition</code>
     * provides almost no information about position. Therefore, we use a
     * different name here.
     *
     * @return The current reading of the encoder for this motor, in ticks.
     */
    @SuppressWarnings("WeakerAccess")
    public int getCurrentTickNumber() {
        return motor.getCurrentPosition();
    }

    /**
     * Get the number of milliseconds for which the motor has been stalled.
     *
     * @return  The integer number of milliseconds during which the motor speed has been
     *          lower than the stall detection tolerance.
     */
    private int getTimeStalledInMilliseconds() {

        // Take the time stalled in (double) milliseconds, round to nearest long and cast to int
        double dblTimeStalledInMilliseconds = timeStalledInMilliseconds.time();
        int intTimeStalledInMilliseconds = (int) Math.round(dblTimeStalledInMilliseconds);
        return intTimeStalledInMilliseconds;
    }

    /**
     * Determine whether the motor is busy.
     *
     * @return Returns true if the motor is currently advancing or retreating to a target position.
     */
    boolean	isBusy() {
        return motor.isBusy();
    }


    /**
     * Detect whether the motor is stalled.
     *
     * The motor must not have moved more than a certain number of encoder clicks during a period
     * of at least so many milliseconds before we consider it stalled.
     *
     * @return  <code>true</code> if the motor is stalled;
     *          <code>false</code> otherwise.
     */
    private boolean isStalled() {

        // TODO: Implement logging framework to allow logging by severity level
        //telemetry.log("time stalled = ", "%d ms", getTimeStalledInMilliseconds());
        //telemetry.log("stall time limit = ", "%d ms", stallTimeLimitInMilliseconds);

        int newTickNumber = getCurrentTickNumber();

        //telemetry.log("checking for a stall", "!");

        if (Math.abs(newTickNumber - oldTickNumber) < stallDetectionToleranceInTicks) {

            // The motor has not moved since the last time the position was read.

            if (timeStalledInMilliseconds.time() > stallTimeLimitInMilliseconds) {

                // The motor has been stalled for more than the time limit

                return true;

            } else {

                // The motor has not moved but the time limit has not yet expired

            }

        } else {

            // The motor has moved since the last time we checked the position

            // Reset the timer

            timeStalledInMilliseconds.reset();
        }

        // Save the new tick number as baseline for the next iteration

        oldTickNumber = newTickNumber;

        // Notify caller that the motor is not stalled

        return false;

    }

    /**
     * Run this motor until it stalls
     *
     * @param power How much power to apply to the motor, in the interval
     *              [-1,1].
     */
    void runUntilStalled(double power) {
        timeStalledInMilliseconds = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        setPower(power);
        while (!(isStalled())) {
            telemetry.log("Not stalled yet...");
        }
    }

    /**
     * Set some reasonable defaults for a motor. The user should then set the real values.
     */
    private void setDefaults() {

        setStallDetectionToleranceInTicks(5);
        setStallTimeLimitInMilliseconds(0);

        // Reset the encoder and force the motor to be stopped
        setMode(RunMode.STOP_AND_RESET_ENCODER);
        setPower(0);
    }

    /**
     * Set the logical direction in which this motor operates.
     *
     * @param direction The logical direction in which this motor operates.
     */
    void setDirection(Direction direction) {
        motor.setDirection(direction);
    }

    /**
     * Set the run mode for this motor.
     *
     * @param mode the new current run mode for this motor
     */
    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    void setMode(RunMode mode) {
        motor.setMode(mode);
    }

    /**
     * Set the power level of the motor.
     *
     * Power is expressed as a fraction of the maximum possible power / speed
     * supported according to the run mode in which the motor is operating.
     *
     * Setting a power level of zero will brake the motor.
     *
     * @param power The new power level of the motor,
     *              a value in the interval [-1.0, 1.0]
     */
    void setPower(double power) {
        motor.setPower(power);
    }

    /**
     * Set the power level of the motor to an integer level.
     * <p>
     * Syntactic sugar when we are stopping the motor (0) or going at max speed (1).
     *
     * @param power The new power level of the motor.
     *              Either 0 or 1.
     */
    void setPower(int power) {
        double dblPower = (double) power;
        motor.setPower(dblPower);
    }

    /**
     * Set the stall detection tolerance
     *
     * @param ticks    An integer number of encoder clicks such that,
     *                                          if the encoder changes fewer than this number of
     *                                          clicks over a period of time defined by
     *                                          stallTimeLimitInMilliseconds, then we consider the
     *                                          motor stalled.
     */
    @SuppressWarnings("WeakerAccess")
    public void setStallDetectionToleranceInTicks(int ticks) {
        stallDetectionToleranceInTicks = ticks;
    }

    /**
     * Set the stall detection time limit
     *
     * @param milliseconds The number of milliseconds during which the motor must
     *                                     not have moved more than the stall detection tolerance
     *                                     before we call it a stall.
     */
    void setStallTimeLimitInMilliseconds(int milliseconds) {
        stallTimeLimitInMilliseconds = milliseconds;
    }

    /**
     * Set up stall detection.
     *
     * @param   timeLimitMilliseconds      How long does the motor have to be still before we consider it stalled?
     * @param   detectionToleranceTicks    An integer number of encoder clicks such that,
     *                                            if the encoder changes less than this over a
     *                                            period of stallTimeLimitInMilliseconds, then we call it a stall.
     */
    public void setupStallDetection(int timeLimitMilliseconds, int detectionToleranceTicks) {

        setStallDetectionToleranceInTicks(detectionToleranceTicks);
        setStallTimeLimitInMilliseconds(timeLimitMilliseconds);
        timeStalledInMilliseconds.reset();
        oldTickNumber = getCurrentTickNumber();

    }


    void setTargetPosition(int targetPositionTicks) {
        motor.setTargetPosition(targetPositionTicks);
    }
}
