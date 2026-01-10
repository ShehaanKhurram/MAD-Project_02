package com.example.version;
public class Message {
    private String senderId;
    private String senderName;
    private String messageText;
    private long timestamp;

    // Required empty constructor for Firebase
    public Message() {}

    public Message(String senderId, String senderName, String messageText, long timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public String getMessageText() { return messageText; }
    public long getTimestamp() { return timestamp; }
}