package krutskikh.calculation.interfaces;

import java.util.function.BiFunction;

public interface SaprCalculationBiFunction extends BiFunction<Double, Integer, String> {
    String representation();
}
