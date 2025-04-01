import java.sql.*;
import java.util.Scanner;

public class StateSubsidy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter state name: ");
        String stateName = scanner.nextLine();
        scanner.close();
        
        try {
            ResultSet rs = DatabaseHelper.getStateSubsidyByState(stateName);
            
            if (rs.next()) {
                System.out.println("State: " + rs.getString("state_name"));
                System.out.println("Subsidy Percentage: " + rs.getFloat("subsidy_percent") + "%");
                System.out.println("--------------------------------------");
            } else {
                System.out.println("No subsidy information found for the entered state.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
