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

    private final Telemetry     ftcTelemetry;
    private final Telemetry.Log ftcLog;
    private final boolean CAREFUL_LOGGING;

    AarreTelemetry(Telemetry ftcTelemetry) {

        this.ftcTelemetry = ftcTelemetry;
        this.ftcTelemetry.setAutoClear(false);
        this.ftcLog = ftcTelemetry.log();

        this.CAREFUL_LOGGING = true;

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
    void addData(java.lang.String caption, java.lang.String message) {

        ftcTelemetry.addData(caption, message);
        this.log(caption, message);

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
    void addData(java.lang.String caption, java.lang.String message, java.lang.Object... args) {

        ftcTelemetry.addData(caption, message, args);
        this.log(caption, message, args);
    }

    /**
     * Append a simple message to the log.
     *
     * All other methods should ultimately call this one, because it also appends the message to the
     * robot controller log.
     *
     * @param message The message to append to the log.
     */
    void log(java.lang.String message) {
        ftcLog.add(message);
        this.syslog(message);
    }

    /**
     * Append a simple message (with a caption) to the log.
     *
     * @param caption A caption for the log entry.
     * @param message The log entry message associated with the caption.
     */
    void log(java.lang.String caption, java.lang.String message) {
        this.log(caption + ": " + message);
    }

    /**
     * Append a formatted message (without a caption) to the log.
     *
     * @param message A printf-formatted message to be logged.
     * @param args Arguments to the printf-formatted message.
     */
    void log(java.lang.String message, java.lang.Object... args) {

        String log_message = String.format(message, args);
        this.log(log_message);

    }

    /**
     * Append a formatted message (with a caption) to the log.
     *
     * @param caption A caption for the log entry.
     * @param message A printf-formatted message associated with the caption.
     * @param args Arguments to the printf-formatted message.
     */
    void log(java.lang.String caption, java.lang.String message, java.lang.Object... args) {

        String log_message = caption + ": " + message;
        this.log(log_message, args);

    }

    /**
     * Append a message to the robot controller log.
     *
     * @param message A message to append to the robot controller log.
     */
    void syslog(String message) {

        System.out.println(message);

    }

    void update() {

        this.ftcTelemetry.update();

    }

}
