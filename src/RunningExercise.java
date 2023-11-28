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
public class RunningExercise extends JFrame {
    private JTextField userIdField;
    private JTextField dateField;
    private JTextField distanceField;
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/nutriguide";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public RunningExercise() {
        // Set frame properties
        setTitle("Running Exercise");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        // Center the frame on the screen
        setLocationRelativeTo(null);
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/Running.png"));
        setIconImage(icon.getImage());
        // Create components
        JLabel title = createBoldLabel("Running Exercise", Font.PLAIN, 36);
        JLabel descriptionLabel = createLabel("Description: Running is a dynamic"
                + " cardiovascular activity that involves moving rapidly on foot.");
        JLabel advantagesLabel = createLabel("Advantages: Regular running" +
                " enhances heart health by improving blood circulation and");
        JLabel instructionsLabel = createLabel("Instructions: Warm-up, Start slow,"
                + " Proper form and Cool down");
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
        JLabel distanceLabel = createBoldLabel("Distance (meters):", Font.PLAIN, 14);
        distanceField = new JTextField();
        distanceField.setPreferredSize(new Dimension(200, 30));
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
        // Adjust the border for extra spacing
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Adjust the weighty to make the title and paragraphs closer
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 0, 0);
        centerPanel.add(titlePanel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
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

        gbc.gridy = 9;
        centerPanel.add(distanceLabel, gbc);

        gbc.gridy = 10;
        centerPanel.add(distanceField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitButton);
        // Add space between buttons
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(cancelButton);

        gbc.gridy = 11;
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
        String distanceText = distanceField.getText();
        int intDistanceText;
        if(distanceText.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else{
            intDistanceText= Integer.parseInt(distanceText);
        }
        if (!isValidUser(userId)) {
            JOptionPane.showMessageDialog(this, "User ID does not exist. Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
            // Clear the text fields
            userIdField.setText("");
            dateField.setText("");
            return;
        }
        if (userId.isEmpty() || date.isEmpty() || String.valueOf(distanceText).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validate if the entered distance is a valid number
        try {
            double distance = Double.parseDouble(String.valueOf(distanceText));
            if (distance < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number for Distance.", "Error", JOptionPane.ERROR_MESSAGE);
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
                preparedStatement.setString(1, "Running");
                preparedStatement.setInt(2, intDistanceText);
                preparedStatement.setString(3, date);
                preparedStatement.setString(4, userId);
                // Execute the query
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } finally {
                // Close the connection in a finaly block to ensure it's closed even if an exception occurs
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Perform actions when Submit button is clicked
            JOptionPane.showMessageDialog(this, "User ID: " + userId + "\nSelected Date: " + date + "\nDistance Run: " + distanceText + " m", "Exercise Submitted", JOptionPane.INFORMATION_MESSAGE);
            // Clear the text fields
            userIdField.setText("");
            dateField.setText("");
            distanceField.setText("");
            // Dispose the frame
            dispose();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        SwingUtilities.invokeLater(() -> new RunningExercise());
    }
}