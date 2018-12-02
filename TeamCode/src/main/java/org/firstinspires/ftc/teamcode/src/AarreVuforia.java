package org.firstinspires.ftc.teamcode.src;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Separate out the portions of the Vuforia code that do not depend on running on the robot so that we can unit test
 * them off-bot.
 */

public class AarreVuforia {

	private final XLogger log = XLoggerFactory.getXLogger("TEMP");
}
