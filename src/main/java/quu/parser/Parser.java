package quu.parser;

import java.time.format.DateTimeParseException;

import quu.exception.InvalidDateException;
import quu.exception.InvalidDurationException;
import quu.exception.InvalidIndexException;
import quu.exception.MissingArgumentException;
import quu.task.Deadline;
import quu.task.Event;
import quu.task.Task;
import quu.task.ToDo;

/**
 * Turns the argument part of a user command into the value that command needs.
 *
 * <p>Every method takes the raw input already split into {@code [command, arguments]},
 * so a one-word command arrives as a length-one array. Each method translates a
 * malformed argument into a {@code QuuException} carrying a message that shows the
 * user the expected format, which keeps error wording out of the main loop.
 */
public class Parser {

    /**
     * Reads the description of a {@code todo} command.
     *
     * @param parts the user input split into command and arguments
     * @return the new to-do
     * @throws MissingArgumentException if the description is absent or blank
     */
    public Task parseToDo(String[] parts) throws MissingArgumentException {
        return new ToDo(requireArgument(parts, "<task>"));
    }

    /**
     * Reads the description and due date of a {@code deadline} command.
     *
     * @param parts the user input split into command and arguments
     * @return the new deadline
     * @throws MissingArgumentException if the description or the {@code /by} clause is absent
     * @throws InvalidDateException if the due date is not a valid {@code yyyy-mm-dd} date
     */
    public Task parseDeadline(String[] parts) throws MissingArgumentException, InvalidDateException {
        try {
            String[] descriptionAndDate = parts[1].split(" /by ", 2);
            return new Deadline(descriptionAndDate[0], descriptionAndDate[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MissingArgumentException(parts[0] + " <task> /by <yyyy-mm-dd>");
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(e.getParsedString());
        }
    }

    /**
     * Reads the description and date range of an {@code event} command.
     *
     * @param parts the user input split into command and arguments
     * @return the new event
     * @throws MissingArgumentException if the description or either date clause is absent
     * @throws InvalidDateException if a date is not a valid {@code yyyy-mm-dd} date
     * @throws InvalidDurationException if the end date falls before the start date
     */
    public Task parseEvent(String[] parts)
            throws MissingArgumentException, InvalidDateException, InvalidDurationException {
        try {
            String[] descriptionAndDates = parts[1].split(" /from ", 2);
            String[] dates = descriptionAndDates[1].split(" /to ", 2);
            return new Event(descriptionAndDates[0], dates[0], dates[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MissingArgumentException(parts[0] + " <task> /from <yyyy-mm-dd> /to <yyyy-mm-dd>");
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(e.getParsedString());
        }
    }

    /**
     * Reads the one-based task number given to commands such as {@code mark} or {@code delete}.
     *
     * @param parts the user input split into command and arguments
     * @return the task number as typed, which the caller must still range-check
     * @throws InvalidIndexException if the argument is not a whole number
     * @throws MissingArgumentException if no argument was given
     */
    public int parseTaskNumber(String[] parts) throws InvalidIndexException, MissingArgumentException {
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MissingArgumentException(parts[0] + " <task number>");
        }
    }

    /**
     * Reads the search term of a {@code find} command.
     *
     * @param parts the user input split into command and arguments
     * @return the keyword to search task descriptions for
     * @throws MissingArgumentException if the keyword is absent or blank
     */
    public String parseKeyword(String[] parts) throws MissingArgumentException {
        return requireArgument(parts, "<keyword>");
    }

    /**
     * Returns the argument part of the input, rejecting input that has none.
     *
     * <p>Shared by the commands whose argument is a single piece of free text, so that
     * they report a missing argument the same way.
     *
     * @param parts the user input split into command and arguments
     * @param expectedFormat how the argument should look, shown to the user on failure
     * @return the argument text
     * @throws MissingArgumentException if the argument is absent or only whitespace
     */
    private String requireArgument(String[] parts, String expectedFormat) throws MissingArgumentException {
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new MissingArgumentException(parts[0] + " " + expectedFormat);
        }
        return parts[1];
    }
}
