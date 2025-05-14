package pharmacy_system;

import java.util.ArrayList;
import java.util.Comparator;

public class Pharmacy {
    private ArrayList<Item> phItems = new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Recipe> recipes = new ArrayList<>();

    public void addItem(Item item) throws IllegalArgumentException {
        if (phItems.stream().anyMatch(i -> i.getItemID().equals(item.getItemID()))) {
            throw new IllegalArgumentException("Item ID exists: " + item.getItemID());
        }
        phItems.add(item);
    }

    public void addOrder(Order order) { orders.add(order); }
    public void addRecipe(Recipe recipe) { recipes.add(recipe); }

    public void sortItemsByPrice() { phItems.sort(Comparator.comparingDouble(Item::getPrice)); }
    public void sortOrdersByTotal() { orders.sort(Comparator.comparingDouble(Order::getTotalPrice)); }
    public void sortRecipesByName() { recipes.sort(Comparator.comparing(Recipe::getName)); }

    // Getters
    public ArrayList<Item> getPhItems() { return phItems; }
    public ArrayList<Order> getOrders() { return orders; }
    public ArrayList<Recipe> getRecipes() { return recipes; }
}