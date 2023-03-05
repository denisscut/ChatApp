package com.example.chatApp.gui;

import com.example.chatApp.gui.utils.UtilsGui;
import com.example.chatApp.service.Service;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class MainView extends Application {
    public static Service srv;
    @Override
    public void start(Stage stage) throws IOException {
        SceneController sceneAndController = UtilsGui.createScene("/loginPage.fxml", srv, null, getClass(), stage);
        stage.setTitle("ChatApp");
        stage.setResizable(false);
        stage.setScene(sceneAndController.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
