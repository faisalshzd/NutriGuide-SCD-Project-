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

public class VegetarianDiet extends JFrame {
    private JTextField idField;
    private JTextField nameField;
    private JTextField dateField;
    private MealPanel2[] mealPanels;
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/nutriguide";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    static MealPanel2 previousSelectedPanel;

    public VegetarianDiet() {
        setTitle("Vegetarian Diet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 650);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(getClass().getResource("/Vdiet.jfif"));
        setIconImage(icon.getImage());

        JLabel title = createBoldLabel("Vegetarian Diet", Font.PLAIN, 36);
        JLabel infoLabel = createLabel("Information: The vegetarian diet excludes meat and fish but may include dairy and eggs.");
        JLabel followLabel = createLabel("Ways to Follow:");
        JLabel waysLabel = createLabel("1. Include a variety of fruits and vegetables.   2. Incorporate plant-based protein sources.");

        JLabel mealsLabel = createBoldLabel("Included Meals:", Font.PLAIN, 18);

        mealPanels = new MealPanel2[]{
                new MealPanel2("Caprese Salad", "Vemeal1.jfif", "Tomatoes, Mozzarella, Basil", "Vegetarian"),
                new MealPanel2("Vegetable Stir-Fry", "Vemeal2.jfif", "Mixed Vegetables, Tofu, Soy Sauce", "Vegetarian"),
                new MealPanel2("Eggplant Parmesan", "Vemeal3.jfif", "Eggplant, Tomato Sauce, Parmesan", "Vegetarian")
        };

        JLabel idLabel = createBoldLabel("ID:", Font.PLAIN, 14);
        idField = new JTextField();
        idField.setPreferredSize(new Dimension(150, 30));

        JLabel nameLabel = createBoldLabel("Name:", Font.PLAIN, 14);
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(150, 30));

        JLabel dateLabel = createBoldLabel("Date:", Font.PLAIN, 14);
        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(150, 30));
        dateField.setEditable(false);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
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
                submitDiet();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 0, 0);
        centerPanel.add(titlePanel, gbc);

        gbc.gridy = 1;
        centerPanel.add(infoLabel, gbc);

        gbc.gridy = 2;
        centerPanel.add(followLabel, gbc);

        gbc.gridy = 3;
        centerPanel.add(waysLabel, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        centerPanel.add(mealsLabel, gbc);

        int column = 0;
        int row = 5;

        for (MealPanel2 panel : mealPanels) {
            gbc.gridx = column;
            gbc.gridy = row;
            centerPanel.add(panel, gbc);
            column++;

            if (row == 5 && column == 1) {
                gbc.insets = new Insets(0, 0, 0, 50);
            }

            if (row == 6 && column == 2) {
                gbc.insets = new Insets(0, 0, 0, 0);
            }

            if (column > 2) {
                column = 0;
                row++;
            }
        }

        gbc.gridx = 0;
        gbc.gridy = row + 1;

        JPanel idNameDatePanel = new JPanel();
        idNameDatePanel.add(idLabel);
        idNameDatePanel.add(idField);
        idNameDatePanel.add(nameLabel);
        idNameDatePanel.add(nameField);
        idNameDatePanel.add(dateLabel);
        idNameDatePanel.add(dateField);

        centerPanel.add(idNameDatePanel, gbc);

        gbc.gridy = row + 2;
        centerPanel.add(selectDateButton, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(cancelButton);

        gbc.gridy = row + 3;
        centerPanel.add(buttonPanel, gbc);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
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

    private void submitDiet() {
        String id = idField.getText();
        String name = nameField.getText();
        String date = dateField.getText();

        if (id.isEmpty() || name.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!checkUserCredentials(id, name)) {
            JOptionPane.showMessageDialog(this, "Invalid user credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MealPanel2 selectedMealPanel = getSelectedMealPanel();

        if (selectedMealPanel == null) {
            JOptionPane.showMessageDialog(this, "Please select a meal.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mealName = selectedMealPanel.getMealName();
        String ingredients = selectedMealPanel.getIngredients();
        int option = JOptionPane.showOptionDialog(
                this,
                "ID: " + id + "\nName: " + name + "\nSelected Meal: " + mealName + "\nSelected Date: " + date,
                "Diet Submitted",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"OK", "Cancel"},
                "OK"
        );
        if (option == JOptionPane.OK_OPTION) {
            try {
                Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
                String sql = "INSERT INTO meal (Name, Ingredients, Diet, Date, ID, MealName) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, ingredients);
                    preparedStatement.setString(3, "Vegetarian");
                    preparedStatement.setString(4, date);
                    preparedStatement.setString(5, id);
                    preparedStatement.setString(6, mealName);
                    preparedStatement.executeUpdate();
                }
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (option == JOptionPane.CANCEL_OPTION) {
            dispose();
            new VegetarianDiet();
            clearForm();
        }
    }

    private boolean checkUserCredentials(String id, String name) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String sql = "SELECT * FROM user WHERE ID = ? AND `First Name` = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                try (var resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private MealPanel2 getSelectedMealPanel() {
        for (MealPanel2 panel : mealPanels) {
            if (panel.isSelected() && panel.isEnabled()) {
                return panel;
            }
        }
        return null;
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        dateField.setText("");
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> new VegetarianDiet());
    }
}

class MealPanel2 extends JPanel {
    private String mealName;
    private String imagePath;
    private String ingredients;
    private String dietType;
    private boolean selected;

    public MealPanel2(String mealName, String imagePath, String ingredients, String dietType) {
        this.mealName = mealName;
        this.imagePath = imagePath;
        this.ingredients = ingredients;
        this.dietType = dietType;
        this.selected = false;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        ImageIcon mealImage = new ImageIcon(getClass().getResource(imagePath));
        JLabel imageLabel = new JLabel(mealImage);

        JLabel nameLabel = new JLabel(mealName, SwingConstants.CENTER);
        nameLabel.setForeground(Color.BLACK);

        add(imageLabel, BorderLayout.CENTER);
        add(nameLabel, BorderLayout.SOUTH);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ((VegetarianDiet.previousSelectedPanel != null) && (VegetarianDiet.previousSelectedPanel != MealPanel2.this)) {
                    VegetarianDiet.previousSelectedPanel.setSelected(false);
                    VegetarianDiet.previousSelectedPanel.updateBorder();
                }

                selected = !selected;
                updateBorder();

                VegetarianDiet.previousSelectedPanel = selected ? MealPanel2.this : null;

                showIngredients();
            }
        });
    }

    private void showIngredients() {
        JOptionPane.showMessageDialog(
                this,
                "Selected Meal: " + mealName + "\nIngredients: " + ingredients + "\nDiet Type: " + dietType,
                "Meal Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public String getMealName() {
        return mealName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateBorder();
    }

    void updateBorder() {
        if (selected) {
            setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        } else {
            setBorder(null);
        }
    }
}
