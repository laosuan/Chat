package pers.rmb122.chat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Net {
    public static Message recv(Socket sck) throws IOException {
        InputStream reader = sck.getInputStream();
        String content = "";
        byte[] byteLength = new byte[4]; // 读取包的长度
        for (int i = 0; i < 4; i++) {
            byteLength[i] = (byte) reader.read();
        }
        int length = Transfer.bytes2int(byteLength);
        if (length == -1) { // 如果长度为 -1, 对方已经终止了链接
            throw new IOException("Connection reseted.");
        }
        byte[] temp = new byte[length]; // 创建对应长度的字节
        int off = 0;
        while (length > 0) {
            int ret = reader.read(temp, off, length);
            off += ret;
            length -= ret;
        }
        content = new String(temp);
        Message m = Transfer.json2Message(content);
        return m;
    }

    public static void send(Socket sck, Message mess) throws IOException {
        String json = Transfer.Message2json(mess);
        OutputStream writer = sck.getOutputStream();
        int length = json.getBytes().length;
        byte[] byteLength = Transfer.int2bytes(length);
        byte[] payload = new byte[byteLength.length + json.getBytes().length];
        System.arraycopy(byteLength, 0, payload, 0, byteLength.length); // 这样写浪费资源但是保证多线程时不会混乱
        System.arraycopy(json.getBytes(), 0, payload, byteLength.length, json.getBytes().length);
        writer.write(payload); // 写入消息
    }
}