package quu.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import quu.exception.InvalidFileContents;
import quu.task.TaskList;
import quu.task.ToDo;

/**
 * Tests that {@link Storage} can persist tasks and reconstruct them from a file.
 */
public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    public void writeFile_thenReadFile_roundTripsTask() throws IOException, InvalidFileContents {
        Path file = tempDir.resolve("quu_test.txt");
        Storage storage = new Storage(file.toString());

        TaskList original = new TaskList();
        original.addTask(new ToDo("read book"));

        storage.writeFile(original.getTodoList());
        TaskList loaded = storage.readFile();

        assertEquals(1, loaded.getSize());
        assertTrue(loaded.getItemAtIndex(0).toString().contains("read book"));
    }
}
