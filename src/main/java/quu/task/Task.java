package quu.task;

/**
 * Represents a single task tracked by Quu.
 * A task stores a description and whether it has been completed.
 * Subclasses such as {@link ToDo}, {@link Deadline} and {@link Event}
 * add their own time-related details on top of this.
 */
public class Task {
    /** Whether this task has been completed. */
    protected boolean isDone = false;

    /** Description of what the task is. */
    protected String task_detail;

    /**
     * Constructs a task that is initially not done.
     *
     * @param task_detail Description of the task.
     */
    public Task(String task_detail){
        this.task_detail = task_detail;
    }

    /**
     * Returns whether this task has been marked as done.
     *
     * @return {@code true} if the task is done, {@code false} otherwise.
     */
    public boolean checkTask(){
        return isDone;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String view_task(){
        return task_detail;
    }

    /**
     * Marks this task as done.
     */
    public void mark(){
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmark(){
        isDone = false;
    }

    /**
     * Returns the user-facing representation of this task,
     * consisting of a status icon followed by the description.
     *
     * @return A string such as {@code [X] read book}.
     */
    @Override
    public String toString(){
        if (isDone){
            return String.format("[X] %s", task_detail);
        } else{
            return String.format("[ ] %s", task_detail);
        }
    }

    /**
     * Returns the representation of this task used when saving to disk.
     * The done flag is encoded as {@code 1} or {@code 0}.
     *
     * @return A string such as {@code | 1 | read book}.
     */
    public String toFileString(){
        if (isDone){
            return String.format("| 1 | %s", task_detail);
        } else{
            return String.format("| 0 | %s", task_detail);
        }
    }
}
