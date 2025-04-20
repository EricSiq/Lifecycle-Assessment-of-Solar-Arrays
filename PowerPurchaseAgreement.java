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
