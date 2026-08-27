package quu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import quu.exception.InvalidFileContents;
import quu.exception.QuuException;
import quu.exception.UnknownCommandException;
import quu.parser.Parser;
import quu.storage.Storage;
import quu.task.Task;
import quu.task.TaskList;
import quu.ui.Ui;

/**
 * Entry point of the Quu chatbot.
 *
 * <p>Reads commands from standard input until the user types {@code bye}, dispatching
 * each one to the {@link Parser}, the {@link TaskList} and the {@link Ui} in turn, and
 * saving the list to disk after every command that succeeds.
 */
public class Quu {
    private static final String NAME = "Quu";
    private static final String TASK_FILE = "./data/Quu.txt";
    private static final String EXIT_COMMAND = "bye";

    /**
     * Starts the chatbot.
     *
     * @param args ignored; the chatbot takes no command-line arguments
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Parser parser = new Parser();
        Storage storage = new Storage(TASK_FILE);

        ui.showBanner();
        ui.greet(NAME);

        TaskList taskList = loadTasks(storage, ui);
        runCommandLoop(taskList, parser, storage, ui);

        ui.goodbye();
    }

    /**
     * Loads the saved tasks, falling back to an empty list if the file is missing or corrupted.
     *
     * @param storage the store to read from
     * @param ui the interface used to report a load failure
     * @return the loaded tasks, or an empty list if they could not be loaded
     */
    private static TaskList loadTasks(Storage storage, Ui ui) {
        try {
            return storage.readFile();
        } catch (FileNotFoundException e) {
            ui.showLoadingError("File not found at this path, a new file will be created at " + TASK_FILE);
            return new TaskList();
        } catch (InvalidFileContents e) {
            ui.showException(e);
            return new TaskList();
        }
    }

    /**
     * Reads and executes commands until input runs out or the user exits.
     *
     * <p>Failures are reported and then swallowed on purpose, so that one bad command
     * does not end the session.
     *
     * @param taskList the tasks being edited
     * @param parser the parser used to read command arguments
     * @param storage the store the list is saved to after each command
     * @param ui the interface used for all output
     */
    private static void runCommandLoop(TaskList taskList, Parser parser, Storage storage, Ui ui) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals(EXIT_COMMAND)) {
                return;
            }

            // Limit of 2 keeps the whole argument string intact, spaces and all.
            String[] parts = input.split(" ", 2);
            try {
                executeCommand(parts, taskList, parser, ui);
                storage.writeFile(taskList.getTodoList());
            } catch (QuuException e) {
                ui.showException(e);
            } catch (IOException e) {
                ui.showSaveError(String.format("%nUnable to write to file, %s", e.getMessage()));
            }
        }
    }

    /**
     * Carries out a single command.
     *
     * @param parts the user input split into command and arguments
     * @param taskList the tasks being edited
     * @param parser the parser used to read command arguments
     * @param ui the interface used for all output
     * @throws QuuException if the command is unknown or its arguments are unusable
     */
    private static void executeCommand(String[] parts, TaskList taskList, Parser parser, Ui ui)
            throws QuuException {
        switch (parts[0]) {
        case "list":
            ui.showList(taskList);
            break;
        case "mark": {
            Task task = taskList.markTask(parser.parseTaskNumber(parts));
            ui.showMarked(task);
            break;
        }
        case "unmark": {
            Task task = taskList.unmarkTask(parser.parseTaskNumber(parts));
            ui.showUnMarked(task);
            break;
        }
        case "todo": {
            Task task = parser.parseToDo(parts);
            taskList.addTask(task);
            ui.showAdded(task, taskList.getSize());
            break;
        }
        case "deadline": {
            Task task = parser.parseDeadline(parts);
            taskList.addTask(task);
            ui.showAdded(task, taskList.getSize());
            break;
        }
        case "event": {
            Task task = parser.parseEvent(parts);
            taskList.addTask(task);
            ui.showAdded(task, taskList.getSize());
            break;
        }
        case "delete": {
            Task task = taskList.removeTask(parser.parseTaskNumber(parts));
            ui.showRemoved(task, taskList.getSize());
            break;
        }
        case "find":
            ui.showFound(taskList.buildFoundList(parser.parseKeyword(parts)));
            break;
        default:
            throw new UnknownCommandException(parts[0]);
        }
    }
}
