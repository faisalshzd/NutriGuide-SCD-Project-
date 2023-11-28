import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Diets extends JFrame {
    public Diets() {
        // Set frame properties
        setTitle("Types of Diets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 650);
        setLocationRelativeTo(null);

        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/DietIcon.png"));
        setIconImage(icon.getImage());

        // Create a home button
        JButton homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        homeButton.setBackground(new Color(173, 216, 230)); // Light blue
        homeButton.addActionListener(e -> {
            new HomePage();
            dispose();
        });

        // Create a panel for the home button
        JPanel homeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        homeButtonPanel.add(homeButton);
        homeButtonPanel.setBackground(Color.BLACK);
        // Add an action listener to handle panel click
        // Add the home button panel to the frame
        add(homeButtonPanel, BorderLayout.WEST);
        // Create diet panels
        DietPanel[] dietPanels = new DietPanel[]{
                new DietPanel("Keto", "Kdiet.png"),
                new DietPanel("Vegan", "Vgdiet.png"),
                new DietPanel("Vegetarian", "Vdiet.jfif"),
                new DietPanel("Mediterranean", "Mdiet.png"),
                new DietPanel("Low-carbohydrate", "LCdiet.jfif"),
                new DietPanel("Gluten-free", "Gdiet.jfif")
        };

        // Create a layout to arrange diet panels
        GridLayout gridLayout = new GridLayout(2, 3);
        // Increase horizontal gap between panels
        gridLayout.setHgap(20);
        // Increase vertical gap between panels
        gridLayout.setVgap(20);

        JPanel dietPanelContainer = new JPanel(gridLayout);
        dietPanelContainer.setBackground(Color.BLACK);

        // Add diet panels to the container
        for (DietPanel panel : dietPanels) {
            dietPanelContainer.add(panel);
        }

        // Create label for the heading
        JLabel headingLabel = new JLabel("Types of Diets", SwingConstants.CENTER);
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Create a panel for the heading
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.add(headingLabel, BorderLayout.CENTER);
        headingPanel.setBackground(Color.BLACK);
        headingPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Create a main panel to hold both heading and diet panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headingPanel, BorderLayout.NORTH);
        mainPanel.add(dietPanelContainer, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(mainPanel);

        // Set frame visibility
        setVisible(true);
    }

        private void openDietFrame(Class<?> dietClass) {
            try {
                // Instantiate the corresponding diet class and show its frame
                JFrame dietFrame = (JFrame) dietClass.getDeclaredConstructor().newInstance();
                dietFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Diets());
    }
}

class DietPanel extends JPanel {
    private String dietName;
    private String imagePath;

    public DietPanel(String dietName, String imagePath) {
        this.dietName = dietName;
        this.imagePath = imagePath;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Load image from resources
        ImageIcon dietImage = new ImageIcon(getClass().getResource(imagePath));
        JLabel imageLabel = new JLabel(dietImage);

        // Create a label for diet name
        JLabel nameLabel = new JLabel(dietName, SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);

        // Add components to the panel
        add(imageLabel, BorderLayout.CENTER);
        add(nameLabel, BorderLayout.SOUTH);

        // Add an action listener to handle panel click
        addMouseListener((MouseListener) new ActionListener());
    }
    private class ActionListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent evt) {
            switch (dietName) {
                case "Keto":
                    openDietFrame(new KetoDiet());
                    break;
                case "Vegan":
                    openDietFrame(new VeganDiet());
                    break;
                case "Vegetarian":
                    openDietFrame(new VegetarianDiet());
                    break;
                case "Mediterranean":
                    openDietFrame(new MediterraneanDiet());
                    break;
                case "Low-carbohydrate":
                    openDietFrame(new LowCarbohydrateDiet());
                    break;
                case "Gluten-free":
                    openDietFrame(new GlutenFreeDiet());
                    break;
                default:
                    break;
            }
        }
        private void openDietFrame(JFrame dietFrame) {
            dietFrame.setVisible(true);
        }
    }
}