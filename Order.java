package pharmacy_system;

import java.util.HashMap;
import java.util.Date;

public class Order {
    private String orderID;
    private HashMap<Medicine, Integer> items = new HashMap<>();
    private Date orderDate = new Date();
    private double totalPrice = 0;

    public Order(String orderID) {
        if (orderID == null || orderID.isEmpty()) throw new IllegalArgumentException("Invalid Order ID");
        this.orderID = orderID;
    }

    public void addItem(Medicine medicine, int quantity) {
        if (medicine == null) throw new IllegalArgumentException("Medicine cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Invalid quantity");
        if (medicine.getQuantity() < quantity) throw new IllegalArgumentException("Insufficient stock");
        
        items.put(medicine, quantity);
        totalPrice += medicine.getPrice() * quantity;
        medicine.setQuantity(medicine.getQuantity() - quantity);
    }

    // Getters
    public String getOrderID() { return orderID; }
    public double getTotalPrice() { return totalPrice; }
    public HashMap<Medicine, Integer> getItems() { return items; }
    public Date getOrderDate() { return orderDate; }
}