package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.aarre.src.MotorTorqueNADO;
import org.firstinspires.ftc.teamcode.aarre.src.NonNegativeDouble;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor TorqueNADO Unit Tests", group = "Aarre")
@Disabled
public class MotorTorqueNADOUnitTests extends MotorUnitTests implements ConcreteMotorUnitTestsInterface {

	Logger          javaLog         = Logger.getLogger(this.getClass().getName());
	MotorTorqueNADO torqueNADOMotor = new MotorTorqueNADO(this, "left");

	@Override
	public MotorTorqueNADO getMotor() {
		return torqueNADOMotor;
	}



	@Test
	public final void testGetTicksPerMillisecond01() {
		NonNegativeDouble ticksPerMillisecond = getMotor().getTicksPerMillisecond();
		assertEquals(2.4, ticksPerMillisecond.doubleValue());
	}

	@Test
	public final void testGetTicksPerMinute01() {
		NonNegativeDouble ticksPerMinute = getMotor().getTicksPerMinute();
		assertEquals(144000.0, ticksPerMinute.doubleValue());
	}

	@Test
	public final void testGetTicksPerSecond01() {
		NonNegativeDouble ticksPerSecond = getMotor().getTicksPerSecond();
		assertEquals(2400.0, ticksPerSecond.doubleValue());
	}



}
