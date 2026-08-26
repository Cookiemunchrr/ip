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
 * while {@code remove_from_list}, {@code mark_items} and {@code unmark_items} are one-based,
 * because the latter three take the number the user typed.
 */
public class TaskListTest {

    private TaskList listOf(String... descriptions) {
        TaskList taskList = new TaskList();
        for (String description : descriptions) {
            taskList.add_to_list(new ToDo(description));
        }
        return taskList;
    }

    @Test
    public void add_to_list_increasesSize() {
        TaskList taskList = new TaskList();
        assertEquals(0, taskList.getSize());
        taskList.add_to_list(new ToDo("read book"));
        assertEquals(1, taskList.getSize());
    }

    @Test
    public void getItemAtIndex_isZeroBased() {
        TaskList taskList = listOf("first", "second");
        assertTrue(taskList.getItemAtIndex(0).toString().contains("first"));
        assertTrue(taskList.getItemAtIndex(1).toString().contains("second"));
    }

    @Test
    public void remove_from_list_isOneBased_andReturnsRemovedTask() throws TaskNotFoundException {
        TaskList taskList = listOf("first", "second");
        Task removed = taskList.remove_from_list(1);
        assertTrue(removed.toString().contains("first"));
        assertEquals(1, taskList.getSize());
        assertTrue(taskList.getItemAtIndex(0).toString().contains("second"));
    }

    @Test
    public void remove_from_list_indexZero_throwsTaskNotFound() {
        TaskList taskList = listOf("first");
        assertThrows(TaskNotFoundException.class, () -> taskList.remove_from_list(0));
    }

    @Test
    public void remove_from_list_indexPastEnd_throwsTaskNotFound() {
        TaskList taskList = listOf("first");
        assertThrows(TaskNotFoundException.class, () -> taskList.remove_from_list(2));
    }

    @Test
    public void mark_items_marksTaskAtOneBasedIndex() throws TaskNotFoundException {
        TaskList taskList = listOf("first", "second");
        Task marked = taskList.mark_items(2);
        assertEquals("[T][X] second", marked.toString());
        assertEquals("[T][ ] first", taskList.getItemAtIndex(0).toString());
    }

    @Test
    public void unmark_items_clearsAPreviouslyMarkedTask() throws TaskNotFoundException {
        TaskList taskList = listOf("first");
        taskList.mark_items(1);
        assertEquals("[T][ ] first", taskList.unmark_items(1).toString());
    }

    @Test
    public void mark_items_outOfRange_throwsTaskNotFound() {
        TaskList taskList = listOf("first");
        assertThrows(TaskNotFoundException.class, () -> taskList.mark_items(2));
    }

    @Test
    public void constructor_withExistingList_adoptsItsContents() {
        List<Task> existing = new ArrayList<>();
        existing.add(new ToDo("read book"));
        TaskList taskList = new TaskList(existing);
        assertEquals(1, taskList.getSize());
        assertEquals("[T][ ] read book", taskList.getItemAtIndex(0).toString());
    }
}
