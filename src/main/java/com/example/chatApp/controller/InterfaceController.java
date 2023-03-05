package com.example.chatApp.controller;

import com.example.chatApp.service.Service;
import javafx.fxml.Initializable;

public interface InterfaceController extends Initializable {
    void setService(Service srv);
}
