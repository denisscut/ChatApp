package com.example.chatApp;

import com.example.chatApp.domain.Friendship;
import com.example.chatApp.domain.Message;
import com.example.chatApp.domain.User;
import com.example.chatApp.domain.validators.UserValidator;
import com.example.chatApp.domain.validators.Validator;
import com.example.chatApp.gui.MainView;
import com.example.chatApp.repository.Repository;
import com.example.chatApp.repository.database.FriendshipDbRepository;
import com.example.chatApp.repository.database.MessageDbRepository;
import com.example.chatApp.repository.database.UserDbRepository;
import com.example.chatApp.service.Service;

public class Main {
    public static void main(String[] args) {
        Validator<User> val = new UserValidator();
        Repository<Long, User> repoUsers = new UserDbRepository("", "", "");
        Repository<Long, Friendship>repoFriendship = new FriendshipDbRepository("", "", "");
        Repository<Long, Message>repoMessage = new MessageDbRepository("", "", "");
        MainView.srv = new Service(repoUsers, repoFriendship, repoMessage, val);
        MainView.main(args);
    }
}
