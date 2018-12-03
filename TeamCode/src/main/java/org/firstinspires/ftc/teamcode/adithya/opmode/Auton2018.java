package org.firstinspires.ftc.teamcode.adithya.opmode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.*;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
import java.util.Locale;


@Autonomous

public class Auton2018 extends LinearOpMode {
	//variables assosiated with vuforia/TFod
	private static final String               TFOD_MODEL_ASSET     = "RoverRuckus.tflite";
	private static final String               LABEL_GOLD_MINERAL   = "Gold Mineral";
	private static final String               LABEL_SILVER_MINERAL = "Silver Mineral";
	private static final String               VUFORIA_KEY          = "AQL3GSz/////AAABmU0iuBTYZUZyqsDIbBriyR5+fo3kTV9e8dpVguROyqV8Cfa//LkjRFXUIK22aroE9+JL+K6fSkFKoR7WmLw6Rh9+/726CK1FGzcdaZ62wVJlP2Ksex6H2FLpJevxHcfBLTsywxWuLYDGwxCywCmgb9zrzyjGWj8z9Qd+0wkg9GQolVdEaZVd1p8xNNHicFRyf5X5u1pna8F/GIm9xXBTtmURyo/msigPolDTqKLBIN+60S609XoVM4dE/d7NYNGILqj6WVZfPqRy+NyWMfuc3os1RiUWWxK7UbVE7yDrUJeRS5fy6ikVZJaSdn8+zkWDpPayrDVncwf9+kTL4aExzdjQwSbn32X5OWXWOOpoB3no";
	//variables assositated with the IMU
	private final        BNO055IMU.Parameters parameters           = new BNO055IMU.Parameters();//the parameters we will later set to inizialize the imu
	public               String               goldPosition;
	public               int                  objectsDetected;
	public               String               status;
	public               String               calib;
	public               String               heading;
	public               String               roll;
	public               String               pitch;
	public               String               grvty;
	public               String               mag;
	public               String               robotVelocity;
	public               Orientation          angles;
	public               Acceleration         gravity;
	//motors
	private              DcMotor              left;
	private              DcMotor              right;
	private              DcMotor              riser;
	private              DcMotor              arm;
	private              Servo                hook;
	private              VuforiaLocalizer     vuforia;
	private              TFObjectDetector     tfod;
	private              BNO055IMU            imu;

	public void wait(int milliseconds) {
		sleep(milliseconds);
	}

	public void persiseTurn(double degrees, double motorPower) {

		//the groscope wraps at 179.9 and -179.9
		if (degrees > 179.9) {
			degrees = 179.9;
		} else if (degrees < -179.9) {
			degrees = -179.9;
		}

		telemetry.update();
		double headingDouble = Double.parseDouble(heading);


		double goal = headingDouble + degrees;
		if (degrees < 0)//we want to turn to the left
		{
			while (headingDouble >= goal) {
				left.setPower(-motorPower);//left is mounted backward
				right.setPower(-motorPower);//we want the right motor to spin the oppisite diretion from the left motor

				getImuDataAndOutputToTelemtry();
				telemetry.update();

				headingDouble = Double.parseDouble(heading);
			}
		} else if (degrees > 0)//we want ot turn to the right
		{
			while (headingDouble <= goal) {
				left.setPower(motorPower);
				right.setPower(motorPower);

				getImuDataAndOutputToTelemtry();
				telemetry.update();

				headingDouble = Double.parseDouble(heading);
			}
		}
		left.setPower(0);
		right.setPower(0);
		imu.initialize(parameters);//reinialize the imu to set it to zero
	}

	public void persiseTurnTo(double degrees, double motorPower) {

		//the groscope wraps at 179.9 and -179.9
		if (degrees > 179.9) {
			degrees = 179.9;
		} else if (degrees < -179.9) {
			degrees = -179.9;
		}

		telemetry.update();
		double headingDouble = Double.parseDouble(heading);

		double goal = degrees;


		while (headingDouble < goal) {
			left.setPower(motorPower);
			right.setPower(motorPower);

			getImuDataAndOutputToTelemtry();
			telemetry.update();

			headingDouble = Double.parseDouble(heading);
		}
		left.setPower(0);
		right.setPower(0);
		imu.initialize(parameters);//reinialize the imu to set it to zero
	}

