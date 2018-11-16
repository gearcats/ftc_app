package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Wrap Telemetry class to provide Telemetry.log methods with same interface as Telemetry.addData methods.
 * This makes it easier to switch back and forth between Telemetry.addData and Telemetry.log in other classes.
 */
class AarreTelemetry {

    private final Telemetry     ftcTelemetry;
    private final Telemetry.Log log;

    AarreTelemetry(Telemetry ftcTelemetry) {

        this.ftcTelemetry = ftcTelemetry;
        this.log = ftcTelemetry.log();

    }

    /**
     * Add a simple string to telemetry.
     *
     * In this class, all items added to telemetry are also logged.
     *
     * @param caption A caption for the telemetry entry.
     * @param message The telemetry message associated with the caption.
     */
    void addData(java.lang.String caption, java.lang.String message) {

        ftcTelemetry.addData(caption, message);
        log.add(caption, message);

    }

    /**
     * Add a formatted string to telemetry.
     *
     * In this class, all items added to telemetry are also logged.
     *
     * @param caption A caption for the telemetry entry.
     * @param message A printf-formatted telemetry message associated with the caption.
     * @param args Arguments to the printf-formatted message.
     */
    void addData(java.lang.String caption, java.lang.String message, java.lang.Object... args) {

        ftcTelemetry.addData(caption, message, args);
        log(caption, message, args);
    }

    /**
     * Append a simple message (with a caption) to the log.
     *
     * @param caption A caption for the log entry.
     * @param message The log entry message associated with the caption.
     */
    void log(java.lang.String caption, java.lang.String message) {

        log.add(caption + ": " + message);

    }

    /**
     * Append a formatted message (without a caption) to the log.
     *
     * @param message A printf-formatted message to be logged.
     * @param args Arguments to the printf-formatted message.
     */
    void log(java.lang.String message, java.lang.Object... args) {

        log.add(message, args);

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
        log_message = String.format(log_message, args);
        log.add(log_message);

    }

    void update() {
        this.ftcTelemetry.update();
    }

}
