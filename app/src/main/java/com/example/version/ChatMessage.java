package com.example.version;

public class ChatMessage {
    public static final String SENT_BY_USER = "user";
    public static final String SENT_BY_BOT = "bot";

    String message;
    String sentBy;

    public ChatMessage(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }
    public String getSentBy() {
        return sentBy;
    }
}