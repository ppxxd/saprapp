package krutskikh.calculation.interfaces;

import org.apache.commons.math3.util.Precision;

public class UXCalculate {
    private String UX_FORMATTER = "U%dx: (%f * x^2) + (%f * x) + (%f)";
    private double firstArg;
    private double secondArg;
    private double thirdArg;

    public Double calculate(double x) {
        return Precision.round((firstArg * x * x) + (secondArg * x) + thirdArg, 4);
    }

    public UXCalculate(double firstArg, double secondArg, double thirdArg) {
        this.firstArg = firstArg;
        this.secondArg = secondArg;
        this.thirdArg = thirdArg;
    }
}
