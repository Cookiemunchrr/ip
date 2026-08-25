public class InvalidDateException extends QuuException{
    public InvalidDateException(String input) {
        super("'" + input + "' is not a valid date. Use yyyy-mm-dd, e.g. 2026-06-06.");
    }
}