	public void persiseMove(double cm, double motorPower) {
		final double WHEEL_DIAMETER_IN_CM = 16;
		final double WHEEL_CICUM          = WHEEL_DIAMETER_IN_CM * java.lang.Math.PI;
		final double RATIO                = 1440.0 / WHEEL_CICUM;//the ratio between ticks and cm moved

		final int goalLeft  = -1 * (int) ((cm * RATIO) + left.getCurrentPosition());
		final int goalRight = (int) ((cm * RATIO) + right.getCurrentPosition());

		telemetry.addData("goal right", goalRight);
		telemetry.addData("goal left", goalLeft);

		telemetry.update();

		if (cm > 0) {
			while (left.getCurrentPosition() > goalLeft && right.getCurrentPosition() < goalRight) {
				left.setPower(-motorPower);//left is mounted backwards and we wan t to go forwards
				right.setPower(motorPower);

				telemetry.addData("left", left.getCurrentPosition());
				telemetry.addData("right", right.getCurrentPosition());
				telemetry.update();
			}
		} else if (cm < 0) {
			while (left.getCurrentPosition() < goalLeft && right.getCurrentPosition() > goalRight) {
				left.setPower(motorPower);//left is mounted backwards and we want to go backwards
				right.setPower(-motorPower);

				telemetry.addData("left", left.getCurrentPosition());
				telemetry.addData("right", right.getCurrentPosition());
				telemetry.update();
			}
		} else {
			telemetry.addData("Bozo", "why would you move 0 cm.");
			telemetry.update();
		}
		left.setPower(0);
		right.setPower(0);
	}

