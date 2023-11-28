import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    public LoginPage() {
        // Set frame properties
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        // Center the frame on the screen
        setLocationRelativeTo(null);
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/Pencil.png"));
        setIconImage(icon.getImage());
        // Create components
        JLabel appNameLabel = new JLabel("NutriGuide", SwingConstants.CENTER);
        appNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel loginLabel = new JLabel("Enter your credentials below:", SwingConstants.CENTER);
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        // Add empty border to increase spacing
        loginLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(20); // Increased the size
        passwordField = new JPasswordField(20); // Increased the size
        // Create Back button
        JButton backButton = new JButton("Back");
        // Add action listener to the Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
               // show the StartPage again
                StartPage startPage = new StartPage();
            }
        });
        JButton loginButton = new JButton("Login");
        messageLabel = new JLabel("");
        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                char[] enteredPasswordChars = passwordField.getPassword();
                String enteredPassword = new String(enteredPasswordChars);
                // Authenticate the user based on database information
                if (authenticateUser(enteredUsername, enteredPassword)) {
                    // Close the login page
                    dispose();
                    // Open the home page
                    new HomePage();
                } else {
                    messageLabel.setText("Login Failed. Please check your credentials.");
                }
                // Clear the password field for security
                passwordField.setText("");
            }
        });
        // Create panels and set layouts
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(appNameLabel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Padding
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        // Span across two columns
        gbc.gridwidth = 2;
        inputPanel.add(loginLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        // Reset gridwidth
        gbc.gridwidth = 1;
        inputPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        inputPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        inputPanel.add(messageLabel, gbc);

        // Add panels to the content pane
        add(topPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        // Set frame visibility
        setVisible(true);
    }
    private boolean authenticateUser(String enteredUsername, String enteredPassword) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/nutriguide";
        // Database username
        String dbUsername = "root";
        // Database password
        String dbPassword = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl,
                    dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user" +
                    " WHERE `First Name` = '" + enteredUsername + "'" +
                    " AND `Password` = '" + enteredPassword + "'");
            // If a record is found, the user is authenticated
            boolean isAuthenticated = resultSet.next();
            resultSet.close();
            statement.close();
            connection.close();
            return isAuthenticated;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Authentication failed due to an exception
            return false;
        }
    }
            public static void main(String[] args) {
        // Run the GUI code on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPage();
            }
        });
    }
}