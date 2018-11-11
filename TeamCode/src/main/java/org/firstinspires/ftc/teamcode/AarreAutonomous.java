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
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains Aarre's experimental code
 *
 */

@Autonomous(name="Aarre Autonomous", group="Aarre")
public class AarreAutonomous extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private static final double DRIVE_SPEED                     = 0.6;      // How fast to move forward or back
    private static final double TURN_SPEED                      = 0.5;      // How fast to move when turning
    private static final double TEST_TIME_SECONDS               = 0.5;
    private static final int COUNTS_PER_MOTOR_REVOLUTION        = 1440 ;    // eg: TETRIX Motor Encoder, TorqueNado
    private static final double DRIVE_GEAR_REDUCTION            = 1.0 ;     // This is 1.0 for our direct-drive wheels
    private static final double WHEEL_DIAMETER_INCHES           = 5.5 ;     // For figuring circumference; could be 5.625, also depends on treads
    private static final double COUNTS_PER_INCH                 = (COUNTS_PER_MOTOR_REVOLUTION * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    // TODO: Use Math.PI

    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;
    private DcMotor armMotor = null;
    private DcMotor riserMotor = null;
    private Servo   hookServo = null;
    private CRServo scoopServo = null;

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. The strings used here as parameters
        // to 'get' must correspond to the names assigned in the robot configuration
        // in the FTC Robot Controller app on the phone

        leftMotor  = hardwareMap.get(DcMotor.class, "left");
        rightMotor = hardwareMap.get(DcMotor.class, "right");
        armMotor   = hardwareMap.get(DcMotor.class, "arm");
        riserMotor = hardwareMap.get(DcMotor.class, "riser");
        hookServo  = hardwareMap.get(Servo.class, "hook");
        scoopServo = hardwareMap.get(CRServo.class, "scoop");

        // Configure drive motors such that a positive power command moves them forwards
        // and causes the encoders to count UP. Note that, as in most robots, the drive
        // motors are mounted in opposite directions.

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        // This code REQUIRES that you have encoders on the wheel motors

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                leftMotor.getCurrentPosition(),
                rightMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
            encoderDrive(DRIVE_SPEED,  12,  12, 5.0);  // Forward 12 inches with 5 sec timeout
            encoderDrive(TURN_SPEED,   12, -12, 4.0);  // Turn right 12 inches with 4 sec timeout
            encoderDrive(DRIVE_SPEED, -12, -12, 4.0);  // Reverse 12 inches with 4 Sec timeout

            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    private void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            leftMotor.setTargetPosition(newLeftTarget);
            rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftMotor.setPower(Math.abs(speed));
            rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftMotor.isBusy() && rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d", leftMotor.getCurrentPosition(), rightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftMotor.setPower(0);
            rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    /*
     *  Move the riser based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     *
     *  @param revolutions The number of (motor shaft) revolutions to move the riser.
     *                     Positive values move the riser up.
     *                     Negative values move the riser down.
     */
    public void moveRiser(int revolutions) {

        int newLeftTarget;
        int newRightTarget;

        int startPositionCounts;
        int startPositionRevolutions;

        int targetPositionCounts;
        int targetPositionRevolutions;

        final double riserSpeed         = 0.1;
        final int    timeoutS           = 1;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position and pass to motor controller

            startPositionCounts = riserMotor.getCurrentPosition();
            startPositionRevolutions = startPositionCounts / COUNTS_PER_MOTOR_REVOLUTION;

            targetPositionRevolutions = startPositionRevolutions + 1;
            targetPositionCounts = targetPositionRevolutions * COUNTS_PER_MOTOR_REVOLUTION;

            //targetPositionInches = leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            //newRightTarget = rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            riserMotor.setTargetPosition(targetPositionCounts);

            // Turn On RUN_TO_POSITION
            riserMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time
            runtime.reset();

            riserMotor.setPower(Math.abs(riserSpeed));

            // Keep looping while we are still active, and there is time left, and both motors are running.

            //noinspection StatementWithEmptyBody
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && riserMotor.isBusy());

            // Stop all motion;
            riserMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            riserMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
