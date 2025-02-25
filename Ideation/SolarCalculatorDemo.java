public class SolarCalculatorDemo {
    public static void main(String[] args) {
        // Configuration for the solar panel lifecycle assessment
        int lifespan = 25;                  // Panel lifespan (can be set to 25-40 years)
        double degradationRate = 0.5;         // 0.5% annual degradation
        double annualMaintenanceCost = 5000;  // INR per year
        double discountRate = 0.05;           // 5% discount rate for NPV calculations
        
        // Create an instance of SolarLifecycleAssessment
        SolarLifecycleAssessment lifecycle = new SolarLifecycleAssessment(
                lifespan, degradationRate, annualMaintenanceCost, discountRate);
        
        // Define system parameters
        double systemSize = 5;                // in kW
        double initialAnnualOutput = systemSize * 1500; // kWh/year (typical for rooftop solar)
        double tariff = 7.5;                  // INR per kWh
        
        // Calculate the lifecycle net present value (NPV)
        double lifecycleNPV = lifecycle.calculateLifecycleNPV(initialAnnualOutput, tariff);
        System.out.printf("Lifecycle Net Present Value over %d years: INR %.2f%n", lifespan, lifecycleNPV);
    }
}
