package com.example.chatApp.service;

import com.example.chatApp.domain.*;
import com.example.chatApp.domain.validators.DuplicateException;
import com.example.chatApp.domain.validators.NonExistentEntityException;
import com.example.chatApp.domain.validators.Validator;
import com.example.chatApp.gui.utils.events.ChangeEventType;
import com.example.chatApp.gui.utils.events.Event;
import com.example.chatApp.gui.utils.events.FriendshipEntityChangeEvent;
import com.example.chatApp.gui.utils.events.UserEntityChangeEvent;
import com.example.chatApp.gui.utils.observer.Observable;
import com.example.chatApp.gui.utils.observer.Observer;
import com.example.chatApp.repository.Repository;
import java.time.LocalDateTime;
import java.util.*;

public class Service implements Observable<Event>{
    Repository<Long, User> repoUser;
    Repository<Long, Friendship> repoFriendship;
    Repository<Long, Message> repoMessage;
    private List<Observer<UserEntityChangeEvent>> observersUsers=new ArrayList<>();
    private List<Observer<FriendshipEntityChangeEvent>> observersFriendships=new ArrayList<>();
    Validator<User> validator;
    Long userId, friendshipId;

    public Service(Repository<Long, User> repoUser, Repository<Long, Friendship> repoFriendship, Repository<Long, Message> repoMessage, Validator<User> validator) {
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;
        this.repoMessage = repoMessage;
        this.validator = validator;
        initializeUserId();
        initializeFriendshipId();
    }

    public Iterable<Message> getMessages() {
        return repoMessage.findAll();
    }

    public void addMessage(Long user1Id,Long user2Id,String message,LocalDateTime date) {
        repoMessage.save(new Message(user1Id, user2Id, message, date));
    }

    public Iterable<User> getUsersFriends(Long userId) {
        Iterable<Friendship> friendships = repoFriendship.findAll();
        Set<User> users = new HashSet<>();
        for(Friendship friendship: friendships) {
            if(friendship.getUserId1().equals(userId) && friendship.getStatus().equals("accepted")) {
                users.add(repoUser.findOne(friendship.getUserId2()));
            }
            else if(friendship.getUserId2().equals(userId) && friendship.getStatus().equals("accepted")) {
                users.add(repoUser.findOne(friendship.getUserId1()));
            }
        }
        return users;
    }

    @Override
    public void addObserver(Observer e, String str) {
        if(str.equals("friendship"))
            observersFriendships.add(e);
        else if(str.equals("user"))
            observersUsers.add(e);
    }

    @Override
    public void removeObserver(Observer e, String str) {
        if(str.equals("friendship"))
            observersFriendships.remove(e);
        else if(str.equals("user"))
            observersUsers.remove(e);
    }

    @Override
    public void notifyObservers(Event t, String str) {
        if(str.equals("friendship"))
            observersFriendships.stream().forEach(x->x.update(str));
        else if(str.equals("user"))
            observersUsers.stream().forEach(x->x.update(str));
    }

    public void initializeUserId() {
        userId = 0l;
        for(User user: repoUser.findAll())
            if(userId < user.getId())
                userId = user.getId();
    }

    public void initializeFriendshipId() {
        friendshipId = 0l;
        for(Friendship friendship: repoFriendship.findAll())
            if(friendshipId < friendship.getId())
                friendshipId = friendship.getId();
    }

