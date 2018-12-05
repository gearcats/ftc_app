package org.firstinspires.ftc.teamcode.aarre.src;

/**
 * A motor excursion is the travel of a motor in a single direction with acceleration (ramping up / speeding up) at the
 * beginning and deceleration (ramping down / slowing down) at the end.
 */
public class Excursion {

	private SlowDown slowdown;
	private SpeedUp  speedup;

	public Excursion() {
		setSlowDown(new SlowDown());
		setSpeedUp(new SpeedUp());
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
}
