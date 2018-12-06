package org.firstinspires.ftc.teamcode.aarre.test;

import org.firstinspires.ftc.teamcode.aarre.src.Motor;
import org.junit.jupiter.api.Test;

/**
 * This interface declares revHDCoreHexMotor-related unit tests that apply to all motors. Motor-related unit tests whose
 * results depend on specific kinds of motors are declared in {@link ConcreteMotorUnitTestsInterface}.
 */
interface MotorUnitTestsInterface {


	// Subclasses should override this method to return their own special type of motorRevHDCoreHex
	Motor getMotor();

	/**
	 * Must override runOpMode to avoid compiler error
	 */
	@Test
	void runOpMode();

	@Test
	void testSetDirection();

}
