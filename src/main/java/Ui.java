public class Ui {
    String banner = "  ___\n"
            + " / _ \\ _   _ _   _\n"
            + "| | | | | | | | | |\n"
            + "| |_| | |_| | |_| |\n"
            + " \\__\\_\\\\__,_|\\__,_|\n";

    public void greet(String name){
        String greeting = String.format("Hello! I'm %s.%nWhat can I do for you?%n", name);
        System.out.println(greeting);
    }

    public void print_banner(){
        String banner = "  ___\n"
                + " / _ \\ _   _ _   _\n"
                + "| | | | | | | | | |\n"
                + "| |_| | |_| | |_| |\n"
                + " \\__\\_\\\\__,_|\\__,_|\n";
        System.out.println(banner);
    }


}
