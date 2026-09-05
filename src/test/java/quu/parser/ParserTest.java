package quu.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import quu.exception.InvalidDateException;
import quu.exception.InvalidDurationException;
import quu.exception.InvalidIndexException;
import quu.exception.MissingArgumentException;
import quu.task.Task;

/**
 * Tests {@link Parser}'s translation of typed commands into tasks, and the exception it raises
 * for each way a command can be malformed.
 *
 * <p>Input arrives already split by {@code Quu} into {@code {command, arguments}}, so the tests
 * hand the parser arrays in that shape rather than raw command lines.
 */
public class ParserTest {

    private final Parser parser = new Parser();

    @Test
    public void parseToDo_validInput_returnsTodo() throws MissingArgumentException {
        Task task = parser.parseToDo(new String[]{"todo", "read book"});
        assertEquals("[T][ ] read book", task.toString());
    }

    @Test
    public void parseToDo_blankDescription_throwsMissingArgument() {
        assertThrows(MissingArgumentException.class, () ->
                parser.parseToDo(new String[]{"todo", "   "}));
    }

    @Test
    public void parseToDo_noDescription_throwsMissingArgumentNamingTheFormat() {
        MissingArgumentException thrown = assertThrows(MissingArgumentException.class, () ->
                parser.parseToDo(new String[]{"todo"}));
        assertEquals("Invalid format. Please follow this format: todo <task>", thrown.getMessage());
    }

    @Test
    public void parseDeadline_validInput_returnsDeadline()
            throws MissingArgumentException, InvalidDateException {
        Task task = parser.parseDeadline(new String[]{"deadline", "return book /by 2026-06-06"});
        assertEquals("[D][ ] return book (by: Jun 6 2026)", task.toString());
    }

    @Test
    public void parseDeadline_missingByClause_throwsMissingArgument() {
        MissingArgumentException thrown = assertThrows(MissingArgumentException.class, () ->
                parser.parseDeadline(new String[]{"deadline", "return book"}));
        assertEquals("Invalid format. Please follow this format: deadline <task> /by <yyyy-mm-dd>",
                thrown.getMessage());
    }

    @Test
    public void parseDeadline_unparseableDate_throwsInvalidDateNamingTheInput() {
        InvalidDateException thrown = assertThrows(InvalidDateException.class, () ->
                parser.parseDeadline(new String[]{"deadline", "return book /by Sunday"}));
        assertEquals("'Sunday' is not a valid date. Use yyyy-mm-dd, e.g. 2026-06-06.",
                thrown.getMessage());
    }

    @Test
    public void parseEvent_validInput_returnsEvent()
            throws MissingArgumentException, InvalidDateException, InvalidDurationException {
        Task task = parser.parseEvent(new String[]{"event", "project meeting /from 2026-08-06 /to 2026-08-08"});
        assertEquals("[E][ ] project meeting (from: Aug 6 2026 to: Aug 8 2026)", task.toString());
    }

    @Test
    public void parseEvent_missingToClause_throwsMissingArgument() {
        MissingArgumentException thrown = assertThrows(MissingArgumentException.class, () ->
                parser.parseEvent(new String[]{"event", "project meeting /from 2026-08-06"}));
        assertEquals("Invalid format. Please follow this format: "
                + "event <task> /from <yyyy-mm-dd> /to <yyyy-mm-dd>", thrown.getMessage());
    }

    @Test
    public void parseEvent_endBeforeStart_throwsInvalidDuration() {
        assertThrows(InvalidDurationException.class, () ->
                parser.parseEvent(new String[]{"event", "project meeting /from 2026-08-08 /to 2026-08-06"}));
    }

    @Test
    public void parseTaskNumber_numericArgument_returnsIndex()
            throws InvalidIndexException, MissingArgumentException {
        assertEquals(3, parser.parseTaskNumber(new String[]{"mark", "3"}));
    }

    @Test
    public void parseTaskNumber_nonNumeric_throwsInvalidIndexNamingTheInput() {
        InvalidIndexException thrown = assertThrows(InvalidIndexException.class, () ->
                parser.parseTaskNumber(new String[]{"mark", "abc"}));
        assertEquals("\"abc\" isn't a task number", thrown.getMessage());
    }

    @Test
    public void parseTaskNumber_noArgument_throwsMissingArgument() {
        MissingArgumentException thrown = assertThrows(MissingArgumentException.class, () ->
                parser.parseTaskNumber(new String[]{"mark"}));
        assertEquals("Invalid format. Please follow this format: mark <task number>",
                thrown.getMessage());
    }
}
