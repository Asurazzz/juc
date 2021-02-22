package com.ymj.volatiledemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;


/**
 * @author Yemingjie
 * @version 1.0
 * @date 2021/2/22 16:35
 */
public class ABA {
    static AtomicInteger atomicInteger = new AtomicInteger(100);
    static AtomicStampedReference atomicStampedReference = new AtomicStampedReference(
            Integer.valueOf(100), 1);
    public static void main(String[] args) {
        System.out.println("====================以下是ABA问题的产生");
        new Thread( () -> {
            atomicInteger.compareAndSet(100, 101);
            atomicInteger.compareAndSet(101, 100);
        },"t1").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(atomicInteger.compareAndSet(100, 2020)
                        + "\t" + atomicInteger.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();



        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println("====================以下是ABA问题的解决===");

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()
                    + "的第一次版本号：" + stamp);//实际版本
            try {
                TimeUnit.SECONDS.sleep(1);
                // 开始ABA
                atomicStampedReference.compareAndSet(Integer.valueOf(100),
                        Integer.valueOf(101),
                        atomicStampedReference.getStamp(),//期待的版本
                        atomicStampedReference.getStamp() + 1);//要更新的版本
                System.out.println(Thread.currentThread().getName()
                        + "的第二次版本号：" + atomicStampedReference.getStamp());
                atomicStampedReference.compareAndSet(Integer.valueOf(101),
                        Integer.valueOf(100),
                        atomicStampedReference.getStamp(),
                        atomicStampedReference.getStamp() + 1);
                System.out.println(Thread.currentThread().getName()
                        + "的第3次版本号：" + atomicStampedReference.getStamp());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t3").start();


        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()
                    + "的第一次版本号：" + stamp);
            try {
                // 暂停3秒钟，保证上面的t3线程完成一次ABA操作
                TimeUnit.SECONDS.sleep(3);
                boolean result=atomicStampedReference.compareAndSet(Integer.valueOf(100),
                        Integer.valueOf(2020),
                        stamp,//此处为期望值的版本号为1，实际上版本号已经被线程3更改为3，此时不更改数值，版本号也不会由3到4
                        atomicStampedReference.getStamp() + 1);
                System.out.println(Thread.currentThread().getName()+"\t最终结果为："+result);
                System.out.println(Thread.currentThread().getName()+"\t当前版本号为："+atomicStampedReference.getStamp());
                System.out.println("当前实际值为："+atomicStampedReference.getReference());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t4").start();

    }
}
