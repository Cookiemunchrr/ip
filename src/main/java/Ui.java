import java.util.List;

public class Ui {

    public void greet(String name){
        String greeting = String.format("Hello! I'm %s.%nWhat can I do for you?%n", name);
        System.out.println(greeting);
    }

    public void showBanner(){
        String banner = "  ___\n"
                + " / _ \\ _   _ _   _\n"
                + "| | | | | | | | | |\n"
                + "| |_| | |_| | |_| |\n"
                + " \\__\\_\\\\__,_|\\__,_|\n";
        System.out.println(banner);
    }

    public void showAdded(Task task, int size){
        String response = String.format("Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, size);
        System.out.println(response);
    }

    public void showRemoved(Task task, int size){
        String response = String.format("Noted. I've removed this task:%n %s%nNow you have %d tasks in the list.", task, size);
        System.out.println(response);
    }

    public void showMarked(Task task){
        String response = String.format("Nice! I've marked this task as done:%n %s", task);
        System.out.println(response);
    }

    public void showUnMarked(Task task){
        String response = String.format("OK, I've marked this task as not done yet:%n %s", task);
        System.out.println(response);
    }

    public void showList(TaskList taskList){
        String response = String.format("Here are the tasks in your list:");
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getItemAtIndex(i);
            String item_string = String.format("%n%d.%s", i+1, task.toString());
            response += item_string;
        }
        System.out.println(response);
    }

    public void showException(Exception e){
        System.out.println(e.getMessage());
    }

    public void showSaveError(String message){
        System.out.println(message);
    }

    public void showLoadingError(String message){
        System.out.println(message);
    }

    public void goodbye(){
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }

    private void say(String message){
        System.out.println(message);
    }
}
