package com.rc.chat.message;

public class Encryption {
    private final String encryptionMessage = "encryptionMessage";
    private final int encryptionLength = encryptionMessage.length();

    public String encryptMessage(String message){
        int tempLength = message.length();
        String newMessage = encryptionMessage;
        for(int i = 0; i < tempLength; i++){
            newMessage = newMessage.concat(String.valueOf((char) (message.charAt(i) + 1)));
        }
        return newMessage;
    }

    public String decryptMessage(String message){
        int tempLength = message.length();
        String newMessage = "";
        for(int i = encryptionLength; i < tempLength; i++){
            newMessage = newMessage.concat(String.valueOf((char) (message.charAt(i) - 1)));
        }
        return newMessage;
    }
}