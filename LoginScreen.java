import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        // Set up the frame
        setTitle("Login Screen");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        
        // Create components
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);
        
        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        panel.add(loginButton, gbc);
        
        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                // Simple validation (in a real app, you'd check against a database)
                if (username.equals("admin") && password.equals("password")) {
                    JOptionPane.showMessageDialog(LoginScreen.this, 
                            "Login Successful!", 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                    // Here you would typically open the main application window
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, 
                            "Invalid username or password", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Add panel to frame
        add(panel);
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });
    }
}