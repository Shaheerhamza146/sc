// ManualBusReservationGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Graphical User Interface for the Bus Reservation System.
 * Provides a user-friendly interface for creating, viewing, editing, and canceling bus reservations.
 */
public class ManualBusReservationGUI extends JFrame {
    // Form input fields
    private JTextField busNoField, routeField, nameField, dateField, timeField;
    private JTextField startLocationField, endLocationField, purposeField, passengersField, vehicleTypeField;
    
    // Display area for existing reservations
    private JTextArea reservedBusesArea;
    
    // Action buttons
    private JButton editButton, cancelButton;
    
    // Business logic controller
    private BusReservationController controller;

    /**
     * Main constructor for the reservation GUI.
     * Initializes the UI components and sets up the controller.
     */
    public ManualBusReservationGUI() {
        try {
            // Basic window setup
            setTitle("Bus Reservation System");
            setSize(800, 700);
            setLocationRelativeTo(null); // Center the window
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            // Initialize controller with DAO
            controller = new BusReservationController(new BusReservationDAO());

            // Build the interface
            initializeUI();
            refreshReservations(); // Load initial data
            setVisible(true); // Make window visible (must be last)
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Failed to initialize application: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Initializes all UI components and layouts.
     */
    private void initializeUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create all form input fields
        busNoField = addField(panel, "Bus No:");
        routeField = addField(panel, "Route:");
        nameField = addField(panel, "Reservor Name:");
        dateField = addField(panel, "Date of Reservation:", controller.getCurrentDate());
        timeField = addField(panel, "Time of Reservation:", controller.getCurrentTime());
        startLocationField = addField(panel, "Start Location:");
        endLocationField = addField(panel, "End Location:");
        purposeField = addField(panel, "Purpose:");
        passengersField = addField(panel, "No. of Passengers:");
        vehicleTypeField = addField(panel, "Vehicle Type:");

        // Create and configure action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton reserveButton = new JButton("Reserve Bus");
        reserveButton.addActionListener(e -> reserveBus());
        
        editButton = new JButton("Edit Reservation");
        editButton.addActionListener(e -> editReservation());
        
        cancelButton = new JButton("Cancel Reservation");
        cancelButton.addActionListener(e -> cancelReservation());
        
        JButton refreshButton = new JButton("Refresh List");
        refreshButton.addActionListener(e -> refreshReservations());
        
        // Add buttons to panel
        buttonPanel.add(reserveButton);
        buttonPanel.add(editButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Create reservations display area
        reservedBusesArea = new JTextArea(15, 60);
        reservedBusesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reservedBusesArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Reserved Buses"));
        panel.add(scrollPane);

        add(panel);
    }

    /**
     * Helper method to create labeled text fields.
     * @param panel The parent panel to add to
     * @param label The field label text
     * @return The created text field
     */
    private JTextField addField(JPanel panel, String label) {
        return addField(panel, label, "");
    }

    /**
     * Helper method to create labeled text fields with default values.
     * @param panel The parent panel to add to
     * @param label The field label text
     * @param defaultValue The initial field value
     * @return The created text field
     */
    private JTextField addField(JPanel panel, String label, String defaultValue) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(150, 25));
        JTextField textField = new JTextField(defaultValue, 40);
        fieldPanel.add(jLabel);
        fieldPanel.add(textField);
        panel.add(fieldPanel);
        return textField;
    }

