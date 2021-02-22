package com.ymj.volatilescene;

/**
 * @author Yemingjie
 * @version 1.0
 * @date 2021/2/22 16:53
 * DCL (Double Check Lock双端检索机制)
 *
 * 双端检索机制不一定安全，原因是有指令重排序的存在，加入volatile可以禁止指令重排
 *
 * 在某一个线程执行到第一次检测时，此时instance不为null，但是insatnce的引用对象可能没有初始化完成
 *
 *  new一个对象的步骤
 *      1.分配对象内存空间
 *      2.初始化对象
 *      3.设置初始化的对象指向刚分配的内存地址，此时instacne ! =null
 *
 *   所以需要添加volatile关键字
 */
public class DCL {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                DCLSingletonDemo.getInstance();
            }, String.valueOf(i)).start();
        }
    }
}

 class DCLSingletonDemo {
    private volatile static DCLSingletonDemo instance = null;

    public DCLSingletonDemo () {
        System.out.println(Thread.currentThread().getName() + "\t 构造方法");
    }

    public static DCLSingletonDemo getInstance() {
        if (instance == null) {
            synchronized (DCLSingletonDemo.class) {
                if (instance == null) {
                    instance = new DCLSingletonDemo();
                }
            }
        }
        return instance;
    }

 }
