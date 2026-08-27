package quu.task;

import java.util.ArrayList;
import java.util.List;

import quu.exception.TaskNotFoundException;

/**
 * Represents the collection of tasks currently held by Quu.
 * Task numbers exposed to the user are one-based, while the
 * underlying list is zero-based.
 */
public class TaskList {
    private final List<Task> todoList;

    /**
     * Constructs an empty task list.
     */
    public TaskList(){
        todoList = new ArrayList<>();
    }

    /**
     * Constructs a task list backed by the given list of tasks.
     *
     * @param todoList Tasks to populate the list with.
     */
    public TaskList(List<Task> todoList){
        this.todoList = todoList;
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return The tasks in this list, in order.
     */
    public List<Task> getTodoList(){
        return todoList;
    }

    /**
     * Returns the task stored at the given zero-based position.
     *
     * @param index Zero-based position of the task.
     * @return The task at that position.
     * @throws IndexOutOfBoundsException If the position is out of range.
     */
    public Task getItemAtIndex(int index){
        return todoList.get(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return The number of tasks.
     */
    public int getSize(){
        return todoList.size();
    }

    /**
     * Appends a task to the end of this list.
     *
     * @param task Task to add.
     */
    public void add_to_list(Task task){
        todoList.add(task);
    }

    /**
     * Removes the task with the given one-based task number.
     *
     * @param index One-based task number to remove.
     * @return The task that was removed.
     * @throws TaskNotFoundException If no task has that task number.
     */
    public Task remove_from_list(int index) throws TaskNotFoundException{
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
        Task task = todoList.remove(index - 1);
        return task;
    }

    /**
     * Marks the task with the given one-based task number as done.
     *
     * @param index One-based task number to mark.
     * @return The task that was marked.
     * @throws TaskNotFoundException If no task has that task number.
     */
    public Task mark_items(int index) throws TaskNotFoundException{
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
        Task task = todoList.get(index - 1);
        task.mark();
        return task;
    }

    /**
     * Marks the task with the given one-based task number as not done.
     *
     * @param index One-based task number to unmark.
     * @return The task that was unmarked.
     * @throws TaskNotFoundException If no task has that task number.
     */
    public Task unmark_items(int index) throws TaskNotFoundException{
        if (index < 1 || index > todoList.size()) {
            throw new TaskNotFoundException(index);
        }
        Task task = todoList.get(index - 1);
        task.unmark();
        return task;
    }
}
