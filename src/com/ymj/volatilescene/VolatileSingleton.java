package com.ymj.volatilescene;

/**
 * @author Yemingjie
 * @version 1.0
 * @date 2021/2/22 16:45
 *
 *
 *  多线程下不使用volatile的单例模式
 *
 *  单例模式在多线程下,创造了多个对象,违背了单例模式
 *
 *  解决方法
 *      1.加入synchronized，我们真正需要控制的是instance = new SingletonDemo();这一行代码
 *      2. DCL (Double Check Lock双端检索机制)
 */
public class VolatileSingleton {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                SingletonDemo.getInstance();
            },String.valueOf(i)).start();
        }
    }
}

class SingletonDemo {
    private static SingletonDemo instance = null;

    public SingletonDemo() {
        System.out.println(Thread.currentThread().getName() + "\t 构造方法");
    }

//    public synchronized static SingletonDemo getInstance() {
//        if (instance == null) {
//            instance = new SingletonDemo();
//        }
//        return instance;
//    }
    public static SingletonDemo getInstance() {
        if (instance == null) {
            instance = new SingletonDemo();
        }
        return instance;
    }
}
