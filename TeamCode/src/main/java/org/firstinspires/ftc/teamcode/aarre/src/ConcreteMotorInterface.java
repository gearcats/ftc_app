package org.firstinspires.ftc.teamcode.aarre.src;

interface ConcreteMotorInterface extends MotorInterface {

	NonNegativeDouble getRevolutionsPerMinute();

	NonNegativeDouble getRevolutionsPerMinute(PowerMagnitude powerMagnitude);

	NonNegativeDouble getTicksPerRevolution();

}
