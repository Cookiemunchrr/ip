package quu.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import quu.exception.TaskNotFoundException;

/**
 * Tests {@link TaskList}'s bookkeeping, including its index bounds.
 *
 * <p>Note the deliberate asymmetry these tests pin down: {@code getItemAtIndex} is zero-based
 * while {@code removeTask}, {@code markTask} and {@code unmarkTask} are one-based,
 * because the latter three take the number the user typed.
 */
public class TaskListTest {

    private TaskList listOf(String... descriptions) {
        TaskList taskList = new TaskList();
        for (String description : descriptions) {
            taskList.addTask(new ToDo(description));
        }
        return taskList;
    }

    @Test
    public void addTask_newTask_sizeIncreases() {
        TaskList taskList = new TaskList();
        assertEquals(0, taskList.getSize());
        taskList.addTask(new ToDo("read book"));
        assertEquals(1, taskList.getSize());
    }

    @Test
    public void getItemAtIndex_populatedList_zeroBasedTaskReturned() {
        TaskList taskList = listOf("first", "second");
        assertTrue(taskList.getItemAtIndex(0).toString().contains("first"));
        assertTrue(taskList.getItemAtIndex(1).toString().contains("second"));
    }

    @Test
    public void removeTask_oneBasedIndex_removedTaskReturned() throws TaskNotFoundException {
        TaskList taskList = listOf("first", "second");
        Task removed = taskList.removeTask(1);
        assertTrue(removed.toString().contains("first"));
        assertEquals(1, taskList.getSize());
        assertTrue(taskList.getItemAtIndex(0).toString().contains("second"));
    }

    @Test
    public void removeTask_indexZero_exceptionThrown() {
        TaskList taskList = listOf("first");
        assertThrows(TaskNotFoundException.class, () -> taskList.removeTask(0));
    }

    @Test
    public void removeTask_indexPastEnd_exceptionThrown() {
        TaskList taskList = listOf("first");
        assertThrows(TaskNotFoundException.class, () -> taskList.removeTask(2));
    }

    @Test
    public void markTask_oneBasedIndex_taskMarked() throws TaskNotFoundException {
        TaskList taskList = listOf("first", "second");
        Task marked = taskList.markTask(2);
        assertEquals("[T][X] second", marked.toString());
        assertEquals("[T][ ] first", taskList.getItemAtIndex(0).toString());
    }

    @Test
    public void unmarkTask_markedTask_taskUnmarked() throws TaskNotFoundException {
        TaskList taskList = listOf("first");
        taskList.markTask(1);
        assertEquals("[T][ ] first", taskList.unmarkTask(1).toString());
    }

    @Test
    public void markTask_indexOutOfRange_exceptionThrown() {
        TaskList taskList = listOf("first");
        assertThrows(TaskNotFoundException.class, () -> taskList.markTask(2));
    }

    @Test
    public void constructor_existingList_contentsAdopted() {
        List<Task> existing = new ArrayList<>();
        existing.add(new ToDo("read book"));
        TaskList taskList = new TaskList(existing);
        assertEquals(1, taskList.getSize());
        assertEquals("[T][ ] read book", taskList.getItemAtIndex(0).toString());
    }
}
