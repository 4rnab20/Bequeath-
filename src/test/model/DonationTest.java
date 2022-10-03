package model;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.InvalidAmountException;
import exceptions.InvalidIdException;
import exceptions.InvalidTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class DonationTest {
    private Donation testDonation;

    @BeforeEach
    void runBefore() {
        testDonation = new Donation(1, 500, "h");
    }

    @Test
    void testConstructor() {
        assertEquals(500, testDonation.getAmount());
        assertEquals("h", testDonation.getType());

        testDonation = new Donation(0, 0, "h");
        assertEquals(0, testDonation.getId());
        assertEquals(0, testDonation.getAmount());
        assertEquals("h", testDonation.getType());


        testDonation = new Donation(-1, 500, "o");
        assertEquals(1, testDonation.getId());
        assertEquals(500, testDonation.getAmount());
        assertEquals("o", testDonation.getType());

        testDonation = new Donation(2, 0, "e");
        assertEquals(2, testDonation.getId());
        assertEquals(0, testDonation.getAmount());
        assertEquals("e", testDonation.getType());

        try {
            testDonation = new Donation(-5, 10, "o");
        } catch (InvalidIdException e) {
            // Expected
        }

        try {
            testDonation = new Donation(1, -10, "o");
        } catch (InvalidAmountException e) {
            // Expected
        }

        try {
            testDonation = new Donation(1, 10, "k");
        } catch (InvalidTypeException e) {
            // Expected
        }

        try {
            testDonation = new Donation(5, 100, "o");
        } catch (InvalidIdException e) {
            fail("Error was not expected");
        }

    }
}