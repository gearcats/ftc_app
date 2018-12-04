package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.Arm;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArmUnitTests {

    private final Arm    arm;
    private final Logger javaLog = Logger.getLogger(this.getClass().getName());

    ArmUnitTests() {
        arm = new Arm();
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