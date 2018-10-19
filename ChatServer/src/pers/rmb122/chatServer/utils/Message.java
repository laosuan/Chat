package pers.rmb122.chatServer.utils;

public class Message {
    public enum MessType {
        broadcast, register, chat, server, list, alive, undefined;
    }

    public MessType type;
    public String username;
    public String payload;

    public Message(MessType type, String username, String payload) {
        this.type = type;
        this.username = username;
        this.payload = payload;
    }

    public Message() {
        this.type = MessType.undefined;
        this.username = "";
        this.payload = "";
    }
}
