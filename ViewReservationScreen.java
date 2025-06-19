import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewReservationScreen extends JFrame {
    private ReservationController reservationController;

    public ViewReservationScreen() {
        setTitle("University Bus Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        reservationController = new ReservationController();

        // Header Panel
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 102, 204));
        header.setPreferredSize(new Dimension(0, 50));

        JLabel title = new JLabel("University Bus Management System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(title, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.ORANGE);
        logoutBtn.setFocusPainted(false);
        header.add(logoutBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Main Content Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table Setup
        String[] columnNames = {
            "ID", "Bus No", "Route", "Passenger Name", "Date", 
            "Time", "Start Location", "End Location", 
            "Purpose", "Passengers", "Vehicle Type"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1100, 500));

        // Load Data Button
        JButton loadButton = new JButton("Load Reservations");
        loadButton.setBackground(new Color(0, 102, 204));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFocusPainted(false);
        loadButton.addActionListener(e -> loadReservationData(tableModel));

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.setBackground(Color.WHITE);

        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footer = new JPanel();
        footer.setBackground(new Color(0, 102, 204));
        footer.setPreferredSize(new Dimension(0, 40));
        JLabel footerLabel = new JLabel("© 2025 University Transport Department");
        footerLabel.setForeground(Color.WHITE);
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        // Load data initially
        loadReservationData(tableModel);
    }

    private void loadReservationData(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Clear existing data
        
        List<Reservation> reservations = reservationController.getAllReservations();
        
        if (reservations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No reservations found in database", 
                "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Reservation r : reservations) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getBusNo(),
                r.getRoute(),
                r.getReserveName(),
                r.getDate(),
                r.getTime(),
                r.getStartLocation(),
                r.getEndLocation(),
                r.getPurpose(),
                r.getNoOfPassengers(),
                r.getVehicleType()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new ViewReservationScreen().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}