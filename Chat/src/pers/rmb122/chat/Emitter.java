package pers.rmb122.chat;

import pers.rmb122.chat.utils.Message;
import pers.rmb122.chat.utils.Message.MessType;
import pers.rmb122.chat.utils.Net;

import java.io.IOException;
import java.net.Socket;

public class Emitter extends Thread { // 定时发送心跳包
    private Socket sck;

    Emitter(Socket sck) {
        this.sck = sck;
    }

    @Override
    public void run() {
        try {
            emit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emit() throws IOException, InterruptedException {
        while (true) {
            Net.send(sck, new Message(MessType.alive, "", ""));
            Thread.sleep(3000); // 每3s发送一次心跳包
        }
    }
}