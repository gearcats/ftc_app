package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.hardware.DcMotor;

interface ConcreteMotorInterface extends MotorInterface {

	DcMotor getMotor();

	double getTicksPerRevolution();

	double getRevolutionsPerMinute();

	double getRevolutionsPerMinute(PowerMagnitude powerMagnitude);

}
