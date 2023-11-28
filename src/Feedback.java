import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Feedback extends JFrame {
    private JComboBox<String> nutritionistComboBox;
    private JTextArea feedbackTextArea;
    private JButton submitButton, homeButton;  // Added homeButton
    private JRadioButton excellentRadio, goodRadio, averageRadio, poorRadio;
    private ButtonGroup performanceGroup;

    private Map<String, String> nutritionistFeedbackMap;

    // Database connection parameters
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/nutriguide";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Feedback() {
        setTitle("Feedback Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 670);
        setLocationRelativeTo(null);

        nutritionistFeedbackMap = new HashMap<>();
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/FeedbackForm.jfif"));
        setIconImage(icon.getImage());
        // Create UI components
        JLabel titleLabel = new JLabel("Feedback Form:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titleLabel);

        // Added Home button
        homeButton = new JButton("Home");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Dispose the current frame
                new HomePage();  // Create and show the home page
            }
        });

        nutritionistComboBox = new JComboBox<>();
        loadNutritionistsFromDatabase();

        feedbackTextArea = new JTextArea(5, 20);

        excellentRadio = new JRadioButton("Excellent");
        goodRadio = new JRadioButton("Good");
        averageRadio = new JRadioButton("Average");
        poorRadio = new JRadioButton("Poor");

        performanceGroup = new ButtonGroup();
        performanceGroup.add(excellentRadio);
        performanceGroup.add(goodRadio);
        performanceGroup.add(averageRadio);
        performanceGroup.add(poorRadio);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitFeedback();
            }
        });

        // Set layout
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(homeButton);
        topPanel.add(titlePanel);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titlePanel, gbc);

        gbc.gridy = 1;
        mainPanel.add(new JLabel("Select Nutritionist:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(nutritionistComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Provide Feedback:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(new JScrollPane(feedbackTextArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Rate the performance of the app:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(excellentRadio, gbc);

        gbc.gridy = 4;
        mainPanel.add(goodRadio, gbc);

        gbc.gridy = 5;
        mainPanel.add(averageRadio, gbc);

        gbc.gridy = 6;
        mainPanel.add(poorRadio, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        mainPanel.add(submitButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadNutritionistsFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            String query = "SELECT name FROM nutritionist";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                var resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    nutritionistComboBox.addItem(resultSet.getString("Name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void submitFeedback() {
        String selectedNutritionist = (String) nutritionistComboBox.getSelectedItem();
        String feedbackText = feedbackTextArea.getText();
        String performanceRating = getPerformanceRating();

        if (!selectedNutritionist.isEmpty() && !feedbackText.isEmpty() && !performanceRating.isEmpty()) {
            nutritionistFeedbackMap.put(selectedNutritionist, feedbackText);

            // Store feedback and performance rating in the database
            storeFeedbackInDatabase(selectedNutritionist, feedbackText);

            JOptionPane.showMessageDialog(this, "Thank you! Your feedback has been submitted.", "Feedback Submitted", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new HomePage();
            // Optionally, you can clear the form after submission
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Please complete all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getPerformanceRating() {
        if (excellentRadio.isSelected()) {
            return "Excellent";
        } else if (goodRadio.isSelected()) {
            return "Good";
        } else if (averageRadio.isSelected()) {
            return "Average";
        } else if (poorRadio.isSelected()) {
            return "Poor";
        }
        return "";
    }

    private void storeFeedbackInDatabase(String nutritionistName, String feedbackText) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            String updateQuery = "UPDATE nutritionist SET Feedback = ? WHERE Name = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, feedbackText);
                updateStatement.setString(2, nutritionistName);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        nutritionistComboBox.setSelectedIndex(0);
        feedbackTextArea.setText("");
        performanceGroup.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Feedback::new);
    }
}
