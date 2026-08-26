import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
public class Event extends Task{
    protected LocalDate eventStart;
    protected LocalDate eventEnd;

    public Event(String task_detail, String eventStart, String eventEnd) throws InvalidDurationException{
        super(task_detail);
        this.eventStart = LocalDate.parse(eventStart);
        this.eventEnd = LocalDate.parse(eventEnd);
        if (this.eventEnd.isBefore(this.eventStart)){
            throw new InvalidDurationException(this.eventStart, this.eventEnd);
        }
    }

    @Override
    public String toString(){
        return "[E]" + super.toString() + String.format(" (from: %s to: %s)",
                eventStart.format(DateTimeFormatter.ofPattern("MMM d yyyy")),
                eventEnd.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }

    @Override
    public String toFileString() {
        return "E " + super.toFileString() + String.format(" /from %s /to %s",
                eventStart,
                eventEnd);
    }

    public static Event fromFileString(String[] fields) throws InvalidDurationException, InvalidDateException, MissingArgumentException{
        try{
            String[] e = fields[2].split(" /from ", 2);
            String[] t = e[1].split(" /to ", 2);
            Event event = new Event(e[0], t[0], t[1]);
            return event;
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(fields[0] + " <task> /from <yyyy-mm-dd> /to <yyyy-mm-dd>");
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getParsedString());
        }
    }
}
