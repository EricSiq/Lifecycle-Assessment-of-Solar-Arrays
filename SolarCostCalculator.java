import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SolarCostCalculator {
    private static double latestPanelEfficiency;
    private static double latestPanelArea;

    public static void setLatestPanelData(double efficiency, double area) {
        latestPanelEfficiency = efficiency;
        latestPanelArea = area;
    }

    public static double getLatestPanelEfficiency() {
        return latestPanelEfficiency;
    }

    public static double getLatestPanelArea() {
        return latestPanelArea;
    }

    public static void calculateAndStorePanelEfficiency(int selectedPanelId) {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT power_output, length, width FROM SolarPanelModels WHERE panel_id = ?")) {

            pstmt.setInt(1, selectedPanelId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double powerOutput = rs.getDouble("power_output");
                double length = rs.getDouble("length");
                double width = rs.getDouble("width"); // Fixed extra parenthesis here.

                double panelArea = length * width;
                double panelEfficiency = (powerOutput / panelArea) * 100;

                setLatestPanelData(panelEfficiency, panelArea);

                try (PreparedStatement updateStmt = conn.prepareStatement(
                    "INSERT INTO UserSelectedPanel (panel_efficiency, panel_area) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE panel_efficiency = VALUES(panel_efficiency), panel_area = VALUES(panel_area)")) {

                    updateStmt.setDouble(1, panelEfficiency);
                    updateStmt.setDouble(2, panelArea);
                    updateStmt.executeUpdate();
                }
            } else {
                System.out.println("Panel ID not found in database.");
            }
       } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 
    public static void main(String[] args) {
        displaySolarPanelModels();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Panel ID you want to select: ");
        int selectedPanelId = scanner.nextInt();

        calculateAndStorePanelEfficiency(selectedPanelId);

        double panelEfficiency = getLatestPanelEfficiency();
        double panelArea = getLatestPanelArea();

        System.out.print("Enter your rooftop length (in meters): ");
        double rooftopLength = scanner.nextDouble();
        System.out.print("Enter your rooftop width (in meters): ");
        double rooftopWidth = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter your state: ");
        String state = scanner.nextLine();

        System.out.print("Enter your district: ");
        String district = scanner.nextLine();

        OptimalPanelLayout layout = calculateOptimalLayout(rooftopLength, rooftopWidth, selectedPanelId);

        // Fetch all required data in a single query
        try (Connection con = DatabaseHelper.getConnection()) {
            // Fetch cost_per_panel from SolarPanelModels and subsidy_percent from StateSubsidies
            double costPerPanel = fetchDouble("SELECT cost_per_panel FROM SolarPanelModels WHERE panel_id = ?", selectedPanelId);
            double subsidyPercent = fetchDouble("SELECT subsidy_percent FROM StateSubsidies WHERE state_name = ?", state);

            // Fetch tariff and net_metering from Projects
            double tariff = fetchDouble("SELECT tariff FROM Projects WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ? LIMIT 1)", state);
            boolean netMetering = fetchBoolean("SELECT net_metering FROM Projects WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ? LIMIT 1)", state);

            // Fetch additional data like panel_lifespan, degradation_rate, maintenance_cost from SolarPanelData
            double panelLifespan = fetchDouble("SELECT panel_lifespan FROM SolarPanelData WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ?)", state);
            double degradationRate = fetchDouble("SELECT degradation_rate FROM SolarPanelData WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ?)", state);
            double maintenanceCost = fetchDouble("SELECT maintenance_cost FROM SolarPanelData WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ?)", state);

            double totalPanelCost = layout.totalPanels * costPerPanel;
            double finalCost = totalPanelCost * (1 - subsidyPercent / 100);
            double totalMaintenanceCost = maintenanceCost * panelLifespan;
            double systemSize = layout.totalPanels * 0.3;
            double avgSunlightHours = DatabaseHelper.getSunlightHours(state, district);

            double annualEnergyOutput = systemSize * avgSunlightHours * 365;
            double electricityPrice = 6.50; // Rs. per kWh

            double annualSavings = annualEnergyOutput * electricityPrice;
            annualSavings *= 1.04; // 4% extra savings due to net metering

            double lifetimeSavings = annualSavings * panelLifespan;
            double paybackPeriod = finalCost / annualSavings;

            double roi = (lifetimeSavings / finalCost) * 100;

            System.out.println("\n========================================");
            System.out.println("          SOLAR PROJECT SUMMARY          ");
            System.out.println("========================================");
            System.out.printf("Panel ID Selected: %d\n", selectedPanelId);
            System.out.printf("Panel Cost Per Unit: Rs. %.2f\n", costPerPanel);
            System.out.printf("Panel Orientation :- Horizontal: %d, Vertical: %d, Total: %d\n",
                    layout.horizontalPanels, layout.verticalPanels, layout.totalPanels);
            System.out.printf("Total System Size: %.2f kW\n", layout.systemSize);
            System.out.printf("Total Panel Cost (Before Subsidy): Rs. %.2f\n", totalPanelCost);
            System.out.printf("Subsidy Applied: %.2f%%\n", subsidyPercent);
            System.out.printf("Final Cost After Subsidy: Rs. %.2f\n", finalCost);
            System.out.printf("Total Maintenance Cost (Over 25 years): Rs. %.2f\n", totalMaintenanceCost);
            System.out.printf("Tariff at Start: Rs. %.2f per kWh\n", tariff);
            System.out.printf("Estimated Annual Energy Output: %.2f kWh\n", annualEnergyOutput);
            System.out.printf("Estimated Annual Savings (Without Net Metering): Rs. %.2f\n", annualSavings / 1.10);
            if (netMetering) {
                System.out.println("Net Metering Benefits Applied: 4% Extra Savings");
            }
            System.out.printf("Estimated Lifetime Energy Savings: Rs. %.2f\n", lifetimeSavings);
            System.out.printf("Return on Investment (ROI): %.2f%%\n", roi);
            System.out.printf("Payback Period: %.2f years\n", paybackPeriod);
            System.out.printf("Panel Efficiency: %.2f%%\n", panelEfficiency);
            System.out.printf("Panel Area: %.2f m²\n", panelArea);
            System.out.println("========================================\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }        

    private static void displaySolarPanelModels() {
        System.out.println("Available Solar Panels:");
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println("ID    Brand          Model              Power (kW)  Cost (Rs.)  Length (m)  Width (m)");
        System.out.println("----------------------------------------------------------------------------------------------");

        String query = "SELECT panel_id, brand, model_name, cost_per_panel, length, width, power_output FROM SolarPanelModels";

        try (Connection con = DatabaseHelper.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int panelId = rs.getInt("panel_id");
                String brand = rs.getString("brand");
                String model = rs.getString("model_name");
                double powerOutput = rs.getDouble("power_output");
                double cost = rs.getDouble("cost_per_panel");
                double length = rs.getDouble("length");
                double width = rs.getDouble("width");

                System.out.printf("%-5d %-12s %-18s %-10.2f %-10.2f %-10.2f %-10.2f\n",
                        panelId, brand, model, powerOutput, cost, length, width);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching solar panel models from database.");
            e.printStackTrace();
        }

            System.out.println("----------------------------------------------------------------------------------------------");
    }

    private static double fetchDouble(String query, Object... params) {
        try (Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return 0;
    }        


    private static boolean fetchBoolean(String query, Object... params) {
        try (Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }

        } catch (SQLException e) {
              e.printStackTrace();
        }
        return false;
    }

    private static OptimalPanelLayout calculateOptimalLayout(double rooftopLength, double rooftopWidth, int selectedPanelId) {
      // Example implementation, assuming fixed values for panel dimensions
      double panelLength = 2; // meters
      double panelWidth = 1; // meters
      int panelsAcrossLength = (int) (rooftopLength / panelLength);
      int panelsAcrossWidth = (int) (rooftopWidth / panelWidth);

      int totalPanels = panelsAcrossLength * panelsAcrossWidth;
      double systemSize = totalPanels * 0.3; // kW

      return new OptimalPanelLayout(totalPanels, panelsAcrossLength, panelsAcrossWidth, systemSize);
  }

  // Inner class to store optimal panel layout data
  static class OptimalPanelLayout {
      int totalPanels;
      int horizontalPanels;
      int verticalPanels;
      double systemSize;

      public OptimalPanelLayout(int totalPanels, int horizontalPanels, int verticalPanels, double systemSize) {
          this.totalPanels = totalPanels;
          this.horizontalPanels = horizontalPanels;
          this.verticalPanels = verticalPanels;
          this.systemSize = systemSize;
      }
  }
}


