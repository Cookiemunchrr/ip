package quu.exception;

/**
 * Signals that the argument given where a task number was expected is not a number.
 */
public class InvalidIndexException extends QuuException {

    /**
     * Constructs an exception naming the text that was not a task number.
     *
     * @param input Text that was expected to be a task number.
     */
    public InvalidIndexException(String input) {
        super("\"" + input + "\" isn't a task number");
    }
}
