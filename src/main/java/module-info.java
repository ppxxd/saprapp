module krutskikh {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires commons.math3;
    requires lombok;
    requires com.google.gson;

    opens krutskikh to javafx.fxml;
    exports krutskikh;
    exports krutskikh.controller;
    opens krutskikh.controller to javafx.fxml;
    opens krutskikh.elements to javafx.fxml;
}