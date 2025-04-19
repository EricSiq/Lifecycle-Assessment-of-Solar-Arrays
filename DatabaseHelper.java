//DatabaseHelper.java
import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/PIJ_Project_Database";
    private static final String USER = "root";
    private static final String PASSWORD = "XlR8#2137";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found! Add the connector JAR.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet getProjects() throws SQLException {
        return executeQuery("SELECT * FROM Projects");
    }

    public static ResultSet getPowerPurchaseAgreements() throws SQLException {
        return executeQuery("SELECT * FROM PowerPurchaseAgreements");
    }

    public static ResultSet getSolarPanelData() throws SQLException {
        return executeQuery("SELECT * FROM SolarPanelData");
    }

    public static ResultSet getStateSubsidies() throws SQLException {
        return executeQuery("SELECT * FROM StateSubsidies");
    }

    public static ResultSet getSolarIntensity() throws SQLException {
       return executeQuery("SELECT * FROM SolarIntensity");
    }

    public static ResultSet getSolarPanelModels() throws SQLException {
       return executeQuery("SELECT panel_id, brand, model_name, power_output, cost_per_panel, length, width FROM SolarPanelModels");
    }
