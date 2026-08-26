package quu.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import quu.exception.InvalidDateException;
import quu.exception.MissingArgumentException;

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
        return "D " + super.toFileString() + String.format(" /by %s", deadline);
    }

    public static Deadline fromFileString(String[] fields) throws MissingArgumentException, InvalidDateException{
        try{
            String[] d = fields[2].split(" /by ", 2);
            Deadline deadline = new Deadline(d[0], d[1]);
            return deadline;
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(fields[0] + " <task> /by <yyyy-mm-dd>");
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getParsedString());
        }
    }
}
