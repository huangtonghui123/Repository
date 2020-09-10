package cn.smbms.dao;

public class Singleton {

    //定义了静态的该类类型的私有对象
    private static Singleton singleton;

    //构造方法私有化
    private Singleton(){}

    //提供一个静态的公共方法用来返回创建或者获取本身的静态私有对象
    public static Singleton getSingleton(){
        //懒汉模式
        if(singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }
}
