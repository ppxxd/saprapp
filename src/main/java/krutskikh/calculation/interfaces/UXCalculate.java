package krutskikh.calculation.interfaces;

import org.apache.commons.math3.util.Precision;

public class UXCalculate {
    private final double firstArg;
    private final double secondArg;
    private final double thirdArg;

    public Double calculate(double x) {
        return Precision.round((firstArg * x * x) + (secondArg * x) + thirdArg, 4);
    }

    public UXCalculate(double firstArg, double secondArg, double thirdArg) {
        this.firstArg = firstArg;
        this.secondArg = secondArg;
        this.thirdArg = thirdArg;
    }
}
