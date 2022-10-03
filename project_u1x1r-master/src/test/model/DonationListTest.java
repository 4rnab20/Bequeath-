package model;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.DonationListFullException;
import exceptions.InvalidAmountException;
import exceptions.UnavailableIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DonationListTest {
    private DonationList testDonationList;
    Donation testDonation1;
    Donation testDonation2;
    Donation testDonation3;

    @BeforeEach
    void runBefore() {
        testDonationList = new DonationList();
        testDonation1 = new Donation(1,100,"h");
        testDonation2 = new Donation(2,50,"e");
        testDonation3 = new Donation(3,25,"o");

    }

    @Test
    void testAddDonation() {
        testDonationList.addDonation(testDonation1);
        assertEquals(testDonation1, testDonationList.get(0));

        testDonationList = new DonationList();
        try {
            for (int i = 0; i < DonationList.MAX_SIZE; i++) {
                testDonationList.addDonation(testDonation1);
            }
        } catch (DonationListFullException e) {
            fail("Error is not expected");
        }

        try {
            testDonationList.addDonation(testDonation2);
        } catch (DonationListFullException e) {
        }

        assertNotEquals(testDonation2, testDonationList.get(DonationList.MAX_SIZE - 1));
        assertEquals(testDonation1, testDonationList.get(DonationList.MAX_SIZE - 1));
    }

    @Test
    void testViewTypes() {
        String[] testTypes1 = {};
        assertArrayEquals(testTypes1, testDonationList.viewTypes().toArray());

        testDonationList.addDonation(testDonation1);
        testDonationList.addDonation(testDonation2);
        String[] testTypes = {"h", "e"};
        assertArrayEquals(testTypes, testDonationList.viewTypes().toArray());
    }

    @Test
    void testViewList() {
        String[] testTypes1 = {};
        assertArrayEquals(testTypes1, testDonationList.viewList().toArray());

        testDonationList.addDonation(testDonation1);
        testDonationList.addDonation(testDonation2);
        testDonationList.addDonation(testDonation3);

        String[] testTypes = {"1            100.0            Healthcare", "2            50.0            Education", "3            25.0            Others"};
        assertArrayEquals(testTypes, testDonationList.viewList().toArray());
    }

    @Test
    void testSearchDonation() {
        try {
            testDonationList.searchDonation(1);
        } catch (UnavailableIdException e) {
            // Expected
        }

        try {
            testDonationList.searchDonation(-1);
        } catch (UnavailableIdException e) {
            // Expected
        }

        testDonationList.addDonation(testDonation1);

        try {
            testDonationList.searchDonation(1);
        } catch (UnavailableIdException e) {
            fail("Should not show any exceptions.");
        }

        testDonationList.addDonation(testDonation2);
        assertEquals(testDonation1, testDonationList.searchDonation(1));
        assertEquals(testDonation2, testDonationList.searchDonation(2));
    }

    @Test
    void testChangeDonation() {
        try {
            testDonationList.changeDonation(1,100);
        } catch (UnavailableIdException e) {
            // Expected
        }

        testDonationList.addDonation(testDonation1);

        try {
            testDonationList.changeDonation(1,25);
        } catch (UnavailableIdException e) {
            fail("Error not expected");
        }

        try {
            testDonationList.changeDonation(1,-100);
        } catch (InvalidAmountException e) {
            // Expected
        }

        try {
            testDonationList.changeDonation(3,25);
        } catch (UnavailableIdException e) {
            // Expected
        }

        try {
            testDonationList.changeDonation(-1,-25);
        } catch (UnavailableIdException e) {
            // Expected
        }

        testDonationList.addDonation(testDonation2);

        testDonationList.changeDonation(1,200);
        testDonationList.changeDonation(2,100);

        Donation newTestDonation1 = new Donation(1,200,"h");
        Donation newTestDonation2 = new Donation(2,100,"e");
        assertEquals(newTestDonation1, testDonationList.get(0));
        assertEquals(newTestDonation2, testDonationList.get(1));
    }

    @Test
    void testDonationTotal() {
        assertEquals(0, testDonationList.donationTotal());

        testDonationList.addDonation(testDonation1);
        assertEquals(100, testDonationList.donationTotal());

        testDonationList.addDonation(testDonation2);
        assertEquals(150, testDonationList.donationTotal());
    }

    @Test
    void getAmountOfList() {
        assertEquals(testDonationList.getAmountOfType().get(0), 0);
        assertEquals(testDonationList.getAmountOfType().get(1), 0);
        assertEquals(testDonationList.getAmountOfType().get(2), 0);

        Donation testDonation4 = new Donation(3,25,"h");

        testDonationList.addDonation(testDonation1);
        testDonationList.addDonation(testDonation2);
        testDonationList.addDonation(testDonation3);
        testDonationList.addDonation(testDonation4);

        assertEquals(testDonationList.getAmountOfType().get(0), 125);
        assertEquals(testDonationList.getAmountOfType().get(1), 50);
        assertEquals(testDonationList.getAmountOfType().get(2), 25);
    }
}
