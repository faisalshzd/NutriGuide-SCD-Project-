import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CalorieTracker extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextArea resultArea;
    private JTextArea calorieArea; // TextArea for displaying calories

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/nutriguide";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public CalorieTracker() {
        setTitle("Calorie Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        // Set the background color to light orange
        Color lightOrange = new Color(255, 200, 100); // You can adjust the RGB values as needed
        getContentPane().setBackground(lightOrange);
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/CalorieTracking.png"));
        setIconImage(icon.getImage());
        JLabel pageLabel = new JLabel("Calorie Tracker", SwingConstants.CENTER);
        pageLabel.setFont(new Font("Arial", Font.BOLD, 36));
        add(pageLabel, BorderLayout.NORTH);
        JButton homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        homeButton.setBackground(new Color(173, 216, 230)); // Light blue
        homeButton.addActionListener(e -> {
            dispose();
            new HomePage();
        });

        // Create a panel for the home button
        JPanel homeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        homeButtonPanel.add(homeButton);
        add(homeButtonPanel, BorderLayout.WEST);
        homeButtonPanel.setBackground(lightOrange);

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(10);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(10);

        JButton submitButton = new JButton("Submit");
        JButton calculateCaloriesButton = new JButton("Calculate Calories");

        resultArea = new JTextArea(15, 60);
        resultArea.setEditable(false);

        calorieArea = new JTextArea(2, 20);
        calorieArea.setEditable(false);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();

                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(CalorieTracker.this, "Please enter ID and Name.");
                    return;
                }

                if (checkUserCredentials(id, name)) {
                    displayUserDetails(id, name);
                } else {
                    JOptionPane.showMessageDialog(CalorieTracker.this, "Invalid user credentials.");
                }
            }
        });

        calculateCaloriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();

                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(CalorieTracker.this, "Please enter ID and Name.");
                    return;
                }
                // Disable the button to prevent multiple clicks
                calculateCaloriesButton.setEnabled(false);

                float totalCalories = calculateTotalCalories(id);
                calorieArea.setText("Total Calories: " + totalCalories);

                // Store calculated calories in the Calorie table
                storeCaloriesInDatabase(id, name);


            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(idLabel, gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);
        gbc.gridy = 3;
        panel.add(new JScrollPane(resultArea), gbc);
        gbc.gridy = 4;
        panel.add(new JScrollPane(calorieArea), gbc);
        gbc.gridy = 5;
        panel.add(calculateCaloriesButton, gbc);

        add(panel);
        panel.setBackground(lightOrange);
        setVisible(true);
    }

    private boolean checkUserCredentials(String id, String name) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String sql = "SELECT * FROM User WHERE ID = ? AND `First Name` = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void displayUserDetails(String id, String name) {
        resultArea.setText("User Details for ID: " + id + ", Name: " + name + "\n");

        // Retrieve and display exercise details
        resultArea.append("\nExercise Details:\n");
        displayDetailsFromExerciseTable("exercise", id);

        // Retrieve and display meal details
        resultArea.append("\nMeal Details:\n");
        displayDetailsFromMealTable("meal", id);
    }

    private void displayDetailsFromExerciseTable(String tableName, String id) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String sql = "SELECT * FROM " + tableName + " WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String date = resultSet.getString("Date");
                        int exerciseCount = resultSet.getInt("ExerciseCount");
                        String type = resultSet.getString("Type");
                        resultArea.append("Date: " + date + ", Exercise Count: " + exerciseCount + ", Type: " + type + "\n");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayDetailsFromMealTable(String tableName, String id) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String sql = "SELECT * FROM " + tableName + " WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String date = resultSet.getString("Date");
                        String diet = resultSet.getString("Diet");
                        String mealName = resultSet.getString("MealName");
                        resultArea.append("Date: " + date + ", Diet Followed: " + diet + ", Meal Name: " + mealName + "\n");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void storeCaloriesInDatabase(String id, String name) {
        float totalCalories = calculateTotalCalories(id); // Generate random calorie count
        calorieArea.setText("Total Calories: " + totalCalories); // Display the generated calories

        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String insertCaloriesSQL = "INSERT INTO Calorie (ID, Name, CalorieCount) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertCaloriesSQL)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setFloat(3, totalCalories);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private float calculateTotalCalories(String id) {
        float exerciseCalories = getExerciseCalories(id);
        float mealCalories = getMealCalories(id);

        // If both exercise and meal details are empty, set total calories to 0
        if (exerciseCalories == 0 && mealCalories == 0) {
            return 0;
        }

        // If exercise or meal details don't exist, set the corresponding calories to 0
        if (exerciseCalories < 0) {
            exerciseCalories = 0;
        }
        if (mealCalories < 0) {
            mealCalories = 0;
        }
        float exerciseCalories1 = generateRandomCalories();
        float mealCalories1 = generateRandomCalories();
        return exerciseCalories1 + mealCalories1;
    }

    private float getExerciseCalories(String id) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String sql = "SELECT SUM(ExerciseCount) AS TotalCalories FROM exercise WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getFloat("TotalCalories");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // Return -1 if there's an error or no exercise data
    }

    private float getMealCalories(String id) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String sql = "SELECT COUNT(*) AS MealCount FROM meal WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int mealCount = resultSet.getInt("MealCount");
                        // If there are no meals, return 0 calories
                        if (mealCount == 0) {
                            return 0;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // Return -1 if there's an error or no meal data
    }

    private float generateRandomCalories() {
        // Generate a random number between 100 and 500 for demonstration purposes
        return (float) (Math.random() * (500 - 100 + 1) + 100);
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        SwingUtilities.invokeLater(CalorieTracker::new);
    }
}
