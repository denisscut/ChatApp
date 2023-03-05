package com.example.chatApp.controller;

import com.example.chatApp.Client;
import com.example.chatApp.domain.Message;
import com.example.chatApp.domain.User;
import com.example.chatApp.gui.SceneController;
import com.example.chatApp.gui.utils.UtilsGui;
import com.example.chatApp.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ChatPageController extends AbstractController {

    @FXML
    Button homeButton;
    @FXML
    ScrollPane scrollChatArea;
    @FXML
    VBox vboxChatArea;
    @FXML
    TextArea chatArea;
    @FXML
    TextField textField;
    private User currentUser;
    private User otherUser;
    @FXML
    Text otherUserText;
    public void getUserData() {
        try {
            currentUser = ((List<User>) myStage.getUserData()).get(0);
            otherUser = ((List<User>) myStage.getUserData()).get(1);
            otherUserText.setText(otherUser.getFirstName() + " " + otherUser.getLastName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void sendMessageFromTextField(ActionEvent event) throws IOException{
        if(!textField.getText().equals("")) {
            srv.addMessage(currentUser.getId(), otherUser.getId(), textField.getText(), LocalDateTime.now());
            client.sendMessage("message", currentUser.getId().toString(), otherUser.getId().toString(), textField.getText());
            textField.setText("");
        }
    }

    @FXML
    public void sendMessage(ActionEvent event) throws IOException {
        if(!textField.getText().equals("")) {
            srv.addMessage(currentUser.getId(), otherUser.getId(), textField.getText(), LocalDateTime.now());
            client.sendMessage("message", currentUser.getId().toString(), otherUser.getId().toString(), textField.getText());
            textField.setText("");
        }
    }

    @Override
    public void setStage(Stage stage) {
        this.myStage = stage;
        scrollChatArea.vvalueProperty().bind(vboxChatArea.heightProperty());
        scrollChatArea.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollChatArea.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    void initChat() {
        Iterable<Message> messages = srv.getMessages();
        if(messages != null) {
            callback callback = new callback();
            for (Message message : messages) {
                if (message.getUser1Id().equals(currentUser.getId()) && message.getUser2Id().equals(otherUser.getId())) {
                    callback.updateChat(message.getUser1Id().toString(), message.getMessage());
                }
                else if (message.getUser2Id().equals(currentUser.getId()) && message.getUser1Id().equals(otherUser.getId())) {
                    callback.updateChat(message.getUser1Id().toString(), message.getMessage());
                }
            }
        }
    }

    @Override
    public void setService(Service srv) {
        this.srv = srv;
        getUserData();
        initChat();
    }
    @Override
    public void setClient(Client client) throws IOException {
        this.client = client;
        client.readMessages(new callback());
        client.sendMessage("enterRoom", otherUser.getId().toString(), "", "");
    }

    public class callback {
        public void updateChat(String user, String msg) {
            Label label = null;
            HBox hboxPane = new HBox();
            vboxChatArea.setStyle("-fx-font-size: 20px;");
            String msgFormatted = "";
            int lines = 1;
            for(int i = 0; i < msg.length(); i++) {
                msgFormatted += msg.charAt(i);
                if(i % 30 == 0 && i != 0) {
                    msgFormatted += "\n";
                    lines++;
                }
            }
            label = new Label(msgFormatted);
            label.setMinHeight(15 + 15 + 20 * lines);
            if(currentUser.getId().toString().equals(user)) {
                label.setStyle("-fx-fill: #32302f;-fx-label-padding: 5px; -fx-background-radius: 15 0 15 15 ;-fx-background-color: #d79921;");
                hboxPane.setAlignment(Pos.CENTER_RIGHT);
            }
            else {
                label.setStyle("-fx-fill: #32302f;-fx-label-padding: 5px; -fx-background-radius: 0 15 15 15 ;-fx-background-color: #d79921;");
                hboxPane.setAlignment(Pos.CENTER_LEFT);
            }

            hboxPane.getChildren().add(label);
            vboxChatArea.getChildren().add(hboxPane);
        }
    }

    @FXML
    public void goToHomePage(ActionEvent event) throws IOException {
        try {
            client.sendMessage("leaveRoom", "", "", "");
            client.stopReadMessages();
            Stage stage = (Stage) myStage.getScene().getWindow();
            stage.setUserData(currentUser);
            SceneController sceneAndController = UtilsGui.createScene("/homePage.fxml", srv, client, getClass(), stage);
            HomePageController controller = (HomePageController) sceneAndController.getController();
            stage.setScene(sceneAndController.getScene());
            stage.show();
            controller.getUserData();
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
