package quu.task;

/**
 * Represents a single task tracked by Quu.
 * A task holds a description and a done/not-done flag; subclasses add their own
 * date information and their own prefix in the displayed and saved forms.
 */
public class Task {
    protected boolean isDone = false;
    protected String description;

    /**
     * Constructs a task that is initially not done.
     *
     * @param description Text describing what the task is.
     */
    public Task(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

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

    @Override
    public String toString() {
        if (isDone) {
            return String.format("[X] %s", description);
        } else {
            return String.format("[ ] %s", description);
        }
    }

    /**
     * Returns the save-file representation of this task.
     * Subclasses prepend their own type letter to this value.
     *
     * @return The done flag and description, separated by pipes.
     */
    public String toFileString() {
        if (isDone) {
            return String.format("| 1 | %s", description);
        } else {
            return String.format("| 0 | %s", description);
        }
    }
}
