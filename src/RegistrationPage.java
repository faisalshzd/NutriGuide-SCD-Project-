import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Pattern;
public class RegistrationPage extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField ageField;
    public RegistrationPage() {
        // Set frame properties
        setTitle("Registration Page");
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
        JLabel registrationLabel = new JLabel("Enter your information to create an account:", SwingConstants.CENTER);
        registrationLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        // Add empty border to increase spacing
        registrationLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel firstNameLabel = new JLabel("First Name:");
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel phoneLabel = new JLabel("Phone Number:");
        JLabel ageLabel = new JLabel("Age:");
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        phoneField = new JTextField(20);
        ageField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        // Create Back button
        JButton backButton = new JButton("Back");
        // Add action listener to the Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
               //show the StartPage again
                StartPage startPage = new StartPage();
            }
        });
        // Add action listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    // Retrieve user input
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String email = emailField.getText();
                    String password = new String(passwordField.getPassword());
                    String phone = phoneField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    // Attempt to insert user into the database
                    int generatedId = insertUser(firstName, lastName, email, password, phone, age);
                    if (generatedId != -1) {
                        // Show success message along with the generated ID
                        JOptionPane.showMessageDialog(null,
                                "Account created successfully! Your ID is: " + generatedId,
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Clear the fields after successful submission
                        clearFields();
                        dispose();

                        // Open a new instance of the StartPage
                        StartPage startPage = new StartPage();
                    } else {
                        // Show error message if insertion fails
                        JOptionPane.showMessageDialog(null,
                                "Failed to create account. Please try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Add action listener to the cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the fields when cancel is clicked
                clearFields();
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
        inputPanel.add(registrationLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        // Reset gridwidth
        gbc.gridwidth = 1;
        inputPanel.add(firstNameLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(lastNameLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(ageLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        // Span across two columns
        gbc.gridwidth = 2;
        inputPanel.add(submitButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        // Span across two columns
        gbc.gridwidth = 2;
        inputPanel.add(cancelButton, gbc);

        // Add panels to the content pane
        add(topPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        // Set frame visibility
        setVisible(true);
    }
    private boolean validateFields() {
        boolean isValid = true;
        // Validate first name (characters only)
        if (!Pattern.matches("[a-zA-Z]+", firstNameField.getText())) {
            setInvalidField(firstNameField);
            isValid = false;
        } else {
            setValidField(firstNameField);
        }
        // Validate last name (characters only)
        if (!Pattern.matches("[a-zA-Z]+", lastNameField.getText())) {
            setInvalidField(lastNameField);
            isValid = false;
        } else {
            setValidField(lastNameField);
        }
        // Validate email format
        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", emailField.getText())) {
            setInvalidField(emailField);
            isValid = false;
        } else {
            setValidField(emailField);
        }
        // Validate password (non-empty)
        if (passwordField.getPassword().length == 0) {
            setInvalidField(passwordField);
            isValid = false;
        } else {
            setValidField(passwordField);
        }
        // Validate phone number (digits only, less than or equal to 11)
        if (!Pattern.matches("\\d{1,11}", phoneField.getText())) {
            setInvalidField(phoneField);
            isValid = false;
        } else {
            setValidField(phoneField);
        }
        // Validate age (digits only)
        if (!Pattern.matches("\\d+", ageField.getText())) {
            setInvalidField(ageField);
            isValid = false;
        } else {
            setValidField(ageField);
        }
        return isValid;
    }
    private void setInvalidField(JTextField field) {
        // Set the border color to red for invalid fields
        field.setBorder(new LineBorder(Color.RED));
    }
    private void setValidField(JTextField field) {
        // Set the default border for valid fields
        field.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
    }
    private void clearFields() {
        // Clear all input fields
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        ageField.setText("");
        // Reset the borders to default
        setValidField(firstNameField);
        setValidField(lastNameField);
        setValidField(emailField);
        setValidField(passwordField);
        setValidField(phoneField);
        setValidField(ageField);
    }
    private int insertUser(String firstName, String lastName, String email,
                           String password, String phone, int age) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/nutriguide";
        // Database username
        String dbUsername = "root";
        // Database password
        String dbPassword = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl,
                    dbUsername, dbPassword);

            // Use a prepared statement to avoid SQL injection
            String query = "INSERT INTO user (`First Name`, `Last Name`, `Email`, `Password`, `Phone Number`, `Age`)" +
                    " VALUES (?, ?, ?, ?, ?, ?)";

            // Specify that we want to retrieve the generated keys
            try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, password);
                preparedStatement.setString(5, phone);
                preparedStatement.setInt(6, age);

                int rowsAffected = preparedStatement.executeUpdate();

                // If rowsAffected is greater than 0, the insertion was successful
                if (rowsAffected > 0) {
                    // Retrieve the generated keys (in this case, just one key)
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        // Return the generated ID
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        // Return -1 if the insertion fails
        return -1;
    }

    private void displayUserID(int userID) {
        // Display a message with the user's ID
        JOptionPane.showMessageDialog(null,
                "Account created successfully! Your User ID is: " + userID,
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showStartPage() {
        dispose();
        // Open a new instance of the StartPage
        StartPage startPage = new StartPage();
    }

    public static void main(String[] args) {
        // Run the GUI code on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new RegistrationPage());
    }
}