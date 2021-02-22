package com.ymj.volatiledemo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yemingjie
 * @version 1.0
 * @date 2021/2/22 16:28
 *
 * Var 1 AtomicInteger对象本身
 * .var2 该对象值的引用地址
 * var4 需要变动的数值
 * var5 是用过var1 var2找出内存中真实的值 用该对象当前的值与var5比较如果相同,更新var5的值并且返回true如果不同,继续取值然后比较,直到更新完成
 *
 *          底层使用了do while循环
 *
 *  CAS的算法缺点：
 *      1.循环时间长，开销大
 *          getAndAddInt执行时，dowhile执行语句while中的条件期待值与预估值不相等返回false,取反为true;如果一直循环，CAS一直不成功，给CPU带来的很大开销
 *      2.只能保证一个共享变量的原子操作
 *      3.ABA问题
 *
 *   ABA问题
 *      比如说-一个线程one从内存位置V中取出A,这时候另一个线程two也从内存中取出A，并且线程two进行了一些操作将值变成了B,
 *       然后线程two又将V位置的数据变成A，这时候线程one进行CAS操作发现内存中仍然是A，然后线程one操作成功
 *
 *   ABA问题的解决方法：
 *      1.AtomicReference原子引用
 *      2.添加版本号
 */
public class CAS {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(atomicInteger.compareAndSet(5, 100)+"  data="+atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(100, 2020)+"  data="+atomicInteger.get());
    }
}

