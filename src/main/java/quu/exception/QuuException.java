package quu.exception;

/**
 * Represents an error that Quu can report to the user and recover from.
 * All application-specific exceptions in Quu extend this class, so the
 * main loop can catch them in one place.
 */
public class QuuException extends Exception{
    /**
     * Constructs an exception carrying a message meant to be shown to the user.
     *
     * @param message Explanation of what went wrong.
     */
    public QuuException(String message){
        super(message);
    }
}
