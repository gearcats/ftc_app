package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.src.AarreMotor;
import org.firstinspires.ftc.teamcode.src.AarrePowerVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor Unit Tests", group = "Aarre")
@Disabled
public class AarreMotorUnitTests extends LinearOpMode implements AarreMotorUnitTestsInterface {

    private AarreMotor motor;

	@Override
	@BeforeEach
	public void AarreMotorUnitTests() {
		motor = new AarreMotor(this, "left");
	}

	@Test
	public void testGetNumberOfCycles01() {
	}

	@Test
	public void testGetNumberOfCycles02() {
	}

	@Test
	public void testGetNumberOfCycles03() {
	}

	@Test
	public void testGetNumberOfCycles04() {
	}

	@Override
	@Test
	public final void testGetProportionPowerNew01() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(0.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.9, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew02() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(-1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(0.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.9, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew03() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(0.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.1, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew04() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(0.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(-1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.1, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew05() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(-1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.9, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew06() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(-1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-0.9, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew07() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(-1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(-1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(-1.0, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew08() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(0.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(0.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(0.0, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
	@Test
	public final void testGetPowerVectorNew09() {
		AarrePowerVector powerVectorCurrent   = new AarrePowerVector(1.0);
		AarrePowerVector powerVectorRequested = new AarrePowerVector(1.0);
		AarrePowerVector proportionPowerNew   = motor.getPowerVectorNew(powerVectorCurrent, powerVectorRequested);
		assertEquals(1.0, proportionPowerNew.asDouble(), "Wrong proportion power");
	}

	@Override
    @Test
    public final void testGetPowerVectorNew10() {

        /*
         * The current power is ...
         */
        AarrePowerVector proportionPowerCurrent = new AarrePowerVector(0.0);

        /*
         * We are ramping to power of ...
         */
        AarrePowerVector proportionPowerRequested = new AarrePowerVector(1.0);


        /*
         * So the correct power is...
         */
        double correctValue = 0.1;

        AarrePowerVector newPowerVector = motor.getPowerVectorNew(proportionPowerCurrent, proportionPowerRequested);

        assertEquals(newPowerVector.asDouble(), correctValue);

    }


	@Override
    @Test
    public final void testGetPowerVectorNew11() {

        /*
         * The current power is ...
         */
        AarrePowerVector proportionPowerCurrent = new AarrePowerVector(1.0);

        /*
         * We are ramping to power of ...
         */
        AarrePowerVector proportionPowerRequested = new AarrePowerVector(1.0);

        /*
         * So the correct power is...
         */
        double correctValue = 1.0;

        AarrePowerVector newProportion = motor.getPowerVectorNew(proportionPowerCurrent, proportionPowerRequested);


        assertEquals(newProportion.asDouble(), correctValue);

    }

	@Test
	public void testIsRampDownToEncoderTicksRunning01() {
	}

	@Override
    @Test
    public final void testIsRampDownToEncoderTicksRunning02() {

        final int tickNumberAtStartOfPeriod = 60;
        final int tickNumberCurrent = 61;
        final int numberOfTicksInPeriod = 10000;
        final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
        final AarrePowerVector powerAtEnd = new AarrePowerVector(0.0);

        boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
                numberOfTicksInPeriod, powerAtStart, powerAtEnd);

        assertFalse(result);
    }

	@Override
    @Test
    public final void testIsRampDownToEncoderTicksRunning03() {

        /*
         * The current tick number exceed the total number of ticks we were supposed to
         * move, so ramping down should stop.
         *
         * This is a made up example.
         */

        final int tickNumberAtStartOfPeriod = 0;
        final int tickNumberCurrent = 11000;
        final int numberOfTicksInPeriod = 10000;

        final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
        final AarrePowerVector powerAtEnd = new AarrePowerVector(0.0);

        final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
                numberOfTicksInPeriod, powerAtStart, powerAtEnd);

        assertFalse(result);
    }

	@Override
    @Test
    public final void testIsRampDownToEncoderTicksRunning04() {

        /*
         * The current tick number exceeds the total number of ticks we were supposed to
         * move, so ramping down should stop.
         */
        final int tickNumberAtStartOfPeriod = 0;
        final int tickNumberCurrent = 123;
        final int numberOfTicksInPeriod = 100;
        final AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
        final AarrePowerVector powerAtEnd = new AarrePowerVector(0.0);

        final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
                numberOfTicksInPeriod, powerAtStart, powerAtEnd);

        assertFalse(result);
    }

	@Test
	public void testIsRampDownToEncoderTicksRunning05() {
	}

	@Override
    @Test
    public final void testIsRampDownToEncoderTicksRunning06() {

        final int tickNumberAtStartOfPeriod = 0;
        final int tickNumberCurrent = 59;
        final int numberOfTicksInPeriod = 120;
        final AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
        final AarrePowerVector powerAtEnd = new AarrePowerVector(0.0);

        final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
                numberOfTicksInPeriod, powerAtStart, powerAtEnd);

        assertFalse(result);
    }

	@Override
    @Test
    public final void testIsRampDownToEncoderTicksRunning07() {

        final int tickNumberAtStartOfPeriod = 0;
        final int tickNumberCurrent = -59;
        final int numberOfTicksInPeriod = 120;
        final AarrePowerVector powerAtStart = new AarrePowerVector(0.5);
        final AarrePowerVector powerAtEnd = new AarrePowerVector(0.0);

        final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
                numberOfTicksInPeriod, powerAtStart, powerAtEnd);

        assertFalse(result);
    }

	@Test
	public void testIsRampDownToEncoderTicksRunning08() {
	}

	@Override
    @Test
    public final void testIsRampDownToEncoderTicksRunning09() {

        /*
         * The current tick number exceed the total number of ticks we were supposed to
         * move, so ramping down should stop.
         *
         * This is a made up example.
         */

        final int tickNumberAtStartOfPeriod = 0;
        final int tickNumberCurrent = -11000;
        final int numberOfTicksInPeriod = 10000;

        final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
        final AarrePowerVector powerAtEnd = new AarrePowerVector(0.0);

        final boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
                numberOfTicksInPeriod, powerAtStart, powerAtEnd);

        assertFalse(result);
    }


	@Override
    @Test
    public final void testIsRampDownToEncoderTicksRunning10() {

        final int tickNumberAtStartOfPeriod = -60;
        final int tickNumberCurrent = -61;
        final int numberOfTicksInPeriod = 10000;
        final AarrePowerVector powerAtStart = new AarrePowerVector(1.0);
        final AarrePowerVector powerAtEnd = new AarrePowerVector(0.0);

        boolean result = motor.isRampDownToEncoderTicksRunning(tickNumberAtStartOfPeriod, tickNumberCurrent,
                numberOfTicksInPeriod, powerAtStart, powerAtEnd);

        assertFalse(result);
    }

	@Test
	public void testIsRampDownToEncoderTicksRunning11() {
	}

	@Override
    @Test
    public final void testIsRampUpToEncoderTicksDone01() {

		/*
		  ticksMoved is less than ticksMaximum, so no reason to stop. (We haven't moved far enough
		  yet).
		 */
        int ticksMaximum = 1440;
        int ticksMoved = 0;

		/*
		  Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
        double secondsTimeout = 5.0;
        double secondsRunning = 0.0;

		/*
		  Power delta is not within the tolerance, so no reason to stop. (We haven't ramped
		  enough yet.)
		 */
        AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		  Current power is within reason, so no reason to stop.
		 */
        AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

        boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved,
                powerDelta, powerCurrent);

        assertFalse(result);
    }

	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone02() {

		/*
		  We have moved farther than we intended, so it is time to stop.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 1441;

		/*
		  Seconds running is less than timeout, so no reason to stop. (We haven't timed out yet.)
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 0.0;

		/*
		  Power delta is not within the tolerance, so no reason to stop. (We haven't ramped
		  enough yet.)
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		  Current power is within reason, so no reason to stop.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertTrue(result);
	}

	@Override
	@Test
	public final void testIsRampUpToEncoderTicksDone03() {

		/*
		  We have not moved enough yet, so continue.
		 */
		int ticksMaximum = 1440;
		int ticksMoved   = 1439;

		/*
		  Seconds running is more than timeout, so stop.
		 */
		double secondsTimeout = 5.0;
		double secondsRunning = 6.0;

		/*
		  Power delta is not within the tolerance, so no reason to stop. (We haven't ramped
		  enough yet.)
		 */
		AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		  Current power is within reason, so no reason to stop.
		 */
		AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

		boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved, powerDelta, powerCurrent);

		assertTrue(result);
	}

	@Override
    @Test
    public final void testIsRampUpToEncoderTicksDone04() {

        /*
         * We have not moved enough yet, so continue.
         */
        int ticksMaximum = 1440;
        int ticksMoved = 1439;

        /*
         * Seconds running is less than timeout, so continue.
         */
        double secondsTimeout = 5.0;
        double secondsRunning = 4.0;

        /*
         * Power delta is within tolerance, so continue.
         * (Continue moving, although power should not continue to increase.)
         */
        AarrePowerVector powerDelta = new AarrePowerVector(0.001);

        /*
         * Current power is within reason, so no reason to stop.
         */
        AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

        boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved,
                powerDelta, powerCurrent);

        assertFalse(result);

    }

	@Override
    @Test
    public final void testIsRampUpToEncoderTicksDone05() {

		/*
		  We have moved exactly the right amount, so stop.
		 */
        int ticksMaximum = 1440;
        int ticksMoved = 1440;

		/*
		  Seconds running is less than timeout, so continue.
		 */
        double secondsTimeout = 5.0;
        double secondsRunning = 4.0;

		/*
		  Power delta is greater than tolerance, so continue.
		 */
        AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		  Current power is within reason, so no reason to stop.
		 */
        AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

        boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved,
                powerDelta, powerCurrent);

        assertTrue(result);
    }

	@Override
    @Test
    public final void testIsRampUpToEncoderTicksDone06() {

		/*
		  We have not moved the right amount, so continue.
		 */
        int ticksMaximum = 1440;
        int ticksMoved = 14;

		/*
		  Seconds running is less than timeout, so continue.
		 */
        double secondsTimeout = 5.0;
        double secondsRunning = 4.0;

		/*
		  Power delta is greater than tolerance, so continue.
		 */
        AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		  Current power is maxed out, but still no reason to stop, so continue.
		 */
        AarrePowerVector powerCurrent = new AarrePowerVector(1.0);

        boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved,
                powerDelta, powerCurrent);

        assertFalse(result);
    }


	@Override
    @Test
    public final void testIsRampUpToEncoderTicksDone07() {

		/*
		  We have not moved enough, so continue.
		 */
        int ticksMaximum = 1440;
        int ticksMoved = 144;

		/*
		  Seconds running is less than timeout, so continue.
		 */
        double secondsTimeout = 5.0;
        double secondsRunning = 4.0;

		/*
		  Power delta is negative, but absolute power delta is greater than tolerance, so
		  continue.
		 */
        AarrePowerVector powerDelta = new AarrePowerVector(-0.1);

		/*
		  Current power is within reason, so no reason to stop.
		 */
        AarrePowerVector powerCurrent = new AarrePowerVector(0.6);

        boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved,
                powerDelta, powerCurrent);

        assertFalse(result);
    }

	@Override
    @Test
    public final void testIsRampUpToEncoderTicksDone08() {

		/*
		  We have not moved enough, so continue.
		 */
        int ticksMaximum = 1440;
        int ticksMoved = 190;

		/*
		  Seconds running is less than timeout, so continue.
		 */
        double secondsTimeout = 5.0;
        double secondsRunning = 4.0;

		/*
		  Power delta is greater than tolerance, so continue.
		 */
        AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		  Current power is negative but within reason, so continue.
		 */
        AarrePowerVector powerCurrent = new AarrePowerVector(-0.6);

        boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved,
                powerDelta, powerCurrent);

        assertFalse(result);
    }

	@Override
    @Test
    public final void testIsRampUpToEncoderTicksDone09() {

		/*
		  We have not moved enough, so continue.
		 */
        int ticksMaximum = 1440;
        int ticksMoved = -190;

		/*
		  Seconds running is less than timeout, so continue.
		 */
        double secondsTimeout = 5.0;
        double secondsRunning = 4.0;

		/*
		  Power delta is greater than tolerance, so continue.
		 */
        AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		  Current power is negative but within reason, so continue.
		 */
        AarrePowerVector powerCurrent = new AarrePowerVector(-0.6);

        boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved,
                powerDelta, powerCurrent);

        assertFalse(result);
    }

