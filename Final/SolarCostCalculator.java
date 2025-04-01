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
            double width = rs.getDouble("width");

            double panelArea = length * width;
            double panelEfficiency = (powerOutput / panelArea) * 100;  // Efficiency formula

            // Store values globally
            setLatestPanelData(panelEfficiency, panelArea);

            // Store values in the database
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
    // Display available panel options
    displaySolarPanelModels();

    Scanner scanner = new Scanner(System.in);
    // Ask user to select a solar panel
    System.out.print("Enter the Panel ID you want to select: ");
    int selectedPanelId = scanner.nextInt();

// Calculate and store efficiency and area
    calculateAndStorePanelEfficiency(selectedPanelId);
    
    // Now, efficiency and area are stored in database & memory
    double panelEfficiency = getLatestPanelEfficiency();  // Retrieve stored value
    double panelArea = getLatestPanelArea();  // Retrieve stored value

    System.out.print("Enter your rooftop length (in meters): ");
    double rooftopLength = scanner.nextDouble();
    System.out.print("Enter your rooftop width (in meters): ");
    double rooftopWidth = scanner.nextDouble();
    scanner.nextLine(); // Consume newline

    System.out.print("Enter your state: ");
    String state = scanner.nextLine();
    
    System.out.print("Enter your district: ");
    String district = scanner.nextLine();

        // Calculate optimal layout
        OptimalPanelLayout layout = calculateOptimalLayout(rooftopLength, rooftopWidth, selectedPanelId);

        // Fetch data from database
        double costPerPanel = fetchDouble("SELECT cost_per_panel FROM SolarPanelModels WHERE panel_id = ?", selectedPanelId);
        double subsidyPercent = fetchDouble("SELECT subsidy_percent FROM StateSubsidies WHERE state_name = ?", state);
        double tariff = fetchDouble("SELECT tariff FROM Projects WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ?)", state);
        boolean netMetering = fetchBoolean("SELECT net_metering FROM Projects WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ?)", state);

        double panelLifespan = fetchDouble("SELECT panel_lifespan FROM SolarPanelData WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ?)", state);

        double degradationRate = fetchDouble("SELECT degradation_rate FROM SolarPanelData WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ?)", state);

        double maintenanceCost = fetchDouble("SELECT maintenance_cost FROM SolarPanelData WHERE state_id = (SELECT state_id FROM StateSubsidies WHERE state_name = ?)", state);

        // Calculate costs and savings
        double totalPanelCost = layout.totalPanels * costPerPanel;
        double finalCost = totalPanelCost * (1 - subsidyPercent / 100);
        double totalMaintenanceCost = maintenanceCost * panelLifespan;
        double systemSize = layout.totalPanels * 0.3;  // 0.3 kW per panel (300 W per panel)
        // Fetch sunlight hours from database
        double avgSunlightHours = DatabaseHelper.getSunlightHours(state, district);

        // Calculate annual energy output using solar intensity data
double annualEnergyOutput = systemSize * avgSunlightHours * 365; // avgSunlightHours from DB

// Assume electricity price per kWh
double electricityPrice = 6.50; // Rs. per kWh (adjust based on actual rates)

// Calculate annual savings
double annualSavings = annualEnergyOutput * electricityPrice;  

// Apply net metering benefits
annualSavings *= 1.10; // 10% extra savings due to net metering

// Lifetime savings
double lifetimeSavings = annualSavings * panelLifespan;

// Payback period
double paybackPeriod = finalCost / annualSavings;

        double roi = (lifetimeSavings / finalCost) * 100; // ROI percentage

        // Display results
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
            System.out.println("Net Metering Benefits Applied: 10% Extra Savings");
        }
        System.out.printf("Estimated Lifetime Energy Savings: Rs. %.2f\n", lifetimeSavings);
        System.out.printf("Return on Investment (ROI): %.2f%%\n", roi);  // Corrected ROI calculation
        System.out.printf("Payback Period: %.2f years\n", paybackPeriod);  // Corrected Payback Period calculation

        System.out.printf("Panel Efficiency: %.2f%%\n", panelEfficiency);
        System.out.printf("Panel Area: %.2f m²\n", panelArea);
        System.out.println("========================================\n");
    }

    private static void displaySolarPanelModels() {
    System.out.println("Available Solar Panels:");
    System.out.println("----------------------------------------------------------------------------------------------");
    System.out.println("ID    Brand          Model              Power (kW)  Cost (Rs.)  Length (m)  Width (m)");
    System.out.println("----------------------------------------------------------------------------------------------");

    String query = "SELECT panel_id, brand, model_name, power_output, cost_per_panel, length, width FROM SolarPanelModels";

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

    private static OptimalPanelLayout calculateOptimalLayout(double rooftopLength, double rooftopWidth, int panelChoice) {
        double panelLength = 0, panelWidth = 0, panelPower = 0;

        try (Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT length, width, power_output FROM SolarPanelModels WHERE panel_id = ?")) {
            ps.setInt(1, panelChoice);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                panelLength = rs.getDouble("length");
                panelWidth = rs.getDouble("width");
                panelPower = rs.getDouble("power_output");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Calculate number of panels that can fit horizontally and vertically
        int horizontalPanels = (int) (rooftopLength / panelLength) * (int) (rooftopWidth / panelWidth);
        int verticalPanels = (int) (rooftopLength / panelWidth) * (int) (rooftopWidth / panelLength);
        int totalPanels = Math.max(horizontalPanels, verticalPanels);
        
        // Calculate system size in kW
        double systemSize = totalPanels * panelPower / 1000;  // Convert W to kW
        
        return new OptimalPanelLayout(horizontalPanels, verticalPanels, totalPanels, systemSize);
    }

    // Helper class to hold the optimal layout information
    static class OptimalPanelLayout {
        int horizontalPanels;
        int verticalPanels;
        int totalPanels;
        double systemSize;

        OptimalPanelLayout(int horizontalPanels, int verticalPanels, int totalPanels, double systemSize) {
            this.horizontalPanels = horizontalPanels;
            this.verticalPanels = verticalPanels;
            this.totalPanels = totalPanels;
            this.systemSize = systemSize;
        }
    }
}
