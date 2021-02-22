package com.ymj.volatiledemo;

import java.util.concurrent.TimeUnit;

/**
 * @author Yemingjie
 * @version 1.0
 * @date 2021/2/22 15:32
 */
public class VolatileDemo {
    public static void main(String[] args) {
        ThreadDemo td=new ThreadDemo();
        new Thread(td).start();
        //  什么都不加
        while (true) {
            if (td.isFlag()) {
                //这里获得的flag并不是true ,不能打印，这里涉及到了内存可见性问题
                // volatile添加之后可以输出该语句
                System.out.println("----------------------");
                break;
            }
        }
        // 解决方法一：添加同步锁zsdfa
        // 特点：使用锁，效率低  具备互斥性
        while (true) {
            synchronized (td) {
                if (td.isFlag()) {
                    System.out.println("----------------------");
                    break;
                }
            }
        }
    }
}

class ThreadDemo implements Runnable {

    //这里为共享数据
    private boolean flag = false;

    // 解决方法二：添加volatile关键字，当多个线程操作共享数据时，保证内存数据可见
    //private volatile boolean flag = false;


    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        flag = true;
        System.out.println("flag = " + isFlag());

    }
}
/**
 *  volatile和synchronized的特点：
 *
 *  谈谈对volatile的理解？
 *  volatile是JAVA虚拟机提供的轻量级的同步机制，有三大特点：
 *  1.1保证可见性  1.2 不保证原子性 1.3禁止指令重排
 *
 *  对JMM模型的理解？
 *      java内存模型是一种抽象的概念，本身并不存在，他描述的是一组规则或规范通过规范定制了程序中
 *      各个变量（包括实例字段，静态字段，和构成数组对象的元素）的访问方式
 *      JMM三大特点 :可见性 原子性 有序性
 *
 *  由于JVM运行程序的实体是线程,而每个线程创建时JVM都会为其创建一个工作内存(有些地方成为栈空间),工作内存是每个线程的私有数据区域,
 * 而Java内存模型中规定所有变量都存储在主内存,主内存是共享内存区域,所有线程都可访问,但线程对变量的操作(读取赋值等)必须在工作内存中进行,
 * 首先要将变量从主内存拷贝到自己的工作空间,然后对变量进行操作,操作完成再将变量写回主内存,不能直接操作主内存中的变量,
 * 各个线程中的工作内存储存着主内存中的变量副本拷贝,因此不同的线程无法访问对方的工作内存,此案成间的通讯(传值) 必须通过主内存来完成
 *
 *
 */
