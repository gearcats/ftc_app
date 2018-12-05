package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.aarre.src.IMU;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;


@Autonomous(name = "Aarre IMU Unit Tests", group = "Aarre")
/*
  Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Disabled
public class IMUUnitTests extends LinearOpMode {

	IMU imu;

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	@Override
	public final void runOpMode() {
		stop();
	}

	@BeforeEach
	public final void testConstructor() {
		imu = new IMU(this);
	}

}
