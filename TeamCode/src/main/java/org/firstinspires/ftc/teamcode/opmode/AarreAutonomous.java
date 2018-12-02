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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.src.AarrePowerMagnitude;
import org.firstinspires.ftc.teamcode.src.AarreRobot;
import org.firstinspires.ftc.teamcode.src.AarreTelemetry;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This file contains Aarre's experimental code for the autonomous mode
 * <p>
 * To avoid issuing an error on the phones, any OpMode class must be
 * declared public.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Autonomous(name = "Aarre Autonomous", group = "Aarre")
public class AarreAutonomous extends LinearOpMode {

	private static final double              INCHES                = 12.0;
	private static final double              TIMEOUT               = 5.0;
	// How fast to move forward or back
	private static final AarrePowerMagnitude DRIVE_POWER_MAGNITUDE = new AarrePowerMagnitude(0.6);
	// How fast to move when turning
	private static final AarrePowerMagnitude    TURN_POWER_MAGNITUDE  = new AarrePowerMagnitude(0.5);
	private static final double              TEST_TIME_SECONDS     = 0.5;
	private final        ElapsedTime         runtime               = new ElapsedTime();
	private              AarreTelemetry      betterTelemetry;
	private              AarreRobot          robot;

	private final XLogger log = XLoggerFactory.getXLogger("TEMP");

	public AarreAutonomous() {
	}

	/**
	 * Properties inherited from LinearOpMode include:
	 * <p>
	 * hardwareMap
	 * telemetry
	 */
	@Override
	public final void runOpMode() {

		// 'telemetry' comes from FTC....
		// It is only available in runOpMode

		if (telemetry == null) {
			throw new AssertionError("Unexpected null object: telemetry");
		}
		betterTelemetry = new AarreTelemetry(telemetry);

		// 'hardwareMap comes from FTC....
		// It is only available in runOpMode

		if (hardwareMap == null) {
			throw new AssertionError("Unexpected null object: hardwareMap");
		}
		robot = new AarreRobot(this);

		betterTelemetry.log("Initializing robot");

		// Wait for the driver to press PLAY
		waitForStart();

		runtime.reset();

		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {

			betterTelemetry.log("Ready to run");    //

			// Step through each leg of the path,
			// Note: Reverse movement is obtained by setting a negative distance (not speed)

			try {
				robot.drive(DRIVE_POWER_MAGNITUDE, INCHES, INCHES, TIMEOUT);
				robot.drive(TURN_POWER_MAGNITUDE, INCHES, -INCHES, TIMEOUT);
				robot.drive(DRIVE_POWER_MAGNITUDE, -INCHES, -INCHES, TIMEOUT);
			} catch (NoSuchMethodException e) {
				log.error(e.toString());
			}

			betterTelemetry.log("Path", "Complete");
		}
	}

}
