package krutskikh.service;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import krutskikh.component.Bar;
import krutskikh.component.Construction;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Drawer {
    private static Drawer instance;

    private Canvas canvas;
    @Getter
    private Construction construction;

    public static Drawer getInstance() {
        if (instance == null) {
            instance = new Drawer();
        }

        return instance;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setConstruction(Construction construction) {
        this.construction = construction;
    }

    public void draw() {
        //clear canvas
        if (canvas == null) {
            return;
        }
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Double startX = 30d;
        Double startY = canvas.getHeight()/2;

        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setLineWidth(3);

        List<Bar> bars = construction.getBars().stream()
                .sorted(Comparator.comparing(Bar::getBarId, Integer::compareTo)).collect(Collectors.toList());

        Double constructionLength = 1d;
        Double constructionHeight = 1d;
        for (Bar bar : bars) {
            constructionLength += bar.getL();
            if (bar.getA() >= constructionHeight) {
                constructionHeight = bar.getA();
            }
        }

        Double baseLength = (canvas.getWidth() - 20) / constructionLength;
        Double baseHeight = (canvas.getHeight() / 8) / constructionHeight;

        if (construction.getLeftSupport()) {
            drawSupport(context, startX, true);
        }

        for (Bar bar : bars) {
            drawConcentratedWeight(context, bar.getLeftJoint().getF(), startX, startY);
            drawBar(context, startX, startY, bar.getL() * baseLength, bar.getA() * baseHeight);
            drawDistributedWeight(context, bar.getQ(), bar.getL() * baseLength, startX, startY);
            startX += bar.getL() * baseLength;
            drawConcentratedWeight(context, bar.getRightJoint().getF(), startX, startY);

        }

        if (construction.getRightSupport()) {
            drawSupport(context, startX, false);
        }
    }

    private void drawConcentratedWeight(GraphicsContext context, Double weight, Double x, Double y) {  //F
        context.setStroke(Color.NAVY);

        if (weight == null || weight.isNaN() || weight.equals(0d)) {
            return;
        } else if (weight < 0) {
            double endX = x - 20;
            context.strokeLine(x, y, endX, y);
            context.strokeLine(endX, y, endX + 5, y + 5);
            context.strokeLine(endX, y, endX + 5, y - 5);
        } else {
            double endX = x + 20;
            context.strokeLine(x, y, endX, y);
            context.strokeLine(endX, y, endX - 5, y + 5);
            context.strokeLine(endX, y, endX - 5, y - 5);
        }
    }

    private void drawDistributedWeight(GraphicsContext context, Double weight, Double length, Double x, Double y) {  //Q
        context.setStroke(Color.NAVY);

        //drawDistributedWeight(context, bar.getQ(), bar.getL() * baseLength, startX, startY);
        double end = x + length;
        if (weight.isNaN() || weight.equals(0d)) {
            return;
        } else if (weight < 0) {
            //System.out.println(x + " " + length + " " + end);
            while (x < end) {
                double endX = x;
                context.strokeLine(x + 10, y, endX, y);
                context.strokeLine(endX, y, endX + 2, y + 2);
                context.strokeLine(endX, y, endX + 2, y - 2);

                x += 15;
            }
        } else {
           // System.out.println(x + " " + length + " " + end);
            while (x < end) {
                double endX = x + 10;
                context.strokeLine(x, y, endX, y);
                context.strokeLine(endX, y, endX - 2, y + 2);
                context.strokeLine(endX, y, endX - 2, y - 2);

                x += 15;
            }
        }
    }

    private void drawBar(GraphicsContext context, Double x, Double y, Double length, Double square) {
        context.setStroke(Color.BLACK);

        context.strokeRect(x, y - square/2, length, square);
    }

    private void drawSupport(GraphicsContext context, Double x, Boolean isLeft) {
        context.setStroke(Color.BLACK);
        double length = context.getCanvas().getHeight() / 2;
        double startY = context.getCanvas().getHeight() / 4;
        double endY = startY + length;

        context.strokeLine(x, startY, x, endY);

        double endX = x + 5;
        if (isLeft) {
            endX = x - 5;
        }
        while (startY < endY) {
            context.strokeLine(x, startY, endX, startY + 10);
            startY += 10;
        }
    }
}





