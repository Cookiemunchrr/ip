package quu.exception;

/**
 * Thrown when the first word of the input does not name a command the chatbot knows.
 */
public class UnknownCommandException extends QuuException {

    /**
     * Creates the exception, quoting the unrecognised command back to the user.
     *
     * @param command the word that was not recognised
     */
    public UnknownCommandException(String command) {
        super("I don't know what \"" + command + "\" does");
    }
}
