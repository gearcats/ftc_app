package org.firstinspires.ftc.teamcode.aarre.src;

/**
 * A motor excursion is the travel of a motor in a single direction with acceleration (ramping up / speeding up) at the
 * beginning and deceleration (ramping down / slowing down) at the end.
 */
public class Excursion {

	private PowerVector        powerVector;
	private NonNegativeDouble  secondsTimeout;
	private SlowDown           slowdown;
	private SpeedUp            speedup;
	private NonNegativeInteger ticksToRotate;

	public Excursion() {
		setPowerVector(new PowerVector(0.0));
		setSecondsTimeout(new NonNegativeDouble(0.0));
		setSlowDown(new SlowDown());
		setSpeedUp(new SpeedUp());
		setTicksToRotate(new NonNegativeInteger(1));
	}

	public SpeedUp getSpeedUp() {
		return speedup;
	}

	public void setSpeedUp(SpeedUp speedup) {
		this.speedup = speedup;
	}

	public SlowDown getSlowDown() {
		return slowdown;
	}

	public void setSlowDown(SlowDown slowdown) {
		this.slowdown = slowdown;
	}

	public PowerVector getPowerVector() {
		return powerVector;
	}

	public void setPowerVector(PowerVector powerVector) {
		this.powerVector = powerVector;
	}

	public NonNegativeInteger getTicksToRotate() {
		return ticksToRotate;
	}

	public void setTicksToRotate(NonNegativeInteger ticksToRotate) {
		this.ticksToRotate = ticksToRotate;
	}

	public NonNegativeDouble getSecondsTimeout() {
		return secondsTimeout;
	}

	public void setSecondsTimeout(NonNegativeDouble secondsTimeout) {
		this.secondsTimeout = secondsTimeout;
	}
}
