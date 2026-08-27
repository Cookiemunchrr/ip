package quu.exception;

/**
 * Signals that the user typed a command Quu does not recognise, or that the save
 * file contains an unknown task type letter.
 */
public class UnknownCommandException extends QuuException {

    /**
     * Constructs an exception naming the unrecognised command.
     *
     * @param command Command word that was not recognised.
     */
    public UnknownCommandException(String command) {
        super("I don't know what \"" + command + "\" does");
    }
}
