package quu.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import quu.Quu;

/**
 * Controls the main JavaFX window and connects it to a {@link Quu} instance.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image quuImage = new Image(getClass().getResourceAsStream("/images/DaQuu.png"));
    private Quu quu;

    /**
     * Configures automatic scrolling after the FXML fields are injected.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Connects the chatbot to this window and displays its opening messages.
     *
     * @param quu the chatbot this window communicates with
     */
    public void setQuu(Quu quu) {
        this.quu = quu;
        String commandType = quu.getCommandType();
        dialogContainer.getChildren().add(DialogBox.getQuuDialog(quu.getGreeting(), quuImage, commandType));
        if (!quu.getLoadMessage().isEmpty()) {
            dialogContainer.getChildren().add(
                    DialogBox.getQuuDialog(quu.getLoadMessage(), quuImage, commandType));
        }
    }

    /**
     * Adds the user's input and Quu's response to the dialog container.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = quu.getResponse(input);
        String commandType = quu.getCommandType();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getQuuDialog(response, quuImage, commandType));
        userInput.clear();
    }
}
