package persistence;

import model.Donation;
import model.DonationList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads list from file and returns it;
    // throws IOException if an error occurs reading data from file
    public DonationList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseDonationList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses list from JSON object and returns it
    private DonationList parseDonationList(JSONObject jsonObject) {
        DonationList list = new DonationList();
        addDonations(list, jsonObject);
        return list;
    }

    // MODIFIES: wr
    // EFFECTS: parses donations from JSON object and adds them to list
    private void addDonations(DonationList list, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("donations");
        for (Object json : jsonArray) {
            JSONObject nextDonation = (JSONObject) json;
            addDonation(list, nextDonation);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses donation from JSON object and adds it to list
    private void addDonation(DonationList list, JSONObject jsonObject) {
        Integer id = jsonObject.getInt("ID");
        Double amount = jsonObject.getDouble("Amount");
        String type = jsonObject.getString("Type");
        Donation donation = new Donation(id, amount, type);
        list.addDonation(donation);
    }
}
