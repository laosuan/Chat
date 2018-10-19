package pers.rmb122.chat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pers.rmb122.chat.utils.Config;

import java.io.IOException;
import java.util.ArrayList;

public class ConfigController {
    @FXML
    private Button btn_save;
    @FXML
    private TextField text_ip;
    @FXML
    private TextField text_username;
    @FXML
    private TextField text_port;

    public void setFixedSize(Stage primaryStage, double width, double hight) {
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(hight);
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(hight);
    }

    @FXML
    public void onButtonClick() throws IOException {
        Config config = new Config();
        String ip = text_ip.getText();
        String username = text_username.getText();
        String port = text_port.getText();
        config.remoteIp = ip;
        config.username = username;
        config.remotePort = Integer.parseInt(port);
        config.saveConfig();
        System.out.println("Config saved");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent mainWindow = loader.load();
        MainController controller = loader.getController();
        ArrayList<Parent> parentPtr = controller.parentPtr; //将 mainWindow 添加进 parentPtr
        parentPtr.add(mainWindow);
        Stage mainStage = new Stage();
        mainStage.setTitle("Chat");
        mainStage.setScene(new Scene(mainWindow, 900, 700));
        mainStage.show();
        setFixedSize(mainStage, 670, 547); //启动主窗口

        Stage stage = (Stage) btn_save.getScene().getWindow(); //关闭配置窗口，也就是本窗口
        stage.close();
    }
}
