package pharmacy_system;

public abstract class Item {
    protected String itemID;
    protected String name;
    protected int quantity;
    protected double price;

    public Item(String itemID, String name, int quantity, double price) {
        if (itemID == null || itemID.isEmpty()) throw new IllegalArgumentException("Invalid Item ID");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Invalid Name");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        
        this.itemID = itemID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public abstract double calculatePrice();

    public String getItemID() { return itemID; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}