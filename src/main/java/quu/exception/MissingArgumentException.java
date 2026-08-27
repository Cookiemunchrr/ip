package quu.exception;

/**
 * Signals that a command was entered without all the arguments it needs.
 */
public class MissingArgumentException extends QuuException{
    /**
     * Constructs an exception showing the user the expected command format.
     *
     * @param usage The correct usage of the command, for example {@code todo <task>}.
     */
    public MissingArgumentException(String usage) {
        super("Invalid format. Please follow this format: " + usage);
    }
}
