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
