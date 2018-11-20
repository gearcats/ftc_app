package org.firstinspires.ftc.teamcode.test;

import com.google.common.base.VerifyException;

import org.firstinspires.ftc.teamcode.AarreRiser;
import org.testng.Assert;
import org.testng.annotations.Test;


public class AarreRiserUnitTests {

    AarreRiser riser;

    AarreRiserUnitTests() {
        riser = new AarreRiser();
    }

    @Test
    public void testNewAarreRiserObjectNotNull() {
        Assert.assertNotNull(riser);
    }

    @Test(expectedExceptions = VerifyException.class)
    public void testLowerMethod() {
        riser.lower();
        double position = riser.getCurrentPosition();
    }

    @Test(expectedExceptions = VerifyException.class)
    public void testRaiseMethod() {
        riser.raise();
        double position = riser.getCurrentPosition();
    }

}