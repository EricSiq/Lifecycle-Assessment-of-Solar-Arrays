public class SolarLCA{
    private int lifespanYears;              // Lifespan in years (e.g., 25 to 40)
    private double degradationRate;         // Annual degradation rate in percent (e.g., 0.5%)
    private double annualMaintenanceCost;   // Annual maintenance cost in INR
    private double discountRate;            // Discount rate for NPV calculations (e.g., 5%)

    public SolarLifecycleAssessment(int lifespanYears, double degradationRate, 
                                    double annualMaintenanceCost, double discountRate) {
        this.lifespanYears = lifespanYears;
        this.degradationRate = degradationRate;
        this.annualMaintenanceCost = annualMaintenanceCost;
        this.discountRate = discountRate;
    }

    /**
     * Calculates the lifecycle net present value (NPV) of the system's net revenue.
     * 
     * @param initialAnnualOutput The initial annual energy output in kWh (before degradation).
     * @param tariff              Revenue per kWh (INR per kWh).
     * @return                    The NPV of net revenues over the lifecycle.
     */
    public double calculateLifecycleNPV(double initialAnnualOutput, double tariff) {
        double totalNPV = 0.0;
        double currentOutput = initialAnnualOutput;

        for (int year = 1; year <= lifespanYears; year++) {
            // Annual revenue generated before maintenance cost
            double annualRevenue = currentOutput * tariff;
            
            // Net revenue after subtracting annual maintenance cost
            double netAnnualRevenue = annualRevenue - annualMaintenanceCost;
            
            // Discount the net revenue to present value
            totalNPV += netAnnualRevenue / Math.pow(1 + discountRate, year);
            
            // Reduce the output by the degradation rate for the next year
            currentOutput *= (1 - degradationRate / 100.0);
        }
        return totalNPV;
    }
    
    // Getters and setters can be added as needed.
}
