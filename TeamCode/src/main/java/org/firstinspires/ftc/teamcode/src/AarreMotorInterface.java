package org.firstinspires.ftc.teamcode.src;

interface AarreMotorInterface {

	double getRevolutionsPerMinute();

	void setRevolutionsPerMinute(double revolutionsPerMinute);

	void setTicksPerRevolution(double ticksPerRevolution);

	int getMillisecondsPerCycle();

	double getTicksPerMinute();

	double getTicksPerRevolution();

	double getTicksPerSecond();

	double getTicksPerMillisecond();

	double getTicksPerCycle();

	int getCurrentTickNumber();

	int getNumberOfCycles(int ticksToMove, AarrePowerVector powerVectorCurrent, AarrePowerVector
			powerVectorRequested);

	AarrePowerVector getPowerVectorCurrent();

	AarrePowerVector getPowerVectorNew(AarrePowerVector powerVectorCurrent, AarrePowerVector
			powerVectorRequested, AarrePowerMagnitude powerIncrementMagnitude);

	double getPower();

	double getTickNumberToStartRampDown(int tickNumberAtStartOfPeriod, int numberOfTicksInPeriod,
	                                    AarrePowerVector powerAtStartOfPeriod, AarrePowerVector
			                                    powerAtEndOfPeriod);

	int getTicksInInterval(double power, int millisecondsInInterval);

	boolean isRampUpToEncoderTicksDone(int ticksMaximum, double secondsTimeout, double
			secondsRunning, int ticksMoved, AarrePowerVector powerDelta, AarrePowerVector
			powerCurrent);

	boolean isRampDownToEncoderTicksRunning(int tickNumberAtStartOfPeriod, int tickNumberCurrent,
	                                        int numberOfTicksInPeriod, AarrePowerVector
			                                        powerAtStart, AarrePowerVector powerAtEnd);

	void rampToPower(AarrePowerVector powerVectorRequested);

	void rampToPower(AarrePowerVector powerVectorRequested, AarrePowerMagnitude
			powerIncrementMagnitude, int millisecondsCycleLength, AarrePowerMagnitude
			powerToleranceMagnitude, double secondsTimeout);

	void setPowerVector(AarrePowerVector powerVector);

	@SuppressWarnings("WeakerAccess")
	void setStallDetectionToleranceInTicks(int ticks);

	void setupStallDetection(int timeLimitMs, int toleranceTicks);
}
