package com.ymj.ConcurrentModificationExceptionTest;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Yemingjie
 * @version 1.0
 * @date 2021/2/22 17:01
 *
 * 并发修改异常
 *  java.util.ConcurrentModificationException
 *  原因：
 *      并发争抢修改导致，参考花名册签名情况。
 *      一个人正在写入，另外一个同学过来抢夺，导致数据不一致异常
 *
 *
 *   解决方案
 *   1. 使用Vector
 *      final List<String> arrayList = new Vector<String>();
 *      使用vector类vector的add方法加锁，使数据性一致，但并发性下降
 *   2.Collections工具 类
 *      final List<String> arrayList = Collections.synchronizedList(new ArrayList<String>());
 *   3.CopyOnWriteArrayList
 *      CopyOnWrite容器即写时复制的容器。往一个容器添加元素的时候，不直接往当前容器object[]添加，而是先将当前容器object[]进Copy,
 *      复制出一个新的容器object[] newELements，然后向新的容器object[] newELements 里添加元素，添加完元素之后，
 *      再将原容器的引用指向新的容器setArray(newELements);.这样做的好处是可以对CopyonWrite容器进行并发的读，
 *      而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite 容器也是一. 种读写分离的思想，读和写不同的容器
 */
public class ArrayListTest {
    public static void main(String[] args) {
        final ArrayList<String> arrayList = new ArrayList<String>();

        CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                cowList.add("aaa");
                System.out.println(cowList);
            }).start();
        }

//        for (int i = 0; i < 20; i++) {
//            new Thread(() -> {
//                arrayList.add("aaa");
//                System.out.println(arrayList);
//            }).start();
//        }
    }
}
