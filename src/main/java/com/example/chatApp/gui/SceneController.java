package com.example.chatApp.gui;

import com.example.chatApp.controller.AbstractController;
import javafx.scene.Scene;

public class SceneController {
    private AbstractController controller;
    private Scene scene;

    public SceneController(AbstractController controller, Scene scene) {
        this.controller = controller;
        this.scene = scene;
    }

    public AbstractController getController() {
        return controller;
    }

    public Scene getScene() {
        return scene;
    }
}
