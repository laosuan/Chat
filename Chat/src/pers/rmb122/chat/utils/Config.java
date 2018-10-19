package pers.rmb122.chat.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    public int remotePort = 0;
    public String remoteIp = "";
    public String username = "";

    public void loadConfig() throws IOException {
        File f = new File(getDefaultConfigPath());
        Long length = f.length();
        char[] content = new char[length.intValue()];
        FileReader fr = new FileReader(f);
        fr.read(content); // 读取内容
        fr.close();
        String json = new String(content);
        Gson gson = new Gson(); // 将 json 转换为类
        Config c = gson.fromJson(json, Config.class);
        this.remotePort = c.remotePort;
        this.remoteIp = c.remoteIp;
        this.username = c.username;
    }

    public boolean isConfigExists() {
        File f = new File(getDefaultConfigPath());
        return f.exists();
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