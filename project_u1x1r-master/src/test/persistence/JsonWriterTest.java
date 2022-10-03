package persistence;

import model.Donation;
import model.DonationList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            DonationList list = new DonationList();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyDonationList() {
        try {
            DonationList list = new DonationList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyDonationList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyDonationList.json");
            list = reader.read();
            assertEquals(0, list.numDonations());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralDonationList() {
        try {
            DonationList list = new DonationList();
            list.addDonation(new Donation(1, 100,"h"));
            list.addDonation(new Donation(2, 200,"o"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralDonationList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralDonationList.json");
            list = reader.read();
            List<Donation> donations = list.getDonations();
            assertEquals(2, donations.size());
            checkDonation(1, 100.00, "h",donations.get(0));
            checkDonation(2, 200.00, "o",donations.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
