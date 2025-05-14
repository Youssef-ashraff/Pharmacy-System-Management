package pharmacy_system;

public class CashPayment implements Payable {
    private double amountPaid;
    private String status = "Pending";

    @Override
    public boolean processPayment(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.amountPaid = amount;
        this.status = "Completed";
        return true;
    }

    @Override
    public String getPaymentStatus() { return status; }
}