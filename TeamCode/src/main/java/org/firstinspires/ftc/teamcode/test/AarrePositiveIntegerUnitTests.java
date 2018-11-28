package org.firstinspires.ftc.teamcode.test;

import android.os.Build;
import android.support.annotation.RequiresApi;
import org.firstinspires.ftc.teamcode.src.AarrePositiveInteger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class AarrePositiveIntegerUnitTests {

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Test
	public void whenConstructorArgumentPositive_thenNoProblem() {
		int randomPositiveInteger = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
		new AarrePositiveInteger(randomPositiveInteger);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Test
	public void whenConstructorArgumentNegative_thenExceptionThrown() {
		int randomNegativeInteger = ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, -1);
		assertThrows(IllegalArgumentException.class, () -> {
			new AarrePositiveInteger(randomNegativeInteger);
		});
	}
}
