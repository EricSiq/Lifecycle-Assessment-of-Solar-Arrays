import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/PIJ_Project_Database";
    private static final String USER = "username";
    private static final String PASSWORD = "password";

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

    public static double getSunlightHours(String state, String district) {
        String query = "SELECT avg_sunlight_hours_per_day FROM SolarIntensity WHERE state_name = ? AND district_name = ?";
        return fetchDouble(query, state, district);
    }

    private static ResultSet executeQuery(String query) throws SQLException {
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        return stmt.executeQuery(query);
    }

    public static ResultSet getStateSubsidyByState(String stateName) throws SQLException {
    String query = "SELECT * FROM StateSubsidies WHERE state_name = ?";
    Connection con = getConnection();
    PreparedStatement ps = con.prepareStatement(query);
    ps.setString(1, stateName);
    return ps.executeQuery();
    }
    
    private static double fetchDouble(String query, String param1, String param2) {
        double result = 0.0;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, param1);
            ps.setString(2, param2);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}

    
