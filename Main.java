package pharmacy_system;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        PharmacyFXGUI gui = new PharmacyFXGUI();
        gui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}