package pers.rmb122.chatServer.utils;

import com.google.gson.Gson;

public class Transfer {
    public static byte[] int2bytes(int number) {
        int width = 4;
        byte[] result = new byte[width];
        for (int i = width - 1; i >= 0; i--) {
            result[i] = (byte) (number & 0xFF);
            number = number >> 8;
        }
        return result;
    }

    public static int bytes2int(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = result << 8;
            result ^= (bytes[i] & 0xFF);
        }
        return result;
    }

    public static String Message2json(Message mess) {
        Gson gson = new Gson();
        return gson.toJson(mess);
    }

    public static Message json2Message(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Message.class);
    }
}