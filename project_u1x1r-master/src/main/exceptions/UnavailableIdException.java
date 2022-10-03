package exceptions;

public class UnavailableIdException extends RuntimeException {
    public UnavailableIdException() {
        super("Selected ID not available.");
    }
}
