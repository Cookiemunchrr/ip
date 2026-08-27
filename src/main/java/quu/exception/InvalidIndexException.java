package quu.exception;

/**
 * Signals that the user supplied something other than a number where a task number was expected.
 */
public class InvalidIndexException extends QuuException{
    /**
     * Constructs an exception naming the text that was not a task number.
     *
     * @param input The text supplied in place of a task number.
     */
    public InvalidIndexException(String input) {
        super("\"" + input +  "\" isn't a task number");
    }
}
