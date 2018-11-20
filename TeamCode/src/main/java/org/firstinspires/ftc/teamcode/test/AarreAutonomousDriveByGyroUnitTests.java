package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.AarreAutonomousDriveByGyro;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test constructing an instance of AarreAutonomousDriveByGyro
 */
public class AarreAutonomousDriveByGyroUnitTests {

    @Test
    public void testNewGyroObjectNotNull() {
        AarreAutonomousDriveByGyro driveByGyro = new AarreAutonomousDriveByGyro();
        Assert.assertNotNull(driveByGyro);
    }


}
