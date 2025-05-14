package pharmacy_system;

public class CardPayment implements Payable {
    private String cardNumber;
    private String expiryDate;
    private String status = "Pending";

    public CardPayment(String cardNumber, String expiryDate) {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            throw new IllegalArgumentException("Invalid card number (16 digits required)");
        }
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Invalid expiry format (MM/YY required)");
        }
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean processPayment(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        status = "Completed";
        return true;
    }

    @Override
    public String getPaymentStatus() { return status; }
}