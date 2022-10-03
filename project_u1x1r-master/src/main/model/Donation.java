package model;

// Represents a donation having an id, amount and type of donation

import exceptions.InvalidAmountException;
import exceptions.InvalidIdException;
import exceptions.InvalidTypeException;
import org.json.JSONObject;
import persistence.Writable;

public class Donation implements Writable {
    private static int nextDonationId = 1;          // tracks id of next donation created
    private int id;                                 // account id
    private double amount;                          // amount of donation
    private String type;                            // type of donation

    // REQUIRES: amount >= 0 AND (id >= 0 or id == -1)
    // MODIFIES: this
    // EFFECTS: incident has given case number and description, and is not closed
    public Donation(int id, double amount, String type) {
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new InvalidAmountException();
        }
        if (id == -1) {
            this.id = nextDonationId;             // when id = -1, an unique positive ID is assigned.
            changeNextId(1);
        } else if (id >= 0) {
            this.id = id;
        } else {
            throw new InvalidIdException();
        }
        if (type.equals("h") || type.equals("e") || type.equals("o")) {
            this.type = type;
        } else {
            throw new InvalidTypeException();
        }
    }

    // EFFECTS: returns id of donation
    public int getId() {
        return id;
    }

    // EFFECTS: returns id of donation after loading
    public void changeNextId(Integer i) {
        nextDonationId += i;
    }

    // EFFECTS: returns amount of donation
    public double getAmount() {
        return amount;
    }

    // EFFECTS: returns type of donation
    public String getType() {
        return type;
    }

    // EFFECTS: overrides equals for testing.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {                          // if both of them points the same address in memory
            return true;
        }

        // than we can cast it to Donation safely
        Donation objDonation = (Donation) obj;

        // if they have the same id, then the 2 objects are equal.
        return this.id == objDonation.id;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ID", id);
        json.put("Amount", amount);
        json.put("Type", type);
        return json;
    }
}