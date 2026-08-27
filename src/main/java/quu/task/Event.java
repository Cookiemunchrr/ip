package quu.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import quu.exception.InvalidDateException;
import quu.exception.InvalidDurationException;
import quu.exception.MissingArgumentException;

/**
 * Represents a task that spans a period between a start date and an end date.
 */
public class Event extends Task{
    /** Date on which the event starts. */
    protected LocalDate eventStart;

    /** Date on which the event ends. */
    protected LocalDate eventEnd;

    /**
     * Constructs an event with the given description and period.
     *
     * @param task_detail Description of the event.
     * @param eventStart Start date in {@code yyyy-mm-dd} format.
     * @param eventEnd End date in {@code yyyy-mm-dd} format.
     * @throws InvalidDurationException If the end date falls before the start date.
     * @throws java.time.format.DateTimeParseException If either date is not in {@code yyyy-mm-dd} format.
     */
    public Event(String task_detail, String eventStart, String eventEnd) throws InvalidDurationException{
        super(task_detail);
        this.eventStart = LocalDate.parse(eventStart);
        this.eventEnd = LocalDate.parse(eventEnd);
        if (this.eventEnd.isBefore(this.eventStart)){
            throw new InvalidDurationException(this.eventStart, this.eventEnd);
        }
    }

    /**
     * Returns the user-facing representation of this event,
     * prefixed with the {@code [E]} type icon and ending with the period.
     *
     * @return A string such as {@code [E][ ] camp (from: Jun 6 2026 to: Jun 8 2026)}.
     */
    @Override
    public String toString(){
        return "[E]" + super.toString() + String.format(" (from: %s to: %s)",
                eventStart.format(DateTimeFormatter.ofPattern("MMM d yyyy")),
                eventEnd.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }

    /**
     * Returns the representation of this event used when saving to disk.
     *
     * @return A string such as {@code E | 0 | camp /from 2026-06-06 /to 2026-06-08}.
     */
    @Override
    public String toFileString() {
        return "E " + super.toFileString() + String.format(" /from %s /to %s",
                eventStart,
                eventEnd);
    }

    /**
     * Creates an event from the fields of a line read from the save file.
     *
     * @param fields Fields of a saved line, where {@code fields[2]} holds the
     *               description and the period separated by {@code /from} and {@code /to}.
     * @return The event described by the given fields.
     * @throws InvalidDurationException If the end date falls before the start date.
     * @throws InvalidDateException If either date is not in {@code yyyy-mm-dd} format.
     * @throws MissingArgumentException If the description, start date or end date is missing.
     */
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