    public void addUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        ++userId;
        user.setId(userId);
        this.validator.validate(user);
        this.repoUser.save(user);
    }

    public void updateUser(Long userId, String updatedFristName, String updatedLastName) {
        User user = repoUser.findOne(userId);
        validator.validate(user);
        user.setFirstName(updatedFristName);
        user.setLastName(updatedLastName);
        User updatedUser = this.repoUser.update(user);
        validator.validate(updatedUser);
    }

    public void updateFriendship(Long friendshipId, Long userId1, Long userId2, LocalDateTime friendsFrom, String status, LocalDateTime requestDate) {
        Friendship friendship = repoFriendship.findOne(friendshipId);
        if(friendship == null)
            throw new NonExistentEntityException("Id-ul este invalid.");

        friendship.setUserId1(userId1);
        friendship.setUserId2(userId2);
        friendship.setFriendsFrom(friendsFrom);
        friendship.setStatus(status);
        friendship.setRequestDate(requestDate);
        Friendship updatedFriendship = this.repoFriendship.update(friendship);
        notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.UPDATE, friendship), "friendship");
    }

    public void removeUser(Long id) {
        User deletedUser = repoUser.findOne(id);
        validator.validate(deletedUser);
        this.repoUser.delete(id);
        deleteFriendships(deletedUser);
    }

    public void deleteFriendships(User deletedUser) {
        for(Friendship friendship: repoFriendship.findAll()) {
            if(friendship.isUserPartOfTheFriendship(deletedUser.getId()))
                repoFriendship.delete(friendship.getId());
        }
    }

    public void addFriendship(Long id1, Long id2) {
        User user1 = repoUser.findOne(id1);
        User user2 = repoUser.findOne(id2);

        if(user1 == null || user2 == null)
            throw new NonExistentEntityException("The users doesn't exist.");

        boolean exist = false;
        for (Friendship friendship : repoFriendship.findAll()) {
            if (friendship.getUserId1().equals(id1) && friendship.getUserId2().equals(id2))
                throw new DuplicateException("This friendship already exist.");
            else if (friendship.getUserId1().equals(id2) && friendship.getUserId2().equals(id1))
                throw new DuplicateException("This friendship already exist.");
        }

        if (!exist){
            LocalDateTime friendsFrom = LocalDateTime.now();
            LocalDateTime requestDate = LocalDateTime.now();
            String status = "pending";
            ++friendshipId;
            Friendship friendship = new Friendship(id1, id2,status, friendsFrom, requestDate);
            friendship.setId(friendshipId);
            repoFriendship.save(friendship);
        }
    }

    public void removeFriendship(Long id1, Long id2) {
        User user1 = repoUser.findOne(id1);
        User user2 = repoUser.findOne(id2);

        if(user1 == null || user2 == null)
            throw new NonExistentEntityException("The users doesn't exist.");

        Long friendshipId = -1l;
        for(Friendship friendship: repoFriendship.findAll())
            if(friendship.getUserId1().equals(id1) && friendship.getUserId2().equals(id2)) {
                friendshipId = friendship.getId();
                break;
            }
            else if(friendship.getUserId1().equals(id2) && friendship.getUserId2().equals(id1)) {
                friendshipId = friendship.getId();
                break;
            }
        Friendship deletedFriendship;
        if(friendshipId != -1)
            deletedFriendship = repoFriendship.delete(friendshipId);
        else
            throw new NonExistentEntityException("The friendship doesn't exist.");
        notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.DELETE, deletedFriendship), "friendship");
    }

    public int getNumberOfConnectedComponents() {
        Long numberOfUsers = 0L;
        for(User user: getUsers())
            if(numberOfUsers < user.getId())
                numberOfUsers = user.getId();
        Graph G = new Graph(Math.toIntExact(numberOfUsers), getFriendships(), getUsers());
        return G.ConnectedComponents();
    }

    public Iterable<User> getBiggestCommunity() {
        Long numberOfUsers = 0L;
        for(User user: getUsers())
            if(numberOfUsers < user.getId())
                numberOfUsers = user.getId();
        Graph G = new Graph(Math.toIntExact(numberOfUsers), getFriendships(), getUsers());
        Iterable<Long> biggestCommunity = G.BiggestComponent();
        ArrayList<User> usersFromBiggestCommunity = new ArrayList<>();
        for(Long id : biggestCommunity) {
            usersFromBiggestCommunity.add(repoUser.findOne(id));
        }
        return usersFromBiggestCommunity;
    }

    public FriendshipDTO getFriendshipDTO(Friendship friendship) {
        return new FriendshipDTO(
                friendship.getId(),
                friendship.getUserId1(),
                friendship.getUserId2(),
                friendship.getStatus(),
                friendship.getFriendsFrom(),
                friendship.getRequestDate(),
                repoUser.findOne(friendship.getUserId1()),
                repoUser.findOne(friendship.getUserId2()));
    }

    public Iterable<User> getUsers() {
        return repoUser.findAll();
    }

    public Iterable<User> getUsersExceptMe(Long id) {
        Iterable<User> users = repoUser.findAll();
        Set<User> usersWithoutMe = new HashSet<>();
        for(User user: users) {
            if(!user.getId().equals(id))
                usersWithoutMe.add(user);
        }
        return usersWithoutMe;
    }

    public boolean checkUser(String email, String password) {
        Iterable<User> users = getUsers();
        for(User user : users) {
            if(user.getEmail().equals(email) && user.getPassword().equals(password))
                return true;
        }
        return false;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        Iterable<User> users = getUsers();
        for(User user : users) {
            if(user.getEmail().equals(email) && user.getPassword().equals(password))
                return user;
        }
        return null;
    }

    public Iterable<Friendship> getFriendships() {
        return repoFriendship.findAll();
    }
}
