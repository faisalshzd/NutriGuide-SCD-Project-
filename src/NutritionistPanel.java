import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NutritionistPanel extends JFrame {
    private JTable nutritionistTable;
    private JButton selectButton;
    private JButton homeButton;

    private List<Nutritionist> nutritionists;
    private Nutritionist selectedNutritionist;

    // Database connection parameters
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/nutriguide";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public NutritionistPanel() {
        setTitle("Nutritionist Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/Nutritionist.png"));
        setIconImage(icon.getImage());

        nutritionists = loadNutritionistsFromDatabase();

        // Initialize UI components
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Status");

        for (Nutritionist nutritionist : nutritionists) {
            tableModel.addRow(new Object[]{nutritionist.getId(), nutritionist.getName(), nutritionist.getPhoneNumber(), nutritionist.getStatus()});
        }

        nutritionistTable = new JTable(tableModel);
        selectButton = new JButton("Select Nutritionist");
        homeButton = new JButton("Home");

        // Add action listeners
        selectButton.addActionListener(e -> selectNutritionist());
        homeButton.addActionListener(e -> goHome());

        // Set layout
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(nutritionistTable);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Align center
        buttonPanel.add(selectButton); // Add "Select Nutritionist" button

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Align at the bottom

        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Align left
        topButtonPanel.add(homeButton);

        // Create a border layout for the main panel
        setLayout(new BorderLayout());
        add(topButtonPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private List<Nutritionist> loadNutritionistsFromDatabase() {
        List<Nutritionist> nutritionists = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM nutritionist WHERE Status = 'Y'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    String phoneNumber = resultSet.getString("Phone Number");
                    String status = resultSet.getString("Status");
                    nutritionists.add(new Nutritionist(id, name, phoneNumber, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nutritionists;
    }

    private void selectNutritionist() {
        int selectedRow = nutritionistTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedNutritionist = nutritionists.get(selectedRow);
            nutritionists.remove(selectedNutritionist);
            updateTable();

            try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
                String updateQuery = "UPDATE nutritionist SET Status = 'N' WHERE ID = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, selectedNutritionist.getId());
                    updateStatement.executeUpdate();
                }

                // Show a message to the user
                JOptionPane.showMessageDialog(this,
                        "You have selected Nutritionist: " + selectedNutritionist.getName(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh the table after a delay (1 minute)
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
                            String updateQuery = "UPDATE nutritionist SET Status = 'Y' WHERE ID = ?";
                            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                updateStatement.setInt(1, selectedNutritionist.getId());
                                updateStatement.executeUpdate();
                            }

                            // Reload nutritionists in the table
                            updateTable();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }, 60000); // 2 minute

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a nutritionist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) nutritionistTable.getModel();
        model.setRowCount(0); // Clear existing rows
        for (Nutritionist nutritionist : nutritionists) {
            model.addRow(new Object[]{nutritionist.getId(), nutritionist.getName(), nutritionist.getPhoneNumber(), nutritionist.getStatus()});
        }
    }

    private void goHome() {
        dispose();
        new HomePage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NutritionistPanel::new);
    }
}

class Nutritionist {
    private int id;
    private String name;
    private String phoneNumber;
    private String status;

    public Nutritionist(int id, String name, String phoneNumber, String status) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
