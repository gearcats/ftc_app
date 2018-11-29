package org.firstinspires.ftc.teamcode.test;

import android.os.Build;
import android.support.annotation.RequiresApi;
import org.firstinspires.ftc.teamcode.src.AarreNonNegativeInteger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AarreNonNegativeIntegerUnitTests {

	@Test
	public void whenConstructorArgumentZero_thenNoProblem() {
		new AarreNonNegativeInteger(0);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Test
	public void whenConstructorArgumentPositive_thenNoProblem() {
		int randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		new AarreNonNegativeInteger(randomPositiveInteger);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Test
	public void whenConstructorArgumentNegative_thenExceptionThrown() {
		int randomNegativeInteger = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, 0);
		assertThrows(IllegalArgumentException.class, () -> {
			new AarreNonNegativeInteger(randomNegativeInteger);
		});
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Test
	public void whenDoubleValueRequested_thenReturnsCorrectValue() {
		int                     randomPositiveInteger      = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		AarreNonNegativeInteger randomAarrePositiveInteger = new AarreNonNegativeInteger(randomPositiveInteger);
		double                  expected                   = (double) randomPositiveInteger;
		double                  actual                     = randomAarrePositiveInteger.doubleValue();
		assertEquals(expected, actual);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Test
	public void whenIntValueRequested_thenReturnsCorrectValue() {
		int                     randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		AarreNonNegativeInteger aarrePositiveInteger  = new AarreNonNegativeInteger(randomPositiveInteger);
		int                     returnValue           = aarrePositiveInteger.intValue();
		assertEquals(randomPositiveInteger, returnValue);
	}


}
