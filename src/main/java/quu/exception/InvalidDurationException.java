package quu.exception;

import java.time.LocalDate;

public class InvalidDurationException extends QuuException{
    public InvalidDurationException(LocalDate eventStart, LocalDate eventEnd) {
        super(String.format("%s to %s is not a valid duration", eventStart, eventEnd));
    }
}
