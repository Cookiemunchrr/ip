package quu.parser;

import java.time.format.DateTimeParseException;

import quu.exception.InvalidDateException;
import quu.exception.InvalidDurationException;
import quu.exception.InvalidIndexException;
import quu.exception.MissingArgumentException;
import quu.task.Deadline;
import quu.task.Event;
import quu.task.Task;
import quu.task.TaskList;
import quu.task.ToDo;

/**
 * Turns the argument portion of a user command into the object it describes.
 * Each method reports a usage hint through {@link MissingArgumentException} when
 * the arguments do not match the expected shape.
 */
public class Parser {

    /**
     * Parses an {@code event} command into an event task.
     *
     * @param taskList Current task list; reserved for future validation against existing tasks.
     * @param parts Command word and its argument string, in that order.
     * @return The event described by the arguments.
     * @throws MissingArgumentException If the description, start date, or end date is absent.
     * @throws InvalidDateException If either date is not in yyyy-mm-dd form.
     * @throws InvalidDurationException If the end date falls before the start date.
     */
    public Task parseEvent(TaskList taskList, String[] parts)
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
     * Parses a {@code deadline} command into a deadline task.
     *
     * @param taskList Current task list; reserved for future validation against existing tasks.
     * @param parts Command word and its argument string, in that order.
     * @return The deadline described by the arguments.
     * @throws MissingArgumentException If the description or the due date is absent.
     * @throws InvalidDateException If the due date is not in yyyy-mm-dd form.
     */
    public Task parseDeadline(TaskList taskList, String[] parts)
            throws MissingArgumentException, InvalidDateException {
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
     * Parses a {@code todo} command into a to-do task.
     *
     * @param taskList Current task list; reserved for future validation against existing tasks.
     * @param parts Command word and its argument string, in that order.
     * @return The to-do described by the arguments.
     * @throws MissingArgumentException If the description is absent or blank.
     */
    public Task parseToDo(TaskList taskList, String[] parts) throws MissingArgumentException {
        try {
            if (parts[1].trim().isEmpty()) {
                throw new MissingArgumentException(parts[0] + " <task>");
            }
            return new ToDo(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MissingArgumentException(parts[0] + " <task>");
        }
    }

    /**
     * Parses the one-based task number given to a {@code mark}, {@code unmark}, or
     * {@code delete} command.
     *
     * @param parts Command word and its argument string, in that order.
     * @return The task number as typed by the user; not yet checked against the list size.
     * @throws InvalidIndexException If the argument is not an integer.
     * @throws MissingArgumentException If no argument was given.
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
}
