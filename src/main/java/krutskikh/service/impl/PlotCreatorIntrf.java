package krutskikh.service.impl;

import javafx.scene.Node;
import krutskikh.component.Construction;
import krutskikh.service.PlotCreator;
import krutskikh.calculation.Processor;
import krutskikh.calculation.CalculatorResult;
import javafx.scene.Group;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.List;

// TODO refactor this
public class PlotCreatorIntrf implements PlotCreator {
    private final Processor processor = new Processor();

    @Override
    public Group create(Construction construction, double samplingStep, int stepPrecision, double[] barLengths) {
        AreaChart<Number, Number> nxArea = new AreaChart<>(new NumberAxis(), new NumberAxis());
        AreaChart<Number, Number> OxArea = new AreaChart<>(new NumberAxis(), new NumberAxis());
        AreaChart<Number, Number> uxArea = new AreaChart<>(new NumberAxis(), new NumberAxis());
        XYChart.Series<Number, Number> nxSeries = null;
        XYChart.Series<Number, Number> OxSeries = null;
        XYChart.Series<Number, Number> uxSeries = null;
        double leftBorder = 0.0;
        for (int i = 0; i < barLengths.length; i++) {
            nxSeries = new XYChart.Series<>();
            OxSeries = new XYChart.Series<>();
            uxSeries = new XYChart.Series<>();
            List<CalculatorResult> resultList = processor.calculate(construction, i, samplingStep, stepPrecision);
            for (CalculatorResult result : resultList) {
                nxSeries.getData().add(new XYChart.Data<>(leftBorder + result.getX(), result.getNX()));
                OxSeries.getData().add(new XYChart.Data<>(leftBorder + result.getX(), result.getUX()));
                uxSeries.getData().add(new XYChart.Data<>(leftBorder + result.getX(), result.getSigma()));
            }
            leftBorder += barLengths[i];
            nxSeries.setName(String.valueOf(i + 1));
            OxSeries.setName(String.valueOf(i + 1));
            uxSeries.setName(String.valueOf(i + 1));
            nxArea.getData().add(nxSeries);
            OxArea.getData().add(OxSeries);
            uxArea.getData().add(uxSeries);
        }
        Tab nxTab = new Tab("Nx", nxArea);
        nxTab.setClosable(false);
        Tab uxTab = new Tab("Ux", OxArea);
        uxTab.setClosable(false);
        Tab oxTab = new Tab("∂x", uxArea);
        oxTab.setClosable(false);
        TabPane tabPane = new TabPane(nxTab, uxTab, oxTab);
        Node node = nxArea.lookup(".default-color0.chart-series-area-fill");
        node.setStyle("-fx-fill: #336699;");
        node = OxArea.lookup(".default-color0.chart-series-area-fill");
        node.setStyle("-fx-fill: #336699;");
        node = uxArea.lookup(".default-color0.chart-series-area-fill");
        node.setStyle("-fx-fill: #336699;");
        return new Group(tabPane);
    }
}
