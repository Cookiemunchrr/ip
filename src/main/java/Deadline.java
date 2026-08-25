import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
public class Deadline extends Task{
    protected LocalDate deadline;
    public Deadline(String task_detail, String deadline) {
        super(task_detail);
        this.deadline =  LocalDate.parse(deadline);
    }

    @Override
    public String toString(){
        return "[D]" + super.toString() + String.format(" (by: %s)", deadline.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }

    @Override
    public String toFileString() {
        return "D " + super.toFileString() + String.format(" /by %s", deadline.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }
}
