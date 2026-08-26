package quu.exception;

public class TaskNotFoundException extends QuuException{
    public TaskNotFoundException(int index) {
        super("There's no task at " + index + " use list to check available tasks.");
    }
}
