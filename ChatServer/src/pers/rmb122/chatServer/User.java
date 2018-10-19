package pers.rmb122.chatServer;

import java.net.Socket;

public class User {
    public Socket sck;
    public String username;
    public boolean isAlive = true;

    public User(Socket sck, String username) {
        this.sck = sck;
        this.username = username;
    }
}