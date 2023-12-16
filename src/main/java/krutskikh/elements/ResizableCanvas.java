package krutskikh.elements;

import krutskikh.service.Drawer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;

public class ResizableCanvas extends Canvas {
    private final Drawer drawer = Drawer.getInstance();

    public ResizableCanvas() {
        widthProperty().addListener(evt -> {
            try {
                draw();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        heightProperty().addListener(evt -> {
            try {
                draw();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void draw() throws IOException {
        double width = getWidth();
        double height = getHeight();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        if (drawer != null) {
            drawer.draw();
        }
    }

    @Override
    public boolean isResizable() {
        return true;
    }
}