package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AarreDriveMotors;
import org.junit.jupiter.api.BeforeEach;

public class AarreDriveMotorsUnitTests extends LinearOpMode {

	AarreDriveMotors driveMotors;

	/**
	 * Override this method and place your code here.
	 * <p>
	 * Please do not swallow the InterruptedException, as it is used in cases
	 * where the op mode needs to be terminated early.
	 */
	@BeforeEach
	public void runOpMode() {
		driveMotors = new AarreDriveMotors(this);
	}

}
