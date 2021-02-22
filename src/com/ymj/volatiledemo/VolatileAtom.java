package com.ymj.volatiledemo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yemingjie
 * @version 1.0
 * @date 2021/2/22 16:07
 *  Volatile 不保证原子性
 *  原子性：不可分割，也即某个线程在做某个具体业务时，中间不可以被加塞或者分割，需要整体完整
 *           要么同时成功 要么同时失败
 *
 *
 *  volatile为什么不保证原子性？
 *
 *      num++ 本身就不是原子操作，被分为3个步骤
 *          先执行getfield拿到原始n
 *          执行iadd进行+1操作
 *          执行putfield把累加的值写回
 *
 *
 *   如何解决原子性问题？
 *   1.加锁 synchronized
 *   2.使用原子变量 AtomicInteger
 *
 *
 *   为什么使用AtomicInteger 就可以保证原子性？
 *   1.源码中使用了Unsafe类，是cas的核心类，由于Java 方法无法直接访问底层 ,需要通过本地(native)方法来访问,UnSafe相当于一个后门,
 *   基于该类可以直接操作特额定的内存数据.UnSafe类在于sun.misc包中,
 *   其内部方法操作可以向C的指针一样直接操作内存,因为Java中CAS操作的助兴依赖于UNSafe类的方法
 *   2.变量ValueOffset,便是该变量在内存中的偏移地址,因为UnSafe就是根据内存偏移地址获取数据的
 *   3.变量value和volatile修饰,保证了多线程之间的可见性
 *
 *
 *   CAS算法
 *      Compare-And-Swap ,它是一条CPU并发原语，判断内存某个位置的值是否为预期值,如果是则更新为新的值,这个过程是原子的
 *      CAS(compare And Swap)算法保证了数据的原子性.
 * CAS包含了三个操作数
 * -  内存值V
 * -  预估值A
 * -  更新值B
 * 当且仅当v==A时，此时V 才等于 B； 否则不做任何操作。
 *
 *
 *
 *
 *
 */
public class VolatileAtom {
    public static void main(String[] args) {
        MyData myData = new MyData();
        MyDataAtomic myDataAtomic = new MyDataAtomic();

        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    // 使用volatile
                    myData.addNum();
                    // 使用atomicInteger
                    myDataAtomic.addNum();
                }
            }, String.valueOf(i)).start();
        }
        while(Thread.activeCount()>2){
            Thread.yield();
        }
        System.out.println("使用volatile ： "+ Thread.currentThread().getName()+"\t number= "+myData.num);
        System.out.println("使用atomicInteger ： "+ Thread.currentThread().getName()+"\t number= "+myDataAtomic.atomicInteger);

    }
}


class MyData {
    // 添加了volatile，最终结果应该为20000，然而结果小于等于20000, 说明volatile不保证原子性
    public volatile int num = 0;


    public void addNum() {
        num++;
    }

    //原本线程1在自己的工作空间中将num改为1，写回主内存，主内存由于内存可见性，通知线程2 3，num=1；线程2通过变量的副本拷贝，
    // 将num拷贝并++，num=2；再次写入主内存通知线程3，num=2，线程3通过变量的副本拷贝，将num拷贝并++，num=3；
    //然而 多线程竞争调度的原因，1号线程刚刚要写1的时候被挂起，2号线程将1写入主内存，此时应该通知其他线程，
    // 主内存的值更改为1，由于线程操作极快，还没有通知到其他线程，刚才被挂起的线程1 将num=1 又再次写入了主内存，主内存的值被覆盖，出现了丢失写值；
}


class MyDataAtomic {
    // 使用原子类
    public AtomicInteger atomicInteger = new AtomicInteger();

    public void addNum() {
        atomicInteger.getAndIncrement();
    }
}
