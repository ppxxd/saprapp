package krutskikh.component;

import krutskikh.service.Drawer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import krutskikh.exceptions.ExceptionHandler;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

public class Joint extends GridPane implements Serializable {
    public static int JOINT_COUNT = 1;
    private transient final Drawer drawer = Drawer.getInstance();

    private transient final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();
    private transient final TextField FInput = getInputTextField(this::setF, (value) -> true); //TODO check if double

    @Getter
    private Integer jointId;
    @Getter
    private Double F;

    public Joint() {
        super();
        this.jointId = JOINT_COUNT;
        setWidth(Double.MAX_VALUE);
        this.setHgap(5);

        this.add(getInputLabel("№"), 0, 0);
        this.add(new Label(String.valueOf(jointId)), 1, 0);

        this.add(getInputLabel("F"), 1, 0);
        this.add(FInput, 2, 0);

        this.autosize();
        JOINT_COUNT++;
    }

    public void recalculateInputs() {
        this.FInput.setText(String.valueOf(this.F));
    }

    private Label getInputLabel(String text) {
        Label label = new Label(text);
        label.setMinWidth(50);
        label.setAlignment(Pos.CENTER);

        return label;
    }

    private TextField getInputTextField(Consumer<Double> consumer, Function<Double, Boolean>function) {
        TextField textField = new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            double fieldValue = 0d;
            try {
                fieldValue = Double.parseDouble(newValue);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            if (function.apply(fieldValue)) {
                consumer.accept(fieldValue);
                drawer.draw();
            } else {
                String message = "Неправильно введено F в узле " + this.jointId;
                exceptionHandler.handle(new IllegalArgumentException(message));
                textField.setText("");
            }
        });

        return textField;
    }

    public void setF(Double f) {
        F = f;
    }

    public void setJointId(Integer jointId) {
        this.jointId = jointId;
    }

}
