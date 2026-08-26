import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class Quu {
    private static final String NAME = "Quu";
    private static final String TASK_FILE = "./data/Quu.txt";

    public static void main(String[] args) {
        Ui ui = new Ui();
        Parser parser = new Parser();
        Storage storage = new Storage(TASK_FILE);
        TaskList taskList;

        ui.showBanner();
        ui.greet(NAME);
        try {
            taskList = storage.readFile();
        } catch (FileNotFoundException e){
            ui.showLoadingError("File not found at this path, a new file will be created at " + TASK_FILE);
            taskList = new TaskList();
        } catch (InvalidFileContents e){
            ui.showException(e);
            taskList = new TaskList();
        }

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            try {
                switch (command) {
                    case "list": {
                        ui.showList(taskList);
                        break;
                    }
                    case "mark": {
                        int index = parser.parseTaskNumber(parts);
                        Task task = taskList.mark_items(index);
                        ui.showMarked(task);
                        break;
                    }
                    case "unmark": {
                        int index = parser.parseTaskNumber(parts);
                        Task task = taskList.unmark_items(index);
                        ui.showUnMarked(task);
                        break;
                    }
                    case "todo": {
                        Task task = parser.parseToDo(taskList, parts);
                        taskList.add_to_list(task);
                        ui.showAdded(task, taskList.getSize());
                        break;
                    }
                    case "deadline": {
                        Task task = parser.parseDeadline(taskList, parts);
                        taskList.add_to_list(task);
                        ui.showAdded(task, taskList.getSize());
                        break;
                    }
                    case "event": {
                        Task task = parser.parseEvent(taskList, parts);
                        taskList.add_to_list(task);
                        ui.showAdded(task, taskList.getSize());
                        break;
                    }
                    case "delete":{
                        int index = parser.parseTaskNumber(parts);
                        Task task = taskList.remove_from_list(index);
                        ui.showRemoved(task, taskList.getSize());
                        break;
                    }
                    default:
                        throw new UnknownCommandException(parts[0]);
                }
                storage.writeFile(taskList.getTodoList());
            } catch (QuuException e){
                ui.showException(e);
            } catch (IOException e){
                ui.showSaveError(String.format("%nUnable to write to file, %s", e.getMessage()));
            }

        }
        ui.goodbye();
    }

}
