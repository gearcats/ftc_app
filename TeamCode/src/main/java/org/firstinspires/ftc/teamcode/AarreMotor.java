package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

    private final DcMotor motor;
    private int oldTickNumber;
    private int stallTimeLimitInMilliseconds = 0;
    private int stallDetectionToleranceInTicks = 5;
    private AarreTelemetry telemetry;

    private static final int COUNTS_PER_MOTOR_REVOLUTION = 1440;    // eg: TETRIX Motor Encoder, TorqueNado
    private static final double DRIVE_GEAR_REDUCTION            = 1.0 ;     // This is 1.0 for our direct-drive wheels
    private static final double WHEEL_DIAMETER_INCHES           = 5.5 ;     // For figuring circumference; could be 5.625, also depends on treads
    private static final double COUNTS_PER_INCH = ((double) COUNTS_PER_MOTOR_REVOLUTION * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    private ElapsedTime timeStalledInMilliseconds;

    /**
     * Construct an instance of AarreMotor without telemetry.
     *
     * @param motorName The name of the motor.
     * @param hardwareMap The hardware map upon which the motor may be found.
     */
    @SuppressWarnings("unused")
    private AarreMotor(final HardwareMap hardwareMap, final String motorName) {

        motor = hardwareMap.get(DcMotor.class, motorName);

        // These are defaults. The user should customize them
        stallDetectionToleranceInTicks = 5;
        setStallTimeLimitInMilliseconds(100);

        // Reset the encoder and force the motor to be stopped
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0.0);
    }

    /**
     * Construct an instance of AarreMotor with telemetry.
     *
     * @param motorName The name of the motor for which to provide telemetry.
     * @param hardwareMap The hardware map upon which the motor may be found.
     * @param telemetry An instance of AarreTelemetry to associate with the
     */
    AarreMotor(final HardwareMap hardwareMap, final String motorName, @SuppressWarnings("ParameterHidesMemberVariable") final AarreTelemetry telemetry) {

        // Call the other constructor to create the underlying DcMotor member
        this(hardwareMap, motorName);

        // Add a telemetry member
        if (telemetry == null)
            throw new AssertionError("Unexpected null object: telemetry");
        this.telemetry = telemetry;
    }

    /**
     * Get counts per inch.
     *
     * @return The number of encoder ticks (counts) required to make the robot travel one inch.
     */
    public static final double getCountsPerInch() {
        return COUNTS_PER_INCH;
    }

    /**
     * Get the current reading of the encoder for this motor.
     *
     * Despite its name, the {@link DcMotor} method {@code getCurrentPosition}
     * provides almost no information about position. Therefore, we use a
     * different name here.
     *
     * @return The current reading of the encoder for this motor, in ticks.
     */
    @SuppressWarnings("WeakerAccess")
    public final int getCurrentTickNumber() {
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
        final double msStalledDbl = timeStalledInMilliseconds.time();
        return (int) Math.round(msStalledDbl);
    }

    /**
     * Determine whether the motor is busy.
     *
     * @return Returns true if the motor is currently advancing or retreating to a target position.
     */
    final boolean isBusy() {
        return motor.isBusy();
    }


    /**
     * Detect whether the motor is stalled.
     *
     * The motor must not have moved more than a certain number of encoder clicks during a period
     * of at least so many milliseconds before we consider it stalled.
     *
     * @return  {@code true} if the motor is stalled;
     *          {@code false} otherwise.
     */
    private boolean isStalled() {

        // TODO: Implement logging framework to allow logging by severity level
        //telemetry.log("Time stalled = ", "%d ms", getTimeStalledInMilliseconds());
        //telemetry.log("Stall time limit = ", "%d ms", stallTimeLimitInMilliseconds);

        boolean stalled = false;
        final int newTickNumber = getCurrentTickNumber();

        //telemetry.log("checking for a stall", "!");

        if (Math.abs(newTickNumber - oldTickNumber) < stallDetectionToleranceInTicks) {

            // The motor has not moved since the last time the position was read.

            if (timeStalledInMilliseconds.time() > (double) stallTimeLimitInMilliseconds) {

                // The motor has been stalled for more than the time limit

                //telemetry.log("Motor stalled");
                stalled = true;

            }

        } else {

            // The motor has moved since the last time we checked the position

            // Reset the timer

            timeStalledInMilliseconds.reset();
        }

        // Save the new tick number as baseline for the next iteration

        oldTickNumber = newTickNumber;

        // Notify caller whether or not the motor is not stalled

        return stalled;

    }

    /**
     * Rotate this motor a certain number of ticks
     *
     * @param speed    The power at which to rotate, in the interval [-1.0, 1.0].
     * @param ticks    Maximum number of ticks to rotate. Must be positive.
     * @param timeoutS Maximum number of seconds to rotate. Must be positive
     */
    final void runByEncoderTicks(final double speed, final int ticks, final double timeoutS) {

        if ((speed < -1.0) || (speed > 1.0))
            throw new AssertionError("Speed out of range [-1.0, 1.0]");

        if (timeoutS < 0.0)
            throw new AssertionError("timeoutS must be positive");

        @SuppressWarnings("NumericCastThatLosesPrecision") final int targetTicks = getCurrentTickNumber() + ((int) Math.signum(speed) * ticks);

        setTargetPosition(targetTicks);
        setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        final ElapsedTime runtime = new ElapsedTime();
        setPower(speed);

        // Keep looping while we are still active, and there is time left, and the motor is running.
        while ((runtime.seconds() < timeoutS) && (isBusy())) {

            telemetry.log("Motor", "Running to %7d", targetTicks);

        }

        // Stop all motion;
        setPower(0.0);

        // Turn off RUN_TO_POSITION
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    final void runByRevolutions(final double speed, final double revolutions, final double timeoutS) {

        @SuppressWarnings("NumericCastThatLosesPrecision") final int ticks = (int) Math.round((double) COUNTS_PER_MOTOR_REVOLUTION * revolutions);
        runByEncoderTicks(speed, ticks, timeoutS);
    }

    /**
     * Run this motor until it stalls
     *
     * @param power How much power to apply to the motor, in the interval
     *              [-1,1].
     */
    void runUntilStalled(final double power) {
        timeStalledInMilliseconds = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        setPower(power);
        //noinspection StatementWithEmptyBody
        while (!(isStalled())) {
            //telemetry.log("Not stalled yet...");
        }
    }

    /**
     * Set the logical direction in which this motor operates.
     *
     * @param direction The logical direction in which this motor operates.
     */
    void setDirection(final DcMotorSimple.Direction direction) {
        motor.setDirection(direction);
    }

    /**
     * Set the run mode for this motor.
     *
     * @param mode the new current run mode for this motor
     */
    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    void setMode(final DcMotor.RunMode mode) {
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
    void setPower(final double power) {
        motor.setPower(power);
        telemetry.log("Motor power set to %f", power);
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
    public void setStallDetectionToleranceInTicks(final int ticks) {
        stallDetectionToleranceInTicks = ticks;
    }

    /**
     * Set the stall detection time limit
     *
     * @param milliseconds The number of milliseconds during which the motor must
     *                                     not have moved more than the stall detection tolerance
     *                                     before we call it a stall.
     */
    void setStallTimeLimitInMilliseconds(final int milliseconds) {
        stallTimeLimitInMilliseconds = milliseconds;
    }

    /**
     * Set up stall detection.
     *
     * @param   timeLimitMs      How long does the motor have to be still before we consider it stalled?
     * @param   toleranceTicks    An integer number of encoder clicks such that,
     *                                            if the encoder changes less than this over a
     *                                            period of stallTimeLimitInMilliseconds, then we call it a stall.
     */
    public void setupStallDetection(final int timeLimitMs, final int toleranceTicks) {

        setStallDetectionToleranceInTicks(toleranceTicks);
        setStallTimeLimitInMilliseconds(timeLimitMs);
        timeStalledInMilliseconds.reset();
        oldTickNumber = getCurrentTickNumber();

    }


    void setTargetPosition(final int targetPositionTicks) {
        motor.setTargetPosition(targetPositionTicks);
    }
}
