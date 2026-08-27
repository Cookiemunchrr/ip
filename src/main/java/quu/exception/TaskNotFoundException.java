package quu.exception;

/**
 * Thrown when a task number is well formed but no task sits at that position.
 */
public class TaskNotFoundException extends QuuException {

    /**
     * Creates the exception, naming the position that held no task.
     *
     * @param index the one-based position the user asked for
     */
    public TaskNotFoundException(int index) {
        super("There's no task at " + index + " use list to check available tasks.");
    }
}
