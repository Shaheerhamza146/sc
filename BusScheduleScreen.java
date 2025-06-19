import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BusScheduleScreen extends JFrame {
//Connects the GUI to the business logic 
    private DefaultTableModel model;
    private BusScheduleController controller;
    private JComboBox<String> routeBox;
    private JComboBox<String> dateBox;
//Creates the entire GUI including:
    public BusScheduleScreen() {
        controller = new BusScheduleController();

        setTitle("Bus Schedule");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(25, 118, 210));
        topPanel.setPreferredSize(new Dimension(1000, 50));

        JLabel title = new JLabel("   🚌 University Bus Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Admin User  ");
        userLabel.setForeground(Color.WHITE);
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(255, 143, 0));
        logoutBtn.setBorderPainted(false);
        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        JLabel pageTitle = new JLabel("Bus Schedule", JLabel.CENTER);
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        centerPanel.add(pageTitle);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        filterPanel.setBackground(Color.WHITE);

        routeBox = new JComboBox<>();
        for (String route : controller.getAvailableRoutes()) {
            routeBox.addItem(route);
        }
        routeBox.setPreferredSize(new Dimension(200, 30));

        dateBox = new JComboBox<>();
        for (String date : controller.getAvailableDates()) {
            dateBox.addItem(date);
        }
        dateBox.setPreferredSize(new Dimension(150, 30));

        JButton viewBtn = new JButton("View Schedule");
        viewBtn.setPreferredSize(new Dimension(150, 30));
        viewBtn.setBackground(new Color(25, 118, 210));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setBorderPainted(false);

        filterPanel.add(routeBox);
        filterPanel.add(dateBox);
        filterPanel.add(viewBtn);
        centerPanel.add(filterPanel);

        // Tabs + Add Schedule Button
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tabPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 0));
        tabPanel.setBackground(Color.WHITE);

        JButton timeListBtn = new JButton("Time List");
        timeListBtn.setBackground(new Color(25, 118, 210));
        timeListBtn.setForeground(Color.BLACK);
        timeListBtn.setBorderPainted(false);

        JButton addScheduleBtn = new JButton("➕ Add Schedule");
        addScheduleBtn.setBackground(new Color(46, 125, 50));
        addScheduleBtn.setForeground(Color.WHITE);
        addScheduleBtn.setBorderPainted(false);

        tabPanel.add(timeListBtn);
        tabPanel.add(addScheduleBtn);
        centerPanel.add(tabPanel);

        // Table
        String[] columns = {"Date", "Route ID", "Departure Time", "Start Location", "End Location", "Stops", "Bus Number"};

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 240, 250));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        centerPanel.add(tableScroll);

        // Back to Dashboard button
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 10));
        backPanel.setBackground(Color.WHITE);
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.setBackground(Color.GRAY);
        backBtn.setForeground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(180, 30));
        backBtn.setBorderPainted(false);
        backBtn.addActionListener(e -> {
            dispose();
            new BusManagementDashboard().setVisible(true);// new UniversityBusManagementSystem(); // Link to your dashboard class
        });
        backPanel.add(backBtn);
        centerPanel.add(backPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(new Color(25, 118, 210));
        footer.setPreferredSize(new Dimension(1000, 40));
        JLabel footerLabel = new JLabel("  © 2025 University Transport Dept. | transport@university.edu | (555) 123-4567");
        footerLabel.setForeground(Color.WHITE);
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        // Load initial schedule
        loadSchedules(controller.getAllSchedules());

        // View button action
        viewBtn.addActionListener(e -> {
            String selectedRoute = (String) routeBox.getSelectedItem();
            String selectedDate = (String) dateBox.getSelectedItem();
            List<Object[]> filtered = controller.getSchedulesByRouteAndDate(selectedRoute, selectedDate);
            loadSchedules(filtered);
        });

        // Add schedule action
        addScheduleBtn.addActionListener(e -> showAddScheduleDialog());
    }
//Clears current rows in table.Adds new rows (each schedule is an Object[]).


    private void loadSchedules(List<Object[]> schedules) {
        model.setRowCount(0);
        for (Object[] row : schedules) {
            model.addRow(row);
        }
    }
//Opens a pop-up form (JDialog) to add a new schedule.
    private void showAddScheduleDialog() {
        JDialog dialog = new JDialog(this, "Add New Schedule", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JTextField dateField = new JTextField();
        JTextField routeIdField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField startField = new JTextField();
        JTextField endField = new JTextField();
        JTextField stopsField = new JTextField();
        JTextField busNoField = new JTextField();

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Route ID:"));
        formPanel.add(routeIdField);
        formPanel.add(new JLabel("Departure Time:"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Start Location:"));
        formPanel.add(startField);
        formPanel.add(new JLabel("End Location:"));
        formPanel.add(endField);
        formPanel.add(new JLabel("Stops (comma-separated):"));
        formPanel.add(stopsField);
        formPanel.add(new JLabel("Bus Number:"));
        formPanel.add(busNoField);

        JButton addBtn = new JButton("Add Schedule");
        addBtn.setBackground(new Color(25, 118, 210));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorderPainted(false);

        addBtn.addActionListener(e -> {
            String date = dateField.getText().trim();
            String routeId = routeIdField.getText().trim();
            String time = timeField.getText().trim();
            String start = startField.getText().trim();
            String end = endField.getText().trim();
            String stops = stopsField.getText().trim();
            String busNo = busNoField.getText().trim();

            if (date.isEmpty() || routeId.isEmpty() || time.isEmpty() || start.isEmpty() || end.isEmpty() || busNo.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all required fields.");
                return;
            }

            Object[] newSchedule = new Object[]{date, routeId, time, start, end, stops, busNo};
            controller.addSchedule(newSchedule);
            loadSchedules(controller.getAllSchedules());

            // Refresh dropdowns
            routeBox.removeAllItems();
            for (String route : controller.getAvailableRoutes()) routeBox.addItem(route);

            dateBox.removeAllItems();
            for (String d : controller.getAvailableDates()) dateBox.addItem(d);

            JOptionPane.showMessageDialog(dialog, "Schedule added successfully!");
            dialog.dispose();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
//Runs the GUI by launching an instance of BusScheduleScreen using SwingUtilities.invokeLater(...).
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new BusScheduleScreen().setVisible(true);
        });
    }
}