package org.firstinspires.ftc.teamcode.src;

import org.firstinspires.ftc.teamcode.aarre.src.PowerMagnitude;

abstract interface AarreConcreteMotorInterface {

	/**
	 * Get the number of ticks in a power change cycle at a given power magnitude.
	 *
	 * @param powerMagnitude
	 * 		The magnitude of motor power for which the caller wants the number of ticks in a power change cycle.
	 *
	 * @return The number of ticks in a power change cycle when the motor is operating at powerMagnitude.
	 */
	double getTicksPerCycle(PowerMagnitude powerMagnitude);

}
