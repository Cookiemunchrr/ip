package quu.exception;

import java.time.LocalDate;

/**
 * Signals that an event's end date falls before its start date.
 */
public class InvalidDurationException extends QuuException {

    /**
     * Constructs an exception naming the period that was rejected.
     *
     * @param eventStart Start date of the event.
     * @param eventEnd End date of the event, which falls before the start date.
     */
    public InvalidDurationException(LocalDate eventStart, LocalDate eventEnd) {
        super(String.format("%s to %s is not a valid duration", eventStart, eventEnd));
    }
}