	@Override
    @Test
    public final void testIsRampUpToEncoderTicksDone10() {

		/*
		  We have not moved enough, so continue.
		 */
        int ticksMaximum = -5040;
        int ticksMoved = 0;

		/*
		  Seconds running is less than timeout, so continue.
		 */
        double secondsTimeout = 5.0;
        double secondsRunning = 4.0;

		/*
		  Power delta is greater than tolerance, so continue.
		 */
        AarrePowerVector powerDelta = new AarrePowerVector(0.1);

		/*
		  Current power is negative but within reason, so continue.
		 */
        AarrePowerVector powerCurrent = new AarrePowerVector(-0.6);

        boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved,
                powerDelta, powerCurrent);

        assertFalse(result);
    }


	@Override
    @Test
    public final void testIsRampUpToEncoderTicksDone11() {

        /*
         * We have moved enough (albeit in a negative direction), so stop
         */
        int ticksMaximum = -5040;
        int ticksMoved = -5041;

        /*
         * Seconds running is less than timeout, so continue.
         */
        double secondsTimeout = 5.0;
        double secondsRunning = 4.0;

        /*
         * Power delta is greater than tolerance, so continue.
         */
        AarrePowerVector powerDelta = new AarrePowerVector(0.1);

        /*
         * Current power is negative but within reason, so continue.
         */
        AarrePowerVector powerCurrent = new AarrePowerVector(-0.6);

        boolean result = motor.isRampUpToEncoderTicksDone(ticksMaximum, secondsTimeout, secondsRunning, ticksMoved,
                powerDelta, powerCurrent);

        assertTrue(result);
    }

	@Test
	public void testGetTickNumberToStartRampDown01() {
	}

	@Test
	public void testGetTickNumberToStartRampDown02() {
	}

	@Test
	public void testGetTickNumberToStartRampDown03() {
	}

	@Test
	public void testGetTickNumberToStartRampDown04() {
	}


	@Test
	public void testGetTicksPerCycle01() {
	}

	@Test
	public void testGetTicksPerMinute01() {
	}

	@Override
	@Test
	public final void testSetDirection() {
		motor.setDirection(DcMotorSimple.Direction.REVERSE);
	}

    @Test
    @Override
    public final void runOpMode() {
    }
}
