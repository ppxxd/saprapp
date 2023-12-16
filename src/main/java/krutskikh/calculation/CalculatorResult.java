package krutskikh.calculation;


import lombok.Getter;

@Getter
public class CalculatorResult {
    private final double x;
    private final double sigma;
    private final double NX;
    private final double UX;

    public CalculatorResult(double x, double sigma, double NX, double UX) {
        this.x = x;
        this.sigma = sigma;
        this.NX = NX;
        this.UX = UX;
    }

}
