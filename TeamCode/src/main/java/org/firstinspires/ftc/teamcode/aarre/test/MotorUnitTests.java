package org.firstinspires.ftc.teamcode.aarre.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.logging.*;

/**
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Aarre Motor Unit Tests", group = "Aarre")
@Disabled
public abstract class MotorUnitTests extends LinearOpMode implements MotorUnitTestsInterface {

	private static Logger log;

	static {

		log = Logger.getLogger("MotorUnitTests");
		log.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		handler.setFormatter(new SimpleFormatter() {

			private static final String format = "%1$tF %1$tT [%2$s] %4$s::%5$s - %6$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				String formattedLogRecord = String.format(format, new Date(lr.getMillis()), lr.getLevel()
						.getLocalizedName(), lr.getLoggerName(), lr.getSourceClassName(), lr.getSourceMethodName(), lr
						.getMessage());
				return formattedLogRecord;
			}

		});
		log.addHandler(handler);
		log.setLevel(Level.ALL);
	}

	protected MotorUnitTests() {
	}

	@Test
	@Override
	public void runOpMode() {
	}


	/**
	 * The tick number to start a slow down should depend on the speed at the start of the slowdown but not on the
	 * speed
	 * in the middle of the slowdown.
	 */

	@Override
	@Test
	public void testSetDirection() {
		getMotor().setDirection(DcMotorSimple.Direction.REVERSE);
	}

}
