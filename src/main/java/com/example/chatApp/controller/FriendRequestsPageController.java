package com.example.chatApp.controller;

import com.example.chatApp.Client;
import com.example.chatApp.domain.Friendship;
import com.example.chatApp.domain.FriendshipDTO;
import com.example.chatApp.domain.User;
import com.example.chatApp.gui.SceneController;
import com.example.chatApp.gui.utils.UtilsGui;
import com.example.chatApp.gui.utils.events.FriendshipEntityChangeEvent;
import com.example.chatApp.gui.utils.observer.Observer;
import com.example.chatApp.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestsPageController extends AbstractController implements Observer<FriendshipEntityChangeEvent> {
    @FXML
    TableView<FriendshipDTO> friendshipsTableView;
    @FXML
    Button homeButtonId, acceptButtonId, declineButtonId;
    @FXML
    Text titleTextId;
    @FXML
    TableColumn<FriendshipDTO, String> senderColumnName, receiverColumnName, statusColumnId, requestDateColumnId;
    User currentUser;
    ObservableList<FriendshipDTO> model = FXCollections.observableArrayList();

    @Override
    public void setService(Service srv) {
        this.srv = srv;
        this.srv.addObserver(this, "friendship");
        getUserData();
        initModel();
    }

    public void getUserData() {
        try {
            currentUser = (User) myStage.getUserData();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void setClient(Client client) {
        this.client = client;
    }
    @Override
    public void setStage(Stage stage) {
        this.myStage = stage;
    }

    private void initModel() {
        Iterable<Friendship> friendships = srv.getFriendships();
        List<Friendship> friendshipsList = StreamSupport.stream(friendships.spliterator(), false)
                .collect(Collectors.toList());
        List<FriendshipDTO> filteredFriendshipsList = new ArrayList<>();
        for(Friendship friendship : friendshipsList) {
            if(friendship.getUserId2().equals(currentUser.getId()) && friendship.getStatus().equals("pending")) {
                filteredFriendshipsList.add(srv.getFriendshipDTO(friendship));
            }
            else if(friendship.getUserId1().equals(currentUser.getId()) && friendship.getStatus().equals("pending")) {
                filteredFriendshipsList.add(srv.getFriendshipDTO(friendship));
            }
        }

        model.setAll(filteredFriendshipsList);
    }

    @Override
    public void update(String s) {
        initModel();
    }

    @FXML
    public void initialize() {
        try {
            senderColumnName.setCellValueFactory(new PropertyValueFactory<>("User1Name"));
            receiverColumnName.setCellValueFactory(new PropertyValueFactory<>("User2Name"));
            statusColumnId.setCellValueFactory(new PropertyValueFactory<>("Status"));
            requestDateColumnId.setCellValueFactory(new PropertyValueFactory<>("RequestDateString"));
            friendshipsTableView.setItems(model);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void goToHomePage(ActionEvent event) throws IOException {
        try {
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

    @FXML
    public void acceptFriendRequest(ActionEvent event) {
        try{
            FriendshipDTO friendship = friendshipsTableView.getSelectionModel().getSelectedItem();
            if(friendship != null) {
                if(!friendship.getUserId1().equals(currentUser.getId()) && friendship.getStatus().equals("pending")) {
                    srv.updateFriendship(friendship.getId(),
                            friendship.getUserId1(),
                            friendship.getUserId2(),
                            LocalDateTime.now(),
                            "accepted",
                            friendship.getRequestDate());
                }
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void declineFriendRequest(ActionEvent event) {
        try{
            FriendshipDTO friendship = friendshipsTableView.getSelectionModel().getSelectedItem();
            if(friendship != null) {
                if(friendship.getStatus().equals("pending"))
                    srv.removeFriendship(friendship.getUserId1(), friendship.getUserId2());
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
