import javax.swing.*;
import java.awt.*;

public class BusManagementDashboard extends JFrame {

    public BusManagementDashboard() {
        setTitle("University Bus Management System - Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔷 Top Header Bar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(25, 118, 210));
        topPanel.setPreferredSize(new Dimension(1000, 50));

        JLabel title = new JLabel("   🚌 University Bus Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(255, 143, 0));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(100, 30));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(logoutBtn);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ⚪ Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        JLabel pageTitle = new JLabel("Dashboard", JLabel.CENTER);
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        pageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pageTitle.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        centerPanel.add(pageTitle);

        // 🔘 First row of buttons
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        row1.setBackground(Color.WHITE);

        row1.add(createSmallButton("View Schedule", new Color(25, 118, 210), () -> openScreen("BusScheduleScreen")));
        row1.add(createSmallButton("View Reservations", new Color(46, 125, 50), () -> openScreen("ViewReservationScreen")));
        row1.add(createSmallButton("Manage Drivers", new Color(255, 143, 0), () -> openPlaceholder("Manage Drivers")));
        row1.add(createSmallButton("Manage Buses", new Color(0, 121, 107), () -> openPlaceholder("Manage Buses")));

        // 🔘 Second row of buttons
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        row2.setBackground(Color.WHITE);

        row2.add(createSmallButton("Reserve Bus/Edit,Cancel", new Color(156, 39, 176), () -> openPlaceholder("Reserve Bus/Edit,Cancel")));

        row2.add(createSmallButton("Maintenance", new Color(100, 221, 23), () -> openPlaceholder("Maintenance")));
        row2.add(createSmallButton("Manage Routes", new Color(3, 169, 244), () -> openPlaceholder("Manage Routes")));

        centerPanel.add(row1);
        centerPanel.add(row2);
        add(centerPanel, BorderLayout.CENTER);

        // 🔻 Footer
        JPanel footer = new JPanel();
        footer.setBackground(new Color(25, 118, 210));
        footer.setPreferredSize(new Dimension(1000, 40));
        JLabel footerLabel = new JLabel("  © 2025 University Transport Dept. | transport@university.edu | (555) 123-4567");
        footerLabel.setForeground(Color.WHITE);
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        // 🔘 Logout Action
        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You have been logged out.");
            System.exit(0);
        });
    }

    private JButton createSmallButton(String text, Color bg, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void openPlaceholder(String screenTitle) {
        dispose();
        new PlaceholderScreen(screenTitle).setVisible(true);
    }
//Dynamically switches to the correct screen (based on name).
    private void openScreen(String screenName) {
        dispose();
        switch (screenName) {
            case "BusScheduleScreen":
                new BusScheduleScreen().setVisible(true);
                break;
            case "ViewReservationScreen":
                new ViewReservationScreen().setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Screen not found.");
        }
    }
//Used for unimplemented features.
//Shows a label and back button to return to dashboard.
    // 🔧 Placeholder screen class
    private static class PlaceholderScreen extends JFrame {
        public PlaceholderScreen(String titleText) {
            setTitle(titleText);
            setSize(600, 400);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            JLabel label = new JLabel(titleText + " screen (not implemented)", JLabel.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 20));
            add(label, BorderLayout.CENTER);

            JButton backBtn = new JButton("Back to Dashboard");
            backBtn.setBackground(Color.GRAY);
            backBtn.setForeground(Color.WHITE);
            backBtn.setFocusPainted(false);
            backBtn.setPreferredSize(new Dimension(180, 30));
            backBtn.addActionListener(e -> {
                dispose();
                new BusManagementDashboard().setVisible(true);
            });

            JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            backPanel.add(backBtn);
            add(backPanel, BorderLayout.SOUTH);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BusManagementDashboard().setVisible(true));
    }
}