package exceptions;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Invalid amount selected.");
    }
}
