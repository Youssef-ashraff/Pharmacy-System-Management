package pharmacy_system;

public class Medicine extends Item {
    private boolean prescriptionRequired;
    private String dosage;

    public Medicine(String itemID, String name, int quantity, double price, 
                   boolean prescriptionRequired, String dosage) {
        super(itemID, name, quantity, price);
        if (dosage.isEmpty()) throw new IllegalArgumentException("Dosage required");
        this.prescriptionRequired = prescriptionRequired;
        this.dosage = dosage;
    }

    @Override
    public double calculatePrice() { return price * quantity; }

    // Getters
    public boolean isPrescriptionRequired() { return prescriptionRequired; }
    public String getDosage() { return dosage; }
}