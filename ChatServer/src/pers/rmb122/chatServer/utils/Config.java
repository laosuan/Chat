package pers.rmb122.chatServer.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Config {
        public int port = 0;

    public void loadConfig() throws FileNotFoundException, IOException {
        File f = new File(getDefaultConfigPath());
        Long length = f.length();
        char[] content = new char[length.intValue()];
        FileReader fr = new FileReader(f);
        fr.read(content); // 读取内容
        fr.close();
        String json = new String(content);
        Gson gson = new Gson(); // 将 json 转换为类
        Config c = gson.fromJson(json, Config.class);
        this.port = c.port;
    }

    public boolean isConfigExists() {
        File f = new File(getDefaultConfigPath());
        return f.exists();
    }

    public void generateConfig() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(">>> Set your server listening port:");
        System.out.print(">>> ");
        boolean success = false;
        while (!success) {
            int port = scanner.nextInt();
            if (port < 65536 && port > 0) {
                this.port = port;
                success = true;
            } else {
                System.out.println(">>> Invaild port, please input again");
                System.out.print(">>> ");
            }
        }
    }

    public void saveConfig() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        FileWriter fw = new FileWriter(new File(getDefaultConfigPath()));
        fw.write(json);
        fw.close();
    }

    public String getDefaultConfigPath() {
        String cwd = getCwd();
        int lastIndex = cwd.lastIndexOf(File.separator) + 1;
        cwd = cwd.substring(0, lastIndex);
        cwd += ".config.json";
        return cwd;
    }

    public String getCwd() {
        return this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    }
}