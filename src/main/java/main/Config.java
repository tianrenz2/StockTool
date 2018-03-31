package main;

import java.io.IOException;
import java.util.Properties;

public class Config {
    Properties configFile;
    public Config(){
        configFile = new Properties();
        try{
            configFile.load(this.getClass().getClassLoader().getResourceAsStream("Conf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getProperty(String key)
    {
        String value = this.configFile.getProperty(key);
        return value;
    }



}
