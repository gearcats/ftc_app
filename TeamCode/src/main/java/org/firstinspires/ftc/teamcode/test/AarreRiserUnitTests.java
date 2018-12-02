package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.src.AarreRiser;
import org.junit.jupiter.api.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class AarreRiserUnitTests {

    AarreRiser riser;

	private final XLogger log = XLoggerFactory.getXLogger(this.getClass().getName());

    AarreRiserUnitTests() {
        riser = new AarreRiser();
    }

    @Test
    public void testNewAarreRiserObjectNotNull() {
        assertNotNull(riser);
    }

    @Test
    public void testLowerMethod() {
        assertThrows(NullPointerException.class, () -> riser.lower());

        double position = riser.getCurrentPosition();
    }

    @Test
    public void testRaiseMethod() {
        assertThrows(NullPointerException.class, () -> riser.raise());
        double position = riser.getCurrentPosition();
    }

}