package quu.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests that each {@link QuuException} builds its user-facing message from its inputs.
 *
 * <p>These messages are what the user actually reads when a command fails, so they are worth
 * pinning down: a careless edit to one of them changes the program's behaviour.
 */
public class ExceptionMessageTest {

    @Test
    public void invalidDate_messageNamesTheOffendingInputAndTheExpectedFormat() {
        assertEquals("'Sunday' is not a valid date. Use yyyy-mm-dd, e.g. 2026-06-06.",
                new InvalidDateException("Sunday").getMessage());
    }

    @Test
    public void invalidIndex_messageQuotesTheOffendingInput() {
        assertEquals("\"abc\" isn't a task number", new InvalidIndexException("abc").getMessage());
    }

    @Test
    public void missingArgument_messageShowsTheExpectedUsage() {
        assertEquals("Invalid format. Please follow this format: todo <task>",
                new MissingArgumentException("todo <task>").getMessage());
    }

    @Test
    public void taskNotFound_messageNamesTheIndexAndSuggestsList() {
        assertEquals("There's no task at 5 use list to check available tasks.",
                new TaskNotFoundException(5).getMessage());
    }

    @Test
    public void invalidDuration_messageShowsBothDates() {
        assertEquals("2026-08-08 to 2026-08-06 is not a valid duration",
                new InvalidDurationException(LocalDate.parse("2026-08-08"),
                        LocalDate.parse("2026-08-06")).getMessage());
    }

    @Test
    public void everyFailure_isCatchableAsASingleQuuException() {
        assertInstanceOf(QuuException.class, new InvalidDateException("Sunday"));
        assertInstanceOf(QuuException.class, new InvalidIndexException("abc"));
        assertInstanceOf(QuuException.class, new MissingArgumentException("todo <task>"));
        assertInstanceOf(QuuException.class, new TaskNotFoundException(5));
        assertInstanceOf(QuuException.class, new UnknownCommandException("blah"));
        assertInstanceOf(QuuException.class, new InvalidFileContents("corrupted line"));
    }
}
