package org.firstinspires.ftc.teamcode.aarre.test;

import org.junit.jupiter.api.Test;

/**
 * This interface declares revHDCoreHexMotor-related unit tests that depend on which kind of revHDCoreHexMotor is being
 * tested. For example, because the TorqueNADO revHDCoreHexMotor and the REV HD Core Hex revHDCoreHexMotor have
 * different characteristics (e.g., number of ticks per revolution), they need separate unit tests. Their tests should
 * both implement this interface, though. Motor-related tests whose results do not depend on the specific kind of
 * revHDCoreHexMotor are declared in {@link MotorUnitTestsInterface}.
 */
interface ConcreteMotorUnitTestsInterface extends MotorUnitTestsInterface {

	@Test
	public void testGetTicksPerMinute01();

}
