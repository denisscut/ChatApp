package com.example.chatApp;

import com.example.chatApp.controller.ChatPageController;
import javafx.application.Platform;
import java.io.*;
import java.net.*;

public class Client
{
    final static int ServerPort = 1234;
    String name;
    private Socket s = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private Thread readMessage;
    boolean shutdown;
    public Client(String name) throws UnknownHostException, IOException{
        InetAddress ip = InetAddress.getByName("localhost");
        s = new Socket(ip, ServerPort);

        dis = new DataInputStream(s.getInputStream());
        dos = new DataOutputStream(s.getOutputStream());
        dos.writeUTF(name);

        this.name = name;
    }

    public void sendMessage(String typeOfCommand, String name1, String name2, String msg) throws IOException {
        try {
            if(typeOfCommand.equals("enterRoom")) {
                dos.writeUTF(typeOfCommand);
                dos.writeUTF(name1);
            }
            else if(typeOfCommand.equals("leaveRoom"))
                dos.writeUTF(typeOfCommand);
            else if(typeOfCommand.equals("logout"))
                dos.writeUTF(typeOfCommand);
            else if(typeOfCommand.equals("stopThread"))
                dos.writeUTF(typeOfCommand);
            else if(typeOfCommand.equals("message")){
                dos.writeUTF(typeOfCommand);
                dos.writeUTF(name1);
                dos.writeUTF(name2);
                dos.writeUTF(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessages(ChatPageController.callback callback) {
        shutdown = false;
        readMessage = new Thread(() -> {
            while (!shutdown) {
                try {
                    String typeOfCommand = dis.readUTF();
                    if(typeOfCommand.equals("message")) {
                        String user = dis.readUTF();
                        String msg = dis.readUTF();
                        Platform.runLater(() -> callback.updateChat(user, msg));
                    }
                    else if(typeOfCommand.equals("logout"))
                        break;
                    else if(typeOfCommand.equals("stopThread"))
                        break;
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            System.out.println("S-A INCHIS");
        });
        readMessage.start();
    }

    public void stopReadMessages() throws IOException {
        sendMessage("stopThread", "", "", "");
    }

    public void disconnectFromServer() throws IOException {
        sendMessage("logout", "", "", "");
    }
}
