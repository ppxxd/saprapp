package krutskikh.controller;

import krutskikh.component.Bar;
import krutskikh.component.Construction;
import krutskikh.component.Joint;
import krutskikh.service.Drawer;
import krutskikh.service.MainService;
import krutskikh.calculation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.apache.commons.math3.util.Precision;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    private final MainService service = new MainService();
    private final Drawer drawer = Drawer.getInstance();
    private final FileChooser fileChooser = new FileChooser();
    private Construction construction;

    private CalculationFile calculationFile;

    @FXML
    private BorderPane root;
    @FXML
    private VBox jointHolder;
    @FXML
    private VBox barHolder;
    @FXML
    private VBox calcHolder;
    @FXML
    private Canvas canvas;
    @FXML
    private CheckBox leftSupportBox;
    @FXML
    private CheckBox rightSupportBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane pane = (AnchorPane) canvas.getParent();

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        construction = new Construction();
        drawer.setCanvas(canvas);
        drawer.setConstruction(construction);

        leftSupportBox.setSelected(construction.getLeftSupport());
        rightSupportBox.setSelected(construction.getRightSupport());
    }

    @FXML
    public void addBar() {
        Bar bar = new Bar();
        if (construction.getBars().isEmpty()) {
            Joint joint = new Joint();
            construction.getJoints().add(joint);
            bar.setLeftJoint(joint);
        } else {
            Bar prevBar = construction.getBars().get(construction.getBars().size() - 1);
            bar.setLeftJoint(prevBar.getRightJoint());
        }
        Joint joint = new Joint();
        construction.getJoints().add(joint);
        bar.setRightJoint(joint);

        construction.getBars().add(bar);

        barHolder.getChildren().add(bar);
        jointHolder.getChildren().clear();
        jointHolder.getChildren().addAll(construction.getJoints());
    }

    @FXML
    public void save() { //TODO (vrode works)
//        if (service.getPath() == null || service.getPath().isBlank()) {
//            File file = fileChooser.showSaveDialog(root.getScene().getWindow());
//            service.setPath(file.getAbsolutePath());
//        }
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        service.setPath(file.getAbsolutePath());
        service.save(construction);
    }

    @FXML
    public void load() { //TODO (vrode works)
//        clearConstruction();

//        if (service.getPath() == null || service.getPath().isBlank()) {
//            File file = fileChooser.showOpenDialog(root.getScene().getWindow());
//            service.setPath(file.getAbsolutePath());
//        }

        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        service.setPath(file.getAbsolutePath());

        if (service.getPath() != null) {
            clearConstruction();
        }
        construction = service.load();
        barHolder.getChildren().addAll(construction.getBars());
        jointHolder.getChildren().addAll(construction.getJoints());

        draw();
    }

    @FXML
    public void draw() {
        drawer.setConstruction(construction);
        drawer.draw();
    }

    @FXML
    public void clearConstruction() {
        Bar.BAR_COUNT = 1;
        Joint.JOINT_COUNT = 1;

        barHolder.getChildren().clear();
        jointHolder.getChildren().clear();
//        if (leftSupportBox.isSelected()) { //clearing out checkboxes
//            leftSupportBox.fire();
//        }
        if (rightSupportBox.isSelected()) {
            rightSupportBox.fire();
        }
        construction = new Construction();

        draw();
    }

    @FXML
    public void setLeftSupport() {
        construction.setLeftSupport(leftSupportBox.isSelected());
        draw();
    }

    @FXML
    public void setRightSupport() {
        construction.setRightSupport(rightSupportBox.isSelected());
        draw();
    }

    @FXML
    public void doCalculation() {
        if (construction.getBars().isEmpty()) {
            return;
        }

        Calculators calculators = new Calculators();
        calculationFile = calculators.calculate(construction);
        if (Objects.nonNull(calculationFile) && !calculationFile.isEmpty()) {
            System.out.println(calculationFile);
            System.out.println("Перемещения: " + calculationFile.getMoving());
            System.out.println("Продольные силы:" + calculationFile.getLongitudinalStrong());
            System.out.println("Нормальные напряжения:" + calculationFile.getNormalVoltage());
        }
        draw();
    }

    @FXML
    public void saveCalculation() {
        if (calculationFile.isEmpty() || construction.getBars().isEmpty() || construction.getJoints().isEmpty()) {
            return;
        }

        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        service.setPath(file.getAbsolutePath());
        service.save(calculationFile, file);

    }


//    public Optional<List<CalculatorResult>> calculate(int barIndex, String shiftStep, int precision, CalculationFile calculationFile) {
//        try {
//            double parsedStep = Double.parseDouble(shiftStep);
//            int stepPrecision = 1;
//            double barLength = construction.getBars().size();
//            List<CalculatorResult> calculatorResults = new ArrayList<>();
//            for (double x = 0.0; Precision.round(x, stepPrecision) <= barLength; x += parsedStep) {
//                calculatorResults.add(calculator.calculate(Precision.round(x, stepPrecision), precision, barIndex - 1));
//            }
//            return Optional.of(calculatorResults);
//        } catch (NumberFormatException e) {
//            throw new RuntimeException(e);
//        }
//        return Optional.empty();
//    }

}