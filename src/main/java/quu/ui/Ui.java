package quu.ui;

import quu.task.Task;
import quu.task.TaskList;

/**
 * Writes everything the user sees to standard output.
 * Keeping all printing here means the rest of the program never calls
 * {@code System.out} directly.
 */
public class Ui {

    /**
     * Prints the welcome message.
     *
     * @param name Name the chatbot introduces itself with.
     */
    public void greet(String name) {
        String greeting = String.format("Hello! I'm %s.%nWhat can I do for you?%n", name);
        System.out.println(greeting);
    }

    /** Prints the ASCII-art banner shown at start-up. */
    public void showBanner() {
        String banner = "  ___\n"
                + " / _ \\ _   _ _   _\n"
                + "| | | | | | | | | |\n"
                + "| |_| | |_| | |_| |\n"
                + " \\__\\_\\\\__,_|\\__,_|\n";
        System.out.println(banner);
    }

    /**
     * Confirms that a task was added.
     *
     * @param task Task that was added.
     * @param size Number of tasks in the list after the addition.
     */
    public void showAdded(Task task, int size) {
        String response = String.format("Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, size);
        System.out.println(response);
    }

    /**
     * Confirms that a task was removed.
     *
     * @param task Task that was removed.
     * @param size Number of tasks in the list after the removal.
     */
    public void showRemoved(Task task, int size) {
        String response = String.format("Noted. I've removed this task:%n %s%nNow you have %d tasks in the list.",
                task, size);
        System.out.println(response);
    }

    /**
     * Confirms that a task was marked as done.
     *
     * @param task Task that was marked.
     */
    public void showMarked(Task task) {
        String response = String.format("Nice! I've marked this task as done:%n %s", task);
        System.out.println(response);
    }

    /**
     * Confirms that a task was marked as not done.
     *
     * @param task Task that was unmarked.
     */
    public void showUnMarked(Task task) {
        String response = String.format("OK, I've marked this task as not done yet:%n %s", task);
        System.out.println(response);
    }

    /**
     * Prints every task in the list, numbered from one.
     *
     * @param taskList Tasks to display.
     */
    public void showList(TaskList taskList) {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getItemAtIndex(i);
            response.append(String.format("%n%d.%s", i + 1, task));
        }
        System.out.println(response);
    }

    /**
     * Prints the message carried by an exception the user should see.
     *
     * @param e Exception whose message explains what went wrong.
     */
    public void showException(Exception e) {
        System.out.println(e.getMessage());
    }

    /**
     * Prints a message explaining that the task list could not be saved.
     *
     * @param message Text describing the failure.
     */
    public void showSaveError(String message) {
        System.out.println(message);
    }

    /**
     * Prints a message explaining that the task list could not be loaded.
     *
     * @param message Text describing the failure.
     */
    public void showLoadingError(String message) {
        System.out.println(message);
    }

    /** Prints the farewell message. */
    public void goodbye() {
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }
}
