package quu.task;

import quu.exception.MissingArgumentException;

/**
 * Represents a task with no associated date.
 */
public class ToDo extends Task {

    /**
     * Constructs a to-do that is initially not done.
     *
     * @param description Text describing what the task is.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Reconstructs a to-do from the fields of one save-file line.
     *
     * @param fields Line split into type letter, done flag, and description.
     * @return The reconstructed to-do.
     * @throws MissingArgumentException If the description is absent or blank.
     */
    public static ToDo fromFileString(String[] fields) throws MissingArgumentException {
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
