package quu.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import quu.exception.InvalidDateException;
import quu.exception.MissingArgumentException;

/**
 * A task that must be completed by a given date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDate deadline;

    /**
     * Creates a deadline.
     *
     * @param description text describing what the task is
     * @param deadline the due date in ISO form, {@code yyyy-mm-dd}
     * @throws DateTimeParseException if the date is not in ISO form
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = LocalDate.parse(deadline);
    }

    /**
     * Rebuilds a deadline from a line of the save file.
     *
     * @param fields the save-file line split into type, done flag and payload
     * @return the reconstructed deadline
     * @throws MissingArgumentException if the description or the due date is missing
     * @throws InvalidDateException if the due date cannot be parsed
     */
    public static Deadline fromFileString(String[] fields)
            throws MissingArgumentException, InvalidDateException {
        try {
            String[] parts = fields[2].split(" /by ", 2);
            return new Deadline(parts[0], parts[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MissingArgumentException(fields[0] + " <task> /by <yyyy-mm-dd>");
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(e.getParsedString());
        }
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + String.format(" (by: %s)", deadline.format(DISPLAY_FORMAT));
    }

    @Override
    public String toFileString() {
        return "D " + super.toFileString() + String.format(" /by %s", deadline);
    }
}
