package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.opmode.AarreAutonomousDriveByGyro;
import org.junit.jupiter.api.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test constructing an instance of AarreAutonomousDriveByGyro
 */
public class AarreAutonomousDriveByGyroUnitTests {

	private final XLogger log = XLoggerFactory.getXLogger("TEMP");

    @Test
    public void testNewGyroObjectNotNull() {
        AarreAutonomousDriveByGyro driveByGyro = new AarreAutonomousDriveByGyro();
        assertNotNull(driveByGyro);
    }


}
