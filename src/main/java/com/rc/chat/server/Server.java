package com.rc.chat.server;


import com.rc.chat.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static String serverIp = "127.0.0.1";
    private final ServerSocket ss = new ServerSocket(5001);
    private List<ServerThread> serverThreads = new ArrayList<>();

    public Server() throws IOException {
    }

    public static void main(String[] args) throws IOException{
        System.out.println(String.format("[SERVER] Listening for socket connections on: %s:5001", serverIp));
        new Server().execute();
    }

    public void execute() throws IOException {
        while (true) {
            Socket s = ss.accept();
            ServerThread serverThread = new ServerThread(s, this);
            serverThreads.add(serverThread);
            serverThread.start();
        }
    }

    public void broadcast(String message, ServerThread exclude) throws IOException {
        for(ServerThread serverThread: serverThreads){
            if(serverThread != exclude){
                serverThread.sendMessage(message);
            }
        }
    }
}
