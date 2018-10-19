package pers.rmb122.chat;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pers.rmb122.chat.utils.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainController {
    ArrayList<Parent> parentPtr = new ArrayList<>(); // 因为初始化的关系，需要在启动时把 parent 传给 Client 只能用这种丑陋的办法了
    Client client;
    HashMap<String, ArrayList<String>> chatHistory = new HashMap<>();

    @FXML
    private TextArea text_input;
    @FXML
    private ListView<String> list_history;
    @FXML
    private ListView<String> list_users;

    @FXML
    public void initialize() throws IOException {
        Config config = new Config();
        config.loadConfig();

        this.client = new Client(config, parentPtr, chatHistory);
        this.client.start();

        // 点击列表内物品后切换到对应的记录
        list_users.setOnMouseClicked(event -> {
            String selectedName = list_users.getSelectionModel().getSelectedItem();
            if(!chatHistory.containsKey(selectedName)) {
                chatHistory.put(selectedName, new ArrayList<>());
            }
            this.client.listener.currSelected = selectedName;
            list_history.setItems(FXCollections.observableList(chatHistory.get(selectedName)));
        });
    }

    @FXML
    public void send() throws IOException {
        if(text_input.getText().equals("")) {
            return;
        }

        String target = list_users.getSelectionModel().getSelectedItem();
        if(target == null) {
            target = "<Public Chat>";
        }
        if(target.equals("<Public Chat>")) {
            this.client.sendBroadcast(text_input.getText());
        } else {
            this.client.sendChat(target, text_input.getText());
        }
        if(!chatHistory.containsKey(target)) {
            chatHistory.put(target, new ArrayList<>());
        }
        list_history.setItems(FXCollections.observableList(chatHistory.get(target)));

        if(chatHistory.containsKey(target)) {
            chatHistory.get(target).add("<You>:");
            chatHistory.get(target).add(text_input.getText());
        }
        text_input.setText("");
    }

    @FXML
    public void refresh() throws IOException {
        this.client.sendList();
    }

    @FXML
    public void exit() {
        Stage s = (Stage) this.parentPtr.get(0).getScene().getWindow();
        s.close();
    }
}
