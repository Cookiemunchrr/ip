import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
public class Storage {
    String filePath;
    public Storage(String filePath){
        this.filePath = filePath;
    }

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
