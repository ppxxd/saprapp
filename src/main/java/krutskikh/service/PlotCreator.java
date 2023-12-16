package krutskikh.service;

import krutskikh.component.Construction;
import javafx.scene.Group;

public interface PlotCreator {
    Group create(Construction construction, double samplingStep, int stepPrecision, double[] barLengths);
}
