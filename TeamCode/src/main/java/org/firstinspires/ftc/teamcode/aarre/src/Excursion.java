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

	public PowerVector getPowerVector() {
		return powerVector;
	}

	public NonNegativeDouble getSecondsTimeout() {
		return secondsTimeout;
	}

	public SlowDown getSlowDown() {
		return slowdown;
	}

	public SpeedUp getSpeedUp() {
		return speedup;
	}

	public NonNegativeInteger getTicksToRotate() {
		return ticksToRotate;
	}

	public NonNegativeInteger getTicksToSlowDown() {
		return getSlowDown().getTicksToRotate();
	}

	public NonNegativeInteger getTicksToSpeedUp() {
		return getSpeedUp().getTicksToRotate();
	}

	public void setPowerVector(PowerVector powerVector) {
		this.powerVector = powerVector;
	}

	public void setSecondsTimeout(NonNegativeDouble secondsTimeout) {
		this.secondsTimeout = secondsTimeout;
	}

	public void setSlowDown(SlowDown slowdown) {
		this.slowdown = slowdown;
	}

	public void setSpeedUp(SpeedUp speedup) {
		this.speedup = speedup;
	}

	public void setTicksToRotate(NonNegativeInteger ticksToRotate) {
		this.ticksToRotate = ticksToRotate;
		NonNegativeInteger ticksToSpeedUp = new NonNegativeInteger((int) Math.round(ticksToRotate.doubleValue() /
				2.0));
		NonNegativeInteger ticksToSlowDown = new NonNegativeInteger(ticksToRotate.intValue() - ticksToSpeedUp.intValue
				());
		getSpeedUp().setTicksToRotate(ticksToSpeedUp);
		getSlowDown().setTicksToRotate(ticksToSlowDown);
	}
}
