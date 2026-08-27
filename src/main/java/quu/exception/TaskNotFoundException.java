package quu.exception;

/**
 * Signals that no task exists at the task number the user referred to.
 */
public class TaskNotFoundException extends QuuException{
    /**
     * Constructs an exception naming the task number that was out of range.
     *
     * @param index The one-based task number that does not exist.
     */
    public TaskNotFoundException(int index) {
        super("There's no task at " + index + " use list to check available tasks.");
    }
}
