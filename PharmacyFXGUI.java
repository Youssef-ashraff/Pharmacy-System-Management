package pharmacy_system;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Optional;

public class PharmacyFXGUI {
    private Pharmacy pharmacy = new Pharmacy();
    private Stage stage;
    private TextArea outputArea;
    
    // Styling constants
        private final String BG_COLOR = "-fx-background-color: #00FFFF;";
    private final String BUTTON_STYLE = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15;";
    private final String TEXT_AREA_STYLE = "-fx-control-inner-background: #1E1E1E; -fx-text-fill: white;";

    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Pharmacy Management System");

        BorderPane root = new BorderPane();
        root.setStyle(BG_COLOR);
        
        outputArea = new TextArea();
        outputArea.setStyle(TEXT_AREA_STYLE);
        outputArea.setEditable(false);
        root.setCenter(new ScrollPane(outputArea));

        VBox buttonBox = new VBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle(BG_COLOR);

        Button addMedicineBtn = createStyledButton("Add Medicine");
        Button addOrderBtn = createStyledButton("Add Order");
        Button processPaymentBtn = createStyledButton("Process Payment");
        Button displayInventoryBtn = createStyledButton("Display Inventory");
        Button displayOrdersBtn = createStyledButton("Display Orders");
        Button addRecipeBtn = createStyledButton("Add Recipe");
        Button displayRecipesBtn = createStyledButton("Display Recipes");
        Button exitBtn = createStyledButton("Exit");

        addMedicineBtn.setOnAction(e -> addMedicine());
        addOrderBtn.setOnAction(e -> addOrder());
        processPaymentBtn.setOnAction(e -> processPayment());
        displayInventoryBtn.setOnAction(e -> displayInventory());
        displayOrdersBtn.setOnAction(e -> displayOrders());
        addRecipeBtn.setOnAction(e -> addRecipe());
        displayRecipesBtn.setOnAction(e -> displayRecipes());
        exitBtn.setOnAction(e -> Platform.exit());

        buttonBox.getChildren().addAll(
            addMedicineBtn, addOrderBtn, processPaymentBtn,
            displayInventoryBtn, displayOrdersBtn, addRecipeBtn,
            displayRecipesBtn, exitBtn
        );

