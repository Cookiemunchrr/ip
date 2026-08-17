import java.util.Scanner;
public class Quu {
    private static final String NAME = "Quu";
    public static void main(String[] args) {
        String banner = "  ___\n"
                + " / _ \\ _   _ _   _\n"
                + "| | | | | | | | | |\n"
                + "| |_| | |_| | |_| |\n"
                + " \\__\\_\\\\__,_|\\__,_|\n";
        System.out.println(banner);

        String greeting = String.format("Hello! I'm %s.%nWhat can I do for you?%n", NAME);
        System.out.println(greeting);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            System.out.println(input);
        }
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }

}
