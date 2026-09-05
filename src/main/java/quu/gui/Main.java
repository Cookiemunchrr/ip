package quu.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import quu.Quu;

/**
 * Displays the Quu chatbot in a JavaFX window loaded from FXML.
 */
public class Main extends Application {
    private static final double WINDOW_WIDTH = 417;
    private static final double MINIMUM_WINDOW_HEIGHT = 220;

    private final Quu quu = new Quu();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Quu");
        stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMaxWidth(WINDOW_WIDTH);

        MainWindow mainWindow = fxmlLoader.getController();
        mainWindow.setQuu(quu);
        stage.show();
    }
}
