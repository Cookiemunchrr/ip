import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
public class Quu {
    private static final String NAME = "Quu";
    private static int itemNum = 0;

    private static String parseEvent(Map<Integer, Task> todoList, String[] parts) throws MissingArgumentException{
        try{
            String[] e = parts[1].split(" /from ", 2);
            String[] t = e[1].split(" /to ", 2);
            return add_to_list(todoList, new Event(e[0], t[0], t[1]));
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task> /from <start> /to <end>");
        }
    }

    private static String parseToDo(Map<Integer, Task> todoList, String[] parts) throws MissingArgumentException{
        try{
            if (parts[1].trim().isEmpty()){
                throw new MissingArgumentException(parts[0] + " <task>");
            }
            return add_to_list(todoList, new ToDo(parts[1]));
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task>");
        }
    }

    private static String parseDeadline(Map<Integer, Task> todoList, String[] parts) throws MissingArgumentException{
        try{
            String[] d = parts[1].split(" /by ", 2);
            return add_to_list(todoList, new Deadline(d[0], d[1]));
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task> /by <time>");
        }
    }

    public static String add_to_list(Map<Integer, Task> todoList, Task task){
        itemNum += 1;
        todoList.put(itemNum, task);
        return String.format("Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, todoList.size());
    }

    public static String list_items(Map<Integer, Task> todoList){
        String response = String.format("Here are the tasks in your list:");
        for (Map.Entry<Integer, Task> e: todoList.entrySet()) {
            String item_string = String.format("%n%d.%s", e.getKey(), e.getValue().toString());
            response += item_string;
        }
        return response;
    }

    public static String mark_items(Map<Integer, Task> todoList, String[] parts) throws InvalidIndexException, TaskNotFoundException, MissingArgumentException{
        try {
            int index = Integer.parseInt(parts[1]);
            Task task = todoList.get(index);
            if (task == null){
                throw new TaskNotFoundException(index);
            }
            task.mark();
            return String.format("Nice! I've marked this task as done:%n %s", task);
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task number>");
        }
    }

    public static String unmark_items(Map<Integer, Task> todoList, String[] parts) throws InvalidIndexException, TaskNotFoundException, MissingArgumentException{
        try {
            int index = Integer.parseInt(parts[1]);
            Task task = todoList.get(index);
            if (task == null) {
                throw new TaskNotFoundException(index);
            }
            task.unmark();
            return String.format("OK, I've marked this task as not done yet:%n %s", task);
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task number>");
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
            try {
                switch (command) {
                    case "list": {
                        response = list_items(todoList);
                        break;
                    }
                    case "mark": {
                        response = mark_items(todoList, parts);
                        break;
                    }
                    case "unmark": {
                        response = unmark_items(todoList, parts);
                        break;
                    }
                    case "todo": {
                        response = parseToDo(todoList, parts);
                        break;
                    }
                    case "deadline": {
                        response = parseDeadline(todoList, parts);
                        break;
                    }
                    case "event": {
                        response = parseEvent(todoList, parts);
                        break;
                    }
                    default:
                        throw new UnknownCommandException(parts[0]);
                }
            } catch (QuuException e){
                response = e.getMessage();
            }
            System.out.println(response);

        }
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }

}
