package pharmacy_system;

import java.util.HashMap;

public class Recipe {
    private String recipeID;
    private String name;
    private HashMap<Medicine, Integer> medicines;
    private String instructions;

    public Recipe(String recipeID, String name, String instructions) {
        if (recipeID == null || recipeID.isEmpty()) throw new IllegalArgumentException("Invalid Recipe ID");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Recipe name required");
        
        this.recipeID = recipeID;
        this.name = name;
        this.instructions = instructions;
        this.medicines = new HashMap<>();
    }

    public void addMedicine(Medicine medicine, int quantity) throws IllegalArgumentException {
        if (medicine == null) throw new IllegalArgumentException("Medicine cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (medicine.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock for " + medicine.getName() + 
                " (Available: " + medicine.getQuantity() + ")");
        }
        medicines.put(medicine, quantity);
    }

    public void processRecipe() {
        medicines.forEach((med, qty) -> med.setQuantity(med.getQuantity() - qty));
    }

    public String getRecipeID() { return recipeID; }
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public HashMap<Medicine, Integer> getMedicines() { return medicines; }
}