    /**
     * Handles the bus reservation process.
     * Validates inputs and creates a new reservation via the controller.
     */
    private void reserveBus() {
        // Create new reservation object from form data
        Reservation reservation = new Reservation();
        reservation.setBusNo(busNoField.getText().trim());
        reservation.setRoute(routeField.getText().trim());
        reservation.setReserveName(nameField.getText().trim());
        reservation.setDate(dateField.getText().trim());
        reservation.setTime(timeField.getText().trim());
        reservation.setStartLocation(startLocationField.getText().trim());
        reservation.setEndLocation(endLocationField.getText().trim());
        reservation.setPurpose(purposeField.getText().trim());
        
        try {
            // Validate passenger count
            reservation.setNoOfPassengers(Integer.parseInt(passengersField.getText().trim()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "No. of Passengers must be a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        reservation.setVehicleType(vehicleTypeField.getText().trim());

        // Attempt reservation creation
        String result = controller.createReservation(reservation);
        JOptionPane.showMessageDialog(this, result);
        refreshReservations();
        clearFields();
    }

    /**
     * Handles editing an existing reservation.
     * Opens an edit dialog for the selected reservation.
     */
    private void editReservation() {
        // Get selected reservation text
        String selectedText = reservedBusesArea.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Find the complete reservation line
        String fullText = reservedBusesArea.getText();
        String[] reservations = fullText.split("\n");
        String selectedReservation = "";
        for (String res : reservations) {
            if (res.contains(selectedText)) {
                selectedReservation = res;
                break;
            }
        }

        if (selectedReservation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Could not find selected reservation", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract reservation ID
        int id = Integer.parseInt(selectedReservation.split("\\|")[0].split(":")[1].trim());

        // Retrieve full reservation details
        Reservation reservation = findReservationById(id);
        if (reservation == null) {
            JOptionPane.showMessageDialog(this, "Reservation not found in database", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create and configure edit dialog
        JDialog editDialog = new JDialog(this, "Edit Reservation", true);
        editDialog.setLayout(new BorderLayout());
        editDialog.setSize(600, 500);
        editDialog.setLocationRelativeTo(this);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create edit fields with current values
        JTextField busNoField = new JTextField(reservation.getBusNo(), 20);
        JTextField routeField = new JTextField(reservation.getRoute(), 20);
        JTextField nameField = new JTextField(reservation.getReserveName(), 20);
        JTextField dateField = new JTextField(reservation.getDate(), 20);
        JTextField timeField = new JTextField(reservation.getTime(), 20);
        JTextField startField = new JTextField(reservation.getStartLocation(), 20);
        JTextField endField = new JTextField(reservation.getEndLocation(), 20);
        JTextField purposeField = new JTextField(reservation.getPurpose(), 20);
        JTextField passengersField = new JTextField(String.valueOf(reservation.getNoOfPassengers()), 20);
        JTextField vehicleField = new JTextField(reservation.getVehicleType(), 20);

        // Add fields to edit panel
        editPanel.add(createFieldPanel("Bus No:", busNoField));
        editPanel.add(createFieldPanel("Route:", routeField));
        editPanel.add(createFieldPanel("Name:", nameField));
        editPanel.add(createFieldPanel("Date:", dateField));
        editPanel.add(createFieldPanel("Time:", timeField));
        editPanel.add(createFieldPanel("Start Location:", startField));
        editPanel.add(createFieldPanel("End Location:", endField));
        editPanel.add(createFieldPanel("Purpose:", purposeField));
        editPanel.add(createFieldPanel("Passengers:", passengersField));
        editPanel.add(createFieldPanel("Vehicle:", vehicleField));

        // Configure save button
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            try {
                // Update reservation with edited values
                reservation.setBusNo(busNoField.getText().trim());
                reservation.setRoute(routeField.getText().trim());
                reservation.setReserveName(nameField.getText().trim());
                reservation.setDate(dateField.getText().trim());
                reservation.setTime(timeField.getText().trim());
                reservation.setStartLocation(startField.getText().trim());
                reservation.setEndLocation(endField.getText().trim());
                reservation.setPurpose(purposeField.getText().trim());
                reservation.setNoOfPassengers(Integer.parseInt(passengersField.getText().trim()));
                reservation.setVehicleType(vehicleField.getText().trim());

                // Attempt update
                boolean success = controller.updateReservation(reservation);
                if (success) {
                    JOptionPane.showMessageDialog(editDialog, "Reservation updated successfully!");
                    refreshReservations();
                    editDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(editDialog, "Failed to update reservation", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editDialog, "No. of Passengers must be a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        editPanel.add(Box.createVerticalStrut(20));
        editPanel.add(saveButton);
        editDialog.add(editPanel, BorderLayout.CENTER);
        editDialog.setVisible(true);
    }

    /**
     * Finds a reservation by its ID.
     * @param id The reservation ID to search for
     * @return The matching Reservation object, or null if not found
     */
    private Reservation findReservationById(int id) {
        List<Reservation> reservations = controller.getAllReservations();
        for (Reservation res : reservations) {
            if (res.getId() == id) {
                return res;
            }
        }
        return null;
    }

    /**
     * Helper method to create labeled field panels for the edit dialog.
     * @param label The field label
     * @param textField The text field component
     * @return Configured panel containing both elements
     */
    private JPanel createFieldPanel(String label, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(textField);
        return panel;
    }

    /**
     * Handles cancellation of an existing reservation.
     * Prompts for confirmation before proceeding with cancellation.
     */
    private void cancelReservation() {
        String selectedText = reservedBusesArea.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Find complete reservation line
        String fullText = reservedBusesArea.getText();
        String[] reservations = fullText.split("\n");
        int selectedIndex = -1;
        for (int i = 0; i < reservations.length; i++) {
            if (reservations[i].contains(selectedText)) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex != -1) {
            // Extract reservation ID
            int id = Integer.parseInt(reservations[selectedIndex].split("\\|")[0].split(":")[1].trim());
            
            // Confirm cancellation
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to cancel reservation #" + id + "?", 
                "Confirm Cancellation", 
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = controller.cancelReservation(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Reservation cancelled successfully!");
                    refreshReservations();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to cancel reservation", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Refreshes the reservations display area with current data from the database.
     */
    private void refreshReservations() {
        List<Reservation> reservations = controller.getAllReservations();
        StringBuilder sb = new StringBuilder();
        
        // Format each reservation for display
        for (Reservation res : reservations) {
            sb.append(String.format(
                "ID: %d | Bus No: %s | Route: %s | Name: %s | Date: %s | Time: %s | From: %s | To: %s | Purpose: %s | Passengers: %d | Vehicle: %s",
                res.getId(),
                res.getBusNo(),
                res.getRoute(),
                res.getReserveName(),
                res.getDate(),
                res.getTime(),
                res.getStartLocation(),
                res.getEndLocation(),
                res.getPurpose(),
                res.getNoOfPassengers(),
                res.getVehicleType()
            )).append("\n");
        }
        reservedBusesArea.setText(sb.toString());
    }

    /**
     * Clears all input fields and resets date/time to current values.
     */
    private void clearFields() {
        busNoField.setText("");
        routeField.setText("");
        nameField.setText("");
        dateField.setText(controller.getCurrentDate());
        timeField.setText(controller.getCurrentTime());
        startLocationField.setText("");
        endLocationField.setText("");
        purposeField.setText("");
        passengersField.setText("");
        vehicleTypeField.setText("");
    }

    /**
     * Main entry point for the application.
     * @param args Command line arguments (unused)
     */
    public static void main(String[] args) {
        // Launch GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new ManualBusReservationGUI());
    }
}