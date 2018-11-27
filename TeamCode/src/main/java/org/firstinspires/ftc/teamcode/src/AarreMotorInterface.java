package org.firstinspires.ftc.teamcode.src;

abstract interface AarreMotorInterface {

    double getRevolutionsPerMinute();

    double getRevolutionsPerMinute(AarrePowerMagnitude powerMagnitude);

    void setRevolutionsPerMinute(double revolutionsPerMinute);

    int getMillisecondsPerCycle();

    double getTicksPerMinute();

    double getTicksPerMinute(AarrePowerMagnitude powerMagnitude);

    double getTicksPerRevolution();

    void setTicksPerRevolution(double ticksPerRevolution);

    double getTicksPerSecond();

    double getTicksPerSecond(AarrePowerMagnitude powerMagnitude);

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
    double getTicksPerMillisecond(AarrePowerMagnitude powerMagnitude);

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
    double getTicksPerCycle(AarrePowerMagnitude powerMagnitude);

    int getCurrentTickNumber();

    int getNumberOfCycles(int ticksToMove, AarrePowerVector powerVectorCurrent, AarrePowerVector powerVectorRequested);

	/**
	 * Get the current proportion of power
	 *
	 * @return The proportion of power at which the motor is operating. A value in the range [-1, 1].
	 */
	abstract AarrePowerVector getPowerVectorCurrent();

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
	AarrePowerVector getPowerVectorNew(AarrePowerVector powerVectorCurrent, AarrePowerVector powerVectorRequested);

    double getPower();

    double getTickNumberToStartRampDown(int tickNumberAtStartOfPeriod, int numberOfTicksInPeriod, AarrePowerVector
            powerAtStartOfPeriod, AarrePowerVector powerAtEndOfPeriod);

    boolean isRampUpToEncoderTicksDone(int ticksMaximum, double secondsTimeout, double secondsRunning, int
            ticksMoved, AarrePowerVector powerDelta, AarrePowerVector powerCurrent);

    boolean isRampDownToEncoderTicksRunning(int tickNumberAtStartOfPeriod, int tickNumberCurrent, int
            numberOfTicksInPeriod, AarrePowerVector powerAtStart, AarrePowerVector powerAtEnd);

    void rampToPower(AarrePowerVector powerVectorRequested);

    void rampToPower(AarrePowerVector powerVectorRequested, AarrePowerMagnitude powerIncrementMagnitude, int
            millisecondsCycleLength, AarrePowerMagnitude powerToleranceMagnitude, double secondsTimeout);

	/**
	 * Set the magnitude by which the motor power will increment when ramping power up or down.
	 *
	 * @param powerMagnitude
	 * 		The magnitude by which the motor should increment when ramping power up or down.
	 */
	void setPowerMagnitudeIncrementPerCycle(AarrePowerMagnitude powerMagnitude);

	/**
	 * Get the magnitude by which the motor power will increment when ramping up or down.
	 *
	 * @return The magnitude by which the motor power will increment when ramping up or down.
	 */
	AarrePowerMagnitude getPowerMagnitudeIncrementPerCycle();

	/**
	 * Set the magnitude within which we will consider the requested motor power to have been reached.
	 *
	 * @param powerMagnitude
	 * 		The magnitude within which we will consider the requested motor power to have been reached.
	 */
	void setPowerMagnitudeTolerance(AarrePowerMagnitude powerMagnitude);

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
	void setPowerVector(AarrePowerVector powerVector);

    void setStallDetectionToleranceInTicks(int ticks);

}
