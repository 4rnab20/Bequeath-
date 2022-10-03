package exceptions;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException() {
        super("Invalid ID selected.");
    }
}