	public void checkForMinerals() {
		if (tfod != null) {
			// getUpdatedRecognitions() will return null if no new information is available since
			// the last time that call was made.
			List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
			if (updatedRecognitions != null) {
				telemetry.addData("# Object Detected", updatedRecognitions.size());
				objectsDetected = updatedRecognitions.size();
				if (updatedRecognitions.size() == 3) {
					int goldMineralX    = -1;
					int silverMineral1X = -1;
					int silverMineral2X = -1;
					for (Recognition recognition : updatedRecognitions) {
						if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
							goldMineralX = (int) recognition.getLeft();
						} else if (silverMineral1X == -1) {
							silverMineral1X = (int) recognition.getLeft();
						} else {
							silverMineral2X = (int) recognition.getLeft();
						}
					}
					if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
						if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
							telemetry.addData("Gold Mineral Position", "Left");
							goldPosition = "left";
						} else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
							telemetry.addData("Gold Mineral Position", "Right");
							goldPosition = "right";
						} else if (goldMineralX < silverMineral1X && goldMineralX > silverMineral2X) {
							telemetry.addData("Gold Mineral Position", "Center");
							goldPosition = "center";
						}
					}
				}
				telemetry.update();
			}
		}
	}

	public void runOpMode() {
		telemetry.addData("Loading. Wait three seconds before you run.", "");
		telemetry.update();
		//create an object paramters to pass to the inizialize function of BNO055IMU
		parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
		parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
		parameters.calibrationDataFile = "BNO055IMUCalibration.json";
		parameters.loggingEnabled = true;
		parameters.loggingTag = "IMU";
		parameters.accelerationIntegrationAlgorithm = null;

		//map the imu object to the BNO055IMU in the configuration on the Robot Controller phone:
		imu = hardwareMap.get(BNO055IMU.class, "imu");
		//initialize the imu by passing the parameters object to it
		imu.initialize(parameters);

		getImuDataAndOutputToTelemtry();

		//map the objects to different parts of the configuration
		left = hardwareMap.get(DcMotor.class, "left");
		right = hardwareMap.get(DcMotor.class, "right");
		arm = hardwareMap.get(DcMotor.class, "arm");
		riser = hardwareMap.get(DcMotor.class, "riser");
		hook = hardwareMap.get(Servo.class, "hook");

		initVuforia();//setup vuforia
		initTfod();//setup tensorFlow

		waitForStart();

		if (opModeIsActive()) {

			/** Activate Tensor Flow Object Detection. */
			if (tfod != null) {
				tfod.activate();
			}


			telemetry.update();

			//The motors start movin' and doin' cool stuff here

			//lower riser
			riser.setPower(1);
			wait(6000);
			riser.setPower(0);

			//move the hook
			hook.setPosition(0.75);

			//back up a little so as to not run into the lander
			persiseMove(30.0, 0.8);

			while (opModeIsActive()) {
				getImuDataAndOutputToTelemtry();
				checkForMinerals();
			}
		}

		if (tfod != null) {
			tfod.shutdown();
		}
	}

	/**
	 * Initialize the Vuforia localization engine.
	 */
	private void initVuforia() {
		/*
		 * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
		 */
		VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

		parameters.vuforiaLicenseKey = VUFORIA_KEY;
		parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

		//  Instantiate the Vuforia engine
		vuforia = ClassFactory.getInstance().createVuforia(parameters);

		// Loading trackables is not necessary for the Tensor Flow Object Detection engine.
	}

	/**
	 * Initialize the Tensor Flow Object Detection engine.
	 */
	private void initTfod() {
		int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
		tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
		tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
	}

	public String formatAngle(AngleUnit angleUnit, double angle) {
		return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
	}

	public String formatDegrees(double degrees) {
		return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
	}

	public void getImuDataAndOutputToTelemtry() {
		telemetry.clearAll();//wipe the telemetry so we don't overload it

		// get data from the IMU that we will then display in separate lines.
		telemetry.addAction(new Runnable() {
			@Override
			public void run() {
				//get the absolute rotation on all three axis from the imu
				angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
				//get the gravity from the imu
				gravity = imu.getGravity();
			}
		});

		telemetry.addLine().addData("IMU status", new Func<String>() {
			@Override
			public String value() {
				status = imu.getSystemStatus().toShortString();
				return imu.getSystemStatus().toShortString();
			}
		}).addData("calib", new Func<String>() {
			@Override
			public String value() {
				calib = imu.getCalibrationStatus().toString();
				return imu.getCalibrationStatus().toString();
			}
		});

		telemetry.addLine().addData("heading", new Func<String>() {
			@Override
			public String value() {
				heading = formatAngle(angles.angleUnit, angles.firstAngle);
				return formatAngle(angles.angleUnit, angles.firstAngle);
			}
		}).addData("roll", new Func<String>() {
			@Override
			public String value() {
				roll = formatAngle(angles.angleUnit, angles.secondAngle);
				return formatAngle(angles.angleUnit, angles.secondAngle);
			}
		}).addData("pitch", new Func<String>() {
			@Override
			public String value() {
				pitch = formatAngle(angles.angleUnit, angles.secondAngle);
				return formatAngle(angles.angleUnit, angles.thirdAngle);
			}
		});

		telemetry.addLine().addData("grvty", new Func<String>() {
			@Override
			public String value() {
				grvty = gravity.toString();
				return gravity.toString();
			}
		}).addData("mag", new Func<String>() {
			@Override
			public String value() {
				mag = String.format(Locale.getDefault(), "%.3f", Math.sqrt(gravity.xAccel * gravity.xAccel + gravity.yAccel * gravity.yAccel + gravity.zAccel * gravity.zAccel));
				return String.format(Locale.getDefault(), "%.3f", Math.sqrt(gravity.xAccel * gravity.xAccel + gravity.yAccel * gravity.yAccel + gravity.zAccel * gravity.zAccel));
			}
		});
		telemetry.addLine().addData("velocity", new Func<String>() {
			@Override
			public String value() {
				robotVelocity = imu.getVelocity().toString();
				return robotVelocity;
			}
		});
	}
}