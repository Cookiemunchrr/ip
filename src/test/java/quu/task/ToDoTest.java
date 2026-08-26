package quu.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import quu.exception.MissingArgumentException;

/**
 * Tests how a {@link ToDo} renders itself and survives a trip through the save file format.
 */
public class ToDoTest {

    @Test
    public void toString_rendersTodoIconAndStatus() {
        assertEquals("[T][ ] borrow book", new ToDo("borrow book").toString());
    }

    @Test
    public void toFileString_rendersTypeStatusAndDescription() {
        assertEquals("T | 0 | borrow book", new ToDo("borrow book").toFileString());
    }

    @Test
    public void toFileString_thenFromFileString_roundTripsTask() throws MissingArgumentException {
        ToDo original = new ToDo("borrow book");
        ToDo restored = ToDo.fromFileString(original.toFileString().split("\\s*\\|\\s*", 3));
        assertEquals(original.toString(), restored.toString());
    }

    @Test
    public void fromFileString_blankDescription_throwsMissingArgument() {
        String[] fields = {"T", "0", "   "};
        assertThrows(MissingArgumentException.class, () -> ToDo.fromFileString(fields));
    }

    @Test
    public void fromFileString_noDescriptionField_throwsMissingArgument() {
        String[] fields = {"T", "0"};
        assertThrows(MissingArgumentException.class, () -> ToDo.fromFileString(fields));
    }
}
