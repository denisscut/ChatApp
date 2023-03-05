module com.example.chatApp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.chatApp to javafx.fxml;
    opens com.example.chatApp.gui to javafx.fxml;
    opens com.example.chatApp.controller to javafx.fxml;
    opens com.example.chatApp.domain to javafx.fxml;

    exports com.example.chatApp.controller;
    exports com.example.chatApp.gui;
    exports com.example.chatApp;
    exports com.example.chatApp.gui.utils;
    exports com.example.chatApp.domain;
    opens com.example.chatApp.gui.utils to javafx.fxml;
}