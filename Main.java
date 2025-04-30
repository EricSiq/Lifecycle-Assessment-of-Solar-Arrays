import java.util.Scanner;

public class Main {
    private static final String ADMIN_PASSWORD = "admin123"; // Set your admin password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Are you a User or Admin? (Enter 'user' or 'admin')");
        String role = scanner.nextLine().trim().toLowerCase();

        if (role.equals("admin")) {
            System.out.print("Enter admin password: ");
            String password = scanner.nextLine();

            if (!password.equals(ADMIN_PASSWORD)) {
                System.out.println("Incorrect password. Exiting...");
                return;
            }

            while (true) {
                System.out.println("\n===== ADMIN CRUD MENU =====");
                System.out.println("1. Manage State Subsidies");
                System.out.println("2. Manage Projects");
                System.out.println("3. Manage Power Purchase Agreements");
                System.out.println("4. Manage Solar Panel Data");
                System.out.println("5. Manage Solar Panel Models");
                System.out.println("6. Manage Solar Intensity");
                System.out.println("7. Manage User Selected Panels");
                System.out.println("0. Exit Admin Menu");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        handleStateSubsidies(scanner);
                        break;
                    case 2:
                        handleProjects(scanner);
                        break;
                    case 3:
                        handlePPAs(scanner);
                        break;
                    case 4:
                        handleSolarPanelData(scanner);
                        break;
                    case 5:
                        handlePanelModels(scanner);
                        break;
                    case 6:
                        handleSolarIntensity(scanner);
                        break;
                    case 7:
                        handleUserSelectedPanels(scanner);
                        break;
                    case 0:
                        System.out.println("Exiting Admin Panel.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } else {
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
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }

    // === Submenus for CRUD operations ===

    private static void handleStateSubsidies(Scanner sc) {
        System.out.println("\n1. Add  2. View  3. Update  4. Delete");
        int op = sc.nextInt(); sc.nextLine();
        switch (op) {
            case 1 -> {
                System.out.print("Enter state name: ");
                String s = sc.nextLine();
                System.out.print("Enter subsidy %: ");
                float p = sc.nextFloat();
                DatabaseHelper.addStateSubsidy(s, p);
            }
            case 2 -> DatabaseHelper.viewAllStateSubsidies();
            case 3 -> {
                System.out.print("Enter state name: ");
                String s = sc.nextLine();
                System.out.print("New subsidy %: ");
                float p = sc.nextFloat();
                DatabaseHelper.updateSubsidyPercentage(s, p);
            }
            case 4 -> {
                System.out.print("Enter state name: ");
                String s = sc.nextLine();
                DatabaseHelper.deleteStateSubsidy(s);
            }
        }
    }

    private static void handleProjects(Scanner sc) {
        System.out.println("\n1. Add  2. View  3. Update Tariff  4. Delete");
        int op = sc.nextInt(); sc.nextLine();
        switch (op) {
            case 1 -> {
                System.out.print("Name: ");
                String name = sc.nextLine();
                System.out.print("System Size: ");
                float size = sc.nextFloat();
                System.out.print("Cost/kW: ");
                float cost = sc.nextFloat();
                System.out.print("State ID: ");
                int sid = sc.nextInt();
                System.out.print("Tariff: ");
                float tariff = sc.nextFloat();
                System.out.print("Net Metering: ");
                float net = sc.nextFloat();
                sc.nextLine();
                System.out.print("Type: ");
                String type = sc.nextLine();
                DatabaseHelper.addProject(name, size, cost, sid, tariff, net, type);
            }
            case 2 -> DatabaseHelper.viewAllProjects();
            case 3 -> {
                System.out.print("Project ID: ");
                int id = sc.nextInt();
                System.out.print("New Tariff: ");
                float t = sc.nextFloat();
                DatabaseHelper.updateProjectTariff(id, t);
            }
            case 4 -> {
                System.out.print("Project ID: ");
                int id = sc.nextInt();
                DatabaseHelper.deleteProject(id);
            }
        }
    }

    private static void handlePPAs(Scanner sc) {
        System.out.println("\n1. Add  2. View  3. Update Duration  4. Delete");
        int op = sc.nextInt(); sc.nextLine();
        switch (op) {
            case 1 -> {
                System.out.print("State ID: ");
                int sid = sc.nextInt();
                System.out.print("Tariff: ");
                float t = sc.nextFloat();
                System.out.print("Escalation %: ");
                float e = sc.nextFloat();
                System.out.print("Duration (yrs): ");
                int d = sc.nextInt();
                DatabaseHelper.addPPA(sid, t, e, d);
            }
            case 2 -> DatabaseHelper.viewAllPPAs();
            case 3 -> {
                System.out.print("Agreement ID: ");
                int id = sc.nextInt();
                System.out.print("New duration: ");
                int d = sc.nextInt();
                DatabaseHelper.updatePPADuration(id, d);
            }
            case 4 -> {
                System.out.print("Agreement ID: ");
                int id = sc.nextInt();
                DatabaseHelper.deletePPA(id);
            }
        }
    }

    private static void handleSolarPanelData(Scanner sc) {
        System.out.println("\n1. Add  2. View  3. Update  4. Delete");
        int op = sc.nextInt(); sc.nextLine();
        switch (op) {
            case 1 -> {
                System.out.print("State ID: ");
                int sid = sc.nextInt();
                System.out.print("Lifespan: ");
                int life = sc.nextInt();
                System.out.print("Degradation %: ");
                float deg = sc.nextFloat();
                System.out.print("Maintenance cost: ");
                float m = sc.nextFloat();
                DatabaseHelper.addSolarPanelData(sid, life, deg, m);
            }
            case 2 -> DatabaseHelper.viewAllSolarPanelData();
            case 3 -> {
                System.out.print("Data ID: ");
                int id = sc.nextInt();
                System.out.print("New lifespan: ");
                int life = sc.nextInt();
                System.out.print("New degradation: ");
                float deg = sc.nextFloat();
                System.out.print("New maintenance: ");
                float m = sc.nextFloat();
                DatabaseHelper.updateSolarPanelData(id, life, deg, m);
            }
            case 4 -> {
                System.out.print("Data ID: ");
                int id = sc.nextInt();
                DatabaseHelper.deleteSolarPanelData(id);
            }
        }
    }

    private static void handlePanelModels(Scanner sc) {
        System.out.println("\n1. Add  2. View  3. Update Cost  4. Delete");
        int op = sc.nextInt(); sc.nextLine();
        switch (op) {
            case 1 -> {
                System.out.print("Brand: ");
                String brand = sc.nextLine();
                System.out.print("Model: ");
                String model = sc.nextLine();
                System.out.print("Cost: ");
                float cost = sc.nextFloat();
                System.out.print("Length: ");
                float len = sc.nextFloat();
                System.out.print("Width: ");
                float wid = sc.nextFloat();
                System.out.print("Power: ");
                float power = sc.nextFloat();
                DatabaseHelper.addPanelModel(brand, model, cost, len, wid, power);
            }
            case 2 -> DatabaseHelper.viewAllPanelModels();
            case 3 -> {
                System.out.print("Panel ID: ");
                int id = sc.nextInt();
                System.out.print("New Cost: ");
                float cost = sc.nextFloat();
                DatabaseHelper.updatePanelModelCost(id, cost);
            }
            case 4 -> {
                System.out.print("Panel ID: ");
                int id = sc.nextInt();
                DatabaseHelper.deletePanelModel(id);
            }
        }
    }

    private static void handleSolarIntensity(Scanner sc) {
        System.out.println("\n1. Add  2. View  3. Update Sunlight Hours  4. Delete");
        int op = sc.nextInt(); sc.nextLine();
        switch (op) {
            case 1 -> {
                System.out.print("State: ");
                String s = sc.nextLine();
                System.out.print("District: ");
                String d = sc.nextLine();
                System.out.print("Lat: ");
                double lat = sc.nextDouble();
                System.out.print("Lon: ");
                double lon = sc.nextDouble();
                System.out.print("Radiation: ");
                double rad = sc.nextDouble();
                System.out.print("Sunlight Hours: ");
                double sun = sc.nextDouble();
                DatabaseHelper.addSolarIntensity(s, d, lat, lon, rad, sun);
            }
            case 2 -> DatabaseHelper.viewAllSolarIntensity();
            case 3 -> {
                System.out.print("District ID: ");
                int id = sc.nextInt();
                System.out.print("New sunlight hours: ");
                double sun = sc.nextDouble();
                DatabaseHelper.updateSunlightHours(id, sun);
            }
            case 4 -> {
                System.out.print("District ID: ");
                int id = sc.nextInt();
                DatabaseHelper.deleteSolarIntensity(id);
            }
        }
    }

    private static void handleUserSelectedPanels(Scanner sc) {
        System.out.println("\n1. Add  2. View  3. Update  4. Delete");
        int op = sc.nextInt(); sc.nextLine();
        switch (op) {
            case 1 -> {
                System.out.print("Efficiency %: ");
                double eff = sc.nextDouble();
                System.out.print("Area (m²): ");
                double area = sc.nextDouble();
                DatabaseHelper.addUserSelectedPanel(eff, area);
            }
            case 2 -> DatabaseHelper.viewAllUserSelectedPanels();
            case 3 -> {
                System.out.print("ID: ");
                int id = sc.nextInt();
                System.out.print("New efficiency: ");
                double eff = sc.nextDouble();
                System.out.print("New area: ");
                double area = sc.nextDouble();
                DatabaseHelper.updateUserSelectedPanel(id, eff, area);
            }
            case 4 -> {
                System.out.print("ID: ");
                int id = sc.nextInt();
                DatabaseHelper.deleteUserSelectedPanel(id);
            }
        }
    }
}
