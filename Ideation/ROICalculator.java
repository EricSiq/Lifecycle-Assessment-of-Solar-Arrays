public class ROICalculator {
    public static double calculatePaybackPeriod(double systemSize, double costPerKW, double subsidyPercent, 
                                                double tariff, double netMetering, String type) {
        double annualOutput = (type.equals("Solar")) ? systemSize * 1500 : systemSize * 2500;
        double annualSavings = annualOutput * tariff;
        double revenue = annualOutput * netMetering;
        double subsidyAmount = (subsidyPercent / 100) * (systemSize * costPerKW);
        double netInvestment = (systemSize * costPerKW) - subsidyAmount;

        return netInvestment / (annualSavings + revenue);
    }
}
