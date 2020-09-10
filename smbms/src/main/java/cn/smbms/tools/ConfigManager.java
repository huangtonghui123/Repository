package cn.smbms.tools;

import cn.smbms.dao.BaseDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
//    private static ConfigManager configManager = new ConfigManager();
    private static ConfigManager configManager;

    private static Properties properties;

    private ConfigManager(){
        properties = new Properties();
        String configFile = "database.properties";
        InputStream is= BaseDao.class.getClassLoader().getResourceAsStream(configFile);
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*//懒汉模式：线程不安全,具备延迟加载的特性,增加synchronized关键字实现同步
    public static synchronized ConfigManager getInstance(){
        if(configManager == null){
            configManager = new ConfigManager();
        }
        return configManager;
    }*/

    /*//饿汉模式：在类加载的时候就完成初始化,线程安全,不具备延迟加载的特性
    public static ConfigManager getInstance(){
        return configManager;
    }*/

    private static class InitInstance{
        private static ConfigManager instance = new ConfigManager();
    }

    //使用静态内部类实现：既线程安全,又延迟加载
    public static ConfigManager getInstance(){
        configManager = InitInstance.instance;
        return configManager;
    }

    public String getValue(String key){
        return properties.getProperty(key);
    }
}
