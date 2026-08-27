package quu.exception;

/**
 * Signals that the save file could not be read because one of its lines is corrupted.
 */
public class InvalidFileContents extends QuuException{
    /**
     * Constructs an exception describing the problem found in the save file.
     *
     * @param message Explanation of which part of the file is corrupted.
     */
    public InvalidFileContents(String message) {
        super(message);
    }
}
