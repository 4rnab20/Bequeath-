package exceptions;

public class DonationListFullException extends RuntimeException {
    public DonationListFullException() {
        super("Donation list is full");
    }
}
