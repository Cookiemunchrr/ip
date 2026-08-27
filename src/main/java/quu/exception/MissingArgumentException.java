package quu.exception;

/**
 * Signals that a command was given without all the arguments it needs.
 * The message shows the expected usage so the user can retype the command.
 */
public class MissingArgumentException extends QuuException {

    /**
     * Constructs an exception showing the expected command format.
     *
     * @param usage Expected usage, for example {@code todo <task>}.
     */
    public MissingArgumentException(String usage) {
        super("Invalid format. Please follow this format: " + usage);
    }
}
