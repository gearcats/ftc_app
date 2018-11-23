package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.src.AarreArm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AarreArmUnitTests {

    AarreArm arm;

    AarreArmUnitTests() {
        arm = new AarreArm();
    }

    @Test
    public void testNewAarreArmObjectNotNull() {
        assertNotNull(arm);
    }

    @Test
    public void testLowerMethod() {
        assertThrows(NullPointerException.class, () -> {
            arm.lower();
        });

        double position = arm.getCurrentPosition();
    }

    @Test
    public void testRaiseMethod() {
        assertThrows(NullPointerException.class, () -> {
            arm.raise();
        });
        double position = arm.getCurrentPosition();
    }

}