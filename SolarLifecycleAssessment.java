import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SolarLifecycleAssessment {

    private static final List<String> VALID_STATES = Arrays.asList(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana",
        "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
        "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
        "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
        "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep", "Delhi", "Puducherry", "Ladakh", "Jammu and Kashmir"
    );

    // Updated to use Scanner passed as an argument
    public static void run(Scanner scanner) {
        System.out.print("Enter state name: ");
        String stateName = scanner.nextLine();

        try {
            validateState(stateName);
        } catch (InvalidStateException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
      
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT * FROM SolarPanelData spd JOIN StateSubsidies ss ON spd.state_id = ss.state_id WHERE ss.state_name = ?"
             )) {

            pstmt.setString(1, stateName);
            ResultSet rs = pstmt.executeQuery();

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.println("State: " + rs.getString("state_name"));
                System.out.println("Panel Lifespan: " + rs.getInt("panel_lifespan") + " years");
                System.out.println("Degradation Rate: " + rs.getFloat("degradation_rate") + "% per year");
                System.out.println("Maintenance Cost: Rs." + rs.getFloat("maintenance_cost") + "/year");
                System.out.println("--------------------------------------");
            }

            if (!hasResults) {
                System.out.println("No data found for the specified state.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Custom validation method
    private static void validateState(String state) throws InvalidStateException {
        if (!VALID_STATES.contains(state)) {
            throw new InvalidStateException(
                "Invalid state/UT entered. Please enter a valid one from the predefined list."
            );
        }
    }
}

