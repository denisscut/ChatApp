package com.example.chatApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    static Vector<ClientHandler> ar = new Vector<>();
    static int i = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(1234);
        Socket s;
        while (true) {
            s = ss.accept();

            System.out.println("New client request received : \n" + s);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Creating a new handler for this client...\n");
            String name = dis.readUTF();
            ClientHandler mtch = new ClientHandler(s, name, dis, dos);
            Thread t = new Thread(mtch);

            System.out.printf("Adding %s to active client list\n", name);
            ar.add(mtch);
            t.start();
            i++;
        }
    }
}
