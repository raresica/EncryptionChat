package com.rc.chat.server;

import com.google.gson.Gson;
import com.rc.chat.message.Encryption;
import com.rc.chat.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread {
    private Socket socket;
    private Server server;

    public ServerThread(Socket socket, Server server){

        this.socket = socket;
        this.server = server;
    }

    public void run(){
        System.out.println(String.format("[SERVER] Client Connected with ip: %s", socket.getInetAddress()));
        System.out.println("Waiting for client`s message!");

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String tempString = null;
        try {
            tempString = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message messageFromJson, messageToJson;
        Scanner sc = new Scanner(System.in);
        String serverMessage = null, messageToJsonString;

        messageFromJson = new Gson().fromJson(new Encryption().decryptMessage(tempString), Message.class);

        while(tempString != null && !messageFromJson.getMessage().equals("done")) {

            //System.out.println(String.format("Clients`s message before deserialization: %s", tempString));
            System.out.println(String.format("[CLIENT] Client sent the message: %s", messageFromJson));

            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                server.broadcast(new Encryption().encryptMessage(new Gson().toJson(messageFromJson)), this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Waiting for client`s message!");

            try {
                inputStreamReader = new InputStreamReader(socket.getInputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            bufferedReader = new BufferedReader(inputStreamReader);
            try {
                tempString = bufferedReader.readLine();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            messageFromJson = new Gson().fromJson(new Encryption().decryptMessage(tempString), Message.class);
        }

        BufferedReader bufferedReaderTest = new BufferedReader(inputStreamReader);
        String tempStringTest = null;
        try {
            tempStringTest = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println(String.format("[SERVER] Client with ip address: %s disconnected!", socket.getInetAddress()));

        }
        System.exit(0);
    }

    public void sendMessage(String message) throws IOException {
        PrintWriter printWriter = new PrintWriter((socket.getOutputStream()));
        printWriter.println(message);
        printWriter.flush();
    }
}
