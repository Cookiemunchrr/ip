package quu.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import quu.exception.InvalidDateException;
import quu.exception.MissingArgumentException;

/**
 * Represents a task that must be completed by a particular date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    protected LocalDate deadline;

    /**
     * Constructs a deadline that is initially not done.
     *
     * @param description Text describing what the task is.
     * @param deadline Due date in ISO form, for example 2026-06-06.
     * @throws DateTimeParseException If the date is not in ISO form.
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = LocalDate.parse(deadline);
    }

    /**
     * Reconstructs a deadline from the fields of one save-file line.
     *
     * @param fields Line split into type letter, done flag, and the rest.
     * @return The reconstructed deadline.
     * @throws MissingArgumentException If the description or the date is absent.
     * @throws InvalidDateException If the date cannot be parsed.
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
        return "[D]" + super.toString()
                + String.format(" (by: %s)", deadline.format(DISPLAY_FORMAT));
    }

    @Override
    public String toFileString() {
        return "D " + super.toFileString() + String.format(" /by %s", deadline);
    }
}
