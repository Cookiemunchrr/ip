package quu.exception;

/**
 * Signals that no task exists at the one-based position the user gave.
 */
public class TaskNotFoundException extends QuuException {

    /**
     * Constructs an exception naming the position that was out of range.
     *
     * @param index One-based task number the user typed.
     */
    public TaskNotFoundException(int index) {
        super("There's no task at " + index + " use list to check available tasks.");
    }
}
