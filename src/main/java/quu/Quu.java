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
 * The Quu chatbot.
 *
 * <p>A {@code Quu} owns the task list for one session, together with the {@link Parser},
 * {@link Storage} and {@link Ui} it needs to act on that list. Commands arrive one at a
 * time through {@link #getResponse(String)}, which returns the reply as text instead of
 * printing it. This keeps the chatbot independent of its terminal and JavaFX front ends.
 */
public class Quu {
    private static final String NAME = "Quu";
    private static final String TASK_FILE = "./data/Quu.txt";
    private static final String EXIT_COMMAND = "bye";

    private final Ui ui = new Ui();
    private final Parser parser = new Parser();
    private final Storage storage = new Storage(TASK_FILE);
    private final TaskList taskList;
    private final String loadMessage;
    private CommandType commandType = CommandType.NONE;

    /**
     * Creates a chatbot whose task list is loaded from disk.
     *
     * <p>A missing or corrupted save file is not fatal. The session starts with an empty
     * list, and the reason is retained so that the front end can show it at startup.
     */
    public Quu() {
        TaskList loadedTasks;
        String message;
        try {
            loadedTasks = storage.readFile();
            message = "";
        } catch (FileNotFoundException e) {
            loadedTasks = new TaskList();
            message = ui.getLoadingError("File not found at this path, a new file will be created at " + TASK_FILE);
        } catch (InvalidFileContents e) {
            loadedTasks = new TaskList();
            message = ui.getException(e);
        }
        taskList = loadedTasks;
        loadMessage = message;
    }

    /**
     * Returns the reply to one line of user input.
     *
     * <p>Failures are returned as ordinary replies so that one bad command does not end
     * the session. The task list is saved after every command that succeeds.
     *
     * @param input one line of user input, as typed
     * @return the text to show the user
     */
    public String getResponse(String input) {
        if (isExitCommand(input)) {
            return ui.getGoodbye();
        }

        String[] parts = input.split(" ", 2);
        try {
            String response = executeCommand(parts);
            try {
                storage.writeFile(taskList.getTodoList());
            } catch (IOException e) {
                commandType = CommandType.ERROR;
                return response + System.lineSeparator()
                        + ui.getSaveError(String.format("Unable to write to file, %s", e.getMessage()));
            }
            return response;
        } catch (QuuException e) {
            commandType = CommandType.ERROR;
            return ui.getException(e);
        }
    }

    /**
     * Returns the category of the command handled by the last call to {@link #getResponse(String)}.
     *
     * @return the command category, or {@link CommandType#NONE} before a command is handled
     */
    public CommandType getCommandType() {
        return commandType;
    }

    /**
     * Returns whether a line of input asks to end the session.
     *
     * @param input one line of user input, as typed
     * @return true if the input is the exit command
     */
    public boolean isExitCommand(String input) {
        return input.equals(EXIT_COMMAND);
    }

    /**
     * Returns the program's ASCII-art logo.
     *
     * @return the logo
     */
    public String getBanner() {
        return ui.getBanner();
    }

    /**
     * Returns the opening greeting.
     *
     * @return the greeting
     */
    public String getGreeting() {
        return ui.getGreeting(NAME);
    }

    /**
     * Returns the problem encountered while loading the saved tasks, if any.
     *
     * @return the explanation, or an empty string if the tasks loaded cleanly
     */
    public String getLoadMessage() {
        return loadMessage;
    }

    /**
     * Runs the chatbot in a terminal until input ends or the user exits.
     *
     * @param args ignored; the chatbot takes no command-line arguments
     */
    public static void main(String[] args) {
        Quu quu = new Quu();
        System.out.println(quu.getBanner());
        System.out.println(quu.getGreeting());
        if (!quu.getLoadMessage().isEmpty()) {
            System.out.println(quu.getLoadMessage());
        }
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            System.out.println(quu.getResponse(input));
            if (quu.isExitCommand(input)) {
                return;
            }
        }
    }

    /**
     * Carries out a single command and returns its reply.
     *
     * <p>This method decides only <em>which</em> command was typed. What each command does
     * lives in its own handler below, so that this switch stays a readable summary of the
     * commands Quu understands.
     *
     * @param parts the user input split into command and arguments
     * @return the text describing what the command did
     * @throws QuuException if the command is unknown or its arguments are unusable
     */
    private String executeCommand(String[] parts) throws QuuException {
        return switch (parts[0]) {
            case "list" -> handleList();
            case "mark" -> handleMark(parts);
            case "unmark" -> handleUnmark(parts);
            case "todo" -> handleAdd(parser.parseToDo(parts));
            case "deadline" -> handleAdd(parser.parseDeadline(parts));
            case "event" -> handleAdd(parser.parseEvent(parts));
            case "delete" -> handleDelete(parts);
            case "find" -> handleFind(parts);
            default -> throw new UnknownCommandException(parts[0]);
        };
    }

    /**
     * Shows every task currently in the list.
     *
     * @return the numbered task list
     */
    private String handleList() {
        commandType = CommandType.LIST;
        return ui.getList(taskList);
    }

    /**
     * Marks the task the user named as done.
     *
     * @param parts the user input split into command and arguments
     * @return the confirmation that the task was marked
     * @throws QuuException if the task number is missing, not a number, or out of range
     */
    private String handleMark(String[] parts) throws QuuException {
        commandType = CommandType.MARK;
        Task task = taskList.markTask(parser.parseTaskNumber(parts));
        return ui.getMarked(task);
    }

    /**
     * Marks the task the user named as not done.
     *
     * @param parts the user input split into command and arguments
     * @return the confirmation that the task was unmarked
     * @throws QuuException if the task number is missing, not a number, or out of range
     */
    private String handleUnmark(String[] parts) throws QuuException {
        commandType = CommandType.UNMARK;
        Task task = taskList.unmarkTask(parser.parseTaskNumber(parts));
        return ui.getUnmarked(task);
    }

    /**
     * Adds an already-parsed task to the list and reports it.
     *
     * <p>Shared by the todo, deadline and event commands, which differ only in how the
     * task is built and not in what happens to it once it exists. The caller does the
     * parsing, so this method never has to ask which kind of task it was given.
     *
     * @param task the task to add
     * @return the confirmation that the task was added
     */
    private String handleAdd(Task task) {
        commandType = CommandType.ADD;
        taskList.addTask(task);
        return ui.getAdded(task, taskList.getSize());
    }

    /**
     * Removes the task the user named.
     *
     * @param parts the user input split into command and arguments
     * @return the confirmation that the task was removed
     * @throws QuuException if the task number is missing, not a number, or out of range
     */
    private String handleDelete(String[] parts) throws QuuException {
        commandType = CommandType.DELETE;
        Task task = taskList.removeTask(parser.parseTaskNumber(parts));
        return ui.getRemoved(task, taskList.getSize());
    }

    /**
     * Lists the tasks whose description contains the keyword the user gave.
     *
     * @param parts the user input split into command and arguments
     * @return the numbered list of matching tasks
     * @throws QuuException if the keyword is missing or blank
     */
    private String handleFind(String[] parts) throws QuuException {
        commandType = CommandType.FIND;
        return ui.getFound(taskList.buildFoundList(parser.parseKeyword(parts)));
    }
}
