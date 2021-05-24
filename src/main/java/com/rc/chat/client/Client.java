package com.rc.chat.client;

import com.google.gson.Gson;
import com.rc.chat.message.Encryption;
import com.rc.chat.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5001);

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = sc.nextLine();
        System.out.println("Enter your message for the chat:");
        String tempMessage = sc.nextLine();

        Message message = new Message(username, tempMessage, socket.getInetAddress());
        String objectMessage = new Gson().toJson(message);
        String encryption = new Encryption().encryptMessage(objectMessage);


//        System.out.println(String.format("Client message for the server: %s", tempMessage));
//        System.out.println(String.format("Client object message for the server: %s", message));
//        System.out.println(String.format("Client message for the server after serialization: %s", objectMessage));

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(encryption);
        printWriter.flush();

        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bf = new BufferedReader(inputStreamReader);
        String str = bf.readLine();

        Message message1 = new Gson().fromJson(new Encryption().decryptMessage(str), Message.class);

        while(str != null && !message1.getMessage().equals("done")) {
//            System.out.println(String.format("[SERVER] %s", str));
            System.out.println("Enter your message for the chat:");
            tempMessage = sc.nextLine();

            message = new Message(username, tempMessage, socket.getInetAddress());
            objectMessage = new Gson().toJson(message);
            String encryptionTemp = new Encryption().encryptMessage(objectMessage);

//            System.out.println(String.format("Client message for the server: %s", tempMessage));
//            System.out.println(String.format("Client object message for the server: %s", message));
//            System.out.println(String.format("Client message for the server after serialization: %s", objectMessage));

            printWriter.println(encryptionTemp);
            printWriter.flush();

            inputStreamReader = new InputStreamReader(socket.getInputStream());
            bf = new BufferedReader(inputStreamReader);
            str = bf.readLine();

            message1 = new Gson().fromJson(new Encryption().decryptMessage(str), Message.class);
        }

        System.out.println("Connection ended!");
    }

}
