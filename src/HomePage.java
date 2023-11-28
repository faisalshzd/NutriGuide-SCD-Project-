import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class HomePage extends JFrame {
    public HomePage() {
        // Set frame properties
        setTitle("Home Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/Home.png"));
        setIconImage(icon.getImage());
        setLayout(new BorderLayout(0, 20));
        getContentPane().setBackground(Color.LIGHT_GRAY);
        // Create components
        JLabel welcomeLabel = new JLabel("Welcome to NutriGuide!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton dietsButton = createButton("Diets", new Color(173, 216, 230)); // Light blue
        JButton nutritionPanelButton = createButton("Nutrition Panel", new Color(174, 126, 226)); // Light green
        JButton calorieTrackerButton = createButton("Calorie Tracker", new Color(255, 165, 0)); // Orange
        JButton exercisesButton = createButton("Exercises", new Color(255, 192, 203)); // Light pink
        // Create performance charts button with line wrap
        JButton performanceButton = createButton("<html><center>Performance<br>Tracker</center></html>", new Color(218, 225, 29)); // Light sky blue
        JButton feedbackButton = createButton("Feedback", new Color(152, 251, 152)); // Light green
        // Create logout button with icon
        JButton logoutButton = new JButton("Logout", new ImageIcon(HomePage.class.getResource("Logout.jfif")));
        logoutButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        logoutButton.setHorizontalTextPosition(SwingConstants.CENTER);
        logoutButton.setBackground(new Color(255, 99, 71)); // Tomato red
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StartPage();
            }
        });
        Dimension buttonSize = new Dimension(150, 80);
        dietsButton.setPreferredSize(buttonSize);
        nutritionPanelButton.setPreferredSize(buttonSize);
        calorieTrackerButton.setPreferredSize(buttonSize);
        exercisesButton.setPreferredSize(buttonSize);
        performanceButton.setPreferredSize(buttonSize);
        feedbackButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);
        exercisesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current frame
                dispose();
                // Open the Exercise page
                new Exercises();
            }
        });
        dietsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current frame
                dispose();
                // Open the Diets page
                new Diets();
            }
        });
        calorieTrackerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current frame
                dispose();
                // Open the Calorie Tracker page
                new CalorieTracker();
            }
        });
        performanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current frame
                dispose();
                // Open the Performance Chart page
                new PerformanceTracker();
            }
        });
        nutritionPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current frame
                dispose();
                // Open the Nutritionist Panel page
                new NutritionistPanel();
            }
        });
        feedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current frame
                dispose();
                // Open the Feedback Form page
                new Feedback();
            }
        });
        // Create a panel for buttons using GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Set insets for spacing
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(dietsButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(nutritionPanelButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        buttonPanel.add(calorieTrackerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(exercisesButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        buttonPanel.add(performanceButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        buttonPanel.add(feedbackButton, gbc);

        // Add components to the frame
        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        // Create a panel for the logout button
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.add(logoutButton);
        logoutPanel.setBackground(Color.LIGHT_GRAY);
        // Add the logout panel to the south position
        add(logoutPanel, BorderLayout.SOUTH);
        // Set frame visibility
        setVisible(true);
    }
    private JButton createButton(String buttonText, Color buttonColor) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font("Arial", Font.PLAIN, 16)); // Decreased font size
        button.setBackground(buttonColor);
        return button;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage());
    }
}