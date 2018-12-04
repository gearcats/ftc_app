package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

interface MotorInterface {

	HardwareMap getHardwareMap();

	/**
	 * The number of milliseconds in a ramp up/ramp down cycle.
	 * <p>
	 * It is useful to have this public getter for testing purposes.
	 *
	 * @return The number of milliseconds in a ramp up/ramp down cycle.
	 */
	int getMillisecondsPerCycle();

	/**
	 * Get the underlying DcMotor object.
	 * <p>
	 * The underlying DcMotor object is needed for operations such as setPower() and getCurrentPosition().
	 *
	 * @return
	 */
	DcMotor getMotor();

	LinearOpMode getOpMode();

	double getRevolutionsPerMinute();

	double getRevolutionsPerMinute(PowerMagnitude powerMagnitude);

	double getTicksPerMinute();

	double getTicksPerMinute(PowerMagnitude powerMagnitude);

	double getTicksPerRevolution();

	double getTicksPerSecond();

	double getTicksPerSecond(PowerMagnitude powerMagnitude);

	/**
	 * @return The number of ticks in a millisecond at maximum power.
	 */
	double getTicksPerMillisecond();

	/**
	 * @param powerMagnitude
	 * 		The magnitude of motor power for which the caller wants to know the number of ticks in a power change
	 * 		cycle.
	 *
	 * @return The number of ticks in a power change cycle when the motor is operating at powerMagnitude
	 */
	double getTicksPerMillisecond(PowerMagnitude powerMagnitude);

	/**
	 * @return The number of ticks in a power change cycle at maximum power
	 */
	double getTicksPerCycle();

	/**
	 * Get the number of ticks in a power change cycle at a given power magnitude.
	 *
	 * @param powerMagnitude
	 * 		The magnitude of motor power for which the caller wants the number of ticks in a power change cycle.
	 *
	 * @return The number of ticks in a power change cycle when the motor is operating at powerMagnitude.
	 */
	double getTicksPerCycle(PowerMagnitude powerMagnitude);

	int getCurrentTickNumber();

	int getNumberOfCycles(int ticksToMove, PowerVector powerVectorCurrent, PowerVector powerVectorRequested);

	/**
	 * Get the current proportion of power
	 *
	 * @return The proportion of power at which the motor is operating. A value in the range [-1, 1].
	 */
	abstract PowerVector getPowerVectorCurrent();

	/**
	 * Get the proportion of power to which to change the motor when ramping power up or down.
	 * <p>
	 * Must be public to allow access from unit test suite.
	 *
	 * @param powerVectorCurrent
	 * 		The current power vector.
	 * @param powerVectorRequested
	 * 		The power vector that the caller has requested.
	 * @param powerIncrementMagnitude
	 * 		The maximum magnitude by which we are allowed to change the power.
	 *
	 * @return The new proportion of power to apply to the motor.
	 */
	PowerVector getPowerVectorNew(PowerVector powerVectorCurrent, PowerVector powerVectorRequested);

	double getPower();

	/**
	 * Calculate when (what tick number) to start a ramp down.
	 * <p>
	 * A slowdown can occur with either positive or negative power. Consider a slowdown from 0.5 to 0 versus a slowdown
	 * from -0.5 to 0. Thus we cannot assume that the power at the start of the period is greater than the power at the
	 * end of the period.
	 * <p>
	 * A slowdown also can occur without a stop at 0. For example, we could slow down from -0.5 to 0.5 or from 0.5 to
	 * -0.5. *
	 *
	 * @param tickNumberAtStartOfPeriod
	 * 		The motor encoder tick reading at the start of the period (which includes not only the ramp down at the end
	 * 		but	any time/ticks running at speed before the ramp). This value can be positive or negative depending on
	 * 		the
	 * 		position of the motor relative to the last time the encoder was reset.
	 * @param numberOfTicksInPeriod
	 * 		The total number of ticks in the period. This value must be positive. The direction of travel is
	 * 		determined by
	 * 		the power at the start and end of the period, not by the number of ticks.
	 * @param powerAtStartOfPeriod
	 * 		The motor power at the start of the period, in the range [-1,1].
	 * @param powerAtEndOfPeriod
	 * 		The power that should be applied to the motor at the end of the period, in the range [-1,1]. Because the
	 * 		motor
	 * 		is slowing down, the absolute	value of the power at the end of the period must be less than the absolute
	 * 		value
	 * 		of the power at the beginning of the period.
	 *
	 * @return
	 */
	double getTickNumberToStartSlowDown(int tickNumberAtStartOfPeriod, PositiveInteger numberOfTicksInPeriod,
	                                    PowerVector powerAtStartOfPeriod, PowerVector powerAtEndOfPeriod);

