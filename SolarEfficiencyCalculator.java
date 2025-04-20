mport java.sql.*;
import java.util.Scanner;

public class SolarEfficiencyCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input state and district name
        System.out.print("Enter state name: ");
        String stateName = scanner.nextLine().trim();
        System.out.print("Enter district name: ");
        String districtName = scanner.nextLine().trim();
      
        try (Connection conn = DatabaseHelper.getConnection()) {
            // Fetch latitude, longitude, and sun intensity from the database
            String query = "SELECT latitude, longitude, avg_annual_solar_radiation FROM SolarIntensity WHERE state_name = ? AND district_name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, stateName);
                pstmt.setString(2, districtName);
                ResultSet rs = pstmt.executeQuery();
              
                if (rs.next()) {
                    double latitude = rs.getDouble("latitude");
                    double longitude = rs.getDouble("longitude");

                    // Check if the latitude and longitude are within valid ranges
                    try {
                        checkLatitudeRange(latitude);
                        checkLongitudeRange(longitude);
                    } catch (LatitudeOutOfRange | LongitudeOutOfRange e) {
                        System.out.println(e.getMessage());
                        return; // Exit if latitude/longitude is out of range
                    }
                    
                    double sunIntensity = rs.getDouble("avg_annual_solar_radiation");
                    double sunlightHours = DatabaseHelper.getSunlightHours(stateName, districtName);

                    // Fetch solar panel efficiency and total panel area from database
                    double[] panelData = getLatestPanelData();
                    double panelEfficiency = panelData[0];
                    double panelArea = panelData[1];

                    if (panelEfficiency <= 0 || panelArea <= 0) {
                        System.out.println("No panel data found. Please run SolarCostCalculator first.");
                        return;
                    }
