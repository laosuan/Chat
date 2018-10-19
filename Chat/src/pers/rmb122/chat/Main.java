package pers.rmb122.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pers.rmb122.chat.utils.Config;

import java.util.ArrayList;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void setFixedSize(Stage primaryStage, double width, double hight) {
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(hight);
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(hight);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Config config = new Config();

        if (!config.isConfigExists()) {
            Parent configWindow = FXMLLoader.load(getClass().getResource("ConfigWindow.fxml"));
            primaryStage.setTitle("Config");
            primaryStage.setScene(new Scene(configWindow, 315, 240));
            setFixedSize(primaryStage, 315, 240);
            primaryStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent mainWindow = loader.load();
            MainController controller = loader.getController();
            ArrayList<Parent> parentPtr = controller.parentPtr; //将 mainWindow 添加进 parentPtr
            parentPtr.add(mainWindow);
            //Parent mainWindow = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
            primaryStage.setTitle("Chat");
            primaryStage.setScene(new Scene(mainWindow, 670, 547));
            setFixedSize(primaryStage, 670, 547);
            primaryStage.show();
        }
    }
}

