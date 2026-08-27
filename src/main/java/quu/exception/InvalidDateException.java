package quu.exception;

/**
 * Signals that a date supplied by the user could not be understood.
 */
public class InvalidDateException extends QuuException{
    /**
     * Constructs an exception naming the text that failed to parse as a date.
     *
     * @param input The text that was not a valid {@code yyyy-mm-dd} date.
     */
    public InvalidDateException(String input) {
        super("'" + input + "' is not a valid date. Use yyyy-mm-dd, e.g. 2026-06-06.");
    }
}
