package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.opmode.AarreAutonomousDriveByGyro;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test constructing an instance of AarreAutonomousDriveByGyro
 */
public class AarreAutonomousDriveByGyroUnitTests {

	private final Logger javaLog = Logger.getLogger(this.getClass().getName());

    @Test
    public void testNewGyroObjectNotNull() {
        AarreAutonomousDriveByGyro driveByGyro = new AarreAutonomousDriveByGyro();
        assertNotNull(driveByGyro);
    }


}
