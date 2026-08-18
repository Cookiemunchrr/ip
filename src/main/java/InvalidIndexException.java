public class InvalidIndexException extends QuuException{
    public InvalidIndexException(String input) {
        super("\"" + input +  "\" isn't a task number");
    }
}
