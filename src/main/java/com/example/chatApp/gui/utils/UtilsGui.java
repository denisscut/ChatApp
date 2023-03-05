package com.example.chatApp.gui.utils;

import com.example.chatApp.Client;
import com.example.chatApp.config.Config;
import com.example.chatApp.controller.AbstractController;
import com.example.chatApp.gui.SceneController;
import com.example.chatApp.service.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UtilsGui {
    public static SceneController createScene(String path, Service srv, Client client, Class classSrc, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(classSrc.getResource(path));
        Scene scene = new Scene(fxmlLoader.load(), Config.WIDTH_SIZE, Config.HEIGHT_SIZE);
        scene.getStylesheets().add("/style.css");
        AbstractController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setService(srv);
        controller.setClient(client);
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            double newWidth = newSceneWidth.doubleValue();
            Config.WIDTH_SIZE = newWidth;
        });
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            double newHeight = newSceneHeight.doubleValue();
            Config.HEIGHT_SIZE = newHeight;
        });
        return new SceneController(controller, scene);
    }
}
