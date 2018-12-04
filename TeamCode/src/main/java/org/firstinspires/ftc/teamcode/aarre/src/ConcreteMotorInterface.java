package org.firstinspires.ftc.teamcode.aarre.src;

interface ConcreteMotorInterface extends MotorInterface {

	double getTicksPerRevolution();

	double getRevolutionsPerMinute();

	double getRevolutionsPerMinute(PowerMagnitude powerMagnitude);

}
