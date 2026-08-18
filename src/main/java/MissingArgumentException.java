public class MissingArgumentException extends QuuException{
    public MissingArgumentException(String usage) {
        super("Invalid format. Please follow this format: " + usage);
    }
}
