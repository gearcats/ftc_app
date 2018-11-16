/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains Aarre's experimental code for the autonomous mode
 *
 * To avoid issuing an error on the phones, any OpMode class must be
 * declared public.
 *
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Autonomous(name="Aarre Autonomous", group="Aarre")
public class AarreAutonomous extends LinearOpMode {

    private AarreTelemetry betterTelemetry = new AarreTelemetry(telemetry);

    private AarreRobot robot = new AarreRobot();

    private ElapsedTime runtime = new ElapsedTime();

    private static final double DRIVE_SPEED                     = 0.6;      // How fast to move forward or back
    private static final double TURN_SPEED                      = 0.5;      // How fast to move when turning
    private static final double TEST_TIME_SECONDS               = 0.5;
    private static final int COUNTS_PER_MOTOR_REVOLUTION        = 1440 ;    // eg: TETRIX Motor Encoder, TorqueNado
    private static final double DRIVE_GEAR_REDUCTION            = 1.0 ;     // This is 1.0 for our direct-drive wheels
    private static final double WHEEL_DIAMETER_INCHES           = 5.5 ;     // For figuring circumference; could be 5.625, also depends on treads
    private static final double COUNTS_PER_INCH                 = (COUNTS_PER_MOTOR_REVOLUTION * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    // TODO: Use Math.PI above

    /**
     * Properties inherited from LinearOpMode include:
     *
     * telemetry
     */
    @Override
    public void runOpMode() {

        betterTelemetry.addData("Status", "Initializing robot");
        betterTelemetry.update();

        robot.init(hardwareMap, betterTelemetry);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            betterTelemetry.addData("Status", "Ready to run");    //
            betterTelemetry.update();

            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
            encoderDrive(DRIVE_SPEED,  12,  12, 5.0);  // Forward 12 inches with 5 sec timeout
            encoderDrive(TURN_SPEED,   12, -12, 4.0);  // Turn right 12 inches with 4 sec timeout
            encoderDrive(DRIVE_SPEED, -12, -12, 4.0);  // Reverse 12 inches with 4 Sec timeout

            betterTelemetry.addData("Path", "Complete");
            betterTelemetry.update();
        }
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     */
    private void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {

                // Display it for the driver.
                betterTelemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                betterTelemetry.addData("Path2",  "Running at %7d :%7d", robot.leftMotor.getCurrentPosition(), robot.rightMotor.getCurrentPosition());
                betterTelemetry.update();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    /*
     *  Move the riser based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     *
     *  @param revolutions The number of (motor shaft) revolutions to move the riser.
     *                     Positive values move the riser up.
     *                     Negative values move the riser down.
     */
    public void moveRiser(int revolutions) {

        int startPositionCounts;
        int startPositionRevolutions;

        int targetPositionCounts;
        int targetPositionRevolutions;

        final double riserSpeed         = 0.1;
        final int    timeoutS           = 1;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position and pass to motor controller

            startPositionCounts = robot.riserMotor.getCurrentPosition();
            startPositionRevolutions = startPositionCounts / COUNTS_PER_MOTOR_REVOLUTION;

            targetPositionRevolutions = startPositionRevolutions + 1;
            targetPositionCounts = targetPositionRevolutions * COUNTS_PER_MOTOR_REVOLUTION;

            //targetPositionInches = leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            //newRightTarget = rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            robot.riserMotor.setTargetPosition(targetPositionCounts);

            // Turn On RUN_TO_POSITION
            robot.riserMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time
            runtime.reset();

            robot.riserMotor.setPower(Math.abs(riserSpeed));

            // Keep looping while we are still active, and there is time left, and both motors are running.
            //
            //noinspection StatementWithEmptyBody
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && robot.riserMotor.isBusy());

            // Stop all motion;
            robot.riserMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.riserMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
