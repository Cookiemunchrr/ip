public class UnknownCommandException extends QuuException{
    public UnknownCommandException(String command) {
        super("I don't know what \"" + command + "\" does");
    }
}
