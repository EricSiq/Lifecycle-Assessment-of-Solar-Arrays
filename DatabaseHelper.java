// Ameya
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

    public static double getSunlightHours(String state, String district) {
        String query = "SELECT avg_sunlight_hours_per_day FROM SolarIntensity WHERE state_name = ? AND district_name = ?";
        return fetchDouble(query, state, district);
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
// Ameya
  
    
