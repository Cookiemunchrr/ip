package quu.exception;

/**
 * Base class for every error Quu reports to the user.
 * The message of a {@code QuuException} is written straight to the console, so
 * it should read as an explanation aimed at the user rather than at a developer.
 */
public class QuuException extends Exception {

    /**
     * Constructs an exception carrying a user-facing message.
     *
     * @param message Text explaining what went wrong.
     */
    public QuuException(String message) {
        super(message);
    }
}
