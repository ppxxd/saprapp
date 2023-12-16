package krutskikh.service;

import krutskikh.component.Construction;
import javafx.scene.Group;

public interface GraphCreator {
    Group create(Construction construction, int barIndex, double samplingStep, double barLength, int stepPrecision);
}
