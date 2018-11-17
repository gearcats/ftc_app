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


    // TODO: Use Math.PI above

    /**
     * Properties inherited from LinearOpMode include:
     *
     * telemetry
     */
    @Override
    public void runOpMode() {

        betterTelemetry.log("Initializing robot");

        robot.init(hardwareMap, betterTelemetry);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            betterTelemetry.log("Ready to run");    //

            // Step through each leg of the path,
            // Note: Reverse movement is obtained by setting a negative distance (not speed)
           robot.encoderDrive(DRIVE_SPEED,  12,  12, 5.0);  // Forward 12 inches with 5 sec timeout
            robot.encoderDrive(TURN_SPEED,   12, -12, 4.0);  // Turn right 12 inches with 4 sec timeout
            robot.encoderDrive(DRIVE_SPEED, -12, -12, 4.0);  // Reverse 12 inches with 4 Sec timeout

            betterTelemetry.log("Path", "Complete");
        }
    }

}
