package krutskikh.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import krutskikh.calculation.CalculationExceptionsHandler;
import krutskikh.calculation.CalculatorResult;
import krutskikh.calculation.Processor;
import krutskikh.component.Bar;
import krutskikh.component.Construction;
import krutskikh.component.Joint;
import krutskikh.service.Drawer;
import krutskikh.service.MainService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class Controller implements Initializable {
    @FXML
    private TableView<CalculatorResult> resultsView;
    @FXML
    private TableColumn<CalculatorResult, Double> xValue, Nx, Ux, sigmaX;
    @FXML
    private TextField samplingStep, barIndexes;
    private final MainService service = new MainService();
    private final Drawer drawer = Drawer.getInstance();
    private final FileChooser fileChooser = new FileChooser();
    private Construction construction;
    private transient final CalculationExceptionsHandler calculationExceptionsHandler = CalculationExceptionsHandler.getInstance();

    @FXML
    private BorderPane root;
    @FXML
    private VBox jointHolder;
    @FXML
    private VBox barHolder;
    @FXML
    private Canvas canvas;
    @FXML
    private CheckBox leftSupportBox;
    @FXML
    private CheckBox rightSupportBox;

    private void initColumns() {
        setCellValuesFactory();
    }

    private void setCellValuesFactory() {
        xValue.setCellValueFactory(new PropertyValueFactory<>("x"));
        Nx.setCellValueFactory(new PropertyValueFactory<>("NX"));
        Ux.setCellValueFactory(new PropertyValueFactory<>("UX"));
        sigmaX.setCellValueFactory(new PropertyValueFactory<>("sigma"));
    }

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
        initColumns();
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
            construction = service.load();
            barHolder.getChildren().addAll(construction.getBars());
            jointHolder.getChildren().addAll(construction.getJoints());
            if (construction.getRightSupport()) { //TODO В ОТДЕЛЬНЫЙ МЕТОД
                rightSupportBox.fire();
            }

            draw();
        }
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
        resultsView.getItems().clear();

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
        try {
            resultsView.getItems().clear();
            calculationExceptionsHandler.doCalculationExceptionHandler(barIndexes.getText(), samplingStep.getText(), construction);
            double barIndex = Double.parseDouble(barIndexes.getText());
            double step = Double.parseDouble(samplingStep.getText());
            int stepPrecision = getNumberPrecision(samplingStep.getText());
            Processor processor = new Processor();
            List<CalculatorResult> resultList = processor.calculate(construction, (int) barIndex - 1, step, stepPrecision);
            resultsView.getItems().addAll(resultList);
        } catch (Throwable  e) {
            System.out.println("error:");
            System.out.println(e.getMessage());
        }
        draw();
    }

    @FXML
    public void saveCalculation() {
//        if (calculationFile.isEmpty() || construction.getBars().isEmpty() || construction.getJoints().isEmpty()) {
//            return;
//        }
//
//        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
//        service.setPath(file.getAbsolutePath());
//        service.save(calculationFile, file);

        List<CalculatorResult> calculatorResults = resultsView.getItems();
        save(calculatorResults);
    }


    private int getNumberPrecision(String number) {
        String[] dotSplit = number.split("\\.");
        if (dotSplit.length == 1) {
            return 0;
        }
        return dotSplit[1].length();
    }

    private String prepareResultsForSaving(List<CalculatorResult> results) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("x;Nx;Ux;∂x");
        for (CalculatorResult result : results) {
            String resultLine = result.getX() + ";" + result.getNX() + ";" + result.getUX() + ";" + result.getSigma();
            joiner.add(resultLine);
        }
        return joiner.toString();
    }

    public void save(List<CalculatorResult> Result) {
        if (Result.isEmpty()) {
            calculationExceptionsHandler.saveResultsHandler();
            return;
        }
        File chosenFile = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (chosenFile == null) {
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(chosenFile.toPath())) {
            writer.write(prepareResultsForSaving(Result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}