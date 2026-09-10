package quu.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
        File file = new File(filePath);
        TaskList taskList = new TaskList();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                taskList.addTask(parseLine(line));
            }
        }
        return taskList;
    }

    /**
     * Returns the task described by one line of the save file.
     *
     * @param line one non-blank line of the save file, already trimmed
     * @return the task that line describes
     * @throws InvalidFileContents if the line does not describe a task
     */
    private Task parseLine(String line) throws InvalidFileContents {
        String[] fields = line.split("\\s*\\|\\s*", 3); // [type, doneFlag, description]
        if (fields.length < 3) {
            throw corruptedLine(line);
        }

        Task task;
        try {
            task = createTask(fields);
        } catch (QuuException e) {
            throw corruptedLine(line);
        }

        applyDoneFlag(task, fields[1], line);
        return task;
    }

    /**
     * Returns a task of the kind named by the line's type field.
     *
     * <p>Failures are reported as the {@link QuuException} the task itself raises. Turning
     * that into a complaint about the file is the caller's job, since only the caller knows
     * which line it came from.
     *
     * @param fields the line split into type, done flag and details
     * @return the reconstructed task
     * @throws QuuException if the type is unknown or the details do not fit it
     */
    private Task createTask(String[] fields) throws QuuException {
        return switch (fields[0]) {
            case "T" -> ToDo.fromFileString(fields);
            case "D" -> Deadline.fromFileString(fields);
            case "E" -> Event.fromFileString(fields);
            default -> throw new UnknownCommandException(fields[0]);
        };
    }

    /**
     * Marks the task as done if the line records it as done.
     *
     * @param task the task the line describes
     * @param doneFlag the line's done field, which must be {@code 0} or {@code 1}
     * @param line the whole line, used to report it if the flag is neither
     * @throws InvalidFileContents if the flag is neither {@code 0} nor {@code 1}
     */
    private void applyDoneFlag(Task task, String doneFlag, String line) throws InvalidFileContents {
        if (doneFlag.equals("1")) {
            task.mark();
            assert task.isDone() : "after marking the task, it should be done";
        } else if (!doneFlag.equals("0")) {
            throw corruptedLine(line);
        }
    }

    /**
     * Returns the exception reporting that a line of the save file cannot be understood.
     *
     * <p>Returns the exception rather than throwing it, so that callers keep the
     * {@code throw} keyword and a reader can see where control leaves the method.
     *
     * @param line the line that could not be understood
     * @return the exception to throw
     */
    private InvalidFileContents corruptedLine(String line) {
        return new InvalidFileContents("Corrupted line in " + filePath + ": " + line);
    }

    /**
     * Writes the whole task list to the save file, replacing what was there before.
     *
     * <p>Creates the parent directory and the file if they do not exist.
     *
     * @param taskList the tasks to save
     * @throws IOException if the file or its directory cannot be written
     */
    public void writeFile(TaskList taskList) throws IOException {
        File f = new File(filePath);
        File dir = f.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        try (FileWriter fw = new FileWriter(f)) { // creates the file if absent, truncates if present
            for (int i = 0; i < taskList.getSize(); i++) {
                fw.write(taskList.getTaskAt(i).toFileString() + System.lineSeparator());
            }
        }
    }
}
