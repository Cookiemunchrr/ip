package quu.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import quu.exception.InvalidDateException;
import quu.exception.InvalidDurationException;
import quu.exception.MissingArgumentException;

/**
 * A task that spans a period between two dates.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDate eventStart;
    private final LocalDate eventEnd;

    /**
     * Creates an event.
     *
     * @param description text describing what the task is
     * @param eventStart the start date in ISO form, {@code yyyy-mm-dd}
     * @param eventEnd the end date in ISO form, {@code yyyy-mm-dd}
     * @throws InvalidDurationException if the end date falls before the start date
     * @throws DateTimeParseException if either date is not in ISO form
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
     * Rebuilds an event from a line of the save file.
     *
     * @param fields the save-file line split into type, done flag and payload
     * @return the reconstructed event
     * @throws MissingArgumentException if the description or either date is missing
     * @throws InvalidDateException if a date cannot be parsed
     * @throws InvalidDurationException if the end date falls before the start date
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
        return "E " + super.toFileString() + String.format(" /from %s /to %s", eventStart, eventEnd);
    }
}
