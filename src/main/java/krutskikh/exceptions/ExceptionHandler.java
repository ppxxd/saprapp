package krutskikh.exceptions;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class ExceptionHandler {
    private static ExceptionHandler instance;

    public static ExceptionHandler getInstance() {
        if (instance == null) {
            instance = new ExceptionHandler();
        }
        return instance;
    }

    public void handle(Exception e) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Предупреждение");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText(e.getMessage());
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
    }
}
