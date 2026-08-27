package quu.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import quu.exception.InvalidDateException;
import quu.exception.MissingArgumentException;

/**
 * Represents a task that has to be completed by a particular date.
 */
public class Deadline extends Task{
    /** Date by which the task must be completed. */
    protected LocalDate deadline;

    /**
     * Constructs a deadline with the given description and due date.
     *
     * @param task_detail Description of the task.
     * @param deadline Due date in {@code yyyy-mm-dd} format.
     * @throws java.time.format.DateTimeParseException If the due date is not in {@code yyyy-mm-dd} format.
     */
    public Deadline(String task_detail, String deadline) {
        super(task_detail);
        this.deadline =  LocalDate.parse(deadline);
    }

    /**
     * Returns the user-facing representation of this deadline,
     * prefixed with the {@code [D]} type icon and ending with the due date.
     *
     * @return A string such as {@code [D][ ] return book (by: Jun 6 2026)}.
     */
    @Override
    public String toString(){
        return "[D]" + super.toString() + String.format(" (by: %s)", deadline.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }

    /**
     * Returns the representation of this deadline used when saving to disk.
     *
     * @return A string such as {@code D | 0 | return book /by 2026-06-06}.
     */
    @Override
    public String toFileString() {
        return "D " + super.toFileString() + String.format(" /by %s", deadline);
    }

    /**
     * Creates a deadline from the fields of a line read from the save file.
     *
     * @param fields Fields of a saved line, where {@code fields[2]} holds
     *               the description and due date separated by {@code /by}.
     * @return The deadline described by the given fields.
     * @throws MissingArgumentException If the description or due date is missing.
     * @throws InvalidDateException If the due date is not in {@code yyyy-mm-dd} format.
     */
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
