package quu.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import quu.task.TaskList;
import quu.task.ToDo;

/**
 * Tests the exact wording {@link Ui} prints for each kind of response.
 *
 * <p>{@code Ui} writes to {@code System.out} rather than returning strings, so each test swaps in
 * a capturing stream and restores the real one afterwards.
 */
public class UiTest {

    private final ByteArrayOutputStream captured = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private Ui ui;

    @BeforeEach
    public void redirectStandardOutput() {
        originalOut = System.out;
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
        ui = new Ui();
    }

    @AfterEach
    public void restoreStandardOutput() {
        System.setOut(originalOut);
    }

    /** Returns everything printed so far, without the trailing newline {@code println} adds. */
    private String printed() {
        return captured.toString(StandardCharsets.UTF_8).strip();
    }

    @Test
    public void greet_printsTheNameAndThePrompt() {
        ui.greet("Quu");
        assertEquals(String.format("Hello! I'm Quu.%nWhat can I do for you?"), printed());
    }

    @Test
    public void showAdded_printsTheTaskAndTheNewCount() {
        ui.showAdded(new ToDo("read book"), 1);
        assertEquals(String.format("Got it. I've added this task:%n  [T][ ] read book%n"
                + "Now you have 1 tasks in the list."), printed());
    }

    @Test
    public void showRemoved_printsTheTaskAndTheNewCount() {
        ui.showRemoved(new ToDo("read book"), 0);
        assertEquals(String.format("Noted. I've removed this task:%n [T][ ] read book%n"
                + "Now you have 0 tasks in the list."), printed());
    }

    @Test
    public void showMarked_printsTheTaskWithATickedBox() {
        ToDo task = new ToDo("read book");
        task.mark();
        ui.showMarked(task);
        assertEquals(String.format("Nice! I've marked this task as done:%n [T][X] read book"), printed());
    }

    @Test
    public void showUnMarked_printsTheTaskWithAnEmptyBox() {
        ui.showUnMarked(new ToDo("read book"));
        assertEquals(String.format("OK, I've marked this task as not done yet:%n [T][ ] read book"),
                printed());
    }

    @Test
    public void showList_numbersTasksFromOneInInsertionOrder() {
        TaskList taskList = new TaskList();
        taskList.addTask(new ToDo("first"));
        taskList.addTask(new ToDo("second"));
        ui.showList(taskList);
        assertEquals(String.format("Here are the tasks in your list:%n"
                + "1.[T][ ] first%n2.[T][ ] second"), printed());
    }

    @Test
    public void showList_emptyList_printsOnlyTheHeading() {
        ui.showList(new TaskList());
        assertEquals("Here are the tasks in your list:", printed());
    }

    @Test
    public void showException_printsTheMessageAlone() {
        ui.showException(new IllegalStateException("something went wrong"));
        assertEquals("something went wrong", printed());
    }

    @Test
    public void goodbye_printsTheFarewell() {
        ui.goodbye();
        assertEquals("Bye. Hope to see you again soon!", printed());
    }
}
