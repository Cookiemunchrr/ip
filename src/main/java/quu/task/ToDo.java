package quu.task;

import quu.exception.MissingArgumentException;

/**
 * A task with only a description and no associated date.
 */
public class ToDo extends Task {

    /**
     * Creates a to-do.
     *
     * @param description text describing what the task is
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Rebuilds a to-do from a line of the save file.
     *
     * <p>The caller must have checked that {@code fields} holds all three parts;
     * {@link quu.storage.Storage#readFile()} rejects a shorter line before calling this.
     *
     * @param fields the save-file line split into type, done flag and description
     * @return the reconstructed to-do
     * @throws MissingArgumentException if the description is missing or blank
     */
    public static ToDo fromFileString(String[] fields) throws MissingArgumentException {
        assert fields.length >= 3 : "Storage checks the field count before rebuilding a task";
        try {
            if (fields[2].trim().isEmpty()) {
                throw new MissingArgumentException(fields[0] + " <task>");
            }
            return new ToDo(fields[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MissingArgumentException(fields[0] + " <task>");
        }
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toFileString() {
        return "T " + super.toFileString();
    }
}
