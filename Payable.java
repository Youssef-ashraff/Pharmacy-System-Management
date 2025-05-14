package pharmacy_system;

public interface Payable {
    boolean processPayment(double amount);
    String getPaymentStatus();
}