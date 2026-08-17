import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
public class Quu {
    private static final String NAME = "Quu";
    private static int itemNum = 0;

    public static String add_to_list(Map<Integer, Task> todoList, String input){
        Task task = new Task(input);

        itemNum += 1;
        todoList.put(itemNum, task);
        return "added: " + input;
    }

    public static String list_items(Map<Integer, Task> todoList){
        String response = String.format("Here are the tasks in your list:");
        for (Map.Entry<Integer, Task> e: todoList.entrySet()) {
            String item_string = String.format("%n%d. %s", e.getKey(), e.getValue().toString());
            response += item_string;
        }
        return response;
    }

    public static String mark_items(Map<Integer, Task> todoList, String item_index){
        try {
            int index = Integer.parseInt(item_index);
            Task task = todoList.get(index);
            if (task == null){
                return "Invalid task";
            }
            task.mark();
            return String.format("Nice! I've marked this task as done:%n %s", task);
        } catch (NumberFormatException e) {
            return "Not a task number";
        }
    }

    public static String unmark_items(Map<Integer, Task> todoList, String item_index){
        try {
            int index = Integer.parseInt(item_index);
            Task task = todoList.get(index);
            if (task == null){
                return "Invalid task";
            }
            task.unmark();
            return String.format("OK, I've marked this task as not done yet:%n %s", task);
        } catch (NumberFormatException e) {
            return "Not a task number";
        }
    }

    public static void main(String[] args) {
        String banner = "  ___\n"
                + " / _ \\ _   _ _   _\n"
                + "| | | | | | | | | |\n"
                + "| |_| | |_| | |_| |\n"
                + " \\__\\_\\\\__,_|\\__,_|\n";
        System.out.println(banner);

        String greeting = String.format("Hello! I'm %s.%nWhat can I do for you?%n", NAME);
        System.out.println(greeting);

        Map<Integer, Task> todoList = new HashMap<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String response;
            switch(command){
                case "list":
                    response = list_items(todoList);
                    break;
                case "mark":
                    response = parts.length < 2
                            ? "Which task? Try: mark 2"
                            : mark_items(todoList, parts[1]);
                    break;
                case "unmark":
                    response = parts.length < 2
                            ? "Which task? Try: unmark 2"
                            : unmark_items(todoList, parts[1]);
                    break;
                default: response = add_to_list(todoList, input);
            }
            System.out.println(response);

        }
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }

}
