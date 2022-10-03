package ui;

import exceptions.DonationListFullException;
import exceptions.InvalidAmountException;
import exceptions.InvalidTypeException;
import model.Donation;
import model.DonationList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


// Donation application
public class BequeathApp {                     // some parts of the code are from the teller and JsonDemo project.
    private static final String JSON_STORE = "./data/donationlist.json";
    private Donation donation;
    private DonationList list;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the application
    public BequeathApp() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        boolean keepGoing = true;
        String command;

        init();

        System.out.println("Do you wish to load your save donation list? Press y or n");
        command = input.next();
        command = command.toLowerCase();
        processLoad(command);

        while (keepGoing) {
            displayMenu();
            input = new Scanner(System.in);
            command = input.next().toLowerCase();

            if (command.equals("q")) {
                System.out.println("Do you wish to save your donation list? Press y or n");
                command = input.next();
                command = command.toLowerCase();
                processQuit(command);
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            try {
                addDonation();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        } else if (command.equals("s")) {
            searchDonation();
        } else if (command.equals("t")) {
            viewTypes();
        } else if (command.equals("v")) {
            viewList();
        } else if (command.equals("c")) {
            changeDonation();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    private void processQuit(String command) {
        if (command.equals("y")) {
            saveDonationList();
        }
    }

    private void processLoad(String command) {
        if (command.equals("y")) {
            loadDonationList();
            donation.changeNextId(list.numDonations());
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes DonationList
    private void init() {
        donation = new Donation(0, 0,"h");
        list = new DonationList();
        input = new Scanner(System.in);
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add donation");
        System.out.println("\tv -> View the list of donations");
        System.out.println("\ts -> Search donation using id");
        System.out.println("\tc -> Change donation amount using ID");
        System.out.println("\tt -> Returns the types and total amount in the list");
        System.out.println("\tq -> Quit");
    }

    // MODIFIES: list
    // EFFECTS: prompts user to input amount and type of donation
    //          adds donation to list
    private void addDonation() {
        if (!list.isFull()) {
            System.out.println("Input the amount of donation.");
            double amount = input.nextDouble();

            if (amount > 0) {
                System.out.println("Input the type of donation in lowercase.");
                System.out.println("h for healthcare");
                System.out.println("e for education");
                System.out.println("o for others");
                String type = input.next();

                if (type.equals("h") || type.equals("e") || type.equals("o")) {
                    Donation selected = new Donation(-1, amount, type);
                    list.addDonation(selected);

                    System.out.println("\nDonation of: ");
                    System.out.println("$" + selected.getAmount() + " and " + "type " + selected.getType() + " added.");
                } else {
                    throw new InvalidTypeException();
                }
            } else {
                throw new InvalidAmountException();
            }
        } else {
            throw new DonationListFullException();
        }
    }

    // EFFECTS: prompts user to input the id of donation
    //          prints out the detailed description of the donation
    private void searchDonation() {
        try {
            System.out.println("Input the id of donation");

            input = new Scanner(System.in);
            int id = input.nextInt();
            Donation searchedDonation = list.searchDonation(id);

            if ((searchedDonation.getId() == id) && (id != 0)) {
                System.out.println("The ID of donation: " + searchedDonation.getId());
                System.out.println("The amount donated: " + searchedDonation.getAmount());
                System.out.println("The type of donation: " + searchedDonation.getType());
            } else {
                System.out.println("Donation of ID " + id + " not found.");
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

    }

    // MODIFIES: list
    // EFFECTS: prompts user to input the id of donation and new amount
    //          changes the value of donation.
    private void changeDonation() {
        try {
            System.out.println("Input the id of donation");

            input = new Scanner(System.in);
            int id = input.nextInt();

            Donation checkDonation = list.searchDonation(id);
            int checkId = checkDonation.getId();

            if ((checkId == id) && (checkId != 0)) {
                System.out.println("Add the new amount of donation ");
                double amount = input.nextDouble();
                list.changeDonation(id, amount);
            } else {
                System.out.println("Invalid ID.");
            }

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    // EFFECTS: prints out the detailed description of the donation types and total donations
    private void viewTypes() {
        ArrayList<String> types = list.viewTypes();
        int hcount = 0;
        int ecount = 0;
        int ocount = 0;

        System.out.println("\nThe types of donation in the list: ");

        for (String element : types) {
            if (element.equals("h")) {
                hcount = hcount + 1;
            } else if (element.equals("e")) {
                ecount = ecount + 1;
            } else if (element.equals("o")) {
                ocount = ocount + 1;
            }
        }
        System.out.println("Healthcare x" + hcount);
        System.out.println("Education x" + ecount);
        System.out.println("Other Forms of Charity x" + ocount);

        double total = list.donationTotal();
        System.out.println("Total amount of donations is: " + total);
    }

    // EFFECTS: prints out the list of the donations.
    private void viewList() {
        ArrayList<String> donations = list.viewList();

        System.out.println("ID:          " + "Amount:            " + "Type: ");
        for (String element : donations) {
            System.out.println(element);
        }
    }

    // EFFECTS: saves the list to file
    private void saveDonationList() {
        try {
            jsonWriter.open();
            jsonWriter.write(list);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads list from file
    private void loadDonationList() {
        try {
            list = jsonReader.read();
            System.out.println("Loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}