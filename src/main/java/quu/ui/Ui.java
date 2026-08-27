package quu.ui;

import quu.task.Task;
import quu.task.TaskList;

/**
 * Handles everything the program prints to the user.
 *
 * <p>Keeping all output in one class means the wording of a message can be changed
 * in a single place, and the rest of the program never needs to know how a task is
 * rendered.
 */
public class Ui {
    private static final String BANNER =
              "  ___\n"
            + " / _ \\ _   _ _   _\n"
            + "| | | | | | | | | |\n"
            + "| |_| | |_| | |_| |\n"
            + " \\__\\_\\\\__,_|\\__,_|\n";

    /** Prints the program's ASCII-art logo. */
    public void showBanner() {
        System.out.println(BANNER);
    }

    /**
     * Prints the opening greeting.
     *
     * @param name the name the chatbot introduces itself by
     */
    public void greet(String name) {
        System.out.printf("Hello! I'm %s.%nWhat can I do for you?%n%n", name);
    }

    /**
     * Confirms that a task was added.
     *
     * @param task the task that was added
     * @param size the number of tasks now in the list
     */
    public void showAdded(Task task, int size) {
        System.out.printf("Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.%n",
                task, size);
    }

    /**
     * Confirms that a task was removed.
     *
     * @param task the task that was removed
     * @param size the number of tasks left in the list
     */
    public void showRemoved(Task task, int size) {
        System.out.printf("Noted. I've removed this task:%n %s%nNow you have %d tasks in the list.%n",
                task, size);
    }

    /**
     * Confirms that a task was marked as done.
     *
     * @param task the task that was marked
     */
    public void showMarked(Task task) {
        System.out.printf("Nice! I've marked this task as done:%n %s%n", task);
    }

    /**
     * Confirms that a task was marked as not done.
     *
     * @param task the task that was unmarked
     */
    public void showUnMarked(Task task) {
        System.out.printf("OK, I've marked this task as not done yet:%n %s%n", task);
    }

    /**
     * Prints every task in the list, numbered from one.
     *
     * @param taskList the tasks to print
     */
    public void showList(TaskList taskList) {
        System.out.println("Here are the tasks in your list:" + buildNumberedList(taskList));
    }

    /**
     * Prints the tasks that matched a search, numbered from one.
     *
     * @param taskList the matching tasks
     */
    public void showFound(TaskList taskList) {
        System.out.println("Here are the matching tasks in your list:" + buildNumberedList(taskList));
    }

    /**
     * Prints the message carried by an exception.
     *
     * @param e the exception to report
     */
    public void showException(Exception e) {
        System.out.println(e.getMessage());
    }

    /**
     * Reports that tasks could not be saved to disk.
     *
     * @param message the detail to show the user
     */
    public void showSaveError(String message) {
        System.out.println(message);
    }

    /**
     * Reports that tasks could not be loaded from disk.
     *
     * @param message the detail to show the user
     */
    public void showLoadingError(String message) {
        System.out.println(message);
    }

    /** Prints the farewell shown when the user exits. */
    public void goodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Builds the numbered lines for a task list, each on its own new line.
     *
     * <p>Shared by {@link #showList(TaskList)} and {@link #showFound(TaskList)} so the
     * two differ only in their heading. Returns an empty string for an empty list,
     * which leaves the heading standing alone.
     *
     * @param taskList the tasks to render
     * @return the numbered lines, prefixed by a line separator each
     */
    private String buildNumberedList(TaskList taskList) {
        StringBuilder lines = new StringBuilder();
        for (int i = 0; i < taskList.getSize(); i++) {
            lines.append(String.format("%n%d.%s", i + 1, taskList.getTaskAt(i)));
        }
        return lines.toString();
    }
}
