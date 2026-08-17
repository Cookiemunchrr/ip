import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
public class Quu {
    private static final String NAME = "Quu";
    private static int itemNum = 0;

    public static String add_to_list(Map<Integer, String> todoList, String input){
        if (input.equals("list")){
            String response = "";
            for (Map.Entry<Integer, String> e: todoList.entrySet()) {
                String item_string = String.format("%d. %s%n", e.getKey(), e.getValue());
                response += item_string;
            }
            return response;
        }
        if (todoList.containsValue(input)){
            return "Item already added";
        } else {
            itemNum += 1;
            todoList.put(itemNum, input);
        }
        return "added: " + input;
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

        Map<Integer, String> todoList = new HashMap<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            itemNum += 1;
            String response = add_to_list(todoList, input);
            System.out.println(response);

        }
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }

}
