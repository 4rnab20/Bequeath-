package model;

import exceptions.DonationListFullException;
import exceptions.UnavailableIdException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// Represents a list of donations
// with maximum size MAX_SIZE

public class DonationList implements Writable {
    public static final int MAX_SIZE = 10;
    private LinkedList<Donation> list;

    // EFFECTS: list is empty
    public DonationList() {
        list = new LinkedList<>();
    }

    // REQUIRES: length of DonationList < MAX_SIZE
    // MODIFIES: this
    // EFFECTS: adds new donation to the list
    public void addDonation(Donation newDonation) {
        if (!isFull()) {
            list.addLast(newDonation);
        } else {
            throw new DonationListFullException();
        }
    }

    // EFFECTS: returns array of donation types in the list
    public ArrayList<String> viewTypes() {
        ArrayList<String> typeList = new ArrayList<>();
        for (int i = 0; i < numDonations(); i++) {
            Donation donation = list.get(i);
            typeList.add(donation.getType());
        }
        return typeList;
    }

    // EFFECTS: returns array of donations in the list
    public ArrayList<String> viewList() {
        ArrayList<String> donationList = new ArrayList<>();
        for (Donation element : list) {
            int id = element.getId();
            double amount = element.getAmount();
            String type = element.getType().toLowerCase();
            if (type.equals("h")) {
                type = "Healthcare";
            } else if (type.equals("e")) {
                type = "Education";
            } else {
                type = "Others";
            }
            donationList.add(id + "            " + amount + "            " + type);
        }
        return donationList;
    }

    // REQUIRES: at least 1 donation in the donation list and id > 0
    // EFFECTS: returns the donation in the donation list according to donation ID
    public Donation searchDonation(int id) {
        Donation donation;
        if ((list.size() >= id) && id > 0) {
            donation = list.get(id - 1);
        } else {
            throw new UnavailableIdException();
        }
        return donation;
    }

    // REQUIRES: id of an already listed donation and id > 0
    // MODIFIES: this
    // EFFECTS: returns new donation in place of the donation
    public void changeDonation(int id, double amount) {
        if ((id > 0) && (list.size() > 0) && (id <= list.size())) {
            Donation donation = list.get(id - 1);
            String type = donation.getType();
            Donation newDonation = new Donation(id, amount, type);
            list.set((id - 1), newDonation);
        } else {
            throw new UnavailableIdException();
        }
    }

    // EFFECTS: returns total donation of a type
    public double donationTotal() {
        double total = 0;
        Donation donation;
        for (int i = 0; i < numDonations(); i++) {
            donation = list.get(i);
            total = total + donation.getAmount();
        }
        return total;
    }

    // EFFECTS: returns get the amount for each type of donation
    public ArrayList<Double> getAmountOfType() {
        double hamount = 0;
        double eamount = 0;
        double oamount = 0;

        for (Donation element : list) {
            if (element.getType().equals("h")) {
                hamount = hamount + element.getAmount();
            } else if (element.getType().equals("e")) {
                eamount = eamount + element.getAmount();
            } else {
                oamount = oamount + element.getAmount();
            }
        }
        ArrayList<Double> typeamounts = new ArrayList<>();
        typeamounts.add(hamount);
        typeamounts.add(eamount);
        typeamounts.add(oamount);
        return typeamounts;
    }

    // EFFECTS: returns the number of donations in the list
    public int numDonations() {
        return list.size();
    }

    // EFFECTS: returns true if the list is full, else false
    public boolean isFull() {
        return list.size() == MAX_SIZE;
    }

    // EFFECTS: returns the donation at index i
    public Donation get(int i) {
        return list.get(i);
    }

    // EFFECTS: returns the donationList
    public List<Donation> getDonations() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("donations", donationsToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray donationsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Donation d : list) {
            jsonArray.put(d.toJson());
        }
        return jsonArray;
    }

}