import java.sql.*;
import java.util.Scanner;

public class PowerPurchaseAgreement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter state name: ");
        String stateName = scanner.nextLine();
        
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT s.state_name, ppa.tariff, ppa.escalation_rate, ppa.contract_duration " +
                 "FROM PowerPurchaseAgreements ppa " +
                 "JOIN StateSubsidies s ON ppa.state_id = s.state_id " +
                 "WHERE s.state_name = ?")) {
            
            pstmt.setString(1, stateName);
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No data found for the entered state.");
            } else {
                while (rs.next()) {
                    System.out.println("State: " + rs.getString("state_name"));
                    System.out.println("Tariff: Rs." + rs.getFloat("tariff"));
                    System.out.println("Escalation Rate: " + rs.getFloat("escalation_rate") + "%");
                    System.out.println("Contract Duration: " + rs.getInt("contract_duration") + " years");
                    System.out.println("--------------------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
