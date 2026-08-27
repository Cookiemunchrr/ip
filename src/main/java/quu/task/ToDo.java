package quu.task;

import quu.exception.MissingArgumentException;

/**
 * Represents a task with no date attached to it.
 */
public class ToDo extends Task {

    /**
     * Constructs a todo with the given description.
     *
     * @param task_detail Description of the todo.
     */
    public ToDo(String task_detail) {
        super(task_detail);
    }

    /**
     * Returns the user-facing representation of this todo,
     * prefixed with the {@code [T]} type icon.
     *
     * @return A string such as {@code [T][ ] read book}.
     */
    @Override
    public String toString(){
        return "[T]" + super.toString();
    }

    /**
     * Returns the representation of this todo used when saving to disk.
     *
     * @return A string such as {@code T | 0 | read book}.
     */
    @Override
    public String toFileString(){
        return "T " + super.toFileString();
    }

    /**
     * Creates a todo from the fields of a line read from the save file.
     *
     * @param fields Fields of a saved line, where {@code fields[2]} holds the description.
     * @return The todo described by the given fields.
     * @throws MissingArgumentException If the description is missing or blank.
     */
    public static ToDo fromFileString(String[] fields) throws MissingArgumentException{
        try{
            if (fields[2].trim().isEmpty()){
                throw new MissingArgumentException(fields[0] + " <task>");
            }
            ToDo todo = new ToDo(fields[2]);
            return todo;
        } catch (ArrayIndexOutOfBoundsException e){
            throw new MissingArgumentException(fields[0] + " <task>");
        }
    }
}
