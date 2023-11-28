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
public class SkippingropeExercise extends JFrame {
    private JTextField userIdField;
    private JTextField dateField;
    private JComboBox<Integer> ropeSkipsComboBox;
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/nutriguide";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public SkippingropeExercise() {
        // Set frame properties
        setTitle("Skipping Rope");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        // Center the frame on the screen
        setLocationRelativeTo(null);
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/Skipping.jfif"));
        setIconImage(icon.getImage());
        // Create components
        JLabel title = createBoldLabel("Skipping Rope", Font.PLAIN, 36);
        JLabel descriptionLabel = createLabel("Description: Skipping rope is a cardiovascular exercise that improves heart health and coordination.");
        JLabel advantagesLabel = createLabel("Advantages: Burns calories, improves agility, and enhances cardiovascular fitness.");
        JLabel instructionsLabel = createLabel("Instructions: Hold the handles of the skipping rope, swing it over your head, and jump over the rope as it comes down.");
        JLabel userIdLabel = createBoldLabel("User ID:", Font.PLAIN, 14);
        userIdField = new JTextField();
        userIdField.setPreferredSize(new Dimension(200, 30));
        JLabel dateLabel = createBoldLabel("Date:", Font.PLAIN, 14);
        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(200, 30));
        // Disable direct user input
        dateField.setEditable(false);
        JLabel ropeSkipsLabel = createBoldLabel("Skips Count:", Font.PLAIN, 14);
        Integer[] ropeSkips = new Integer[1000];
        for (int i = 1; i <= 1000; i++) {
            ropeSkips[i - 1] = i;
        }
        ropeSkipsComboBox = new JComboBox<>(ropeSkips);
        ropeSkipsComboBox.setPreferredSize(new Dimension(200, 30));
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
        centerPanel.add(ropeSkipsLabel, gbc);

        gbc.gridy = 10;
        centerPanel.add(ropeSkipsComboBox, gbc);

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
        // Get selected skips count
        int ropeSkips = (Integer) ropeSkipsComboBox.getSelectedItem();
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
                preparedStatement.setString(1, "Skipping Rope");
                preparedStatement.setInt(2, ropeSkips);
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
            JOptionPane.showMessageDialog(this, "User ID: " + userId + "\nSelected Date: " + date + "\nSkips Count: " + ropeSkips, "Exercise Submitted", JOptionPane.INFORMATION_MESSAGE);
            // Clear the text fields
            userIdField.setText("");
            dateField.setText("");
            // Reset the skips count to the default value
            ropeSkipsComboBox.setSelectedIndex(0);
            // Dispose the frame
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
        public static void main (String[]args){
            SwingUtilities.invokeLater(() -> new SkippingropeExercise());
        }
    }