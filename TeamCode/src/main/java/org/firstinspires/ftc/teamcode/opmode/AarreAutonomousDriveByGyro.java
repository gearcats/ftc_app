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

package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.src.AarrePowerMagnitude;
import org.firstinspires.ftc.teamcode.src.AarrePowerVector;
import org.firstinspires.ftc.teamcode.src.AarreRobot;
import org.firstinspires.ftc.teamcode.src.AarreTelemetry;

/**
 * This file illustrates the concept of driving a path based on Gyro heading and encoder counts. It
 * uses the common Pushbot hardware class to define the drive on the robot. The code is structured
 * as a LinearOpMode
 * <p>
 * The code REQUIRES that you DO have encoders on the wheels, otherwise you would use:
 * PushbotAutoDriveByTime;
 * <p>
 * This code ALSO requires that you have a Modern Robotics I2C gyro with the name "gyro" otherwise
 * you would use: PushbotAutoDriveByEncoder;
 * <p>
 * This code requires that the drive Motors have been configured such that a positive power command
 * moves them forward, and causes the encoders to count UP.
 * <p>
 * This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run
 * profile
 * <p>
 * In order to calibrate the Gyro correctly, the robot must remain stationary during calibration.
 * This is performed when the INIT button is pressed on the Driver Station. This code assumes that
 * the robot is stationary when the INIT button is pressed. If this is not the case, then the INIT
 * should be performed again.
 * <p>
 * Note: in this example, all angles are referenced to the initial coordinate frame set during the
 * the Gyro Calibration process, or whenever the program issues a resetZAxisIntegrator() call on the
 * Gyro.
 * <p>
 * The angle of movement/rotation is assumed to be a standardized rotation around the robot Z axis,
 * which means that a Positive rotation is Counter Clock Wise, looking down on the field. This is
 * consistent with the FTC field coordinate conventions set out in the document:
 * ftc_app\doc\tutorial\FTC_FieldCoordinateSystemDefinition.pdf
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new
 * name. Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode
 * list
 */

@Autonomous(name = "Aarre Autonomous Drive By Gyro", group = "Aarre")
@Disabled
public class AarreAutonomousDriveByGyro extends LinearOpMode {

	// These constants define the desired driving/control characteristics
	// The can/should be tweaked to suite the specific robot drive train.

	// Nominal drive speed for better accuracy
	static final AarrePowerMagnitude DRIVE_SPEED = new AarrePowerMagnitude(0.7);

	// Nominal turn speed for better accuracy
	static final AarrePowerVector    TURN_SPEED  = new AarrePowerVector(0.5);

	ModernRoboticsI2cGyro gyro = null;                    // Additional Gyro device

	private AarreTelemetry aarreTelemetry;
	private AarreRobot     robot;


	@Override
	public final void runOpMode() {

		aarreTelemetry = new AarreTelemetry(telemetry);
		robot = new AarreRobot(this);

		// Send telemetry message to alert driver that we are calibrating;
		telemetry.addData(">", "Calibrating Gyro");    //
		telemetry.update();

		gyro.calibrate();

		// make sure the gyro is calibrated before continuing
		while (!isStopRequested() && gyro.isCalibrating()) {
			sleep((long) 50);
			idle();
		}

		telemetry.addData(">", "Robot Ready.");    //
		telemetry.update();

		// Wait for the game to start (Display Gyro value), and reset gyro before we move..
		while (!isStarted()) {
			telemetry.addData(">", "Robot Heading = %d", gyro.getIntegratedZValue());
			telemetry.update();
		}

		gyro.resetZAxisIntegrator();

		// Step through each leg of the path,
		// Note: Reverse movement is obtained by setting a negative distance (not speed)
		// Put a hold after each turn
		robot.gyroDrive(DRIVE_SPEED, 12.0, 0.0);    // Drive FWD 12 inches
		robot.gyroTurn(TURN_SPEED, -45.0);         // Turn  CCW to -45 Degrees
		robot.gyroHold(TURN_SPEED, -45.0, 0.5);    // Hold -45 Deg heading for a 1/2 second
		robot.gyroDrive(DRIVE_SPEED, 12.0, -45.0);  // Drive FWD 12 inches at 45 degrees
		robot.gyroTurn(TURN_SPEED, 45.0);         // Turn  CW  to  45 Degrees
		robot.gyroHold(TURN_SPEED, 45.0, 0.5);    // Hold  45 Deg heading for a 1/2 second
		robot.gyroTurn(TURN_SPEED, 0.0);         // Turn  CW  to   0 Degrees
		robot.gyroHold(TURN_SPEED, 0.0, 1.0);    // Hold  0 Deg heading for a 1 second
		robot.gyroDrive(DRIVE_SPEED, -48.0, 0.0);    // Drive REV 48 inches

		telemetry.addData("Path", "Complete");
		telemetry.update();
	}


}
