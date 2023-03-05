package com.example.chatApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

class ClientHandler implements Runnable
{
    Scanner scn = new Scanner(System.in);
    private final String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    String room = "";
    Socket s;
    boolean isloggedin;

    public ClientHandler(Socket s, String name,
                         DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }

    @Override
    public void run() {

        String user1, user2, msg, typeOfCommand;
        while (true)
        {
            try
            {
                // receive the string
                typeOfCommand = dis.readUTF();
                if(typeOfCommand.equals("enterRoom")) {
                    room = dis.readUTF();
                }
                else if(typeOfCommand.equals("leaveRoom")) {
                    room = "";
                }
                else if(typeOfCommand.equals("stopThread")) {
                    this.dos.writeUTF(typeOfCommand);
                }
                else if(typeOfCommand.equals("logout")){
                    this.dos.writeUTF(typeOfCommand);
                    this.isloggedin=false;
                    Server.ar.remove(this);
                    this.dis.close();
                    this.dos.close();
                    this.s.close();
                    break;
                }
                else if(typeOfCommand.equals("message")) {
                    user1 = dis.readUTF();
                    user2 = dis.readUTF();
                    msg = dis.readUTF();
                    
                    for (ClientHandler mc : Server.ar)
                    {
                        if(mc.isloggedin && mc.name.equals(this.name)) {
                            mc.dos.writeUTF("message");
                            mc.dos.writeUTF(this.name);
                            mc.dos.writeUTF(msg);
                        }
                        else if(mc.isloggedin && mc.name.equals(user2) && mc.room.equals(this.name)){
                            mc.dos.writeUTF("message");
                            mc.dos.writeUTF(this.name);
                            mc.dos.writeUTF(msg);
                        }
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try
        {
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}