package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.src.AarreIMU;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@Autonomous(name = "Aarre IMU Unit Tests", group = "Aarre")
/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Disabled
public class AarreIMUUnitTests extends LinearOpMode {

	AarreIMU imu;

	@BeforeEach
	public final void testConstructor() {
		imu = new AarreIMU(this);
	}

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {
		stop();
	}

}
