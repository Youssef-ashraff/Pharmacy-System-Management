package pharmacy_system;

public class InvalidRecipeException extends Exception {
    public InvalidRecipeException(String message) {
        super(message);
    }
}