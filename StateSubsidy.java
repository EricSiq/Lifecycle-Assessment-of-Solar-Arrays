import java.sql.*;
import java.util.Scanner;

public class StateSubsidy {
    public static void run(Scanner scanner) { // Accept the scanner as an argument
        System.out.print("Enter state name: ");
        String stateName = scanner.nextLine();  // Use the passed scanner


        // Add this line to prompt the user to press Enter before returning to the main menu
        System.out.println("Press Enter to return to the main menu...");
        scanner.nextLine();  // Wait for user to press Enter
    }
}
