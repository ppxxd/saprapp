package krutskikh.calculation;


import lombok.Getter;

@Getter
public class CalculatorResult {
    private double x;
    private double sigma;
    private double NX;
    private double UX;

    public CalculatorResult(double x, double sigma, double NX, double UX) {
        this.x = x;
        this.sigma = sigma;
        this.NX = NX;
        this.UX = UX;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public void setNX(double NX) {
        this.NX = NX;
    }

    public void setUX(double UX) {
        this.UX = UX;
    }

}