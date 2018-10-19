package pers.rmb122.chat.utils;

public class Message {
    public MessType type;
    public String username;
    public String payload;
    public Message(MessType type, String username, String payload) {
        this.type = type;
        this.username = username;
        this.payload = payload;
    }

    public enum MessType {
        broadcast, register, chat, server, list, alive;
    }
}
