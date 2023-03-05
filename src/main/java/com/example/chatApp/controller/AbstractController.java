package com.example.chatApp.controller;

import com.example.chatApp.Client;
import com.example.chatApp.service.Service;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class AbstractController {
    protected Service srv;
    protected Stage myStage;
    protected Client client;
    public abstract void setService(Service srv) throws IOException;
    public abstract void setClient(Client client) throws IOException;
    public abstract void setStage(Stage stage);
}
