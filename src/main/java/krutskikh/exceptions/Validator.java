package krutskikh.exceptions;

import krutskikh.component.Bar;
import krutskikh.component.Construction;

public class Validator {
    public Boolean validate(Construction construction) {
        if (!(construction.getLeftSupport() || construction.getRightSupport())) return false;
        if (construction.getJoints().size() - construction.getBars().size() != 1) return false;
        return construction.getBars().stream().allMatch(this::isBarValid);

    }

    private Boolean isBarValid(Bar bar) {
        if (bar.getA() <= 0) return false;
        if (bar.getL() <= 0) return false;
        if (bar.getE() <= 0) return false;
        return bar.getSigma() > 0;
    }
}


