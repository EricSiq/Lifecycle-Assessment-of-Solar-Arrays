public class PowerPurchaseAgreement {
    private double tariff;          // INR per kWh as per PPA
    private double escalationRate;  // Annual escalation rate in percentage
    private int contractDuration;   // Contract duration in years

    public PowerPurchaseAgreement(double tariff, double escalationRate, int contractDuration) {
        this.tariff = tariff;
        this.escalationRate = escalationRate;
        this.contractDuration = contractDuration;
    }

    public double getTariff() {
        return tariff;
    }

    public double getEscalationRate() {
        return escalationRate;
    }

    public int getContractDuration() {
        return contractDuration;
    }
}
