package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AarreRobot;
import org.firstinspires.ftc.teamcode.AarreTelemetry;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Unit tests for AarreRobot class
 */
@Autonomous(name = "Aarre Robot Unit Tests", group = "Aarre")
public class AarreRobotUnitTests extends LinearOpMode {

    /**
     * Test AarreRobot
     * <p>
     * We can't break this up into different methods because the tests depend on overriding the
     * FTC runOpMode() method. Properties inherited from LinearOpMode include:
     * - hardwareMap
     * - telemetry
     */
    @Test
    @Override
    public final void runOpMode() {

        Assert.assertNotNull(telemetry);
        AarreTelemetry aarreTelemetry = new AarreTelemetry(telemetry);
        Assert.assertNotNull(aarreTelemetry);

        Assert.assertNotNull(hardwareMap, "hardwareMap");
        AarreRobot robot = new AarreRobot(hardwareMap, aarreTelemetry);
        Assert.assertNotNull(robot);

        Assert.assertNotNull(robot, "New robot instance is null");

        Assert.assertEquals(robot.getLeftMotor().getPower(), 0);
        Assert.assertEquals(robot.getRightMotor().getPower(), 0);

        Assert.assertFalse(robot.getLeftMotor().getCurrentTickNumber() < 0);
        Assert.assertFalse(robot.getLeftMotor().getCurrentTickNumber() < 0);

    }

}
