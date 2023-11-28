import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceTracker extends JFrame {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/nutriguide";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public PerformanceTracker() {
        setTitle("Performance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        // Set icon for the top bar
        ImageIcon icon = new ImageIcon(getClass().getResource("/PerformanceTracking.png"));
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
        add(homeButtonPanel, BorderLayout.WEST);
        // Ensure that the necessary JFreeChart libraries are in the classpath
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel chartPanel = createChartPanel();
        add(chartPanel);

        setVisible(true);
    }

    private JPanel createChartPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Performance Tracker",
                "User",
                "Calorie Count",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Set the color of the bars to yellow
        Color barColor = new Color(210, 210, 29); // Yellow
        plot.getRenderer().setSeriesPaint(0, barColor);

        return chart;
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<UserPerformance> userPerformances = getUserPerformances();

        for (UserPerformance userPerformance : userPerformances) {
            dataset.addValue(userPerformance.getCalorieCount(), "Calories", userPerformance.getUserName());
        }

        return dataset;
    }

    private List<UserPerformance> getUserPerformances() {
        List<UserPerformance> userPerformances = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            String sql = "SELECT Name, SUM(CalorieCount) AS TotalCalories FROM Calorie GROUP BY Name";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String userName = resultSet.getString("Name");
                    int calorieCount = resultSet.getInt("TotalCalories");
                    userPerformances.add(new UserPerformance(userName, calorieCount));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return userPerformances;
    }

    public static void main(String[] args) {
        // Ensure that the necessary JFreeChart libraries are in the classpath
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        SwingUtilities.invokeLater(PerformanceTracker::new);
    }
}

class UserPerformance {
    private String userName;
    private int calorieCount;

    public UserPerformance(String userName, int calorieCount) {
        this.userName = userName;
        this.calorieCount = calorieCount;
    }

    public String getUserName() {
        return userName;
    }

    public int getCalorieCount() {
        return calorieCount;
    }
}
