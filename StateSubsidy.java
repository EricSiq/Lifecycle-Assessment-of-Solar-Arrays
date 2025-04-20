import java.sql.*;
import java.util.Scanner;

public class StateSubsidy {
    public static void run(Scanner scanner) { // Accept the scanner as an argument
        System.out.print("Enter state name: ");
        String stateName = scanner.nextLine();  // Use the passed scanner
  
        try {
            ResultSet rs = DatabaseHelper.getStateSubsidies();
            boolean hasData = false;

            while (rs.next()) {
                if (rs.getString("state_name").equalsIgnoreCase(stateName)) {
                    hasData = true;
                    System.out.println("State: " + rs.getString("state_name"));
                    System.out.println("Subsidy Percentage: " + rs.getFloat("subsidy_percent") + "%");
                    System.out.println("--------------------------------------");
                }
            }

            if (!hasData) {
                System.out.println("No subsidy information found for the entered state.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add this line to prompt the user to press Enter before returning to the main menu
        System.out.println("Press Enter to return to the main menu...");
        scanner.nextLine();  // Wait for user to press Enter
    }
}
