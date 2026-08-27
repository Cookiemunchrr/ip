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
 * Reads the task list from disk and writes it back.
 *
 * <p>Each task occupies one line in the form {@code <type> | <doneFlag> | <details>}.
 * A line that does not fit that shape is treated as corruption of the whole file rather
 * than skipped, so the user is told rather than silently losing tasks.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a store backed by a file.
     *
     * @param filePath path of the save file, which need not exist yet
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads every task from the save file.
     *
     * @return the tasks the file holds, in file order
     * @throws FileNotFoundException if the save file does not exist yet
     * @throws InvalidFileContents if any line cannot be understood
     */
    public TaskList readFile() throws FileNotFoundException, InvalidFileContents {
        File f = new File(filePath);
        TaskList taskList = new TaskList();

        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] fields = line.split("\\s*\\|\\s*", 3);   // [type, doneFlag, description]
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
     * Writes the whole task list to the save file, replacing what was there before.
     *
     * <p>Creates the parent directory and the file if they do not exist.
     *
     * @param todoList the tasks to save
     * @throws IOException if the file or its directory cannot be written
     */
    public void writeFile(List<Task> todoList) throws IOException {
        File f = new File(filePath);
        File dir = f.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (FileWriter fw = new FileWriter(f)) {   // creates the file if absent, truncates if present
            for (Task task : todoList) {
                fw.write(task.toFileString() + System.lineSeparator());
            }
        }
    }
}
