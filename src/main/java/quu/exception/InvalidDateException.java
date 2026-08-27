package quu.exception;

/**
 * Signals that a date typed by the user, or read from the save file, is not in
 * the expected yyyy-mm-dd form.
 */
public class InvalidDateException extends QuuException {

    /**
     * Constructs an exception naming the text that could not be parsed.
     *
     * @param input Text that was expected to be a date.
     */
    public InvalidDateException(String input) {
        super("'" + input + "' is not a valid date. Use yyyy-mm-dd, e.g. 2026-06-06.");
    }
}
