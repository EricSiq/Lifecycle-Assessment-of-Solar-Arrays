import java.sql.*;
import java.util.Scanner;

public class SolarEfficiencyCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input state and district name
        System.out.print("Enter state name: ");
        String stateName = scanner.nextLine().trim();
        System.out.print("Enter district name: ");
        String districtName = scanner.nextLine().trim();


double sunIntensity = rs.getDouble("avg_annual_solar_radiation");
                    double sunlightHours = DatabaseHelper.getSunlightHours(stateName, districtName);

                    // Fetch solar panel efficiency and total panel area from database
                    double[] panelData = getLatestPanelData();
                    double panelEfficiency = panelData[0];
                    double panelArea = panelData[1];

                    if (panelEfficiency <= 0 || panelArea <= 0) {
                        System.out.println("No panel data found. Please run SolarCostCalculator first.");
                        return;
                    }//Eric

        
        //Eric
                        // Calculate efficiency
                        double efficiencyFactor = calculateEfficiencyFactor(latitude);
                        double energyOutput = panelArea * sunIntensity * (panelEfficiency / 100) * efficiencyFactor;

                        // Display results
                        System.out.println("\n========================================");
                        System.out.println("        SOLAR EFFICIENCY REPORT");
                        System.out.println("========================================");
                        System.out.printf("Estimated Annual Energy Output: %.2f kWh/year\n", energyOutput * 365);
                        System.out.printf("Average Sunlight Hours per Day: %.2f hours\n", sunlightHours);
                        System.out.printf("Efficiency Factor (Latitude-based): %.2f\n", efficiencyFactor);
                        System.out.println("========================================\n");
                    } else {
                        System.out.println("District not found in database.");
                    }
                }
//Eric



//Eric

        // Function to adjust efficiency based on latitude
        private static double calculateEfficiencyFactor(double latitude) {
            return Math.cos(Math.toRadians(latitude - 23.5)) * 0.9 + 0.1; // Adjusted for better accuracy
        }

        // Retrieves latest panel efficiency & area from the database
        private static double[] getLatestPanelData() {
            double[] data = {0, 0};  // Default values if no data is found
            String query = "SELECT panel_efficiency, panel_area FROM UserSelectedPanel ORDER BY timestamp DESC LIMIT 1";


