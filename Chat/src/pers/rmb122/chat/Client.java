package pers.rmb122.chat;

import javafx.scene.Parent;
import pers.rmb122.chat.utils.Config;
import pers.rmb122.chat.utils.Message;
import pers.rmb122.chat.utils.Message.MessType;
import pers.rmb122.chat.utils.Net;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Client extends Thread {
    private int remotePort;
    private String remoteIp;
    private String username;
    private Socket sck;
    private ArrayList<Parent> parentPtr;
    private HashMap<String, ArrayList<String>> chatHistory;
    public Listener listener;
    private boolean DEBUG = false;

    public Client(Config config, ArrayList<Parent> parentPtr, HashMap<String, ArrayList<String>> chatHistory) {
        if (DEBUG) {
            this.username = getRandomString(10);
        } else {
            this.username = config.username;
        }
        this.remotePort = config.remotePort;
        this.remoteIp = config.remoteIp;
        this.parentPtr = parentPtr;
        this.chatHistory = chatHistory;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public void register() throws IOException {
        sck = new Socket(remoteIp, remotePort);
        Net.send(sck, new Message(Message.MessType.register, "", username));
    }

    public void sendBroadcast(String payload) throws IOException {
        Message m = new Message(MessType.broadcast, "", payload);
        Net.send(sck, m);
    }

    public void sendChat(String targetUser, String payload) throws IOException {
        Message m = new Message(MessType.chat, targetUser, payload);
        Net.send(sck, m);
    }

    public void sendList() throws IOException {
        Message m = new Message(MessType.list, this.username, "");
        Net.send(sck, m);
    }

    @Override
    public void run() {
        try {
            register();
            Emitter emitter = new Emitter(sck);
            this.listener = new Listener(sck,username, parentPtr, chatHistory);
            emitter.setDaemon(true);
            this.listener.setDaemon(true);
            emitter.start(); // 开始发送心跳
            this.listener.start(); // 开启监听
            sendList(); // 获得当前用户
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
