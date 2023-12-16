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

    public void doCalculationExceptionHandler(String x, Construction construction) throws Exception {
        if (x.isEmpty()) {
            exceptionHandler.handle(new IllegalArgumentException("Необходимо указать точку!"));
            throw new Exception();
        }
        try { //Если вместо точки написан бред
            Double.parseDouble(x);
        } catch (Throwable e) {
            exceptionHandler.handle(new IllegalArgumentException("Неправильно записана точка"));
            throw new Exception();
        }
        Double lengthSum = 0d;
        for (Bar bar : construction.getBars()) {
            lengthSum += bar.getL();
        }
        if (Double.parseDouble(x) > lengthSum) { //Если больше длины конструкции
            exceptionHandler.handle(new IllegalArgumentException("Координата точки не должна превышать длину" +
                    " конструкции!"));
            throw new Exception();
        } else if (Double.parseDouble(x) < 0) { //Если меньше 0
            exceptionHandler.handle(new IllegalArgumentException("Координата точки не должна быть меньше 0!"));
            throw new Exception();
        }

        checkJointHandler(construction);
        checkBarHandler(construction);
    }

    public void doCalculationExceptionHandler(String barIndexes, String samplingStep, Construction construction)
            throws Exception {
        if (barIndexes.isEmpty()) { //Если стержень не указан
            exceptionHandler.handle(new IllegalArgumentException("Необходимо указать номер стержня!"));
            throw new Exception();
        }
        try { //Если вместо стержня написан бред
            Double.parseDouble(barIndexes);
        } catch (Throwable e) {
            exceptionHandler.handle(new IllegalArgumentException("Неправильно введён номер стержня"));
            throw new Exception();
        }
        if (Double.parseDouble(barIndexes) > construction.getBars().size()) { //Если больше их кол-ва
            exceptionHandler.handle(new IllegalArgumentException("Номер стержня не должен превышать их количество!"));
            throw new Exception();
        } else if (Double.parseDouble(barIndexes) <= 0) { //Если меньше или равен 0
            exceptionHandler.handle(new IllegalArgumentException("Номер стержня не должен быть меньше 1!"));
            throw new Exception();
        }

        if (samplingStep.isEmpty()) { //Если не указан шаг
            exceptionHandler.handle(new IllegalArgumentException("Необходимо указать шаг!"));
            throw new Exception();
        }
        try { //Если вместо шага написан бред
            Double.parseDouble(samplingStep);
        } catch (Throwable e) {
            exceptionHandler.handle(new IllegalArgumentException("Неправильно введён номер стержня"));
            throw new Exception();
        }
        if (Double.parseDouble(samplingStep) <= 0) { //Если не указан шаг
            exceptionHandler.handle(new IllegalArgumentException("Шаг должен быть больше 0!"));
            throw new Exception();
        }

        checkBarHandler(construction);
        checkJointHandler(construction);
    }

    private void checkJointHandler(Construction construction) {
        for (Joint joint : construction.getJoints()) { //Если в узле нет какой-то из сил
            if (!joint.getIsSpecified()) {
                exceptionHandler.handle(new IllegalArgumentException("Не указано значение силы в узле " + joint.getJointId() + "!"));
                return;
            }
        }
    }

    private void checkBarHandler(Construction construction) throws Exception {
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
    }

    public void saveResultsHandler() {
        exceptionHandler.handle(new IllegalArgumentException("Для начала нужно произвести расчёт"));
    }
}
