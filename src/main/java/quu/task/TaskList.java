package quu.task;

import java.util.ArrayList;
import java.util.List;

import quu.exception.TaskNotFoundException;

/**
 * The collection of tasks the user is working with.
 *
 * <p>Indices accepted by {@link #removeTask(int)}, {@link #markTask(int)} and
 * {@link #unmarkTask(int)} are <em>one-based</em>, matching the numbering the user
 * sees when listing tasks. {@link #getTaskAt(int)} is zero-based because it is used
 * for iteration rather than driven by user input.
 */
public class TaskList {
    private final List<Task> todoList;

    /** Creates an empty task list. */
    public TaskList() {
        todoList = new ArrayList<>();
    }

    /**
     * Creates a task list backed by an existing list of tasks.
     *
     * @param todoList the tasks to start with
     */
    public TaskList(List<Task> todoList) {
        this.todoList = todoList;
    }

    /**
     * Returns the underlying list of tasks, mainly so it can be saved to disk.
     *
     * @return the backing list
     */
    public List<Task> getTodoList() {
        return todoList;
    }

    /**
     * Returns the task at a zero-based position.
     *
     * @param index zero-based position of the task
     * @return the task at that position
     */
    public Task getTaskAt(int index) {
        return todoList.get(index);
    }

    /**
     * Returns how many tasks the list holds.
     *
     * @return the number of tasks
     */
    public int getSize() {
        return todoList.size();
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        todoList.add(task);
    }

    /**
     * Returns a new list holding every task whose description contains the given
     * keyword, ignoring case.
     *
     * <p>The returned list is a separate {@code TaskList} that shares the same
     * {@link Task} objects, so marking a task through either list is visible in both,
     * but adding or removing tasks affects only the list acted on. Its numbering
     * restarts at one and therefore does not map back to positions in this list.
     *
     * @param keyword the text to search for in each task's description
     * @return a task list holding the matching tasks, empty if none match
     */
    public TaskList buildFoundList(String keyword) {
        TaskList foundList = new TaskList();
        String lowerCaseKeyword = keyword.toLowerCase();
        for (Task task : todoList) {
            if (task.getDescription().toLowerCase().contains(lowerCaseKeyword)) {
                foundList.addTask(task);
            }
        }
        return foundList;
    }

    /**
     * Removes and returns the task at a one-based position.
     *
     * @param index one-based position of the task to remove
     * @return the task that was removed
     * @throws TaskNotFoundException if no task sits at that position
     */
    public Task removeTask(int index) throws TaskNotFoundException {
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
        return todoList.remove(index - 1);
    }

    /**
     * Marks the task at a one-based position as done.
     *
     * @param index one-based position of the task to mark
     * @return the task that was marked
     * @throws TaskNotFoundException if no task sits at that position
     */
    public Task markTask(int index) throws TaskNotFoundException {
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
        Task task = todoList.get(index - 1);
        task.mark();
        return task;
    }

    /**
     * Marks the task at a one-based position as not done.
     *
     * @param index one-based position of the task to unmark
     * @return the task that was unmarked
     * @throws TaskNotFoundException if no task sits at that position
     */
    public Task unmarkTask(int index) throws TaskNotFoundException {
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
        Task task = todoList.get(index - 1);
        task.unmark();
        return task;
    }
}
