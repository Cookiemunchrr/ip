package quu.task;

import java.util.ArrayList;
import java.util.List;

import quu.exception.TaskNotFoundException;

/**
 * Holds the tasks Quu is tracking and the operations that change them.
 *
 * <p>Note the deliberate index asymmetry: {@code getItemAtIndex} is zero-based
 * because it is used for internal iteration, while {@code removeTask},
 * {@code markTask} and {@code unmarkTask} are one-based because they take the
 * task number the user typed.
 */
public class TaskList {
    private final List<Task> todoList;

    /** Constructs an empty task list. */
    public TaskList() {
        todoList = new ArrayList<>();
    }

    /**
     * Constructs a task list backed by an existing list.
     *
     * @param todoList Tasks to adopt; this list is used directly, not copied.
     */
    public TaskList(List<Task> todoList) {
        this.todoList = todoList;
    }

    public List<Task> getTodoList() {
        return todoList;
    }

    public Task getItemAtIndex(int index) {
        return todoList.get(index);
    }

    public int getSize() {
        return todoList.size();
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        todoList.add(task);
    }

    /**
     * Removes the task at the given one-based position.
     *
     * @param index One-based task number, as typed by the user.
     * @return The task that was removed.
     * @throws TaskNotFoundException If no task exists at that position.
     */
    public Task removeTask(int index) throws TaskNotFoundException {
        requireValidIndex(index);
        return todoList.remove(index - 1);
    }

    /**
     * Marks the task at the given one-based position as done.
     *
     * @param index One-based task number, as typed by the user.
     * @return The task that was marked.
     * @throws TaskNotFoundException If no task exists at that position.
     */
    public Task markTask(int index) throws TaskNotFoundException {
        requireValidIndex(index);
        Task task = todoList.get(index - 1);
        task.mark();
        return task;
    }

    /**
     * Marks the task at the given one-based position as not done.
     *
     * @param index One-based task number, as typed by the user.
     * @return The task that was unmarked.
     * @throws TaskNotFoundException If no task exists at that position.
     */
    public Task unmarkTask(int index) throws TaskNotFoundException {
        requireValidIndex(index);
        Task task = todoList.get(index - 1);
        task.unmark();
        return task;
    }

    private void requireValidIndex(int index) throws TaskNotFoundException {
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
    }
}
