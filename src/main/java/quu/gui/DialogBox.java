package quu.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a message containing the speaker's picture and text.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load the dialog box layout", e);
        }

        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /**
     * Returns a dialog box for something the user typed.
     *
     * @param text the user's input
     * @param image the user's avatar
     * @return the dialog box
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Returns a dialog box for Quu's reply, colored for the command category.
     *
     * @param text the reply
     * @param image Quu's avatar
     * @param commandType the category reported by {@code Quu.getCommandType()}
     * @return the dialog box
     */
    public static DialogBox getQuuDialog(String text, Image image, String commandType) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        dialogBox.changeDialogStyle(commandType);
        return dialogBox;
    }

    /** Flips the dialog box so that the picture is on the left. */
    private void flip() {
        ObservableList<Node> reversedChildren = FXCollections.observableArrayList(getChildren());
        Collections.reverse(reversedChildren);
        getChildren().setAll(reversedChildren);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Adds the style class for the given command category.
     *
     * @param commandType the category reported by {@code Quu.getCommandType()}
     */
    private void changeDialogStyle(String commandType) {
        switch (commandType) {
            case "add":
                dialog.getStyleClass().add("add-label");
                break;
            case "mark":
                dialog.getStyleClass().add("marked-label");
                break;
            case "unmark":
                dialog.getStyleClass().add("unmarked-label");
                break;
            case "delete":
                dialog.getStyleClass().add("delete-label");
                break;
            case "list":
                dialog.getStyleClass().add("list-label");
                break;
            case "find":
                dialog.getStyleClass().add("find-label");
                break;
            case "error":
                dialog.getStyleClass().add("error-label");
                break;
            default:
                break;
        }
    }
}
