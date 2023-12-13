package krutskikh.component;

import krutskikh.exceptions.ExceptionHandler;
import krutskikh.service.Drawer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.Getter;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

public class Bar extends GridPane implements Serializable {
    public static int BAR_COUNT = 1;

    @Getter
    private Integer barId;
    @Getter
    private Double L = 0d; //Длина
    @Getter
    private Double A = 0d; //Площадь
    @Getter
    private Double Q = 0d; //Продольная сила
    @Getter
    private Double E = 0d; //Сила упругости
    @Getter
    private Double sigma = 0d; //Сигма

    @Getter
    private Joint leftJoint; //Левая заделка
    @Getter
    private Joint rightJoint; //Правая заделка

    private transient final Drawer drawer = Drawer.getInstance();
    private transient final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();
    private transient final TextField LInput = getInputTextField(this::setL, (value) -> value > 0, "L");
    private transient final TextField AInput = getInputTextField(this::setA, (value) -> value > 0, "A");
    private transient final TextField QInput = getInputTextField(this::setQ, (value) -> isNumeric(String.valueOf(value)), "Q"); //TODO check if double
    private transient final TextField EInput = getInputTextField(this::setE, (value) -> value > 0, "E");
    private transient final TextField sigmaInput = getInputTextField(this::setSigma, (value) -> value > 0, "sigma");

    public Bar() {
        super();
        this.barId = BAR_COUNT;
        setWidth(Double.MAX_VALUE);
        this.setHgap(3);

        this.add(getInputLabel("№"), 0, 0);
        this.add(new Label(String.valueOf(barId)), 0, 1);

        this.add(getInputLabel("Длина L"), 1, 0);
        this.add(LInput, 1, 1);

        this.add(getInputLabel("Площадь A"), 2, 0);
        this.add(AInput, 2, 1);

        this.add(getInputLabel("Cила Q"), 3, 0);
        this.add(QInput, 3, 1);

        this.add(getInputLabel("Модуль упругости E"), 4, 0);
        this.add(EInput, 4, 1);

        this.add(getInputLabel("Сигма σ"), 5, 0);
        this.add(sigmaInput, 5, 1);

        //delete
        this.autosize();
        generateBarId();
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    private static void generateBarId() { //Update bar ID +1
        BAR_COUNT++;
    }
    public void recalculateInputs() {
        this.LInput.setText(String.valueOf(this.L));
        this.AInput.setText(String.valueOf(this.A));
        this.QInput.setText(String.valueOf(this.Q));
        this.EInput.setText(String.valueOf(this.E));
        this.sigmaInput.setText(String.valueOf(this.sigma));
    }

    private TextField getInputTextField(Consumer<Double> consumer, Function<Double, Boolean> function, String fieldName) {
        TextField textField = new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            double fieldValue = 0d;
            try {
                fieldValue = Double.parseDouble(newValue);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
//            if (isNumeric(newValue)) {
//                try {
//                    fieldValue = Double.parseDouble(newValue);
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }

            if (function.apply(fieldValue)) {
                consumer.accept(fieldValue);
                drawer.draw();
            } else {
                String message = "Неправильно введено " + fieldName + " в стержне " + this.barId;
                exceptionHandler.handle(new IllegalArgumentException(message));
                textField.setText("");
            }
        });

        return textField;
    }

    private Label getInputLabel(String text) {
        Label label = new Label(text);
        label.setMinWidth(30);
        label.setAlignment(Pos.CENTER);

        return label;
    }

    public void setBarId(Integer barId) {
        this.barId = barId;
    }

    public void setL(Double l) {
        L = l;
    }

    public void setA(Double a) {
        A = a;
    }

    public void setQ(Double q) {
        Q = q;
    }

    public void setE(Double e) {
        E = e;
    }

    public void setLeftJoint(Joint leftJoint) {
        this.leftJoint = leftJoint;
    }

    public Double getSigma() {
        return sigma;
    }

    public void setRightJoint(Joint rightJoint) {
        this.rightJoint = rightJoint;
    }

    public void setSigma(Double sigma) {
        this.sigma = sigma;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "barId=" + barId +
                ", L=" + L +
                ", A=" + A +
                ", Q=" + Q +
                ", E=" + E +
                ", leftJoint=" + leftJoint +
                ", rightJoint=" + rightJoint +
                '}';
    }
}
