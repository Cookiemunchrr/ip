import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class Quu {
    private static final String NAME = "Quu";
    private static final String TASK_FILE = "./data/Quu.txt";

    private static Task parseEvent(List<Task> todoList, String[] parts) throws MissingArgumentException, InvalidDateException, InvalidDurationException{
        try{
            String[] e = parts[1].split(" /from ", 2);
            String[] t = e[1].split(" /to ", 2);
            return add_to_list(todoList, new Event(e[0], t[0], t[1]));
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task> /from <yyyy-mm-dd> /to <yyyy-mm-dd>");
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getParsedString());
        }
    }

    private static Task parseToDo(List<Task> todoList, String[] parts) throws MissingArgumentException{
        try{
            if (parts[1].trim().isEmpty()){
                throw new MissingArgumentException(parts[0] + " <task>");
            }
            return add_to_list(todoList, new ToDo(parts[1]));
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task>");
        }
    }

    private static Task parseDeadline(List<Task> todoList, String[] parts) throws MissingArgumentException, InvalidDateException{
        try{
            String[] d = parts[1].split(" /by ", 2);
            return add_to_list(todoList, new Deadline(d[0], d[1]));
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task> /by <yyyy-mm-dd>");
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getParsedString());
        }
    }

    public static Task add_to_list(List<Task> todoList, Task task){
        todoList.add(task);
        return task;
    }

    public static Task remove_from_list(List<Task> todoList, String[] parts) throws TaskNotFoundException, InvalidIndexException, MissingArgumentException{
        try {
            int index = Integer.parseInt(parts[1]);
            if (index < 1 || index > todoList.size()) {
                throw new TaskNotFoundException(index);
            }
            Task task = todoList.remove(index - 1);
            return task;
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task number>");
        }
    }

    public static Task mark_items(List<Task> todoList, String[] parts) throws InvalidIndexException, TaskNotFoundException, MissingArgumentException{
        try {
            int index = Integer.parseInt(parts[1]);
            if (index < 1 || index > todoList.size()) {
                throw new TaskNotFoundException(index);
            }
            Task task = todoList.get(index - 1);
            task.mark();
            return task;
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task number>");
        }
    }

    public static Task unmark_items(List<Task> todoList, String[] parts) throws InvalidIndexException, TaskNotFoundException, MissingArgumentException{
        try {
            int index = Integer.parseInt(parts[1]);
            if (index < 1 || index > todoList.size()) {
                throw new TaskNotFoundException(index);
            }
            Task task = todoList.get(index - 1);
            task.unmark();
            return task;
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task number>");
        }
    }

    private static List<Task> readFile(String filePath) throws FileNotFoundException, InvalidFileContents{
        File f = new File(filePath);
        List<Task> todoList = new ArrayList<>();

        try (Scanner s = new Scanner(f)){
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] fields = line.split("\\s*\\|\\s*", 3);   // [type, doneFlag, description]
                if (fields.length < 3) {
                    throw new InvalidFileContents("Corrupted line in " + TASK_FILE + ": " + line);
                }
                String[] parts = {fields[0], fields[2]};
                try {
                    switch (fields[0]) {
                        case "T": {
                            parseToDo(todoList, parts);
                            break;
                        }
                        case "D": {
                            parseDeadline(todoList, parts);
                            break;
                        }
                        case "E": {
                            parseEvent(todoList, parts);
                            break;
                        }
                        default:
                            throw new UnknownCommandException(parts[0]);
                    }
                } catch (QuuException e){
                    throw new InvalidFileContents("Corrupted line in " + TASK_FILE + ": " + line);
                }

                if (fields[1].equals("1")){
                    todoList.get(todoList.size() - 1).mark();
                } else if (!fields[1].equals("0")) {
                    throw new InvalidFileContents("Corrupted line in " + TASK_FILE + ": " + line);
                }
            }
            return todoList;
        }
    }

    private static void writeFile(String filePath, List<Task> todoList) throws IOException {
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

    public static void main(String[] args) {
        Ui ui = new Ui();


        ui.showBanner();
        ui.greet(NAME);
        List<Task> todoList;
        try {
            todoList = readFile(TASK_FILE);
        } catch (FileNotFoundException e){
            ui.showLoadingError("File not found at this path, a new file will be created at " + TASK_FILE);
            todoList = new ArrayList<>();
        } catch (InvalidFileContents e){
            ui.showException(e);
            todoList = new ArrayList<>();
        }

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String response = "";
            try {
                switch (command) {
                    case "list": {
                        ui.showList(todoList);
                        break;
                    }
                    case "mark": {
                        Task task = mark_items(todoList, parts);
                        ui.showMarked(task);
                        break;
                    }
                    case "unmark": {
                        Task task = unmark_items(todoList, parts);
                        ui.showUnMarked(task);
                        break;
                    }
                    case "todo": {
                        Task task = parseToDo(todoList, parts);
                        ui.showAdded(task, todoList);
                        break;
                    }
                    case "deadline": {
                        Task task = parseDeadline(todoList, parts);
                        ui.showAdded(task, todoList);
                        break;
                    }
                    case "event": {
                        Task task = parseEvent(todoList, parts);
                        ui.showAdded(task, todoList);
                        break;
                    }
                    case "delete":{
                        Task task = remove_from_list(todoList, parts);
                        ui.showRemoved(task, todoList);
                        break;
                    }
                    default:
                        throw new UnknownCommandException(parts[0]);
                }
                writeFile(TASK_FILE, todoList);
            } catch (QuuException e){
                ui.showException(e);
            } catch (IOException e){
                ui.showSaveError(String.format("%nUnable to write to file, %s", e.getMessage()));
            }

        }
        ui.goodbye();
    }

}
