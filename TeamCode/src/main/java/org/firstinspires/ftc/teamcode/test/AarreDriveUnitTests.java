package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AarreDrive;

public class AarreDriveUnitTests extends LinearOpMode {
	/**
	 * Override this method and place your code here.
	 * <p>
	 * Please do not swallow the InterruptedException, as it is used in cases
	 * where the op mode needs to be terminated early.
	 */
	@Override
	public void runOpMode() {
		AarreDrive drive = new AarreDrive(this);
	}
}
