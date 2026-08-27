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
 * Turns the arguments of a user command into the objects the rest of Quu works with.
 * Each method takes the command already split into its command word and its
 * argument string, and reports a {@link quu.exception.QuuException} when the
 * arguments do not fit the expected format.
 */
public class Parser {

    /**
     * Parses the arguments of an {@code event} command into an {@link Event}.
     *
     * @param taskList The current task list.
     * @param parts The command split into {@code [command word, arguments]}.
     * @return The event described by the arguments.
     * @throws MissingArgumentException If the description, start date or end date is missing.
     * @throws InvalidDateException If either date is not in {@code yyyy-mm-dd} format.
     * @throws InvalidDurationException If the end date falls before the start date.
     */
    public Task parseEvent(TaskList taskList, String[] parts) throws MissingArgumentException, InvalidDateException, InvalidDurationException{
        try{
            String[] e = parts[1].split(" /from ", 2);
            String[] t = e[1].split(" /to ", 2);
            return new Event(e[0], t[0], t[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task> /from <yyyy-mm-dd> /to <yyyy-mm-dd>");
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getParsedString());
        }
    }

    /**
     * Parses the arguments of a {@code deadline} command into a {@link Deadline}.
     *
     * @param taskList The current task list.
     * @param parts The command split into {@code [command word, arguments]}.
     * @return The deadline described by the arguments.
     * @throws MissingArgumentException If the description or due date is missing.
     * @throws InvalidDateException If the due date is not in {@code yyyy-mm-dd} format.
     */
    public Task parseDeadline(TaskList taskList, String[] parts) throws MissingArgumentException, InvalidDateException{
        try{
            String[] d = parts[1].split(" /by ", 2);
            return new Deadline(d[0], d[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task> /by <yyyy-mm-dd>");
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getParsedString());
        }
    }

    /**
     * Parses the arguments of a {@code todo} command into a {@link ToDo}.
     *
     * @param taskList The current task list.
     * @param parts The command split into {@code [command word, arguments]}.
     * @return The todo described by the arguments.
     * @throws MissingArgumentException If the description is missing or blank.
     */
    public Task parseToDo(TaskList taskList, String[] parts) throws MissingArgumentException{
        try{
            if (parts[1].trim().isEmpty()){
                throw new MissingArgumentException(parts[0] + " <task>");
            }
            return new ToDo(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task>");
        }
    }

    /**
     * Parses the argument of a command that refers to a task by its number,
     * such as {@code mark}, {@code unmark} or {@code delete}.
     *
     * @param parts The command split into {@code [command word, arguments]}.
     * @return The one-based task number given by the user.
     * @throws InvalidIndexException If the argument is not a whole number.
     * @throws MissingArgumentException If no task number was given.
     */
    public int parseTaskNumber(String[] parts) throws InvalidIndexException, MissingArgumentException{
        try {
            int index = Integer.parseInt(parts[1]);
            return index;
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(parts[1]);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(parts[0] + " <task number>");
        }
    }


}
