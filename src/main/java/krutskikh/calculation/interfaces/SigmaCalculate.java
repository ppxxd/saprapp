package krutskikh.calculation.interfaces;

import org.apache.commons.math3.util.Precision;

public class SigmaCalculate {
    private final double firstArg;
    private final double secondArg;

    public Double calculate(double x) {
        return Precision.round(firstArg * x + secondArg, 4);
    }

    public SigmaCalculate(double firstArg, double secondArg) {
        this.firstArg = firstArg;
        this.secondArg = secondArg;
    }
}

