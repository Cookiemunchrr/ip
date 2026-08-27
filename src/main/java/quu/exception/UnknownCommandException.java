package quu.exception;

/**
 * Signals that the user entered a command word that Quu does not recognise.
 */
public class UnknownCommandException extends QuuException{
    /**
     * Constructs an exception naming the unrecognised command.
     *
     * @param command The command word that Quu does not support.
     */
    public UnknownCommandException(String command) {
        super("I don't know what \"" + command + "\" does");
    }
}
