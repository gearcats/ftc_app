package org.firstinspires.ftc.teamcode.test;

import android.os.Build;
import android.support.annotation.RequiresApi;
import org.firstinspires.ftc.teamcode.src.AarrePositiveInteger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AarreNonNegativeIntegerUnitTests {

	@Test
	public void whenConstructorArgumentZero_thenNoProblem() {
		new AarrePositiveInteger(0);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Test
	public void whenConstructorArgumentPositive_thenNoProblem() {
		int randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		new AarrePositiveInteger(randomPositiveInteger);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Test
	public void whenConstructorArgumentNegative_thenExceptionThrown() {
		int randomNegativeInteger = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, 0);
		assertThrows(IllegalArgumentException.class, () -> {
			new AarrePositiveInteger(randomNegativeInteger);
		});
	}

	@Test
	public void whenDoubleValueRequested_thenReturnsCorrectValue() {
		int                  randomPositiveInteger      = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		AarrePositiveInteger randomAarrePositiveInteger = new AarrePositiveInteger(randomPositiveInteger);
		double               expected                   = (double) randomPositiveInteger;
		double               actual                     = randomAarrePositiveInteger.doubleValue();
		assertEquals(expected, actual);
	}

	@Test
	public void whenIntValueRequested_thenReturnsCorrectValue() {
		int                  randomPositiveInteger = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, -1);
		AarrePositiveInteger aarrePositiveInteger  = new AarrePositiveInteger(randomPositiveInteger);
		int                  returnValue           = aarrePositiveInteger.intValue();
		assertEquals(randomPositiveInteger, returnValue);
	}


}
