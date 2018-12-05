package org.firstinspires.ftc.teamcode.aarre.src;

/**
 * Track a ramp (gradual change) in speed, either a ramp up (a speedup) or a ramp down (a slowdown).
 * <p>
 * Note the distinction between a ramp (speed changes once, either up or down) and an {@link Excursion} (speed changes
 * twice, first up and then down.
 */
public abstract class Ramp {

	private NonNegativeInteger ticksToRotate;

	public Ramp() {
		ticksToRotate = new NonNegativeInteger(0);
	}

	public NonNegativeInteger getTicksToRotate() {
		return ticksToRotate;
	}

	public void setTicksToRotate(NonNegativeInteger ticksToRotate) {
		this.ticksToRotate = ticksToRotate;
	}
}
