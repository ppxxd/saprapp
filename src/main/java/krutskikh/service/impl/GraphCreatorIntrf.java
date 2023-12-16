package krutskikh.service.impl;


import krutskikh.component.Construction;
import krutskikh.calculation.Processor;
import krutskikh.calculation.CalculatorResult;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import krutskikh.service.GraphCreator;

import java.util.List;

public class GraphCreatorIntrf implements GraphCreator {
    private static final List<String> LOADS_NAME = List.of("Nx", "Ux", "∂x");
    private final Processor processor = new Processor();

    public Group create(Construction calculator, int barIndex, double samplingStep, double barLength, int stepPrecision) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(LOADS_NAME);
        comboBox.setValue(LOADS_NAME.get(0));
        XYChart.Series<Number, Number> nxSeries = new XYChart.Series<>();
        LineChart<Number, Number> nxChart = new LineChart<>(new NumberAxis(), new NumberAxis());
        XYChart.Series<Number, Number> OxSeries = new XYChart.Series<>();
        LineChart<Number, Number> OxChart = new LineChart<>(new NumberAxis(), new NumberAxis());
        XYChart.Series<Number, Number> uxSeries = new XYChart.Series<>(); // sigma
        LineChart<Number, Number> uxChart = new LineChart<>(new NumberAxis(), new NumberAxis()); // sigma
        List<CalculatorResult> results = processor.calculate(calculator, barIndex, samplingStep, stepPrecision);
        for (CalculatorResult result : results) {
            nxSeries.getData().add(new XYChart.Data<>(result.getX(), result.getNX()));
            OxSeries.getData().add(new XYChart.Data<>(result.getX(), result.getUX()));
            uxSeries.getData().add(new XYChart.Data<>(result.getX(), result.getSigma()));
        }

        nxSeries.setName(String.valueOf(barIndex + 1));
        OxSeries.setName(String.valueOf(barIndex + 1));
        uxSeries.setName(String.valueOf(barIndex + 1));
        nxChart.getData().add(nxSeries);
        OxChart.getData().add(OxSeries);
        uxChart.getData().add(uxSeries);
        Tab nxTab = new Tab("Nx", nxChart);
        Tab uxTab = new Tab("Ux", OxChart);
        Tab oxTab = new Tab("∂x", uxChart);
        TabPane pane = new TabPane(nxTab, uxTab, oxTab);
        return new Group(pane);
    }
}
