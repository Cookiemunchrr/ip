package quu.task;

/**
 * A single item in the task list.
 *
 * <p>{@code Task} holds the parts every kind of task shares: a description and
 * whether it has been completed. Subclasses such as {@link ToDo}, {@link Deadline}
 * and {@link Event} add their own extra details and their own display prefix.
 */
public class Task {
    private final String description;
    private boolean isDone = false;

    /**
     * Creates a task that is not yet done.
     *
     * @param description text describing what the task is
     */
    public Task(String description) {
        this.description = description;
    }

    /**
     * Returns whether this task has been marked as done.
     *
     * @return true if the task is done
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the text describing this task, without any status or type markers.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /** Marks this task as done. */
    public void mark() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void unmark() {
        isDone = false;
    }

    /**
     * Returns the task as shown to the user, for example {@code [X] read book}.
     */
    @Override
    public String toString() {
        String statusIcon = isDone ? "X" : " ";
        return String.format("[%s] %s", statusIcon, description);
    }

    /**
     * Returns the task in the format used in the save file, for example
     * {@code | 1 | read book}.
     *
     * @return the save-file representation of this task
     */
    public String toFileString() {
        String doneFlag = isDone ? "1" : "0";
        return String.format("| %s | %s", doneFlag, description);
    }
}
