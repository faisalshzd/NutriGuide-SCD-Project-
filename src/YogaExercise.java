import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class YogaExercise extends JFrame {
    private JTextField userIdField;
    private JTextField dateField;
    // JDBC database URL, username, and password of MySQL server
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/nutriguide";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public YogaExercise() {
        // Set frame properties
        setTitle("Yoga");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/Yoga.png"));
        setIconImage(icon.getImage());
        // Create components
        JLabel title = createBoldLabel("Yoga", Font.PLAIN, 36);
        JLabel descriptionLabel = createLabel("Description: Yoga is a group of physical, mental, and spiritual practices or disciplines.");
        JLabel advantagesLabel = createLabel("Advantages: Improved flexibility, strength, and mental well-being.");
        JLabel instructionsLabel = createLabel("Instructions: Follow a yoga routine with various poses and breathing exercises.");
        JLabel userIdLabel = createBoldLabel("User ID:", Font.PLAIN, 14);
        userIdField = new JTextField();
        userIdField.setPreferredSize(new Dimension(200, 30));
        JLabel dateLabel = createBoldLabel("Date:", Font.PLAIN, 14);
        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(200, 30));
        // Disable direct user input
        dateField.setEditable(false);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose the frame on cancel button click
                dispose();
            }
        });
        JButton selectDateButton = new JButton("Select Date");
        selectDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDatePicker();
            }
        });
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitExercise();
            }
        });
        // Create panels and set layouts
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        // Center the title
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Adjust the border for extra spa
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Adjust the weighty to make the title and paragraphs closer
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        // Update the starting position to 0
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 0, 0);
        centerPanel.add(titlePanel, gbc); // Add titlePanel to centerPanel
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0); // Updated insets
        centerPanel.add(descriptionLabel, gbc);

        gbc.gridy = 2;
        centerPanel.add(advantagesLabel, gbc);

        gbc.gridy = 3;
        centerPanel.add(instructionsLabel, gbc);

        gbc.gridy = 4;
        centerPanel.add(userIdLabel, gbc);

        gbc.gridy = 5;
        centerPanel.add(userIdField, gbc);

        gbc.gridy = 6;
        centerPanel.add(dateLabel, gbc);

        gbc.gridy = 7;
        centerPanel.add(dateField, gbc);

        gbc.gridy = 8;
        centerPanel.add(selectDateButton, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitButton);
        // Add space between buttons
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(cancelButton);

        gbc.gridy = 9;
        centerPanel.add(buttonPanel, gbc);

        // Add components to mainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        // Add mainPanel to the frame
        add(mainPanel);
        // Set frame visibility
        setVisible(true);
    }
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        return label;
    }
    private JLabel createBoldLabel(String text, int style, int size) {
        JLabel label = createLabel(text);
        label.setFont(new Font(label.getFont().getName(), style | Font.BOLD, size));
        return label;
    }
    private void showDatePicker() {
        String inputDate = JOptionPane.showInputDialog("Enter Date (DD-MM-YYYY):");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = dateFormat.parse(inputDate);
            dateField.setText(dateFormat.format(date));
        } catch (ParseException | IllegalArgumentException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid date format. Please enter date in DD-MM-YYYY format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void submitExercise() {
        String userId = userIdField.getText();
        String date = dateField.getText();
        // Check if the user ID exists in the 'user' table
        if (!isValidUser(userId)) {
            JOptionPane.showMessageDialog(this, "User ID does not exist. Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
            // Clear the text fields
            userIdField.setText("");
            dateField.setText("");
            return;
        }

        if (userId.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Insert data into the database
        try {
            // Establish a connection
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            // Prepare the SQL statement
            String sql = "INSERT INTO exercise (Type, ExerciseCount, Date, ID) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set values for the parameters
                // Type of exercise
                preparedStatement.setString(1, "Yoga");
                preparedStatement.setInt(2, 0);
                preparedStatement.setString(3, date);
                preparedStatement.setString(4, userId);
                // Execute the query
                preparedStatement.executeUpdate();
            }
            // Close the connection
            connection.close();
            // Perform actions when Submit button is clicked
            JOptionPane.showMessageDialog(this, "User ID: " + userId + "\nSelected Date: " + date, "Exercise Submitted", JOptionPane.INFORMATION_MESSAGE);
            // Clear the text fields
            userIdField.setText("");
            dateField.setText("");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting exercise. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Check if the user ID exists in the 'user' table
    private boolean isValidUser(String userId) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String sql = "SELECT * FROM user WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userId);
                // Returns true if user exists
                return preparedStatement.executeQuery().next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Error occurred, consider user as invalid
            return false;
        }
    }
    public static void main(String[] args) {
        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> new YogaExercise());
    }
}