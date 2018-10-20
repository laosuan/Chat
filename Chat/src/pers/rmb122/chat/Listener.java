package pers.rmb122.chat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import pers.rmb122.chat.utils.Message;
import pers.rmb122.chat.utils.Net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Listener extends Thread { // 监听 Socket
    public String currSelected;
    private Socket sck;
    private ArrayList<Parent> parentPtr;
    private HashMap<String, ArrayList<String>> chatHistory;
    private String username;

    public Listener(Socket sck, String username, ArrayList parentPtr, HashMap<String, ArrayList<String>> chatHistory) {
        this.sck = sck;
        this.parentPtr = parentPtr;
        this.chatHistory = chatHistory;
        this.username = username;
    }

    @Override
    public void run() {
        try {
            listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() throws IOException {
        while (true) {
            Message mess = Net.recv(sck);
            switch (mess.type) { // 作为客户端只可能收到这三种类型的消息
                case broadcast:
                    handleBroadcast(mess);
                    break;
                case chat:
                    handleChat(mess);
                    break;
                case server:
                    handleServer(mess);
                    break;
                case list:
                    handleList(mess);
                    break;
                default:
                    break;
            }
        }
    }

    public void refreshHistory() {
        ListView<String> listView = (ListView) parentPtr.get(0).lookup("#list_history");
        if (chatHistory.get(currSelected) != null) {
            Platform.runLater(() -> { // runLater 保持线程同步
                listView.setItems(null);
                listView.setItems(FXCollections.observableList(chatHistory.get(currSelected)));
                listView.refresh();
                listView.scrollTo(listView.getItems().size() - 1);
            });
        }
    }

    public void sendList() throws IOException {
        Message m = new Message(Message.MessType.list, this.username, "");
        Net.send(sck, m);
    }

    public void addMessage(String username, String message) throws IOException {
        if (!this.chatHistory.containsKey(username)) {
            chatHistory.put(username, new ArrayList<>());
            sendList(); // 如果没有，请求刷新列表
        }
        ArrayList history = chatHistory.get(username);
        history.add(message);
        refreshHistory();
    }

    public void handleBroadcast(Message mess) throws IOException {
        addMessage("<Public Chat>", mess.username + ":");
        addMessage("<Public Chat>", mess.payload);
    }

    public void handleChat(Message mess) throws IOException {
        addMessage(mess.username, mess.username + ":");
        addMessage(mess.username, mess.payload);
    }

    public void handleServer(Message mess) throws IOException {
        addMessage("<Public Chat>", "<Server>:");
        addMessage("<Public Chat>", mess.payload);
    }

    public void handleList(Message mess) {
        Gson gson = new Gson();
        List<String> userList = gson.fromJson(mess.payload, new TypeToken<List<String>>() {
        }.getType()); // 将 json 反序列化到 List
        Platform.runLater(() -> {
            ListView<String> listView = (ListView) parentPtr.get(0).lookup("#list_users");
            userList.add(0, "<Public Chat>");
            listView.setItems(FXCollections.observableList(userList));
        });
    }
}