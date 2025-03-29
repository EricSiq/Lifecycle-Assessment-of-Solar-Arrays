public class ROICalculator2 {

    /**
     * Calculates the payback period considering both standard net metering tariffs and PPA details.
     * 
     * @param systemSize     System capacity in kW.
     * @param costPerKW      Cost per kW in INR.
     * @param standardTariff Standard net metering tariff (INR per kWh).
     * @param netMetering    Revenue per kWh from net metering (INR per kWh).
     * @param type           Type of system ("Solar" or otherwise).
     * @param state          The state name (for subsidy retrieval).
     * @param ppa            Power Purchase Agreement details.
     * @return               Estimated payback period in years.
     */
    public static double calculatePaybackPeriod(double systemSize, double costPerKW, 
                                                double standardTariff, double netMetering, 
                                                String type, String state, 
                                                PowerPurchaseAgreement ppa) {
        // Retrieve the real-time subsidy percentage (using your API integration or static lookup)
        double subsidyPercent = StateSubsidyData.getSubsidy(state);

        // Calculate annual energy output based on system type
        double annualOutput = type.equalsIgnoreCase("Solar") ? systemSize * 1500 : systemSize * 2500;
        
        // Calculate annual savings from standard tariff (net metering)
        double annualSavings = annualOutput * standardTariff;
        
        // Calculate cumulative revenue from the PPA over the contract duration
        double cumulativePPARevenue = 0;
        for (int year = 0; year < ppa.getContractDuration(); year++) {
            double effectivePPATariff = ppa.getTariff() * Math.pow(1 + ppa.getEscalationRate() / 100, year);
            cumulativePPARevenue += annualOutput * effectivePPATariff;
        }
        double averageAnnualPPARevenue = cumulativePPARevenue / ppa.getContractDuration();
        
        // Total annual revenue combining net metering and PPA revenues
        double totalAnnualRevenue = annualSavings + averageAnnualPPARevenue;
        
        // Calculate subsidy amount and net investment
        double subsidyAmount = (subsidyPercent / 100) * (systemSize * costPerKW);
        double netInvestment = (systemSize * costPerKW) - subsidyAmount;

        return netInvestment / totalAnnualRevenue;
    }

    public static void main(String[] args) {
        double systemSize = 5;           // in kW
        double costPerKW = 40000;        // INR per kW
        double standardTariff = 7.5;     // INR per kWh from net metering
        double netMetering = 3.5;        // INR per kWh additional revenue (if applicable)
        String state = "Maharashtra";
        String type = "Solar";

        // Define the PPA details (tariff, escalation rate, contract duration)
        PowerPurchaseAgreement ppa = new PowerPurchaseAgreement(8.0, 3.0, 20);

        double paybackPeriod = calculatePaybackPeriod(systemSize, costPerKW, standardTariff, 
                                                      netMetering, type, state, ppa);
        System.out.printf("Estimated Payback Period for %s: %.2f years%n", state, paybackPeriod);
    }
}
