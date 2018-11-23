package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.opmode.AarreAutonomousDriveByGyro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test constructing an instance of AarreAutonomousDriveByGyro
 */
public class AarreAutonomousDriveTestByGyroUnitTests {

    @Test
    public void testNewGyroObjectNotNull() {
        AarreAutonomousDriveByGyro driveByGyro = new AarreAutonomousDriveByGyro();
        assertNotNull(driveByGyro);
    }


}
