package org.firstinspires.ftc.teamcode.aarre.src;

interface ConcreteMotorInterface extends MotorInterface {

	NonNegativeDouble getTicksPerRevolution();

	NonNegativeDouble getRevolutionsPerMinute();

	NonNegativeDouble getRevolutionsPerMinute(PowerMagnitude powerMagnitude);

}
