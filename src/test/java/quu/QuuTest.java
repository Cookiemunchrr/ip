package quu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests the command category {@link Quu} reports after each command.
 *
 * <p>The JavaFX front end reads that category through {@link Quu#getCommandType()} to decide
 * how to style a reply, so a category reported wrongly is a visible defect even though no
 * window is involved. Each test runs against its own save file, so none of them can disturb
 * the task list the user keeps in {@code ./data/Quu.txt}.
 */
public class QuuTest {

    @TempDir
    Path tempDir;

    private Quu quu;

    @BeforeEach
    public void setUp() {
        quu = new Quu(tempDir.resolve("quu_test.txt").toString());
    }

    @Test
    public void getCommandType_beforeAnyCommand_returnsNone() {
        assertEquals(CommandType.NONE, quu.getCommandType());
    }

    @Test
    public void getResponse_todoCommand_reportsAddCategory() {
        quu.getResponse("todo read book");
        assertEquals(CommandType.ADD, quu.getCommandType());
    }

    @Test
    public void getResponse_deadlineCommand_reportsAddCategory() {
        quu.getResponse("deadline submit ip /by 2026-09-18");
        assertEquals(CommandType.ADD, quu.getCommandType());
    }

    @Test
    public void getResponse_eventCommand_reportsAddCategory() {
        quu.getResponse("event orientation /from 2026-01-01 /to 2026-01-05");
        assertEquals(CommandType.ADD, quu.getCommandType());
    }

    @Test
    public void getResponse_markCommand_reportsMarkCategory() {
        quu.getResponse("todo read book");
        quu.getResponse("mark 1");
        assertEquals(CommandType.MARK, quu.getCommandType());
    }

    @Test
    public void getResponse_unmarkCommand_reportsUnmarkCategory() {
        quu.getResponse("todo read book");
        quu.getResponse("mark 1");
        quu.getResponse("unmark 1");
        assertEquals(CommandType.UNMARK, quu.getCommandType());
    }

    @Test
    public void getResponse_deleteCommand_reportsDeleteCategory() {
        quu.getResponse("todo read book");
        quu.getResponse("delete 1");
        assertEquals(CommandType.DELETE, quu.getCommandType());
    }

    @Test
    public void getResponse_editCommand_reportsUpdateCategory() {
        quu.getResponse("todo read book");
        quu.getResponse("edit 1 read a better book");
        assertEquals(CommandType.UPDATE, quu.getCommandType());
    }

    @Test
    public void getResponse_listCommand_reportsListCategory() {
        quu.getResponse("list");
        assertEquals(CommandType.LIST, quu.getCommandType());
    }

    @Test
    public void getResponse_findCommand_reportsFindCategory() {
        quu.getResponse("todo read book");
        quu.getResponse("find book");
        assertEquals(CommandType.FIND, quu.getCommandType());
    }

    @Test
    public void getResponse_unknownCommand_reportsErrorCategory() {
        quu.getResponse("blah");
        assertEquals(CommandType.ERROR, quu.getCommandType());
    }

    @Test
    public void getResponse_failedCommandAfterASuccess_reportsErrorCategory() {
        quu.getResponse("todo read book");
        quu.getResponse("mark 99");
        assertEquals(CommandType.ERROR, quu.getCommandType());
    }

    @Test
    public void getResponse_exitCommand_clearsTheCommandCategory() {
        quu.getResponse("todo read book");
        assertEquals(CommandType.ADD, quu.getCommandType());

        quu.getResponse("bye");
        assertEquals(CommandType.NONE, quu.getCommandType());
    }

    @Test
    public void getResponse_exitCommand_returnsTheFarewell() {
        assertTrue(quu.getResponse("bye").startsWith("Bye"));
    }

    @Test
    public void getResponse_addedTask_isListedBack() {
        quu.getResponse("todo read book");
        assertTrue(quu.getResponse("list").contains("read book"));
    }

    @Test
    public void getResponse_addedTask_survivesIntoANewSession() {
        String savePath = tempDir.resolve("quu_test.txt").toString();
        new Quu(savePath).getResponse("todo read book");

        Quu nextSession = new Quu(savePath);
        assertTrue(nextSession.getResponse("list").contains("read book"));
    }

    @Test
    public void isExitCommand_byeAlone_returnsTrue() {
        assertTrue(quu.isExitCommand("bye"));
    }

    @Test
    public void isExitCommand_byeWithTrailingText_returnsFalse() {
        assertFalse(quu.isExitCommand("bye now"));
    }

    @Test
    public void getLoadMessage_saveFileNotYetCreated_explainsItWillBeCreated() {
        assertTrue(quu.getLoadMessage().contains("new file will be created"));
    }
}
