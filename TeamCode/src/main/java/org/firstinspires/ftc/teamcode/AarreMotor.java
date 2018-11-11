package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;



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

   private int oldTickNumber;

   private DcMotor      motor;
   private int          stallTimeLimitInMilliseconds = 0;
   private int          stallDetectionToleranceInTicks = 5;
   private Telemetry    telemetry;
   private ElapsedTime  timeStalledInMilliseconds;

    /**
     * Construct an instance with telemetry from a motor name and hardware map
     *
     * @param motorName
     * @param hardwareMap
     * @param telemetry
     */
    public AarreMotor(HardwareMap hardwareMap, String motorName, Telemetry telemetry) {

        // Call the other constructor to create the underlying DcMotor member
        this(hardwareMap, motorName);

        // Add a telemetry member
        this.telemetry = telemetry;
    }

    /**
     * Construct an instance without telemetry from a motor name and hardware map
     *
     * @param motorName
     * @param hardwareMap
     */
    @SuppressWarnings("unused")
    public AarreMotor(HardwareMap hardwareMap, String motorName) {

        motor = hardwareMap.get(DcMotor.class, motorName);
        timeStalledInMilliseconds = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        setDefaults();
    }



    /**
     * Set some reasonable defaults for a motor. The user should then set the real values.
     */
    private void setDefaults() {

        setStallDetectionToleranceInTicks(5);
        setStallTimeLimitInMilliseconds(0);

        // Reset the encoder and force the motor to be stopped
        this.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.setPower(0);
    }

    /**
     * Get the stall detection time limit
     *
     * @return  The number of milliseconds during which the motor must not have moved more than
     *          the stall detection tolerance before we call it a stall.
     */
    public int getStallTimeLimitInMilliseconds() {
        return stallTimeLimitInMilliseconds;
    }

    /**
     * Get the number of milliseconds for which the motor has been stalled.
     *
     * @return  The integer number of milliseconds during which the motor speed has been
     *          lower than the stall detection tolerance.
     */
    public int getTimeStalledInMilliseconds() {

        // Take the time stalled in (double) milliseconds, round to nearest long and cast to int
        return (int) Math.round(timeStalledInMilliseconds.time());
    }
    /**
     * Set the stall detection time limit
     *
     * @param stallTimeLimitInMilliseconds The number of milliseconds during which the motor must
     *                                     not have moved more than the stall detection tolerance
     *                                     before we call it a stall.
     */
    public void setStallTimeLimitInMilliseconds(int stallTimeLimitInMilliseconds) {
        this.stallTimeLimitInMilliseconds = stallTimeLimitInMilliseconds;
    }

    /**
     * Get the stall detection tolerance
     *
     * @return  An integer number of encoder clicks such that, if the encoder changes fewer than this
     *          number of clicks since the last time we read it, then we consider the motor
     *          stalled.
     */
    public int getStallDetectionToleranceInTicks() {
        return stallDetectionToleranceInTicks;
    }

    /**
     * Set the stall detection tolerance
     *
     * @param stallDetectionToleranceInTicks    An integer number of encoder clicks such that,
     *                                          if the encoder changes fewer than this number of
     *                                          clics over a period of time defined by
     *                                          stallTimeLimitInMilliseconds, then we consider the
     *                                          motor stalled.
     */
    @SuppressWarnings("WeakerAccess")
    public void setStallDetectionToleranceInTicks(int stallDetectionToleranceInTicks) {
        this.stallDetectionToleranceInTicks = stallDetectionToleranceInTicks;
    }

    /**
     * Set up stall detection.
     *
     * @param   stallTimeLimitInMilliseconds             How long does the motor have to be still before we consider it stalled?
     *                                     TODO: Is this in seconds?
     * @param   stallDetectionToleranceInTicks    An integer number of encoder clicks such that,
     *                                            if the encoder changes less than this over a
     *                                            period of stallTimeLimitInMilliseconds, then we call it a stall.
     */
    public void setupStallDetection(int stallTimeLimitInMilliseconds, int stallDetectionToleranceInTicks) {
        setStallDetectionToleranceInTicks(stallDetectionToleranceInTicks);
        setStallTimeLimitInMilliseconds(stallTimeLimitInMilliseconds);
        timeStalledInMilliseconds.reset();
        this.oldTickNumber = this.getCurrentTickNumber();
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
    public boolean isStalled() {

        telemetry.addData("time stalled = ", "%d ms", getTimeStalledInMilliseconds());
        telemetry.addData("stall time limit = ", "%d ms", stallTimeLimitInMilliseconds);

        int newTickNumber = this.getCurrentTickNumber();

        telemetry.addData("checking for a stall", "!");

        // if the motor has not moved since the last time the position was read

        if (Math.abs(newTickNumber - oldTickNumber) < stallDetectionToleranceInTicks) {
            telemetry.addData("motor is not moving", "!");

            // motor has not moved, checking to see how long the motor has been stalled for
            if (timeStalledInMilliseconds.time() > stallTimeLimitInMilliseconds) {

                telemetry.addData("stall timer has expired", "!");

                // it has been stalled for more than the time limit
                return true;

            } else {

                telemetry.addData("stall time has NOT expired", "!");

            }
        } else {

            if(telemetry != null) {
                telemetry.addData("motor is still moving.", " Resetting stall timer.");
            }

            // reset the timer because the motor is not stalled
            timeStalledInMilliseconds.reset();
        }

        // Save the new tick number as baseline for the next iteration
        this.oldTickNumber = newTickNumber;

        return false;
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
     * Run this motor until it stalls
     *
     * @param power How much power to apply to the motor, in the interval
     *              [-1,1].
     */
    public void runUntilStalled(double power) {
        setPower(power);
        while (!(isStalled())) {
            telemetry.addData("Status", "Not stalled yet...");
            telemetry.update();
        }
    }

    /**
     * Set the run mode for this motor.
     *
     * @param mode the new current run mode for this motor
     */
    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    void setMode(DcMotor.RunMode mode) {
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

}