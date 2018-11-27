package org.firstinspires.ftc.teamcode.src;

interface AarreMotorInterface {

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

    AarrePowerVector getPowerVectorCurrent();

    AarrePowerVector getPowerVectorNew(AarrePowerVector powerVectorCurrent, AarrePowerVector powerVectorRequested,
                                       AarrePowerMagnitude powerIncrementMagnitude);

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

    void setPowerVector(AarrePowerVector powerVector);

    void setStallDetectionToleranceInTicks(int ticks);

}
