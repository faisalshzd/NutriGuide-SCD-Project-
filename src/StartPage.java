import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class StartPage extends JFrame {
    public StartPage() {
        // Set frame properties
        setTitle("NutriGuide Start Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        // Center the frame on the screen
        setLocationRelativeTo(null);
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/NG.png"));
        setIconImage(icon.getImage());
        // Set background color (Lightest Green)
        getContentPane().setBackground(new Color(204, 255, 204));
        // Create components
        JLabel appNameLabel = new JLabel("NutriGuide", SwingConstants.CENTER);
        appNameLabel.setFont(new Font("Arial", Font.BOLD, 36));
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");
        // Set button sizes
        Dimension buttonSize = new Dimension(150, 50);
        loginButton.setPreferredSize(buttonSize);
        createAccountButton.setPreferredSize(buttonSize);
        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the login page when the login button is clicked
                new LoginPage();
                // Close the home page
                dispose();
            }
        });
        // Add action listener to the create account button
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the registration page when the create account button is clicked
                new RegistrationPage();
                // Close the home page
                dispose();
            }
        });
        // Create panels and set layouts
        JPanel centerPanel = new JPanel(new GridBagLayout());
        // Make the panel transparent
        centerPanel.setOpaque(false);
        // Add components to the center panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(appNameLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0); // Add top margin
        centerPanel.add(loginButton, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0); // Add top margin
        centerPanel.add(createAccountButton, gbc);
        // Add the center panel to the content pane
        add(centerPanel);
        // Set frame visibility
        setVisible(true);
    }
    public static void main(String[] args) {
        // Run the GUI code on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartPage();
            }
        });
    }
}