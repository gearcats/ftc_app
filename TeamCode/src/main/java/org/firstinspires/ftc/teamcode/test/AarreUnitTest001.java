package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.AarreAutonomousDriveByGyro;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test constructing an instance of AarreAutonomousDriveByGyra
 */
public class AarreUnitTest001 {

    @Test
    public void AarreUnitTest001() {
        AarreAutonomousDriveByGyro driveByGyro = new AarreAutonomousDriveByGyro();
        Assert.assertNotNull(driveByGyro);
    }

}
