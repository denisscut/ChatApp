package com.example.chatApp.controller;

import com.example.chatApp.Client;
import com.example.chatApp.domain.Friendship;
import com.example.chatApp.domain.FriendshipDTO;
import com.example.chatApp.domain.User;
import com.example.chatApp.gui.SceneController;
import com.example.chatApp.gui.utils.UtilsGui;
import com.example.chatApp.gui.utils.events.UserEntityChangeEvent;
import com.example.chatApp.gui.utils.observer.Observer;
import com.example.chatApp.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomePageController extends AbstractController implements Observer<UserEntityChangeEvent> {

    @FXML
    Text messageText;
    @FXML
    TextField searchBar;
    @FXML
    Text nameText;
    @FXML
    AnchorPane anchorPaneId;
    @FXML
    Button logoutButton, addFriendButton, friendRequestButton, deleteFriendButton, openChatButton;
    ObservableList<User> model = FXCollections.observableArrayList();
    ObservableList<FriendshipDTO> modelFriends = FXCollections.observableArrayList();
    @FXML
    TableView<User> usersTableView;
    @FXML
    TableColumn<User, String> firstNameColumn;
    @FXML
    TableColumn<User, String> lastNameColumn;
    @FXML
    TableView<FriendshipDTO> friendsTableView;
    @FXML
    TableColumn<FriendshipDTO, String> friendsFromColumn, nameFriendColumn;
    User currentUser;

    public void setService(Service srv) {
        this.srv = srv;
        this.srv.addObserver(this, "user");
        this.srv.addObserver(this, "friendship");
        getUserData();
        initSearchBar();
        initModelFriends();
        initModel();
    }

    @Override
    public void setStage(Stage stage) {
        this.myStage = stage;
    }

    private void initModel() {
        //Iterable<User> users = srv.getUsersExceptMe(currentUser.getId());
        //List<User> usersList = StreamSupport.stream(users.spliterator(), false)
        //        .collect(Collectors.toList());
        //model.setAll(usersList);
    }
    private void initSearchBar() {
        searchBar.textProperty().addListener((ov, oldV, newV) -> {
            searchEvent();
        });
    }
    private void initModelFriends() {
        Iterable<Friendship> friendships = srv.getFriendships();
        List<Friendship> friendshipsList = StreamSupport.stream(friendships.spliterator(), false)
                .collect(Collectors.toList());
        List<FriendshipDTO> filteredFriendshipsList = new ArrayList<>();
        for(Friendship friendship : friendshipsList) {
            if(friendship.getUserId1().equals(currentUser.getId()) && friendship.getStatus().equals("accepted")) {
                filteredFriendshipsList.add(srv.getFriendshipDTO(friendship));
            }
            else if(friendship.getUserId2().equals(currentUser.getId()) && friendship.getStatus().equals("accepted")) {
                friendship.setUserId2(friendship.getUserId1());
                friendship.setUserId1(currentUser.getId());
                filteredFriendshipsList.add(srv.getFriendshipDTO(friendship));
            }
        }
        modelFriends.setAll(filteredFriendshipsList);
    }

    void searchEvent() {
        List<User> users = new ArrayList<>();
        for(User user: srv.getUsersExceptMe(currentUser.getId())) {
            users.add(user);
        }
        usersTableView.getItems().clear();
        if(searchBar.getText() != "")
            usersTableView.getItems().addAll(searchList(searchBar.getText(), users));
    }

    private List<User> searchList(String searchWords, List<User> listOfUsers) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfUsers.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.getName().trim().toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

    @Override
    public void update(String s) {
        if(s.equals("user"))
            initModel();
        else if(s.equals("friendship"))
            initModelFriends();
    }

    public void getUserData() {
        try {
            User user = (User) myStage.getUserData();
            nameText.setText(user.getName());
            currentUser = user;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void setClient(Client client) {
        this.client = client;
    }
    @FXML
    public void initialize() {
        try {
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("LastName"));
            usersTableView.setItems(model);

            nameFriendColumn.setCellValueFactory(new PropertyValueFactory<>("User2Name"));
            friendsFromColumn.setCellValueFactory(new PropertyValueFactory<>("FriendsFromString"));
            friendsTableView.setItems(modelFriends);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void addFriend(ActionEvent event) {
        try {
            User user = usersTableView.getSelectionModel().getSelectedItem();
            if(user != null) {
                srv.addFriendship(((User) myStage.getUserData()).getId(), user.getId());
                messageText.setText("The friend request was sent!");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void deleteFriend(ActionEvent event) {
        try {
            FriendshipDTO friendship = friendsTableView.getSelectionModel().getSelectedItem();
            if(friendship != null)
                srv.removeFriendship(friendship.getUserId1(), friendship.getUserId2());
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void openChat(ActionEvent event) {
        try {
            FriendshipDTO friendship = friendsTableView.getSelectionModel().getSelectedItem();
            if(friendship != null) {
                Long otherUser;
//                if(currentUser.getId().equals(friendship.getUserId1()))
//                    otherUser = friendship.getUserId2();
//                else
//                    otherUser = friendship.getUserId1();
                //if(chatRooms.get(otherUser) == null) {
                    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

                    List<User> users;
                    if(currentUser.getId().equals(friendship.getUserId1()))
                        users = Arrays.asList(currentUser, friendship.getUser2());
                    else
                        users = Arrays.asList(currentUser, friendship.getUser1());
                    stage.setUserData(users);
                    SceneController sceneAndController = UtilsGui.createScene("/chatPage.fxml", srv, client, getClass(), stage);
                    stage.setScene(sceneAndController.getScene());
                    stage.show();
                    ChatPageController controller = (ChatPageController) sceneAndController.getController();
                    controller.getUserData();
                //}
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        try {
            client.disconnectFromServer();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            SceneController sceneAndController = UtilsGui.createScene("/loginPage.fxml", srv, null, getClass(), stage);
            stage.setScene(sceneAndController.getScene());
            stage.show();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void goToFriendRequestsPage(ActionEvent event) throws IOException {
        try {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setUserData(currentUser);
            SceneController sceneAndController = UtilsGui.createScene("/friendRequestsPage.fxml", srv, client, getClass(), stage);
            stage.setScene(sceneAndController.getScene());
            stage.show();
            FriendRequestsPageController controller = (FriendRequestsPageController) sceneAndController.getController();
            controller.getUserData();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
