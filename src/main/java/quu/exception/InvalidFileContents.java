package quu.exception;

/**
 * Signals that a line of the save file could not be read back into a task.
 */
public class InvalidFileContents extends QuuException {

    /**
     * Constructs an exception describing the corrupted line.
     *
     * @param message Text naming the file and the offending line.
     */
    public InvalidFileContents(String message) {
        super(message);
    }
}
