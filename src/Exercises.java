import javax.swing.*;
import java.awt.*;
public class Exercises extends JFrame {
    public Exercises() {
        // Set frame properties
        setTitle("Exercises");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        // Center the frame on the screen
        setLocationRelativeTo(null);
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/Exercise Icon.png"));
        setIconImage(icon.getImage());
        // Create a home button
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
        homeButtonPanel.setBackground(Color.BLACK);
        add(homeButtonPanel, BorderLayout.WEST);
        // Create exercise panels
        ExercisePanel[] exercisePanels = new ExercisePanel[]{
                new ExercisePanel("Yoga", "Yoga.png"),
                new ExercisePanel("Running", "Running.png"),
                new ExercisePanel("Push-up", "Push-up.png"),
                new ExercisePanel("Squats", "Squat.jfif"),
                new ExercisePanel("Bench Press", "Bench press.jfif"),
                new ExercisePanel("Skipping Rope", "Skipping.jfif")
        };
        // Create a layout to arrange exercise panels
        GridLayout gridLayout = new GridLayout(2, 3);
        // Increase horizontal gap between panels
        gridLayout.setHgap(20);
        // Increase vertical gap between panels
        gridLayout.setVgap(20);
        JPanel exercisePanelContainer = new JPanel(gridLayout);
        // Set background color for the container
        exercisePanelContainer.setBackground(Color.BLACK);
        // Add exercise panels to the container
        for (ExercisePanel panel : exercisePanels) {
            exercisePanelContainer.add(panel);
        }
        // Create label for the heading
        JLabel headingLabel = new JLabel("Types of Exercises", SwingConstants.CENTER);
        // Set text color to white
        headingLabel.setForeground(Color.WHITE);
        // Set font and size
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        // Create a panel for the heading
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.add(headingLabel, BorderLayout.CENTER);
        headingPanel.setBackground(Color.BLACK);
        // Add empty border for spacing
        headingPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        // Create a main panel to hold both heading and exercise panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headingPanel, BorderLayout.NORTH);
        mainPanel.add(exercisePanelContainer, BorderLayout.CENTER);
        // Add the main panel to the frame
        add(mainPanel);
        // Set frame visibility
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Exercises());
    }
}
class ExercisePanel extends JPanel {
    private String exerciseName;
    private String imagePath;

    public ExercisePanel(String exerciseName, String imagePath) {
        this.exerciseName = exerciseName;
        this.imagePath = imagePath;
        setLayout(new BorderLayout());
        // Set background color for each panel
        setBackground(Color.BLACK);
        // Load image from resources
        ImageIcon exerciseImage = new ImageIcon(getClass().getResource(imagePath));
        JLabel imageLabel = new JLabel(exerciseImage);
        // Create a label for exercise name
        JLabel nameLabel = new JLabel(exerciseName, SwingConstants.CENTER);
        // Set text color to white
        nameLabel.setForeground(Color.WHITE);
        // Add components to the panel
        add(imageLabel, BorderLayout.CENTER);
        add(nameLabel, BorderLayout.SOUTH);
        // Add an action listener to handle panel click
        addMouseListener(new ActionListener());
    }

    private class ActionListener extends java.awt.event.MouseAdapter {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            // Create and show the corresponding exercise frame
            switch (exerciseName) {
                case "Yoga":
                    new YogaExercise();
                    break;
                case "Running":
                    new RunningExercise();
                    break;
                case "Push-up":
                    new PushupExercise();
                    break;
                case "Squats":
                    new SquatExercise();
                    break;
                case "Bench Press":
                    new BenchpressExercise();
                    break;
                case "Skipping Rope":
                    new SkippingropeExercise();
                    break;
                default:
                    System.out.println("Exercise not supported");
            }
        }
    }
}