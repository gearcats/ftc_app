package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AarreIMU;
import org.junit.jupiter.api.BeforeEach;


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
}
