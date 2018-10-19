package pers.rmb122.chatServer;

import java.util.HashMap;

public class AliveMonitor extends Thread {
    private HashMap<String, User> users;

    AliveMonitor(HashMap<String, User> users) {
        this.users = users;
    }

    @Override
    public void run() {
        try {
            check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check() throws InterruptedException {
        while (true) {
            for (User user : users.values()) {
                if (user.isAlive) {
                    user.isAlive = false;
                } else { // 如果已经没有心跳, 关闭 Socket
                    users.remove(user.username);
                    try {
                        user.sck.close();
                    } catch (Exception e) {
                        System.out.println(user.username + "has been offline.");
                    }
                }
            }
            Thread.sleep(10000); // 每 10s 检测一次
        }
    }
}