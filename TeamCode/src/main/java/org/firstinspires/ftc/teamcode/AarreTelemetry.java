package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Wrap Telemetry class to provide Telemetry.log methods with same interface as Telemetry.addData methods.
 *
 * This makes it easier to log messages to both the driver station phone and the robot controller log. This is
 * convenient because it (a) allows visibility into more messages than fit on the driver station phone and
 * (b) provides a longer-lasting record of messages than the driver station phone, which resets after
 * 30 seconds in the autonomous mode.
 *
 * This also makes it easier to switch back and forth between calling the Telemetry.addData and Telemetry.log
 * methods in other classes.
 */
class AarreTelemetry {

    private final Telemetry     underlyingTelemetry;
    private final Telemetry.Log telemetryLog;

    private boolean CAREFUL_LOGGING = false;


    /**
     * Base constructor.
     *
     * @param telemetry The underlying @link{Telemetry} instance.
     */
    AarreTelemetry(final Telemetry telemetry) {

        underlyingTelemetry = telemetry;
        underlyingTelemetry.setAutoClear(false);
        telemetryLog = telemetry.log();

    }

    /**
     * Constructor with flag for careful logging.
     *
     * @param underlyingTelemetry The underlying @link{Telemetry} instance.
     * @param carefulLogging Whether to do logging "carefully." If false, there will be no delays between calls
     *                        to log messages, which can result in "folded" log entries where more than one log
     *                        entry is listed under a given time. If true, there will be a small delay between
     *                        calls to log messages, which will prevent "folded" log entries.
     */
    @SuppressWarnings("SameParameterValue")
    AarreTelemetry(@SuppressWarnings("ParameterHidesMemberVariable") final Telemetry underlyingTelemetry, final boolean carefulLogging) {

        this(underlyingTelemetry);
        CAREFUL_LOGGING = carefulLogging;

    }

    /**
     * Add a simple string to telemetry.
     *
     * In this class, all items added to telemetry are also logged, both to the driver station screen and
     * to the robot controller log file.
     *
     * @param caption A caption for the telemetry entry.
     * @param message The telemetry message associated with the caption.
     */
    final void addData(final java.lang.String caption, final java.lang.String message) {

        underlyingTelemetry.addData(caption, message);
        log(caption, message);

    }

    /**
     * Add a formatted string to telemetry.
     *
     * In this class, all items added to telemetry are also logged, both to the driver station screen and
     * to the robot controller log file.
     *
     * @param caption A caption for the telemetry entry.
     * @param message A printf-formatted telemetry message associated with the caption.
     * @param args Arguments to the printf-formatted message.
     */
    final void addData(final java.lang.String caption, final java.lang.String message, final java.lang.Object... args) {

        underlyingTelemetry.addData(caption, message, args);
        log(caption, message, args);
    }

    /**
     * Append a simple message to the log.
     *
     * All other methods should ultimately call this one, because it also appends the message to the
     * robot controller log.
     *
     * @param message The message to append to the log.
     */
    void log(final java.lang.String message) {

        if (CAREFUL_LOGGING) {

            // Wait a couple of milliseconds between log entries to ensure that every entry has its
            // own line in the log. This can make it easier to find and read log entries. It is probably not
            // a good idea to have this set during competition, though....

            try {
                Thread.sleep(2L);
            } catch (final InterruptedException e) {
                log("Sleep interrupted!");
            }
        }

        telemetryLog.add(message);
        syslog(message);
    }

    /**
     * Append a simple message (with a caption) to the log.
     *
     * @param caption A caption for the log entry.
     * @param message The log entry message associated with the caption.
     */
    void log(final java.lang.String caption, final java.lang.String message) {

        log(caption + ": " + message);
    }

    /**
     * Append a formatted message (without a caption) to the log.
     *
     * @param message A printf-formatted message to be logged.
     * @param args Arguments to the printf-formatted message.
     */
    void log(final java.lang.String message, final java.lang.Object... args) {

        final String logMessage = String.format(message, args);
        log(logMessage);

    }

    /**
     * Append a formatted message (with a caption) to the log.
     *
     * @param caption A caption for the log entry.
     * @param message A printf-formatted message associated with the caption.
     * @param args Arguments to the printf-formatted message.
     */
    void log(final java.lang.String caption, final java.lang.String message, final java.lang.Object... args) {

        final String logMessage = caption + ": " + message;
        log(logMessage, args);

    }

    /**
     * Append a message to the robot controller log.
     *
     * @param message A message to append to the robot controller log.
     */
    private void syslog(final String message) {

        System.out.println(message);

    }

    void update() {

        underlyingTelemetry.update();

    }

}
