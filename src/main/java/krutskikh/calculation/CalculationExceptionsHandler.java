package krutskikh.calculation;

import krutskikh.component.Bar;
import krutskikh.component.Construction;
import krutskikh.component.Joint;
import krutskikh.exceptions.ExceptionHandler;

public class CalculationExceptionsHandler {
    private transient final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();
    private static CalculationExceptionsHandler INSTANCE;

    public static CalculationExceptionsHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CalculationExceptionsHandler();
        }
        return INSTANCE;
    }

    public void doCalculationExceptionHandler(String barIndexes, String samplingStep, Construction construction) throws Exception {
        if (barIndexes.isEmpty()) { //Если стержень не указан
            exceptionHandler.handle(new IllegalArgumentException("Необходимо указать номер стержня!"));
            return;
        }
        try { //Если вместо стержня написан бред
            Double.parseDouble(barIndexes);
        } catch (Throwable e) {
            exceptionHandler.handle(new IllegalArgumentException("Неправильно введён номер стержня"));
            return;
        }
        if (Double.parseDouble(barIndexes) > construction.getBars().size()) { //Если больше их кол-ва
            exceptionHandler.handle(new IllegalArgumentException("Номер стержня не должен превышать их количество!"));
            return;
        } else if (Double.parseDouble(barIndexes) <= 0) { //Если меньше или равен 0
            exceptionHandler.handle(new IllegalArgumentException("Номер стержня не должен быть меньше 1!"));
            return;
        }

        if (samplingStep.isEmpty()) { //Если не указан шаг
            exceptionHandler.handle(new IllegalArgumentException("Необходимо указать шаг!"));
            return;
        }
        try { //Если вместо шага написан бред
            Double.parseDouble(samplingStep);
        } catch (Throwable e) {
            exceptionHandler.handle(new IllegalArgumentException("Неправильно введён номер стержня"));
            return;
        }
        if (Double.parseDouble(samplingStep) <= 0) { //Если не указан шаг
            exceptionHandler.handle(new IllegalArgumentException("Шаг должен быть больше 0!"));
            return;
        }

        for (Bar bar : construction.getBars()) {
            if (bar.getL() == 0) {
                exceptionHandler.handle(new IllegalArgumentException("Не указана длина в " + bar.getBarId() + " стержне!"));
                return;
            } else if (bar.getA() == 0) {
                exceptionHandler.handle(new IllegalArgumentException("Не указана площадь в " + bar.getBarId() + " стержне!"));
                return;
            } else if (!bar.getIsQSpecified()) {
                exceptionHandler.handle(new IllegalArgumentException("Не указано значение погонной нагрузки в узле " + bar.getBarId() + "!"));
                throw new Exception();
            } else if (bar.getE() == 0) {
                exceptionHandler.handle(new IllegalArgumentException("Не указан модуль упругости в " + bar.getBarId() + " стержне!"));
                return;
            } else if (bar.getSigma() == 0) {
                exceptionHandler.handle(new IllegalArgumentException("Не указано допускаемое напряжение в " + bar.getBarId() + " стержне!"));
                return;
            }
        }

        for (Joint joint : construction.getJoints()) { //Если в узле нету какой-то из сил
            if (!joint.getIsSpecified()) {
                exceptionHandler.handle(new IllegalArgumentException("Не указано силы значение в узле " + joint.getJointId() + "!"));
                return;
            }
        }
    }

    public void saveResultsHandler() {
        exceptionHandler.handle(new IllegalArgumentException("Для начала нужно произвести расчёт"));
    }
}
