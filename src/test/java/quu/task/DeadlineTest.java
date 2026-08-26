package quu.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

import quu.exception.InvalidDateException;
import quu.exception.MissingArgumentException;

/**
 * Tests {@link Deadline}'s date handling: friendly rendering, ISO storage, and rejection of
 * anything that is not a {@code yyyy-mm-dd} date.
 */
public class DeadlineTest {

    @Test
    public void toString_rendersDateAsMonthDayYear() {
        Deadline deadline = new Deadline("return book", "2026-06-06");
        assertEquals("[D][ ] return book (by: Jun 6 2026)", deadline.toString());
    }

    @Test
    public void toFileString_keepsTheIsoDate() {
        Deadline deadline = new Deadline("return book", "2026-06-06");
        assertEquals("D | 0 | return book /by 2026-06-06", deadline.toFileString());
    }

    @Test
    public void constructor_nonIsoDate_throwsDateTimeParseException() {
        assertThrows(DateTimeParseException.class, () -> new Deadline("return book", "Sunday"));
    }

    @Test
    public void toFileString_thenFromFileString_roundTripsTask()
            throws MissingArgumentException, InvalidDateException {
        Deadline original = new Deadline("return book", "2026-06-06");
        Deadline restored = Deadline.fromFileString(original.toFileString().split("\\s*\\|\\s*", 3));
        assertEquals(original.toString(), restored.toString());
    }

    @Test
    public void fromFileString_missingByClause_throwsMissingArgument() {
        String[] fields = {"D", "0", "return book"};
        assertThrows(MissingArgumentException.class, () -> Deadline.fromFileString(fields));
    }

    @Test
    public void fromFileString_unparseableDate_throwsInvalidDate() {
        String[] fields = {"D", "0", "return book /by Sunday"};
        assertThrows(InvalidDateException.class, () -> Deadline.fromFileString(fields));
    }
}
