package krutskikh.calculation.interfaces;

import org.apache.commons.math3.util.Precision;

public class NxCalculate {
    private static final String FORMATTER = "N%dx: (%f * x) + (%f)";
    private double firstArg;
    private double secondArg;

    public Double calculate(double x) {
        return Precision.round(firstArg * x + secondArg, 4);
    }

    public NxCalculate(double firstArg, double secondArg) {
        this.firstArg = firstArg;
        this.secondArg = secondArg;
    }
}
