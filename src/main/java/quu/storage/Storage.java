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
 * Loads tasks from, and saves tasks to, the save file on disk.
 * Each task occupies one line of the file, with its fields separated by {@code |}.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a storage that reads from and writes to the given file.
     *
     * @param filePath Path of the save file.
     */
    public Storage(String filePath){
        this.filePath = filePath;
    }

    /**
     * Reads the save file and rebuilds the task list it describes.
     * Blank lines are ignored.
     *
     * @return The tasks stored in the save file.
     * @throws FileNotFoundException If the save file does not exist.
     * @throws InvalidFileContents If any line of the file is malformed.
     */
    public TaskList readFile() throws FileNotFoundException, InvalidFileContents {
        File f = new File(filePath);
        TaskList taskList = new TaskList();

        try (Scanner s = new Scanner(f)){
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
                    taskList.add_to_list(task);
                } catch (QuuException e){
                    throw new InvalidFileContents("Corrupted line in " + filePath + ": " + line);
                }

                if (fields[1].equals("1")){
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
     * The parent directory and the file itself are created if they do not exist.
     *
     * @param todoList Tasks to save, in the order they should appear.
     * @throws IOException If the file or its directory cannot be written to.
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
