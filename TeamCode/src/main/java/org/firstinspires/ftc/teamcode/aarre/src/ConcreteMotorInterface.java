package org.firstinspires.ftc.teamcode.aarre.src;

import com.qualcomm.robotcore.hardware.DcMotor;

interface ConcreteMotorInterface extends AarreMotorInterface {

	DcMotor getMotor();

	double getTicksPerRevolution();

	double getRevolutionsPerMinute();

	double getRevolutionsPerMinute(AarrePowerMagnitude powerMagnitude);

}
