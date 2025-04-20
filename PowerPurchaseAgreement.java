import java.sql.*;
import java.util.*;

public class PowerPurchaseAgreement {

    // List of valid states and UTs
    private static final Set<String> VALID_STATES = new HashSet<>(Arrays.asList(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat",
        "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
        "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
        "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh",
        "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
        "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep", "Delhi", "Puducherry",
        "Ladakh", "Jammu and Kashmir"
    ));

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter state name: ");
        String stateName = scanner.nextLine().trim();
      
        // Validate user input
        try {
            validateState(stateName);
        } catch (InvalidStateException e) {
            System.err.println("" + e.getMessage());
            return; // stop execution if invalid
        }

        // Create a Runnable task for threading
        Runnable fetchPPAData = () -> {
            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT s.state_name, ppa.tariff, ppa.escalation_rate, ppa.contract_duration " +
                     "FROM PowerPurchaseAgreements ppa " +
                     "JOIN StateSubsidies s ON ppa.state_id = s.state_id " +
                     "WHERE s.state_name = ?")) {

                pstmt.setString(1, stateName);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("No PPA data found for " + stateName);
                    return;
                }

                while (rs.next()) {
                    System.out.println("\nState: " + rs.getString("state_name"));
                    System.out.println("Tariff: Rs." + rs.getFloat("tariff"));
                    System.out.println("Escalation Rate: " + rs.getFloat("escalation_rate") + "%");
                    System.out.println("Contract Duration: " + rs.getInt("contract_duration") + " years");
                    System.out.println("--------------------------------------");
                }

            } catch (SQLException e) {
                System.err.println("Database Error: " + e.getMessage());
                e.printStackTrace();
            }
        };

        // Start the thread
        Thread ppaThread = new Thread(fetchPPAData);
        ppaThread.start();

        try {
            ppaThread.join(); // Wait for thread to complete
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }
    
// Validate against known states
    private static void validateState(String state) throws InvalidStateException {
        if (!VALID_STATES.contains(state)) {
            throw new InvalidStateException(
                "Invalid state or union territory entered.\n" +
                "Valid entries include: " + String.join(", ", VALID_STATES)
            );
        }
    }
}

