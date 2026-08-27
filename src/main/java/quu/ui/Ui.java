package quu.ui;

import java.util.List;

import quu.task.Task;
import quu.task.TaskList;

/**
 * Handles everything Quu shows to the user on the console.
 * Every method prints a ready-formatted message; no method reads input.
 */
public class Ui {

    /**
     * Prints the welcome message.
     *
     * @param name Name the chatbot introduces itself with.
     */
    public void greet(String name){
        String greeting = String.format("Hello! I'm %s.%nWhat can I do for you?%n", name);
        System.out.println(greeting);
    }

    /**
     * Prints the Quu logo shown when the program starts.
     */
    public void showBanner(){
        String banner = "  ___\n"
                + " / _ \\ _   _ _   _\n"
                + "| | | | | | | | | |\n"
                + "| |_| | |_| | |_| |\n"
                + " \\__\\_\\\\__,_|\\__,_|\n";
        System.out.println(banner);
    }

    /**
     * Prints confirmation that a task was added.
     *
     * @param task The task that was added.
     * @param size Number of tasks in the list after the addition.
     */
    public void showAdded(Task task, int size){
        String response = String.format("Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, size);
        System.out.println(response);
    }

    /**
     * Prints confirmation that a task was deleted.
     *
     * @param task The task that was removed.
     * @param size Number of tasks in the list after the removal.
     */
    public void showRemoved(Task task, int size){
        String response = String.format("Noted. I've removed this task:%n %s%nNow you have %d tasks in the list.", task, size);
        System.out.println(response);
    }

    /**
     * Prints confirmation that a task was marked as done.
     *
     * @param task The task that was marked.
     */
    public void showMarked(Task task){
        String response = String.format("Nice! I've marked this task as done:%n %s", task);
        System.out.println(response);
    }

    /**
     * Prints confirmation that a task was marked as not done.
     *
     * @param task The task that was unmarked.
     */
    public void showUnMarked(Task task){
        String response = String.format("OK, I've marked this task as not done yet:%n %s", task);
        System.out.println(response);
    }

    /**
     * Prints every task in the list, numbered from one.
     *
     * @param taskList The task list to display.
     */
    public void showList(TaskList taskList){
        String response = String.format("Here are the tasks in your list:");
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getItemAtIndex(i);
            String item_string = String.format("%n%d.%s", i+1, task.toString());
            response += item_string;
        }
        System.out.println(response);
    }

    /**
     * Prints the message of an exception that the user should see.
     *
     * @param e The exception to report.
     */
    public void showException(Exception e){
        System.out.println(e.getMessage());
    }

    /**
     * Prints a message explaining that tasks could not be saved.
     *
     * @param message Description of the saving problem.
     */
    public void showSaveError(String message){
        System.out.println(message);
    }

    /**
     * Prints a message explaining that tasks could not be loaded.
     *
     * @param message Description of the loading problem.
     */
    public void showLoadingError(String message){
        System.out.println(message);
    }

    /**
     * Prints the farewell message shown when the program exits.
     */
    public void goodbye(){
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }

    private void say(String message){
        System.out.println(message);
    }
}
