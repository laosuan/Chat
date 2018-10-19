package pers.rmb122.chatServer;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

import com.google.gson.Gson;

import pers.rmb122.chatServer.utils.Message;
import pers.rmb122.chatServer.utils.Message.MessType;
import pers.rmb122.chatServer.utils.Net;

public class Manager extends Thread {
    private Socket sck;
    private String username;
    private HashMap<String, User> users;

    public Manager(Socket sck, HashMap<String, User> users) {
        this.sck = sck;
        this.users = users;
        this.username = ""; // 这个 Manager 所管理的用户名
    }

    @Override
    public void run() {
        try {
            handle();
        } catch (Exception e) {
            e.printStackTrace();
            if (username != "" && users.containsKey(username)) { //避免重复删除
                users.remove(username); // 删除此用户
                System.out.println(">>> " + username + " has been offline.");
            }
        }
    }

    public boolean registerUser() throws IOException {
        Message mess = Net.recv(sck);
        if (mess.type == Message.MessType.register) {
            String username = mess.payload;
            this.username = username;
            if (users.containsKey(username) || username == "" || username.equals("<Public Chat>")) { // 检测是否已经存在此用户或者用户名不合法
                return false;
            } else {
                System.out.println(">>> " + username + " has been online.");
                users.put(username, new User(sck, username));
                return true;
            }
        }
        return false;
    }

    public void handle() throws IOException {
        if (!registerUser()) {
            Net.send(sck,
                    new Message(Message.MessType.server, "", "Login failed: this username already used or invaild."));
            sck.close();
            return;
        } else {
            Net.send(sck, new Message(Message.MessType.server, "", "Login success. Hello! " + username + "."));
        }

        while (true) {
            Message mess = Net.recv(sck);
            switch (mess.type) {
            case broadcast:
                handleBroadcast(mess);
                break;
            case chat:
                handleChat(mess);
                break;
            case list:
                handleList(mess.username);
                break;
            case alive:
                handleAlive();
                break;
            default:
                Net.send(sck, new Message(Message.MessType.server, "", "Unexpect message."));
                break;
            }
        }
    }

    public void handleBroadcast(Message mess) throws IOException {
        Message newMess = new Message(MessType.broadcast, username, mess.payload);
        for (String key : users.keySet()) { // 向除了发送方的每个用户发送消息
            if (key != username) {
                Socket sck = users.get(key).sck;
                Net.send(sck, newMess);
            }
        }
    }

    public void handleChat(Message mess) throws IOException {
        Message newMess = new Message(MessType.chat, username, mess.payload); // 向目标发送消息
        if (!users.containsKey(mess.username)) { // 有可能这个用户不存在
            Net.send(this.sck, new Message(MessType.server, "", "Send fail, this user is not exists."));
        } else {
            Net.send(users.get(mess.username).sck, newMess);
        }
    }

    public void handleList(String sender) throws IOException {
        Set<String> tmp = new HashSet<>(users.keySet());
        tmp.remove(sender);
        List<String> userList = new ArrayList<>(tmp); // 将 set 转换为 list, 并去除发送者
        Gson gson = new Gson();
        String payload = gson.toJson(userList); // 序列化为 json 后传递
        Net.send(sck, new Message(MessType.list, "", payload));
    }

    public void handleAlive() {
        this.users.get(this.username).isAlive = true;
    }
}