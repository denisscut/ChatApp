package com.example.chatApp.controller;

import com.example.chatApp.Client;
import com.example.chatApp.gui.SceneController;
import com.example.chatApp.gui.utils.UtilsGui;
import com.example.chatApp.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupPageController extends AbstractController{
    @FXML
    Button signupButton, loginButton;
    @FXML
    Label emailLabel, passwordLabel, firstNameLabel, lastNameLabel;
    @FXML
    Text errorText;
    @FXML
    TextField emailText, firstNameText, lastNameText;
    @FXML
    PasswordField passwordText;
    @FXML
    private void goToLoginPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        SceneController sceneAndController = UtilsGui.createScene("/loginPage.fxml", srv, null, getClass(), stage);
        stage.setScene(sceneAndController.getScene());
        stage.show();
    }
    @Override
    public void setClient(Client client) {
        this.client = client;
    }
    @Override
    public void setService(Service srv) {
        this.srv = srv;
    }
    @Override
    public void setStage(Stage stage) {
        this.myStage = stage;
    }
    @FXML
    private void signUp() {
        try {
            srv.addUser(firstNameText.getText(), lastNameText.getText(), emailText.getText(), passwordText.getText());
            loginButton.fire();
        }
        catch(RuntimeException e) {
            errorText.setText(e.getMessage());
        }
    }

}
