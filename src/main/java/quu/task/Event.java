package quu.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import quu.exception.InvalidDateException;
import quu.exception.InvalidDurationException;
import quu.exception.MissingArgumentException;

/**
 * Represents a task that spans a period between two dates.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    protected LocalDate eventStart;
    protected LocalDate eventEnd;

    /**
     * Constructs an event that is initially not done.
     *
     * @param description Text describing what the task is.
     * @param eventStart Start date in ISO form, for example 2026-06-06.
     * @param eventEnd End date in ISO form; must not fall before the start date.
     * @throws InvalidDurationException If the end date is before the start date.
     * @throws DateTimeParseException If either date is not in ISO form.
     */
    public Event(String description, String eventStart, String eventEnd) throws InvalidDurationException {
        super(description);
        this.eventStart = LocalDate.parse(eventStart);
        this.eventEnd = LocalDate.parse(eventEnd);
        if (this.eventEnd.isBefore(this.eventStart)) {
            throw new InvalidDurationException(this.eventStart, this.eventEnd);
        }
    }

    /**
     * Reconstructs an event from the fields of one save-file line.
     *
     * @param fields Line split into type letter, done flag, and the rest.
     * @return The reconstructed event.
     * @throws InvalidDurationException If the end date is before the start date.
     * @throws InvalidDateException If either date cannot be parsed.
     * @throws MissingArgumentException If the description or either date is absent.
     */
    public static Event fromFileString(String[] fields)
            throws InvalidDurationException, InvalidDateException, MissingArgumentException {
        try {
            String[] descriptionAndDates = fields[2].split(" /from ", 2);
            String[] dates = descriptionAndDates[1].split(" /to ", 2);
            return new Event(descriptionAndDates[0], dates[0], dates[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MissingArgumentException(fields[0] + " <task> /from <yyyy-mm-dd> /to <yyyy-mm-dd>");
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(e.getParsedString());
        }
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + String.format(" (from: %s to: %s)",
                eventStart.format(DISPLAY_FORMAT),
                eventEnd.format(DISPLAY_FORMAT));
    }

    @Override
    public String toFileString() {
        return "E " + super.toFileString() + String.format(" /from %s /to %s",
                eventStart,
                eventEnd);
    }
}
