package com.example.chatApp.controller;

import com.example.chatApp.Client;
import com.example.chatApp.domain.User;
import com.example.chatApp.gui.SceneController;
import com.example.chatApp.gui.utils.UtilsGui;
import com.example.chatApp.service.Service;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginPageController extends AbstractController{
    @FXML
    Button signupButton, loginButton;
    @FXML
    Text errorText;
    @FXML
    Label emailLabel, passwordLabel;

    @FXML
    TextField emailText, passwordText;

    @FXML
    private void goToSignupPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) myStage.getScene().getWindow();
        SceneController sceneAndController = UtilsGui.createScene("/signupPage.fxml", srv, null, getClass(), stage);
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
    public void login() {
        try {
            if(srv.checkUser(emailText.getText(), passwordText.getText())) {
                Stage stage = (Stage) myStage.getScene().getWindow();
                User user = srv.getUserByEmailAndPassword(emailText.getText(), passwordText.getText());
                stage.setUserData(user);
                client = new Client(user.getId().toString());

                stage.setOnCloseRequest(e -> {
                    try {
                        client.disconnectFromServer();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Platform.exit();
                    System.exit(0);
                });

                SceneController sceneAndController = UtilsGui.createScene("/homePage.fxml", srv, client, getClass(), stage);
                stage.setScene(sceneAndController.getScene());
                stage.show();
                HomePageController controller = (HomePageController) sceneAndController.getController();
                controller.getUserData();
            }
            else{
                errorText.setText("Wrong email or password.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
