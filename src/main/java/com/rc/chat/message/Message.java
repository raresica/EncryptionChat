package com.rc.chat.message;

import java.io.Serializable;
import java.net.InetAddress;

public class Message implements Serializable {
    private String username;
    private String message;
    private InetAddress ipAddress;

    public Message(){

    }

    public Message(String username, String message, InetAddress ipAddress){
        this.username = username;
        this.message = message;
        this.ipAddress = ipAddress;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username=" + username + ", " +
                "message=" + message + ", " +
                "ipAddress=" + ipAddress + ", " +
                '}';
    }
}