	/**
	 * Determine whether to stop speeding up.
	 *
	 * @param ticksMaximum
	 * 		The maximum number of ticks that rampToEncoderTicks is supposed to move.
	 * @param secondsTimeout
	 * 		The maximum number of seconds for which rampToEncoderTicks is supposed to run.
	 * @param secondsRunning
	 * 		The number of seconds for which rampToEncoderTicks has been running.
	 * @param ticksMoved
	 * 		The number of ticks by which rampToEncoderTicks has already moved.
	 *
	 * @return True if we should stop speeding up; false otherwise.
	 */
	boolean isSpeedUpToEncoderTicksDone(PositiveInteger ticksMaximum, double secondsTimeout, double secondsRunning,
	                                    NonNegativeInteger ticksMoved) throws NoSuchMethodException;

	/**
	 * Determine whether a slowdown should be running.
	 * <p>
	 * The slowdown should start when we are at a tick number equal to (or greater than) the tick number to start the
	 * slowdown and continue until we are at a tick number equal to (or greater than) the tick number at the end of the
	 * period
	 *
	 * @param tickNumberAtStartOfPeriod
	 * 		The motor encoder tick number at which the period (including both the ramp and the portion at speed
	 * 		before the ramp started).
	 * @param tickNumberCurrent
	 * 		The current motor encoder tick number.
	 * @param numberOfTicksInPeriod
	 * 		The number of motor encoder ticks over which the ramp should extend. Must be non-negative.
	 * @param powerAtStart
	 * 		The motor power at which the ramp started.
	 * @param powerAtEnd
	 * 		The power the motor should be at when the ramp ends.
	 *
	 * @return True if changing the power should start or continue. False otherwise.
	 */
	boolean isSlowDownToEncoderTicksRunning(int tickNumberAtStartOfPeriod, int tickNumberCurrent, PositiveInteger
			numberOfTicksInPeriod, PowerVector powerAtStart, PowerVector powerAtEnd);

	void rampToPower(PowerVector powerVectorRequested);

	void rampToPower(PowerVector powerVectorRequested, PowerMagnitude powerIncrementMagnitude, int
			millisecondsCycleLength, PowerMagnitude powerToleranceMagnitude, double secondsTimeout);

	/**
	 * Set the magnitude by which the motor power will increment when ramping power up or down.
	 *
	 * @param powerMagnitude
	 * 		The magnitude by which the motor should increment when ramping power up or down.
	 */
	void setPowerMagnitudeIncrementPerCycle(PowerMagnitude powerMagnitude);

	/**
	 * Get the magnitude by which the motor power will increment when ramping up or down.
	 *
	 * @return The magnitude by which the motor power will increment when ramping up or down.
	 */
	PowerMagnitude getPowerMagnitudeIncrementPerCycle();

	/**
	 * Set the magnitude within which we will consider the requested motor power to have been reached.
	 *
	 * @param powerMagnitude
	 * 		The magnitude within which we will consider the requested motor power to have been reached.
	 */
	void setPowerMagnitudeTolerance(PowerMagnitude powerMagnitude);

	/**
	 * Set the power level of the motor without ramping.
	 * <p>
	 * Power is expressed as a fraction of the maximum possible power / speed supported according to the run mode in
	 * which the motor is operating.
	 * <p>
	 * Setting a power level of zero will brake the motor.
	 *
	 * @param powerVector
	 * 		The new power level of the motor, a value in the interval [-1.0, 1.0]
	 */
	void setPowerVector(PowerVector powerVector);

	void setStallDetectionToleranceInTicks(int ticks);

}
