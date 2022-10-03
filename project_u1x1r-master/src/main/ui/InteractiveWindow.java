package ui;

import model.Donation;
import model.DonationList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;

// GUI for Bequeath
public class InteractiveWindow implements ActionListener {
    private static final String JSON_STORE = "./data/donationlist.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Donation donation;
    private DonationList donationList;

    JFrame window = new JFrame("Bequeath");
    JLabel amountLabel = new JLabel("Amount:");
    JTextField amountText = new JTextField();
    JTextArea output = new JTextArea("ID:     " + "    Amount:    " + "      Type:");
    JLabel typeLabel = new JLabel("Type of donation:");
    String[] types = {"healthcare", "education", "others"};
    JComboBox<String> typeDPMenu = new JComboBox<String>(types);
    JButton addButton = new JButton("Add Donation");
    JButton changeDonation = new JButton("Change Donation");
    JButton viewTypesAndTotal = new JButton("View types and total amount");
    JButton chartcreator = new JButton("Display a chart");


    // MODIFIES: this
    // EFFECTS: adds buttons to the panel
    public void button(JPanel panel) {
        panel.add(amountLabel);
        panel.add(amountText);
        panel.add(typeLabel);
        panel.add(typeDPMenu);
        panel.add(addButton);
        panel.add(changeDonation);
        panel.add(viewTypesAndTotal);
        panel.add(chartcreator);
    }

    // EFFECTS: creates an outlay of the application with all the buttons
    public void mainPanel() {
        window.setPreferredSize(new Dimension(500,400));
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                saveFile();
            }
        });

        window.setLayout(new GridLayout(1,3));
        JPanel panel = new JPanel(new GridLayout(0,1));
        window.add(panel);
        window.setLocation(800,350);
        button(panel);

        output.setPreferredSize(new Dimension(100, 100));
        output.setEditable(false);
        panel.add(output);
        output.setPreferredSize(new Dimension(100, 100));
        window.pack();
        window.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: records the input of the buttons in the application
    public void actions() {
        addButton.addActionListener(this);
        changeDonation.addActionListener(this);
        viewTypesAndTotal.addActionListener(this);
        chartcreator.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: loads list from file using a dialog box
    public void loadFile() {
        String title = "Load file";
        String description = "Do you wish to load your previous file?";
        int reply = JOptionPane.showConfirmDialog(null,description, title, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            loadDonationList();
            donation.changeNextId(donationList.numDonations());
            JOptionPane.showMessageDialog(null,loadDonationList());
        } else {
            JOptionPane.showMessageDialog(null,"New donation list created.");
        }
    }

    // EFFECTS: saves list to file using a dialog box
    public void saveFile() {
        String title = "Save file";
        String description = "Do you wish to save your file?";
        int reply = JOptionPane.showConfirmDialog(null,description, title, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            saveDonationList();
            JOptionPane.showMessageDialog(null,saveDonationList());
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else if (reply == JOptionPane.NO_OPTION)  {
            JOptionPane.showMessageDialog(null,"Donation list not saved");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    // EFFECTS: saves the list to file
    private String saveDonationList() {
        try {
            jsonWriter.open();
            jsonWriter.write(donationList);
            jsonWriter.close();
            return ("Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            return ("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads list from file
    private String loadDonationList() {
        try {
            donationList = jsonReader.read();
            return ("Loaded from " + JSON_STORE);
        } catch (IOException e) {
            return ("Unable to read from file: " + JSON_STORE);
        }
    }


    // EFFECTS: runs the application
    public InteractiveWindow() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        donation = new Donation(0, 0,"h");
        donationList = new DonationList();

        loadFile();
        mainPanel();
        actions();

        JScrollPane scrollableTextArea = new JScrollPane(output);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        window.getContentPane().add(scrollableTextArea);
        output.setText(viewList(donationList));
    }

    @Override
    // MODIFIES: this
    // EFFECTS: processes user input
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addButton) {
            double amount = Double.parseDouble(amountText.getText());
            String type = typeDPMenu.getSelectedItem().toString().substring(0, 1);
            Donation donation = new Donation(-1, amount, type);
            donationList.addDonation(donation);
            output.setText(viewList(donationList));
        }

        if (ae.getSource() == changeDonation) {
            String id = JOptionPane.showInputDialog(window,"Enter the donation ID.", null);
            String amount = JOptionPane.showInputDialog(window,"Enter the new amount.", null);
            donationList.changeDonation(Integer.parseInt(id),Double.parseDouble(amount));
            output.setText(viewList(donationList));
        }

        if (ae.getSource() == viewTypesAndTotal) {
            Double totalDonation = donationList.donationTotal();
            String totalAmount = "\nTotal amount of donation is: " + totalDonation;
            String totalType = "\nTypes of donation: " + viewTypes();
            JOptionPane.showMessageDialog(window, totalAmount + totalType);
        }

        if (ae.getSource() == chartcreator) {
            createChart();
        }
    }

    // EFFECTS: prints out the detailed description of the donation types and total donations
    private String viewTypes() {
        ArrayList<String> types = donationList.viewTypes();
        int hcount = 0;
        int ecount = 0;
        int ocount = 0;

        for (String element : types) {
            if (element.equals("h")) {
                hcount = hcount + 1;
            } else if (element.equals("e")) {
                ecount = ecount + 1;
            } else if (element.equals("o")) {
                ocount = ocount + 1;
            }
        }
        String output = "\nHealthcare x" + hcount + "\nEducation x" + ecount + "\nOther Forms of Charity x" + ocount;
        return output;
    }

    // EFFECTS: prints out the list of the donations.
    private String viewList(DonationList list) {
        ArrayList<String> donations = list.viewList();
        String output = "ID:     " + "    Amount:    " + "      Type:";

        for (String element : donations) {
            output = output + "\n" + element;
        }
        return output;
    }

    // EFFECTS: creates a barchart for the donations
    private void createChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String xl = "Type of Donation";
        String yl = "Amount of Donation";
        if (donationList != null) {
            dataset.setValue(donationList.getAmountOfType().get(0), yl, "Healthcare");
            dataset.setValue(donationList.getAmountOfType().get(1), yl, "Education");
            dataset.setValue(donationList.getAmountOfType().get(2), yl, "Others");
        }

        PlotOrientation o = PlotOrientation.VERTICAL;
        JFreeChart chart = ChartFactory.createBarChart("Bequeath Data", xl, yl, dataset, o, false, true, false);
        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.black);
        ChartFrame frame = new ChartFrame("Bar Chart to show donation data", chart);
        frame.setVisible(true);
        frame.setSize(450, 350);
        frame.setLocation(750,400);
    }
}