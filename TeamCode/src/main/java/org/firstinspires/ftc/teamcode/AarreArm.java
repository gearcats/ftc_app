package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class AarreArm {


    private AarreMotor motor;
    private AarreTelemetry telemetry;

    private double currentPosition;

    /**
     * This empty constructor is useful for testing.
     */
    public AarreArm() {

    }

    /**
     * Construct an instance of AarreArm with telemetry.
     *
     * @param nameOfRiserMotor The name of the motor that controls the riser.
     * @param hardwareMap      The hardware map upon which the motor may be found.
     * @param telemetry        An instance of AarreTelemetry to associate with the
     */
    public AarreArm(final HardwareMap hardwareMap, final String nameOfRiserMotor, final AarreTelemetry telemetry) {

        currentPosition = 0.5; // We have no idea where the arm is

        // Make sure there is a hardwareMap parameter
        if (hardwareMap == null)
            throw new IllegalArgumentException("Unexpected null parameter: hardwareMap");


        // Make sure there is a telemetry parameter
        if (telemetry == null)
            throw new AssertionError("Unexpected null parameter: telemetry");

        this.telemetry = telemetry;

        motor = new AarreMotor(hardwareMap, nameOfRiserMotor, telemetry);

        motor.setPower(0.0);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);  // Positive power raises arm
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    /**
     * Best guess at current position of the arm. (We can't know exactly where the arm is
     * because the arm starts in an unknown physical position upon software initialization
     * and does not store any state information.) A value of 0.0 indicates that we believe the
     * arm is fully lowered, and a value of 1.0 indicates that we believe the arm is fully
     * raised. The arm generally should be either fully lowered or fully raised (except when it
     * is in motion), so values in the middle when the arm is stationary suggest uncertainty about
     * where the arm really is.
     */
    public double getCurrentPosition() {
        return currentPosition;
    }


    /**
     * Lower the arm to its downward position while avoiding stalling the arm motor
     */
    public final void lower() {
        motor.setStallTimeLimitInMilliseconds(100);
        motor.runUntilStalled(-1.0);
    }

    /**
     * Raise the arm to its upward position while avoiding stalling the arm motor
     */
    public final void raise() {
        motor.setStallTimeLimitInMilliseconds(100);
        motor.runUntilStalled(1.0);
    }

}
