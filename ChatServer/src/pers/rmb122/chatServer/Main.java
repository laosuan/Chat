package pers.rmb122.chatServer;

import pers.rmb122.chatServer.utils.Config;

public class Main {
    public static void main(String[] args) {
        try {
            Config config = new Config();
            if (!config.isConfigExists()) { // 配置文件不存在, 重新生成
                config.generateConfig();
                config.saveConfig();
            } else {
                config.loadConfig();
            }

            System.out.println(">>> Server Started at " + String.valueOf(config.port) + ".");
            System.out.println("===================");
            Server s = new Server(config);
            s.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}