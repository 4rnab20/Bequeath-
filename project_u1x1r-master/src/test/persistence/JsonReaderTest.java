package persistence;

import model.Donation;
import model.DonationList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            DonationList list = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyDonationList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyDonationList.json");
        try {
            DonationList list = reader.read();
            assertEquals(0, list.numDonations());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralDonationList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralDonationList.json");
        try {
            DonationList list = reader.read();
            List<Donation> donations = list.getDonations();
            assertEquals(2, donations.size());
            checkDonation(1, 100.00, "h", donations.get(0));
            checkDonation(2, 200.00, "o", donations.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}