        root.setLeft(buttonBox);
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.web("#2D2D2D"));
        stage.setScene(scene);
        stage.show();
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(BUTTON_STYLE);
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setStyle(BG_COLOR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addMedicine() {
        Dialog<Medicine> dialog = new Dialog<>();
        dialog.setTitle("Add Medicine");
        dialog.getDialogPane().setStyle(BG_COLOR);

        TextField itemID = new TextField();
        TextField name = new TextField();
        TextField quantity = new TextField();
        TextField price = new TextField();
        TextField prescriptionReq = new TextField("false");
        TextField dosage = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Item ID:"), itemID);
        grid.addRow(1, new Label("Name:"), name);
        grid.addRow(2, new Label("Quantity:"), quantity);
        grid.addRow(3, new Label("Price:"), price);
        grid.addRow(4, new Label("Prescription Required:"), prescriptionReq);
        grid.addRow(5, new Label("Dosage:"), dosage);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    return new Medicine(
                        itemID.getText(),
                        name.getText(),
                        Integer.parseInt(quantity.getText()),
                        Double.parseDouble(price.getText()),
                        Boolean.parseBoolean(prescriptionReq.getText()),
                        dosage.getText()
                    );
                } catch (NumberFormatException e) {
                    showError("Invalid number format!");
                } catch (IllegalArgumentException e) {
                    showError(e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(medicine -> {
            try {
                pharmacy.addItem(medicine);
                outputArea.appendText("Medicine added: " + medicine.getName() + "\n");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    private void addOrder() {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Create Order");
        dialog.getDialogPane().setStyle(BG_COLOR);

        TextField orderIdField = new TextField();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Order ID:"), orderIdField);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    Order order = new Order(orderIdField.getText());
                    addItemsToOrder(order);
                    return order;
                } catch (IllegalArgumentException e) {
                    showError(e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(order -> {
            pharmacy.addOrder(order);
            outputArea.appendText("Order created: " + order.getOrderID() + "\n");
        });
    }

    private void addItemsToOrder(Order order) {
        while(true) {
            TextInputDialog medDialog = new TextInputDialog();
            medDialog.setHeaderText("Enter Medicine ID (or 'done' to finish)");
            Optional<String> medID = medDialog.showAndWait();
            
            if (!medID.isPresent() || medID.get().equalsIgnoreCase("done")) break;
            
            Medicine medicine = (Medicine) findItemByID(medID.get());
            if (medicine == null) {
                showError("Medicine not found: " + medID.get());
                continue;
            }
            
            TextInputDialog qtyDialog = new TextInputDialog();
            qtyDialog.setHeaderText("Enter quantity for " + medicine.getName());
            qtyDialog.showAndWait().ifPresent(qty -> {
                try {
                    order.addItem(medicine, Integer.parseInt(qty));
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            });
        }
    }

    private void processPayment() {
        ChoiceDialog<String> typeDialog = new ChoiceDialog<>("cash", "cash", "card");
        typeDialog.setTitle("Payment Type");
        typeDialog.setHeaderText("Select payment method");
        typeDialog.getDialogPane().setStyle(BG_COLOR);
        
        typeDialog.showAndWait().ifPresent(type -> {
            try {
                TextInputDialog amountDialog = new TextInputDialog();
                amountDialog.setTitle("Payment Amount");
                amountDialog.setHeaderText("Enter amount:");
                amountDialog.getDialogPane().setStyle(BG_COLOR);
                
                amountDialog.showAndWait().ifPresent(amountStr -> {
                    try {
                        double amount = Double.parseDouble(amountStr);
                        Payable payment;
                        
                        if (type.equals("cash")) {
                            payment = new CashPayment();
                        } else {
                            TextInputDialog cardDialog = new TextInputDialog();
                            cardDialog.setTitle("Card Details");
                            cardDialog.setHeaderText("Enter card number (16 digits):");
                            cardDialog.getDialogPane().setStyle(BG_COLOR);
                            String cardNumber = cardDialog.showAndWait().orElse("");
                            
                            TextInputDialog expiryDialog = new TextInputDialog();
                            expiryDialog.setTitle("Expiry Date");
                            expiryDialog.setHeaderText("Enter expiry (MM/YY):");
                            expiryDialog.getDialogPane().setStyle(BG_COLOR);
                            String expiry = expiryDialog.showAndWait().orElse("");
                            
                            payment = new CardPayment(cardNumber, expiry);
                        }
                        
                        if (payment.processPayment(amount)) {
                            outputArea.appendText("Payment successful: $" + amount + "\n");
                        } else {
                            showError("Payment failed!");
                        }
                    } catch (NumberFormatException e) {
                        showError("Invalid amount format!");
                    } catch (IllegalArgumentException e) {
                        showError(e.getMessage());
                    }
                });
            } catch (Exception e) {
                showError("Payment error: " + e.getMessage());
            }
        });
    }

    private void displayInventory() {
        outputArea.clear();
        pharmacy.getPhItems().forEach(item -> 
            outputArea.appendText(String.format("%-8s | %-20s | Qty: %-4d | $%.2f\n",
                item.getItemID(), item.getName(), item.getQuantity(), item.getPrice()))
        );
    }

    private void displayOrders() {
        outputArea.clear();
        pharmacy.getOrders().forEach(order -> {
            outputArea.appendText(String.format("════ Order %s ════\n", order.getOrderID()));
            outputArea.appendText(String.format("Date: %s | Total: $%.2f\n", order.getOrderDate(), order.getTotalPrice()));
            outputArea.appendText("Items:\n");
            order.getItems().forEach((med, qty) -> 
                outputArea.appendText(String.format(" - %-20s x%-3d | $%.2f\n", 
                    med.getName(), qty, med.getPrice() * qty))
            );
            outputArea.appendText("\n");
        });
    }

    private void addRecipe() {
        Dialog<Recipe> dialog = new Dialog<>();
        dialog.setTitle("Add Recipe");
        dialog.getDialogPane().setStyle(BG_COLOR);

        TextField recipeID = new TextField();
        TextField name = new TextField();
        TextArea instructions = new TextArea();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Recipe ID:"), recipeID);
        grid.addRow(1, new Label("Name:"), name);
        grid.addRow(2, new Label("Instructions:"), instructions);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    Recipe recipe = new Recipe(
                        recipeID.getText(),
                        name.getText(),
                        instructions.getText()
                    );

                    while (true) {
                        TextInputDialog medDialog = new TextInputDialog();
                        medDialog.setHeaderText("Enter Medicine ID (or 'done' to finish)");
                        Optional<String> medID = medDialog.showAndWait();
                        
                        if (!medID.isPresent() || medID.get().equalsIgnoreCase("done")) break;
                        
                        Medicine medicine = (Medicine) findItemByID(medID.get());
                        if (medicine == null) {
                            showError("Medicine not found: " + medID.get());
                            continue;
                        }
                        
                        TextInputDialog qtyDialog = new TextInputDialog();
                        qtyDialog.setHeaderText("Enter quantity for " + medicine.getName());
                        qtyDialog.showAndWait().ifPresent(qty -> {
                            try {
                                recipe.addMedicine(medicine, Integer.parseInt(qty));
                            } catch (Exception e) {
                                showError(e.getMessage());
                            }
                        });
                    }
                    
                    return recipe;
                } catch (IllegalArgumentException e) {
                    showError(e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(recipe -> {
            pharmacy.addRecipe(recipe);
            outputArea.appendText("Recipe added: " + recipe.getName() + "\n");
        });
    }

    private void displayRecipes() {
        outputArea.clear();
        pharmacy.getRecipes().forEach(recipe -> {
            outputArea.appendText("════ Recipe: " + recipe.getName() + " ════\n");
            outputArea.appendText("ID: " + recipe.getRecipeID() + "\n");
            outputArea.appendText("Instructions: " + recipe.getInstructions() + "\n");
            outputArea.appendText("Medicines:\n");
            recipe.getMedicines().forEach((med, qty) -> 
                outputArea.appendText(String.format(" - %-20s x%-3d\n", med.getName(), qty))
            );
            outputArea.appendText("\n");
        });
    }

    private Item findItemByID(String itemID) {
        for (Item item : pharmacy.getPhItems()) {
            if (item.getItemID().equals(itemID)) {
                return item;
            }
        }
        return null;
    }
}