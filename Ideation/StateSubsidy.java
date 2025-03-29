import java.util.HashMap;
import java.util.Map;

class StateSubsidyData {
    private static final Map<String, Double> subsidyData = new HashMap<>();

    static {
        // Sample state-wise subsidy percentages (update with actual data)
        subsidyData.put("Maharashtra", 30.0);
        subsidyData.put("Karnataka", 40.0);
        subsidyData.put("Tamil Nadu", 35.0);
        subsidyData.put("Gujarat", 50.0);
        subsidyData.put("Delhi", 45.0);
    }

    public static double getSubsidy(String state) {
        return subsidyData.getOrDefault(state, 25.0); // Default to 25% if state not listed
    }
}

public class StateSubsidy {
    public static double calculatePaybackPeriod(double systemSize, double costPerKW, 
                                                double tariff, double netMetering, 
                                                String type, String state) {
        double subsidyPercent = StateSubsidyData.getSubsidy(state);
        double annualOutput = (type.equalsIgnoreCase("Solar")) ? systemSize * 1500 : systemSize * 2500;
        double annualSavings = annualOutput * tariff;
        double revenue = annualOutput * netMetering;
        double subsidyAmount = (subsidyPercent / 100) * (systemSize * costPerKW);
        double netInvestment = (systemSize * costPerKW) - subsidyAmount;

        return netInvestment / (annualSavings + revenue);
    }

    public static void main(String[] args) {
        double systemSize = 5; // kW
        double costPerKW = 40000; // INR per kW
        double tariff = 7.5; // INR per kWh
        double netMetering = 3.5; // INR per kWh
        String state = "Maharashtra";

        double paybackPeriod = calculatePaybackPeriod(systemSize, costPerKW, tariff, netMetering, "Solar", state);
        System.out.printf("Estimated Payback Period for %s: %.2f years%n", state, paybackPeriod);
    }
}
