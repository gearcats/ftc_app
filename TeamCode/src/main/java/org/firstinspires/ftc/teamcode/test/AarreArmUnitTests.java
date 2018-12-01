package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.src.AarreArm;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AarreArmUnitTests {

	private final AarreArm arm;
	private final Logger   javaLog = Logger.getLogger(this.getClass().getName());

    AarreArmUnitTests() {
        arm = new AarreArm();
    }

    @Test
    final void testNewAarreArmObjectNotNull() {
        assertNotNull(arm);
    }

    @Test
    final void testLowerMethod() {
        assertThrows(NullPointerException.class, arm::lower);
    }

    @Test
    final void testRaiseMethod() {
        assertThrows(NullPointerException.class, arm::raise);
    }

}