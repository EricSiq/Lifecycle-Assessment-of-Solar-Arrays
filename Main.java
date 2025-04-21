import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Creates a single scanner instance
        while (true) {
            System.out.println("\n========== SOLAR ENERGY ANALYSIS MENU ==========");
            System.out.println("1. Power Purchase Agreement Details");
            System.out.println("2. Calculate Solar Cost & ROI");
            System.out.println("3. Calculate Solar Panel Efficiency");
            System.out.println("4. View Solar Lifecycle Assessment");
            System.out.println("5. View State Subsidy Information");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    PowerPurchaseAgreement.run();
                    break;
                case 2:
                    SolarCostCalculator.main(null);
                    break;
                case 3:
                    SolarEfficiencyCalculator.main(null);
                    break;
                case 4:
                    SolarLifecycleAssessment.run(scanner); 
                    break;
                case 5:
                    StateSubsidy.run(scanner); 
                    break;
                case 0:
                    System.out.println("Exiting... Thank you!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
