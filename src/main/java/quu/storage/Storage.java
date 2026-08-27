package quu.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import quu.exception.InvalidFileContents;
import quu.exception.QuuException;
import quu.exception.UnknownCommandException;
import quu.task.Deadline;
import quu.task.Event;
import quu.task.Task;
import quu.task.TaskList;
import quu.task.ToDo;

/**
 * Loads tasks from, and saves tasks to, a plain-text file on disk.
 * Each line holds one task as {@code <type> | <doneFlag> | <details>}.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a storage bound to one save file.
     *
     * @param filePath Path of the save file; it need not exist yet.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads the save file and rebuilds the task list it describes.
     * Blank lines are skipped.
     *
     * @return The tasks recorded in the save file.
     * @throws FileNotFoundException If the save file does not exist.
     * @throws InvalidFileContents If any line is malformed or has an unknown task type.
     */
    public TaskList readFile() throws FileNotFoundException, InvalidFileContents {
        File file = new File(filePath);
        TaskList taskList = new TaskList();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                // [type, doneFlag, description]
                String[] fields = line.split("\\s*\\|\\s*", 3);
                if (fields.length < 3) {
                    throw new InvalidFileContents("Corrupted line in " + filePath + ": " + line);
                }

                Task task;
                try {
                    switch (fields[0]) {
                        case "T": {
                            task = ToDo.fromFileString(fields);
                            break;
                        }
                        case "D": {
                            task = Deadline.fromFileString(fields);
                            break;
                        }
                        case "E": {
                            task = Event.fromFileString(fields);
                            break;
                        }
                        default:
                            throw new UnknownCommandException(fields[0]);
                    }
                    taskList.addTask(task);
                } catch (QuuException e) {
                    throw new InvalidFileContents("Corrupted line in " + filePath + ": " + line);
                }

                if (fields[1].equals("1")) {
                    task.mark();
                } else if (!fields[1].equals("0")) {
                    throw new InvalidFileContents("Corrupted line in " + filePath + ": " + line);
                }
            }
        }
        return taskList;
    }

    /**
     * Writes the given tasks to the save file, replacing whatever it held before.
     * Missing parent directories are created.
     *
     * @param todoList Tasks to save, in list order.
     * @throws IOException If the file or its parent directories cannot be written.
     */
    public void writeFile(List<Task> todoList) throws IOException {
        File file = new File(filePath);
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        // Creates the file if absent, truncates it if present.
        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : todoList) {
                writer.write(task.toFileString() + System.lineSeparator());
            }
        }
    }
}
