package pers.rmb122.chatServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import pers.rmb122.chatServer.utils.Config;

public class Server {
    private HashMap<String, User> users = new HashMap<String, User>();
    private ServerSocket mainSck = null;
    public int port = 0;

    public Server(Config config) {
        this.port = config.port;
    }

    public void start() {
        try {
            AliveMonitor am = new AliveMonitor(users);
            am.setDaemon(true);
            am.start(); // 开启心跳检测器
            mainSck = new ServerSocket(port);
            while (true) {
                Socket subSck = mainSck.accept(); // 对每个用户开启一个线程管理
                Manager m = new Manager(subSck, users);
                m.setDaemon(true);
                m.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mainSck != null) { // 关闭资源
                try {
                    mainSck.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
