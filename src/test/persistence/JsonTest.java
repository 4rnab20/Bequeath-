package persistence;

import model.Donation;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkDonation(Integer id, Double amount, String type, Donation donation) {
        assertEquals(id, donation.getId());
        assertEquals(amount, donation.getAmount());
        assertEquals(type, donation.getType());
    }
}
